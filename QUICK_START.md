# 🚀 Quick Start Guide

Get your soccer tournament manager up and running in 3 minutes!

## Prerequisites

- Docker and Docker Compose installed
- OR Java 17+ and Node.js 18+ for local development

## Method 1: Docker Compose (Recommended) ⭐

### Step 1: Clone and Setup
```bash
git clone https://github.com/Mabdi59/soccerhub-manage.git
cd soccerhub-manage
cp .env.example .env
```

### Step 2: Start All Services
```bash
docker-compose up -d
```

This will start:
- PostgreSQL database on port 5432
- Backend API on port 8080
- Frontend on port 3000

### Step 3: Access the Application
Open your browser and go to:
```
http://localhost:3000
```

### Step 4: Login
Use one of the default accounts:
- **Admin**: `admin` / `admin123`
- **Organizer**: `organizer` / `organizer123`
- **Referee**: `referee` / `referee123`

### Step 5: Start Managing!
You now have access to:
- ✅ Tournament management
- ✅ Team and player management
- ✅ Match scheduling
- ✅ Score entry
- ✅ Live standings

## Method 2: Local Development

### Backend Setup
```bash
cd backend
mvn spring-boot:run
```
Backend runs on http://localhost:8080

### Frontend Setup
In a new terminal:
```bash
cd frontend
npm install
npm run dev
```
Frontend runs on http://localhost:5173

### Database
Make sure PostgreSQL is running on localhost:5432 with database `soccerhub`.

## What's Included?

The application comes pre-loaded with sample data:

### Users
- 1 Admin user
- 1 Organizer user
- 1 Referee user

### Tournament Data
- 1 Organization (Premier Soccer League)
- 1 Tournament (Spring Championship 2024)
- 2 Divisions (Division A, Division B)
- 4 Teams (Thunder FC, Lightning United, Storm City, Galaxy FC)
- 16 Players (4 per team)
- 2 Venues (Central Stadium, North Arena)
- 4 Matches with results
- Auto-calculated standings

## Verify Installation

### Check Backend
```bash
curl http://localhost:8080/api/standings?divisionId=1
```

You should see standings data in JSON format.

### Check Frontend
Open http://localhost:3000 in your browser. You should see the login page.

## Stopping the Application

### Docker Compose
```bash
docker-compose down
```

### Local Development
Press `Ctrl+C` in both terminal windows.

## Common Issues

### Port Already in Use
If port 3000 or 8080 is already in use, edit `.env`:
```env
FRONTEND_PORT=3001
BACKEND_PORT=8081
```

### Database Connection Error
Make sure PostgreSQL is running:
```bash
docker-compose ps
```

### Build Errors
For backend:
```bash
cd backend
mvn clean install
```

For frontend:
```bash
cd frontend
npm install
rm -rf node_modules package-lock.json
npm install
```

## Next Steps

1. **Explore the Dashboard**: Check out the Overview page for statistics
2. **Create a Team**: Go to Teams page and add your first team
3. **Schedule a Match**: Use the Schedule page to create matches
4. **Enter Scores**: Use the Results page to enter match scores
5. **View Standings**: Check the Standings page to see auto-calculated rankings

## Customization

### Change JWT Secret (Production)
Edit `.env`:
```env
JWT_SECRET=your-strong-256-bit-secret-key
```

### Change Database Credentials
Edit `.env`:
```env
POSTGRES_USER=yourusername
POSTGRES_PASSWORD=yourpassword
```

### Change API URL (Frontend)
Edit `frontend/.env`:
```env
VITE_API_URL=http://your-backend-url:8080
```

## Production Deployment

### Build for Production
```bash
# Backend
cd backend
mvn clean package

# Frontend
cd frontend
npm run build
```

### Deploy with Docker
```bash
docker-compose -f docker-compose.yml up -d --build
```

### Environment Variables
For production, set:
```env
HIBERNATE_DDL_AUTO=validate
JWT_SECRET=<strong-random-secret>
POSTGRES_PASSWORD=<strong-password>
```

## API Testing

### Get JWT Token
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

### Use Token to Access API
```bash
curl http://localhost:8080/api/teams \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

## Help & Support

- 📖 [Full Documentation](README.md)
- 🔧 [API Reference](backend/API_REFERENCE.md)
- 🎨 [Frontend Guide](frontend/DOCUMENTATION.md)
- 🔒 [Security Summary](SECURITY_SUMMARY.md)
- 📋 [Implementation Details](IMPLEMENTATION_SUMMARY.md)

## Troubleshooting

### Frontend can't connect to backend
1. Check backend is running: `curl http://localhost:8080/api/standings?divisionId=1`
2. Check VITE_API_URL in `frontend/.env`
3. Check CORS settings in backend SecurityConfig

### Can't login
1. Verify backend is running
2. Check console for errors (F12 in browser)
3. Verify credentials: `admin` / `admin123`

### Standings not updating
1. Make sure match has both scores entered
2. Check match status is COMPLETED
3. Refresh the Standings page

## Development Tips

### Hot Reload
- **Frontend**: Vite provides instant hot reload
- **Backend**: Use `mvn spring-boot:run` for auto-restart on changes

### Database Reset
To reset the database with fresh seed data:
```bash
docker-compose down -v
docker-compose up -d
```

### View Logs
```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f backend
docker-compose logs -f frontend
```

## Success! 🎉

You're now ready to manage soccer tournaments like a pro!

---

**Need help?** Open an issue on GitHub or check the documentation files.