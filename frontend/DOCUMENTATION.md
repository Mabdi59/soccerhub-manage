# SoccerHub Frontend - Complete React Dashboard

## Overview

A complete, production-ready React admin dashboard for managing soccer tournaments. Built with React 18, Vite, and modern best practices.

## 🎯 Key Features

### Authentication & Security
- JWT-based authentication with automatic token management
- Role-based access control (Admin, Organizer, Referee)
- Protected routes with automatic redirects
- Auto-logout on token expiration
- Secure localStorage token storage

### User Roles & Permissions

**Admin & Organizer:**
- Full dashboard access
- Team management (CRUD)
- Match scheduling
- View all results
- Access all features

**Referee:**
- View assigned matches only
- Enter and update match scores
- Limited dashboard access

### Core Features

1. **Dashboard (Overview)**
   - Statistics cards (teams, matches, completed, upcoming)
   - Recent match results
   - Top 5 standings preview
   - Real-time data updates

2. **Teams Management**
   - Create, read, update, delete teams
   - Assign teams to divisions
   - Logo URL support
   - Modal-based forms
   - Data validation

3. **Match Scheduling**
   - Schedule new matches
   - Edit existing matches
   - Delete matches
   - Assign referees
   - Set venues
   - Multiple match statuses (SCHEDULED, IN_PROGRESS, COMPLETED, etc.)
   - Date/time picker

4. **Results (Score Entry)**
   - Special interface for referees
   - Card-based match display
   - Inline score editing
   - Submit scores to update standings
   - Real-time standings calculation
   - Status badges

5. **Standings**
   - Division-filtered standings
   - Sortable table
   - Points, wins, draws, losses
   - Goal difference calculation
   - Color-coded top positions
   - Automatic calculations

6. **Settings**
   - User profile view
   - Role display
   - Password change (placeholder)
   - Account management

## 📁 Project Structure

```
frontend/
├── src/
│   ├── components/          # Reusable UI components
│   │   ├── Layout.jsx      # Main layout with sidebar & header
│   │   ├── Sidebar.jsx     # Navigation sidebar
│   │   ├── Header.jsx      # Page header
│   │   ├── Modal.jsx       # Reusable modal
│   │   └── Loading.jsx     # Loading spinner
│   │
│   ├── pages/              # Page components
│   │   ├── Login.jsx       # Authentication
│   │   ├── Overview.jsx    # Dashboard
│   │   ├── Teams.jsx       # Team management
│   │   ├── Schedule.jsx    # Match scheduling
│   │   ├── Results.jsx     # Score entry
│   │   ├── Standings.jsx   # League table
│   │   └── Settings.jsx    # User settings
│   │
│   ├── services/           # API integration
│   │   └── api.js         # Axios client & endpoints
│   │
│   ├── context/            # React Context
│   │   └── AuthContext.jsx # Authentication state
│   │
│   ├── utils/              # Helper functions
│   │   └── helpers.js     # Date formatting, status colors
│   │
│   ├── App.jsx            # Main app & routing
│   ├── main.jsx           # Entry point
│   └── index.css          # Global styles
│
├── public/                 # Static assets
├── .env                    # Environment variables
├── .env.example           # Environment template
├── Dockerfile             # Container definition
├── nginx.conf             # Nginx configuration
├── package.json           # Dependencies
└── README.md              # Documentation
```

## 🚀 Quick Start

### Prerequisites
- Node.js 18+
- npm or yarn
- Backend API running (default: http://localhost:8080)

### Installation

1. **Install dependencies:**
```bash
cd frontend
npm install
```

2. **Configure environment:**
```bash
cp .env.example .env
```

Edit `.env`:
```env
VITE_API_URL=http://localhost:8080/api
```

3. **Start development server:**
```bash
npm run dev
```

Visit: `http://localhost:5173`

### Build for Production

```bash
npm run build
npm run preview  # Test production build
```

## 🐳 Docker Deployment

### Build Image
```bash
docker build -t soccerhub-frontend .
```

### Run Container
```bash
docker run -p 80:80 soccerhub-frontend
```

### With Custom API URL
```bash
docker run -p 80:80 -e VITE_API_URL=http://api.example.com soccerhub-frontend
```

## 🔌 API Integration

### Axios Configuration
- Base URL from environment variable
- Automatic JWT token injection
- Request/response interceptors
- Error handling with auto-logout
- Centralized API methods

### Available API Methods

**Authentication:**
- `authAPI.login(credentials)`
- `authAPI.register(userData)`

**Teams:**
- `teamsAPI.getAll(divisionId?)`
- `teamsAPI.create(data)`
- `teamsAPI.update(id, data)`
- `teamsAPI.delete(id)`

**Matches:**
- `matchesAPI.getAll(params)`
- `matchesAPI.create(data)`
- `matchesAPI.update(id, data)`
- `matchesAPI.updateResult(id, result)`
- `matchesAPI.delete(id)`

**Standings:**
- `standingsAPI.getByDivision(divisionId)`

And more for divisions, venues, players, tournaments, organizations...

## 🎨 Styling

### CSS Architecture
- **Custom CSS** (no external UI library)
- **CSS Variables** for theming
- **Utility classes** for spacing
- **Responsive design** (mobile-first)
- **Professional color palette**

### Color Scheme
- Primary: Blue (#2563eb)
- Success: Green (#10b981)
- Danger: Red (#ef4444)
- Warning: Orange (#f59e0b)
- Grays: 50-900 scale

### Components
- Cards with shadows
- Data tables with hover effects
- Modal overlays
- Badges for status
- Loading spinners
- Form controls with focus states

## 🔐 Authentication Flow

1. User logs in at `/login`
2. JWT token received and stored in localStorage
3. Token added to all API requests via interceptor
4. User data stored in Auth Context
5. Protected routes check authentication
6. Auto-redirect to login if unauthorized
7. Logout clears token and redirects

## 📊 State Management

### Context API
- **AuthContext**: User authentication state
- Provides: `user`, `login`, `logout`, `isAuthenticated`, `isAdmin`, `isOrganizer`, `isReferee`
- Used throughout app for role-based rendering

### Local State
- Component-level state with useState
- Form data management
- Loading states
- Modal visibility
- Error handling

## 🛠️ Development

### Available Scripts

```bash
npm run dev      # Start dev server
npm run build    # Build for production
npm run preview  # Preview production build
npm run lint     # Lint code (if configured)
```

### Adding New Features

1. **New Page:**
   - Create component in `src/pages/`
   - Add route in `App.jsx`
   - Add navigation item in `Sidebar.jsx`

2. **New API Endpoint:**
   - Add method in `src/services/api.js`
   - Use in page component

3. **New Component:**
   - Create in `src/components/`
   - Import and use in pages

## 📱 Responsive Design

- **Desktop**: Full sidebar + content area
- **Tablet**: Optimized layouts
- **Mobile**: Hidden sidebar, stacked layouts

Breakpoint: 768px

## 🔒 Security Best Practices

✅ JWT tokens in localStorage (not cookies due to CORS)
✅ Automatic token expiration handling
✅ Protected routes
✅ Role-based access control
✅ Input validation
✅ XSS prevention (React default)
✅ API error handling

## 🧪 Testing

To test the application:

1. Start backend with seed data
2. Login with default users:
   - Admin: `admin` / `admin123`
   - Organizer: `organizer` / `organizer123`
   - Referee: `referee` / `referee123`

3. Test features:
   - Create teams
   - Schedule matches
   - Enter scores (as referee)
   - View standings

## 🐛 Troubleshooting

**Build fails:**
- Check Node.js version (18+)
- Delete `node_modules` and reinstall
- Clear Vite cache: `rm -rf node_modules/.vite`

**API calls fail:**
- Verify backend is running
- Check VITE_API_URL in .env
- Check browser console for CORS errors
- Verify JWT token in localStorage

**Auth issues:**
- Clear localStorage
- Re-login
- Check token expiration

## 📦 Dependencies

### Core
- `react` ^18.3.1
- `react-dom` ^18.3.1
- `react-router-dom` ^7.1.3
- `axios` ^1.7.9

### Dev Dependencies
- `vite` ^7.3.1
- `@vitejs/plugin-react` ^4.3.4

## 🚀 Production Checklist

- [ ] Set production API URL in .env
- [ ] Build application: `npm run build`
- [ ] Test production build: `npm run preview`
- [ ] Configure CORS on backend
- [ ] Set up HTTPS
- [ ] Configure nginx for SPA routing
- [ ] Set secure headers
- [ ] Enable gzip compression
- [ ] Monitor error logs

## 📝 Future Enhancements

Potential improvements:
- [ ] Player management UI
- [ ] Venue management UI
- [ ] Tournament/division management
- [ ] Organization management
- [ ] File upload for team logos
- [ ] Live match updates (WebSocket)
- [ ] Statistics and charts
- [ ] Export standings to PDF
- [ ] Email notifications
- [ ] Advanced filtering and search
- [ ] Dark mode
- [ ] Internationalization (i18n)

## 📄 License

MIT License

## 👥 Support

For issues or questions:
1. Check documentation
2. Review API reference
3. Check browser console for errors
4. Verify backend is running
5. Create GitHub issue

---

Built with ❤️ using React + Vite
