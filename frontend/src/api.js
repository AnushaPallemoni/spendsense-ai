const TOKEN_KEY = 'spendsense_token'
const NAME_KEY = 'spendsense_name'

export const getToken = () => localStorage.getItem(TOKEN_KEY)
export const getName = () => localStorage.getItem(NAME_KEY) || ''

export function setSession(token, name) {
  if (token) {
    localStorage.setItem(TOKEN_KEY, token)
    localStorage.setItem(NAME_KEY, name || '')
  } else {
    localStorage.removeItem(TOKEN_KEY)
    localStorage.removeItem(NAME_KEY)
  }
}

const MESSAGES = {
  400: 'Please check the values you entered.',
  401: 'Invalid email or password.',
  409: 'This email is already registered.',
  422: 'Could not find an amount in your text.',
  502: 'AI service is unavailable, please try again.',
}

export async function api(path, options = {}) {
  const headers = { 'Content-Type': 'application/json', ...(options.headers || {}) }
  const token = getToken()
  if (token) headers.Authorization = `Bearer ${token}`

  const res = await fetch(path, { ...options, headers })

  if (res.status === 401 && token) {
    setSession(null)
    window.location.reload()
    throw new Error('Session expired. Please log in again.')
  }
  if (!res.ok) {
    throw new Error(MESSAGES[res.status] || 'Something went wrong. Please try again.')
  }
  return res.status === 204 ? null : res.json()
}