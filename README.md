# SoccerHub - Complete Tournament Management System

Complete soccer tournament management system with Java Spring Boot backend and React frontend.

## Project Structure

```
├── backend/              # Spring Boot REST API
└── frontend/             # React + Vite Dashboard
```

## Quick Start

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
- Role-based access control
- Automatic standings calculation
- MySQL/PostgreSQL support
- Docker ready

### Frontend
- Modern React 18 + Vite
- Team management (CRUD)
- Match scheduling
- Referee score entry
- Real-time standings
- Responsive design

## Documentation

- [Backend README](backend/README.md)
- [Backend API Reference](backend/API_REFERENCE.md)
- [Frontend README](frontend/README.md)
- [Frontend Documentation](frontend/DOCUMENTATION.md)

## Technology Stack

**Backend:** Java 17, Spring Boot 3.x, Spring Security, JWT, MySQL  
**Frontend:** React 18, Vite, React Router, Axios, Custom CSS

## Docker

```bash
# Backend
cd backend && docker build -t soccerhub-backend .

# Frontend
cd frontend && docker build -t soccerhub-frontend .
```

## License

MIT
