
import { useState } from 'react';

function Auth({ onLogin }) {
  const [isLogin, setIsLogin] = useState(true);
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [message, setMessage] = useState('');
  const [loading, setLoading] = useState(false);

  async function handleSubmit(event) {
    event.preventDefault();
    setMessage('');

    if (!email.trim() || !password) {
      setMessage('Please enter your email and password.');
      return;
    }

    if (!isLogin && !name.trim()) {
      setMessage('Please enter your name.');
      return;
    }

    setLoading(true);

    try {
      const endpoint = isLogin ? 'login' : 'register';

const params = new URLSearchParams();

params.append('email', email.trim());
params.append('password', password);

if (!isLogin) {
  params.append('name', name.trim());
}

const response = await fetch(
  `http://localhost:8080/api/auth/${endpoint}`,
  {
    method: 'POST',
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded',
    },
    body: params.toString(),
  }
);

const data = await response.json();


      if (!response.ok) {
        throw new Error(
          data.message || 'Authentication failed. Please try again.'
        );
      }

      if (isLogin) {
        onLogin(data);
      } else {
        setMessage('Registration successful! You can now log in.');
        setIsLogin(true);
        setPassword('');
      }
    } catch (error) {
      setMessage(
        error.message || 'Could not connect to the backend.'
      );
    } finally {
      setLoading(false);
    }
  }

  return (
    <main className="auth-page">
      <section className="auth-card">
        <h1>MyWallet</h1>
        <p>
          {isLogin
            ? 'Welcome back! Log in to continue.'
            : 'Create an account to get started.'}
        </p>

        <form onSubmit={handleSubmit}>
          {!isLogin && (
            <input
              type="text"
              placeholder="Full name"
              value={name}
              onChange={(event) => setName(event.target.value)}
              required
            />
          )}

          <input
            type="email"
            placeholder="Email address"
            value={email}
            onChange={(event) => setEmail(event.target.value)}
            required
          />

          <input
            type="password"
            placeholder="Password"
            value={password}
            onChange={(event) => setPassword(event.target.value)}
            required
          />

          <button type="submit" disabled={loading}>
            {loading
              ? 'Please wait...'
              : isLogin
                ? 'Log In'
                : 'Create Account'}
          </button>
        </form>

        {message && <p role="status">{message}</p>}

        <p>
          {isLogin
            ? "Don't have an account?"
            : 'Already have an account?'}
          {' '}
          <button
            type="button"
            onClick={() => {
              setIsLogin(!isLogin);
              setMessage('');
            }}
          >
            {isLogin ? 'Register' : 'Log In'}
          </button>
        </p>
      </section>
    </main>
  );
}

export default Auth;
