# SpendSense AI

An expense tracker where you type a normal sentence like *"spent 250 on biryani with friends"* and AI saves it as a structured expense.

![Dashboard](docs/dashboard.png)
![AI input](docs/ai-result.png)

## Features
- Register and log in with JWT authentication
- Every user sees only their own expenses
- Add expenses by typing a sentence (Spring AI + Google Gemini), or with a manual form
- Spending totals and a bar per category
- AI output is validated (amount, category list, date) before it is saved

## Tech stack
Java 21, Spring Boot 4, Spring Security (JWT), Spring AI, PostgreSQL, React (Vite), Nginx, Docker, Docker Compose, AWS EC2, JUnit and Mockito.

## Architecture
~~~
Browser -> Nginx (React build, port 80) -> Spring Boot API -> PostgreSQL
                                                |
                                                +-> Gemini API
~~~
Only Nginx is exposed to the internet. The API and the database stay inside the Docker network.

## Run it locally with Docker
~~~bash
git clone https://github.com/AnushaPallemoni/spendsense-ai.git
cd spendsense-ai
cp .env.example .env     # then edit .env with your own values
docker compose up --build -d
~~~
Open http://localhost:8081 (set `WEB_PORT` in `.env`).

## API
| Method | Path | What it does |
|---|---|---|
| POST | /api/auth/register | Create an account |
| POST | /api/auth/login | Get a JWT |
| GET | /api/expenses | List your expenses |
| POST | /api/expenses | Add an expense |
| POST | /api/expenses/ai | Add an expense from a sentence |
| DELETE | /api/expenses/{id} | Delete one of your expenses |

## Deployment
Deployed on an AWS EC2 Ubuntu server with Docker Compose. [Live demo available on request.]

## What I learned
Git branching and pull requests, JWT security, writing unit tests with mocks, containerizing a full-stack app, cloud deployment, and handling an unreliable external AI API with retries and validation.
