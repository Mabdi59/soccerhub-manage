# SoccerHub - Complete Tournament Management System

Complete soccer tournament management system with Java Spring Boot backend and React frontend.

## 🚀 Quick Start with Docker Compose (Recommended)

The easiest way to run the entire application:

```bash
# 1. Copy environment file
cp .env.example .env

# 2. Start all services (backend, frontend, database)
docker-compose up -d

# 3. Access the application
# Frontend: http://localhost:3000
# Backend API: http://localhost:8080
```

That's it! The application will be fully running with seed data.

## Project Structure

```
├── backend/              # Spring Boot REST API
├── frontend/             # React + Vite Dashboard
├── docker-compose.yml    # Docker orchestration
└── .env.example          # Environment variables template
```

## Manual Setup (Development)

### Backend
```bash
cd backend
./mvnw spring-boot:run
```
Runs on: http://localhost:8080

### Frontend
```bash
cd frontend
npm install
npm run dev
```
Runs on: http://localhost:5173

## Default Users

- **Admin**: `admin` / `admin123`
- **Organizer**: `organizer` / `organizer123`  
- **Referee**: `referee` / `referee123`

## Features

### Backend
- RESTful API with JWT auth
- Role-based access control (ADMIN/ORGANIZER/REFEREE)
- Automatic standings calculation
- PostgreSQL support
- Docker ready
- Seed data initialization

### Frontend
- Modern React 18 + Vite
- Admin dashboard with sidebar navigation
- Team management (CRUD)
- Match scheduling
- Referee score entry interface
- Real-time standings display
- Responsive design

## Pages

- **Overview**: Dashboard with statistics and recent matches
- **Teams**: Manage teams with CRUD operations
- **Schedule**: View and manage match schedule
- **Results**: Referee interface for entering match scores
- **Standings**: Auto-calculated league standings
- **Settings**: User profile and preferences

## Documentation

- [Backend README](backend/README.md)
- [Backend API Reference](backend/API_REFERENCE.md)
- [Frontend README](frontend/README.md)
- [Frontend Documentation](frontend/DOCUMENTATION.md)

## Technology Stack

**Backend:** Java 21, Spring Boot 3.2, Spring Security, JWT, PostgreSQL  
**Frontend:** React 18, Vite, React Router, Axios, Custom CSS  
**Infrastructure:** Docker, Docker Compose

## Docker

### Using Docker Compose (Recommended)
```bash
docker-compose up -d
```

### Individual Services
```bash
# Backend
cd backend && docker build -t soccerhub-backend .

# Frontend
cd frontend && docker build -t soccerhub-frontend .
```

## Environment Configuration

Copy `.env.example` to `.env` and customize:

```env
# Database
POSTGRES_DB=soccerhub
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres

# Backend
BACKEND_PORT=8080
JWT_SECRET=your-secret-key

# Frontend
FRONTEND_PORT=3000
VITE_API_URL=http://localhost:8080
```

## API Endpoints

### Authentication
- `POST /api/auth/register` - Register user
- `POST /api/auth/login` - Login and get JWT token

### Entities (Protected)
- `/api/organizations` - Organization management
- `/api/tournaments` - Tournament management
- `/api/divisions` - Division management
- `/api/teams` - Team management
- `/api/players` - Player management
- `/api/venues` - Venue management
- `/api/matches` - Match management
- `/api/standings` - View standings (read-only)

See [API Reference](backend/API_REFERENCE.md) for complete documentation.

## License

MIT
