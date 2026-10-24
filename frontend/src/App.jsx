import { useCallback, useEffect, useState } from 'react'
import { api, getName, getToken, setSession } from './api'

const CATEGORIES = ['Food', 'Travel', 'Shopping', 'Bills', 'Entertainment', 'Health', 'Other']

const money = (n) =>
  new Intl.NumberFormat('en-IN', { style: 'currency', currency: 'INR' }).format(n)

function AuthForm({ onLogin }) {
  const [mode, setMode] = useState('login')
  const [form, setForm] = useState({ name: '', email: '', password: '' })
  const [error, setError] = useState('')
  const [busy, setBusy] = useState(false)

  const update = (field) => (e) => setForm({ ...form, [field]: e.target.value })

  async function submit(e) {
    e.preventDefault()
    setError('')
    setBusy(true)
    try {
      const body = mode === 'login' ? { email: form.email, password: form.password } : form
      const data = await api(`/api/auth/${mode}`, { method: 'POST', body: JSON.stringify(body) })
      setSession(data.token, data.name)
      onLogin()
    } catch (err) {
      setError(err.message)
    } finally {
      setBusy(false)
    }
  }

  return (
    <div className="card auth">
      <h1>SpendSense AI</h1>
      <p className="muted">Track your spending in plain English.</p>
      <form onSubmit={submit}>
        {mode === 'register' && (
          <input placeholder="Name" value={form.name} onChange={update('name')} required />
        )}
        <input type="email" placeholder="Email" value={form.email} onChange={update('email')} required />
        <input
          type="password"
          placeholder="Password (min 8 characters)"
          value={form.password}
          onChange={update('password')}
          minLength={mode === 'register' ? 8 : undefined}
          required
        />
        {error && <p className="error">{error}</p>}
        <button disabled={busy}>
          {busy ? 'Please wait...' : mode === 'login' ? 'Log in' : 'Create account'}
        </button>
      </form>
      <p className="muted">
        {mode === 'login' ? 'New here?' : 'Already have an account?'}{' '}
        <a
          href="#"
          onClick={(e) => {
            e.preventDefault()
            setError('')
            setMode(mode === 'login' ? 'register' : 'login')
          }}
        >
          {mode === 'login' ? 'Create an account' : 'Log in'}
        </a>
      </p>
    </div>
  )
}

function Dashboard({ onLogout }) {
  const [expenses, setExpenses] = useState([])
  const [error, setError] = useState('')
  const [aiText, setAiText] = useState('')
  const [aiBusy, setAiBusy] = useState(false)
  const [manual, setManual] = useState({ amount: '', category: 'Food', description: '', expenseDate: '' })

  const load = useCallback(async () => {
    try {
      setExpenses(await api('/api/expenses'))
    } catch (err) {
      setError(err.message)
    }
  }, [])

  useEffect(() => {
    load()
  }, [load])

  async function addWithAi(e) {
    e.preventDefault()
    if (!aiText.trim() || aiBusy) return
    setError('')
    setAiBusy(true)
    try {
      await api('/api/expenses/ai', { method: 'POST', body: JSON.stringify({ text: aiText }) })
      setAiText('')
      await load()
    } catch (err) {
      setError(err.message)
    } finally {
      setAiBusy(false)
    }
  }

  async function addManual(e) {
    e.preventDefault()
    setError('')
    try {
      await api('/api/expenses', {
        method: 'POST',
        body: JSON.stringify({
          amount: Number(manual.amount),
          category: manual.category,
          description: manual.description,
          expenseDate: manual.expenseDate || null,
        }),
      })
      setManual({ amount: '', category: 'Food', description: '', expenseDate: '' })
      await load()
    } catch (err) {
      setError(err.message)
    }
  }

  async function remove(id) {
    setError('')
    try {
      await api(`/api/expenses/${id}`, { method: 'DELETE' })
      await load()
    } catch (err) {
      setError(err.message)
    }
  }

  const total = expenses.reduce((sum, x) => sum + Number(x.amount), 0)
  const byCategory = expenses.reduce(
    (acc, x) => ({ ...acc, [x.category]: (acc[x.category] || 0) + Number(x.amount) }),
    {},
  )
  const max = Math.max(1, ...Object.values(byCategory))

  return (
    <div className="dashboard">
      <header>
        <h1>SpendSense AI</h1>
        <div>
          <span className="muted">Hi, {getName() || 'there'}</span>{' '}
          <button className="secondary" onClick={onLogout}>Log out</button>
        </div>
      </header>

      {error && <p className="error">{error}</p>}

      <section className="card">
        <h2>Add with AI</h2>
        <form className="row" onSubmit={addWithAi}>
          <input
            placeholder='Try: "spent 250 on biryani with friends"'
            value={aiText}
            onChange={(e) => setAiText(e.target.value)}
            maxLength={300}
          />
          <button disabled={aiBusy}>{aiBusy ? 'Thinking...' : 'Add'}</button>
        </form>
      </section>

      <section className="card">
        <h2>Add manually</h2>
        <form className="row" onSubmit={addManual}>
          <input
            type="number"
            min="0.01"
            step="0.01"
            placeholder="Amount"
            value={manual.amount}
            onChange={(e) => setManual({ ...manual, amount: e.target.value })}
            required
          />
          <select value={manual.category} onChange={(e) => setManual({ ...manual, category: e.target.value })}>
            {CATEGORIES.map((c) => <option key={c}>{c}</option>)}
          </select>
          <input
            placeholder="Description"
            value={manual.description}
            onChange={(e) => setManual({ ...manual, description: e.target.value })}
          />
          <input
            type="date"
            value={manual.expenseDate}
            onChange={(e) => setManual({ ...manual, expenseDate: e.target.value })}
          />
          <button>Add</button>
        </form>
      </section>

      <section className="card">
        <h2>Spending by category</h2>
        <p className="total">Total: {money(total)}</p>
        {Object.keys(byCategory).length === 0 && <p className="muted">No expenses yet.</p>}
        {Object.entries(byCategory).map(([name, value]) => (
          <div className="bar-row" key={name}>
            <span>{name}</span>
            <div className="bar-track">
              <div className="bar" style={{ width: `${(value / max) * 100}%` }} />
            </div>
            <span>{money(value)}</span>
          </div>
        ))}
      </section>

      <section className="card">
        <h2>Your expenses</h2>
        {expenses.length === 0 ? (
          <p className="muted">Nothing here yet. Add your first expense above.</p>
        ) : (
          <table>
            <thead>
              <tr><th>Date</th><th>Category</th><th>Description</th><th className="right">Amount</th><th /></tr>
            </thead>
            <tbody>
              {expenses.map((x) => (
                <tr key={x.id}>
                  <td>{x.expenseDate}</td>
                  <td>{x.category}</td>
                  <td>{x.description}</td>
                  <td className="right">{money(x.amount)}</td>
                  <td><button className="secondary" onClick={() => remove(x.id)}>Delete</button></td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </section>
    </div>
  )
}

export default function App() {
  const [loggedIn, setLoggedIn] = useState(Boolean(getToken()))

  function logout() {
    setSession(null)
    setLoggedIn(false)
  }

  return loggedIn ? (
    <Dashboard onLogout={logout} />
  ) : (
    <AuthForm onLogin={() => setLoggedIn(true)} />
  )
}
