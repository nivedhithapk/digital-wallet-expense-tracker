
import { useEffect, useState } from 'react';
import './App.css';
import Auth from './components/Auth';

const API_BASE = 'https://digital-wallet-expense-tracker-2.onrender.com';

function App() {
  const [loggedInUser, setLoggedInUser] = useState(null);
  const [balance, setBalance] = useState(0);
  const [selectedMonth, setSelectedMonth] = useState(
    new Date().getMonth() + 1
  );
  const [selectedYear, setSelectedYear] = useState(
    new Date().getFullYear()
  );
  const [monthlyExpenses, setMonthlyExpenses] = useState(0);
  const [monthlyBudget, setMonthlyBudget] = useState(2000);
  const [activePage, setActivePage] = useState('Dashboard');
  const [amount, setAmount] = useState('');
  const [message, setMessage] = useState('');
  const [transactions, setTransactions] = useState([]);
  const [expenses, setExpenses] = useState([]);
  const [totalExpenses, setTotalExpenses] = useState(0);
  const [expenseSummary, setExpenseSummary] = useState([]);
  const [expenseAmount, setExpenseAmount] = useState('');
  const [expenseCategory, setExpenseCategory] = useState('');

  useEffect(() => {
    async function loadDashboard() {
      try {
        const balanceResponse = await fetch(
          `${API_BASE}/api/wallets/1/balance`
        );
        if (!balanceResponse.ok) {
          throw new Error('Could not load wallet balance.');
        }
        setBalance(Number(await balanceResponse.text()));

        const transactionsResponse = await fetch(
          `${API_BASE}/api/wallets/1/transactions`
        );
        if (transactionsResponse.ok) {
          setTransactions(await transactionsResponse.json());
        }

        const expensesResponse = await fetch(
          `${API_BASE}/api/expenses/wallet/1`
        );
        if (expensesResponse.ok) {
          setExpenses(await expensesResponse.json());
        }

        const totalResponse = await fetch(
          `${API_BASE}/api/wallets/1/expenses/total`
        );
        if (totalResponse.ok) {
          setTotalExpenses(Number(await totalResponse.json()) || 0);
        }

        const summaryResponse = await fetch(
          `${API_BASE}/api/wallets/1/expenses/summary`
        );
        if (summaryResponse.ok) {
          setExpenseSummary(await summaryResponse.json());
        }
      } catch (error) {
        setMessage(error.message || 'Could not connect to the backend.');
      }
    }

    loadDashboard();
  }, []);

  useEffect(() => {
    async function loadMonthlyData() {
      try {
        const expensesResponse = await fetch(
          `${API_BASE}/api/wallets/1/expenses/monthly/total?year=${selectedYear}&month=${selectedMonth}`
        );
        if (expensesResponse.ok) {
          setMonthlyExpenses(
            Number(await expensesResponse.json()) || 0
          );
        }

        const budgetResponse = await fetch(
          `${API_BASE}/api/budgets?walletId=1&month=${selectedMonth}&year=${selectedYear}`
        );
        if (budgetResponse.ok) {
          const data = await budgetResponse.json();
          if (data && data.amount != null) {
            setMonthlyBudget(Number(data.amount));
          }
        }

        const transactionsResponse = await fetch(
          `${API_BASE}/api/wallets/1/transactions/monthly?year=${selectedYear}&month=${selectedMonth}`
        );
        if (transactionsResponse.ok) {
          setTransactions(await transactionsResponse.json());
        }
      } catch (error) {
        console.error('Monthly data loading error:', error);
      }
    }

    loadMonthlyData();
  }, [selectedMonth, selectedYear]);

  async function refreshBalance() {
    const response = await fetch(
      `${API_BASE}/api/wallets/1/balance`
    );
    if (!response.ok) {
      throw new Error('Could not refresh wallet balance.');
    }
    setBalance(Number(await response.text()));
  }

  async function handleDeposit() {
    const value = Number(amount);

    if (!Number.isFinite(value) || value <= 0) {
      setMessage('Please enter a valid amount.');
      return;
    }

    try {
      const response = await fetch(
        `${API_BASE}/api/wallets/1/deposit`,
        {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ amount: value }),
        }
      );

      if (!response.ok) {
        throw new Error((await response.text()) || 'Deposit failed.');
      }

      await refreshBalance();
      setAmount('');
      setMessage(`₹${value.toFixed(2)} deposited successfully!`);
    } catch (error) {
      setMessage(error.message || 'Could not connect to the backend.');
    }
  }

  async function handleExpense() {
    const value = Number(expenseAmount);

    if (
      !Number.isFinite(value) ||
      value <= 0 ||
      !expenseCategory.trim()
    ) {
      setMessage('Enter a valid amount and category.');
      return;
    }

    try {
      const response = await fetch(
        `${API_BASE}/api/wallets/1/expenses`,
        {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            walletId: 1,
            amount: value,
            category: expenseCategory.trim(),
          }),
        }
      );

      if (!response.ok) {
        throw new Error(
          (await response.text()) || 'Could not add expense.'
        );
      }

      setExpenseAmount('');
      setExpenseCategory('');
      setMessage('Expense saved successfully!');

      const [expensesResponse, transactionsResponse, totalResponse, summaryResponse] =
        await Promise.all([
          fetch(`${API_BASE}/api/expenses/wallet/1`),
          fetch(`${API_BASE}/api/wallets/1/transactions`),
          fetch(`${API_BASE}/api/wallets/1/expenses/total`),
          fetch(`${API_BASE}/api/wallets/1/expenses/summary`),
        ]);

      if (expensesResponse.ok) {
        setExpenses(await expensesResponse.json());
      }
      if (transactionsResponse.ok) {
        setTransactions(await transactionsResponse.json());
      }
      if (totalResponse.ok) {
        setTotalExpenses(Number(await totalResponse.json()) || 0);
      }
      if (summaryResponse.ok) {
        setExpenseSummary(await summaryResponse.json());
      }
    } catch (error) {
      setMessage(error.message || 'Could not connect to the backend.');
    }
  }

  async function saveBudget() {
    try {
      const response = await fetch(
        `${API_BASE}/api/budgets?walletId=1&month=${selectedMonth}&year=${selectedYear}&amount=${monthlyBudget}`,
        { method: 'POST' }
      );

      if (!response.ok) {
        throw new Error('Failed to save budget.');
      }

      setMessage('Budget saved successfully!');
    } catch (error) {
      setMessage(error.message || 'Could not save budget.');
    }
  }

  if (!loggedInUser) {
    return <Auth onLogin={setLoggedInUser} />;
  }

  return (
    <div className="app">
      <aside className="sidebar">
        <h2>MyWallet</h2>

        {['Dashboard', 'Transactions', 'Expenses'].map((page) => (
          <p
            key={page}
            className={activePage === page ? 'active' : ''}
            onClick={() => setActivePage(page)}
          >
            {page}
          </p>
        ))}
      </aside>

      <main className="main-content">
        <header>
          <div>
            <h1>{activePage}</h1>
            <p>Welcome to your Digital Wallet</p>
          </div>
          <span className="avatar">W</span>
        </header>

        <section className="panel">
          <h3>Monthly Expense Tracker</h3>

          <label>
            Select month:
            <select
              value={selectedMonth}
              onChange={(event) =>
                setSelectedMonth(Number(event.target.value))
              }
            >
              {[
                'January', 'February', 'March', 'April',
                'May', 'June', 'July', 'August',
                'September', 'October', 'November', 'December',
              ].map((month, index) => (
                <option key={month} value={index + 1}>
                  {month}
                </option>
              ))}
            </select>
          </label>

          <label>
            Select year:
            <select
              value={selectedYear}
              onChange={(event) =>
                setSelectedYear(Number(event.target.value))
              }
            >
              {[2025, 2026, 2027, 2028].map((year) => (
                <option key={year} value={year}>
                  {year}
                </option>
              ))}
            </select>
          </label>

          <h2>
            Monthly Expenses: ₹
            {monthlyExpenses.toLocaleString('en-IN', {
              minimumFractionDigits: 2,
              maximumFractionDigits: 2,
            })}
          </h2>

          <label>
            Monthly Budget (₹):
            <input
              type="number"
              min="1"
              value={monthlyBudget}
              onChange={(event) =>
                setMonthlyBudget(Number(event.target.value))
              }
            />
          </label>
          <button onClick={saveBudget}>Save Budget</button>

          {monthlyExpenses > monthlyBudget && (
            <p style={{ color: 'red', fontWeight: 'bold' }}>
              ⚠️ Budget exceeded! You have spent ₹
              {(monthlyExpenses - monthlyBudget).toLocaleString('en-IN', {
                minimumFractionDigits: 2,
                maximumFractionDigits: 2,
              })}{' '}
              more than your monthly budget.
            </p>
          )}
        </section>

        <section className="balance-card">
          <p>Total Wallet Balance</p>
          <h2>
            ₹{balance.toLocaleString('en-IN', {
              minimumFractionDigits: 2,
              maximumFractionDigits: 2,
            })}
          </h2>
          <span>Your wallet at a glance</span>
        </section>

        {activePage === 'Dashboard' && (
          <section className="balance-card">
            <p>Total Expenses</p>
            <h2>
              ₹{totalExpenses.toLocaleString('en-IN', {
                minimumFractionDigits: 2,
                maximumFractionDigits: 2,
              })}
            </h2>
            <span>Your total recorded expenses</span>
          </section>
        )}

        <section className="panel">
          <h3>Spending by Category</h3>
          {expenseSummary.length === 0 ? (
            <p>No category expenses found.</p>
          ) : (
            expenseSummary.map((item, index) => (
              <div className="overview-item" key={item.category ?? index}>
                <strong>{item.category}</strong>
                <strong>
                  ₹{Number(item.totalAmount ?? item[1] ?? 0).toLocaleString(
                    'en-IN',
                    {
                      minimumFractionDigits: 2,
                      maximumFractionDigits: 2,
                    }
                  )}
                </strong>
              </div>
            ))
          )}
        </section>

        <section className="actions">
          <div className="panel">
            <h3>Add Money</h3>
            <p>Deposit money into your wallet.</p>
            <input
              type="number"
              min="0.01"
              step="0.01"
              placeholder="Enter amount (₹)"
              value={amount}
              onChange={(event) => setAmount(event.target.value)}
            />
            <button onClick={handleDeposit}>Deposit Money</button>
          </div>

          <div className="panel">
            <h3>Add Expense</h3>
            <input
              type="number"
              min="0.01"
              step="0.01"
              placeholder="Enter expense amount"
              value={expenseAmount}
              onChange={(event) => setExpenseAmount(event.target.value)}
            />
            <input
              type="text"
              placeholder="Category (e.g. Food)"
              value={expenseCategory}
              onChange={(event) => setExpenseCategory(event.target.value)}
            />
            <button onClick={handleExpense}>Add Expense</button>
          </div>

          <div className="panel">
            <h3>Quick Overview</h3>
            <p>Manage your money in one place.</p>
            <div className="overview-item">
              <span>Wallet</span>
              <strong>Active</strong>
            </div>
            <div className="overview-item">
              <span>Currency</span>
              <strong>INR (₹)</strong>
            </div>
          </div>
        </section>

        {message && (
          <p role="status" className="status-message">
            {message}
          </p>
        )}

        {activePage === 'Transactions' && (
          <section className="panel transactions-panel">
            <h3>Recent Transactions</h3>
            <p>Your latest wallet activity.</p>
            {transactions.length === 0 ? (
              <p>No transactions found.</p>
            ) : (
              transactions.map((transaction, index) => (
                <div
                  className="overview-item"
                  key={transaction.id ?? index}
                >
                  <div>
                    <strong>{transaction.transactionType}</strong>
                    <p>
                      {transaction.createdAt
                        ? new Date(transaction.createdAt).toLocaleString()
                        : 'Date unavailable'}
                    </p>
                  </div>
                  <strong>
                    ₹{Number(transaction.amount).toLocaleString('en-IN', {
                      minimumFractionDigits: 2,
                      maximumFractionDigits: 2,
                    })}
                  </strong>
                </div>
              ))
            )}
          </section>
        )}

        {activePage === 'Expenses' && (
          <section className="panel transactions-panel">
            <h3>Recent Expenses</h3>
            <p>Your saved expenses.</p>
            {expenses.length === 0 ? (
              <p>No expenses found.</p>
            ) : (
              expenses.map((expense) => (
                <div className="overview-item" key={expense.id}>
                  <div>
                    <strong>{expense.category}</strong>
                    <p>{expense.description || 'No description'}</p>
                    <p>
                      {expense.createdAt
                        ? new Date(expense.createdAt).toLocaleString()
                        : 'Date unavailable'}
                    </p>
                  </div>
                  <strong>
                    ₹{Number(expense.amount).toLocaleString('en-IN', {
                      minimumFractionDigits: 2,
                      maximumFractionDigits: 2,
                    })}
                  </strong>
                </div>
              ))
            )}
          </section>
        )}

        <footer>Digital Wallet &amp; Expense Tracker</footer>
      </main>
    </div>
  );
}

export default App;
