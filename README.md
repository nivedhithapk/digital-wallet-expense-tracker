# 💰 Digital Wallet & Expense Tracker

A full-stack web application designed to help users manage their wallet balance, track daily expenses, monitor budgets, and develop better financial habits.
#    Source code
https://digital-wallet-expense-tracker.vercel.app/

## 📌 About the Project

Managing personal finances can be challenging when daily expenses go untracked. Small purchases can add up over time, making it difficult to understand spending habits or stay within a budget.

The **Digital Wallet & Expense Tracker** aims to make personal money management simpler by bringing wallet transactions, expense tracking, and financial summaries together in one application.

The goal is to help users understand where their money goes, identify unnecessary spending, and make more informed financial decisions.

## 🎯 Project Objectives

- Encourage responsible spending and better financial habits.
- Help users track income and expenses in one place.
- Make it easier to monitor wallet balances and transaction history.
- Support budget awareness and monthly expense analysis.
- Provide a practical full-stack application using modern development technologies.

## ✨ Features

- **User Registration and Login:** User account creation and login functionality.
- **Wallet Management:** View and manage wallet balances.
- **Deposit and Withdrawal:** Record deposits and withdrawals.
- **Expense Tracking:** Record expenses and organize them by category.
- **Transaction History:** Review recorded wallet transactions.
- **Expense Summary:** View expense information to better understand spending patterns.
- **Budget Awareness:** Track spending against budget information where supported by the application.

## 🛠️ Technologies Used

| Technology | Purpose |
|---|---|
| Java | Backend programming |
| Spring Boot | REST API and backend development |
| React | Interactive frontend |
| JavaScript | Frontend functionality |
| HTML and CSS | Structure and styling |
| MySQL | Database management |
| Maven | Backend dependency and build management |
| Postman | REST API testing |
| Git and GitHub | Version control and source code hosting |

## 🏗️ Project Structure

```text
digital-wallet-expense-tracker/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/wallet/wallettracker/
│       └── resources/
├── wallet-frontend/
│   ├── src/
│   ├── public/
│   ├── package.json
│   └── index.html
├── pom.xml
└── README.md
```

## ⚙️ Prerequisites

Before running the project, install:

- Java Development Kit (JDK)
- Maven, or use the included Maven Wrapper
- MySQL Server
- Node.js and npm
- An IDE such as IntelliJ IDEA or Visual Studio Code

## 🚀 How to Run the Project

### 1. Clone the Repository

```bash
git clone https://github.com/nivedhithapk/digital-wallet-expense-tracker.git
cd digital-wallet-expense-tracker
```

### 2. Configure the Database

- Start your MySQL server.
- Create or select the database configured for the application.
- Update the database connection settings in `src/main/resources/application.properties`.

Use your own local database credentials. **Do not commit database passwords or other secrets to GitHub.**

### 3. Start the Spring Boot Backend

From the project root directory, run:

```bash
.\mvnw.cmd spring-boot:run
```

On Windows, this command uses the included Maven Wrapper.

The backend is expected to run at:

```text
http://localhost:8080
```

### 4. Start the React Frontend

Open a separate terminal and navigate to the frontend directory:

```bash
cd wallet-frontend
```

Install the dependencies:

```bash
npm install
```

Start the development server:

```bash
npm run dev
```

Open the local URL displayed in your terminal, usually:

```text
http://localhost:5173
```

### 5. Verify the Application

- Confirm that the backend starts successfully.
- Open the frontend in your browser.
- Test login and the available wallet and expense features.
- Verify that recorded transactions are saved in the database.

## 💡 Real-World Impact

This project demonstrates how software can help people build better financial habits.

By making expenses visible and easier to review, users can identify spending patterns, plan their budgets more carefully, and make better decisions about their money.

The application also provides practical experience with backend APIs, database integration, frontend development, and communication between frontend and backend services.

## 📚 Learning Outcomes

Through this project, I have gained practical experience in:

- Developing REST APIs using Java and Spring Boot.
- Connecting a backend application to MySQL.
- Building a user interface with React and JavaScript.
- Integrating frontend requests with backend endpoints.
- Testing APIs using Postman.
- Managing source code with Git and GitHub.
- Troubleshooting application configuration and integration issues.

## 🔮 Future Enhancements

- Interactive charts for monthly income and expenses.
- Budget-limit notifications and overspending alerts.
- Recurring expense management.
- Downloadable financial reports.
- Improved authentication and secure user-specific data access.
- Responsive design improvements for mobile devices.

## 👩‍💻 Author

**GitHub:** [nivedhithapk](https://github.com/nivedhithapk)

## ⭐ Feedback

Suggestions and feedback that help improve this project are welcome.
