# SoccerHub Frontend

Modern React + Vite admin dashboard for the SoccerHub Tournament Manager.

## Features

- 🎨 Clean and professional UI with custom CSS
- 🔐 JWT-based authentication
- 👥 Team management (CRUD operations)
- 📅 Match scheduling
- ⚽ Referee score entry interface
- 🏆 Real-time standings
- 📊 Dashboard with statistics
- 🔄 Automatic standings calculation
- 📱 Responsive design
- 🎯 Role-based access (Admin, Organizer, Referee)

## Getting Started

### Installation

```bash
npm install
```

### Configure Environment

```bash
cp .env.example .env
```

Edit `.env`:
```
VITE_API_URL=http://localhost:8080/api
```

### Development

```bash
npm run dev
```

App available at `http://localhost:5173`

### Build

```bash
npm run build
```

## Default Users

- **Admin**: `admin` / `admin123`
- **Organizer**: `organizer` / `organizer123`
- **Referee**: `referee` / `referee123`

## Docker

```bash
docker build -t soccerhub-frontend .
docker run -p 80:80 soccerhub-frontend
```
