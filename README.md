# SpendSense AI — AI-Powered Expense Tracker

An AI-powered expense tracker that lets users describe transactions in everyday language, such as *"Spent 250 on biryani with friends"*, and converts them into structured expense records.

<p align="center">
  <a href="https://spendsense-ai.duckdns.org"><strong>🌐 Live Demo</strong></a>
  &nbsp; • &nbsp;
  <a href="https://github.com/AnushaPallemoni/spendsense-ai"><strong>📂 GitHub Repository</strong></a>
</p>

## Features

- **JWT authentication:** Register and log in securely.
- **User-specific expenses:** Users can access their own expense records.
- **AI-powered expense entry:** Convert natural-language descriptions into structured expense data using Spring AI and Google Gemini.
- **Manual expense entry:** Add expenses through a standard form.
- **Spending insights:** View spending totals and category-wise breakdowns.
- **AI response validation:** Validate generated amounts, categories, and dates before saving.
- **REST APIs:** Support authentication and expense management.
- **Cloud deployment:** Deployed on AWS EC2 using Docker Compose with HTTPS.

## Technology Stack

| Component | Technologies |
|---|---|
| Backend | Java 21, Spring Boot |
| Security | Spring Security, JWT |
| AI integration | Spring AI, Google Gemini API |
| Database | PostgreSQL |
| Frontend | React, Vite |
| Web server and HTTPS | Nginx, Caddy |
| Containerization | Docker, Docker Compose |
| Cloud | AWS EC2, Ubuntu |
| Testing | JUnit, Mockito |

## Architecture

```text
Browser
   |
   | HTTPS
   v
Caddy (TLS / HTTPS)
   |
   v
Nginx (React frontend)
   |
   | /api requests
   v
Spring Boot REST API
   |
   +------> PostgreSQL
   |
   +------> Google Gemini API
```

Caddy handles HTTPS and forwards browser traffic to Nginx. Nginx serves the React frontend and proxies API requests to Spring Boot. The backend and PostgreSQL communicate over the Docker network, keeping the database off the public internet.

## Live Deployment

## Demo Video

Watch the [SpendSense AI live demo video](https://github.com/AnushaPallemoni/spendsense-ai/issues/15#issue-6025105133).

**Live URL:** https://spendsense-ai.duckdns.org

The application is deployed on an Ubuntu-based AWS EC2 instance using Docker Compose. Caddy manages the HTTPS certificate, and Nginx serves the frontend and forwards API requests to the backend.

## Run Locally with Docker Compose

### Prerequisites

- Git
- Docker and Docker Compose
- Google Gemini API key for AI-powered expense entry

### Setup

**1. Clone the repository**

```bash
git clone https://github.com/AnushaPallemoni/spendsense-ai.git
cd spendsense-ai
```

**2. Configure environment variables**

```bash
cp .env.example .env
```

Edit `.env` with your local configuration and required credentials.

**3. Build and start the application**

```bash
docker compose up --build -d
```

**4. Open the application**

Visit [http://localhost:8081](http://localhost:8081) when using the default local configuration. If you change `WEB_PORT` in `.env`, use the configured port.

**Useful Docker commands**

```bash
docker compose ps
docker compose logs --tail=100
docker compose down
```

Never commit real API keys, database passwords, JWT secrets, or production environment files to GitHub.

## API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/register` | Register a new user |
| POST | `/api/auth/login` | Authenticate a user and obtain a JWT |
| GET | `/api/expenses` | Retrieve the authenticated user's expenses |
| POST | `/api/expenses` | Create an expense manually |
| POST | `/api/expenses/ai` | Create an expense from a natural-language description |
| DELETE | `/api/expenses/{id}` | Delete an expense owned by the authenticated user |

## Testing

The backend uses **JUnit and Mockito** for unit testing. Tests can be run from the backend project directory using the build tool configured in the project.

## What I Learned

- Developing REST APIs using Java and Spring Boot
- Implementing JWT authentication and user-specific data access
- Integrating React with a Spring Boot backend
- Connecting PostgreSQL to a full-stack application
- Integrating a generative AI API and validating its responses
- Containerizing services with Docker Compose
- Deploying a full-stack application to AWS EC2 with HTTPS
- Using Git and GitHub for version control

---

**Live Demo:** https://spendsense-ai.duckdns.org

**Source Code:** https://github.com/AnushaPallemoni/spendsense-ai

