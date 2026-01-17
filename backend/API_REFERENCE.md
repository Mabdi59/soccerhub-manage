# SoccerHub API Quick Reference

## Base URL
```
http://localhost:8080/api
```

## Authentication

### Register
```http
POST /auth/register
Content-Type: application/json

{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "password123",
  "role": "ORGANIZER"
}

Response: 201 Created
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "id": 1,
  "username": "john_doe",
  "email": "john@example.com",
  "role": "ORGANIZER"
}
```

### Login
```http
POST /auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}

Response: 200 OK
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "id": 1,
  "username": "admin",
  "email": "admin@soccerhub.com",
  "role": "ADMIN"
}
```

## Using JWT Token
For all protected endpoints, include the token in the Authorization header:
```http
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

---

## Organizations

### Get All Organizations
```http
GET /organizations
Authorization: Bearer {token}

Response: 200 OK
[
  {
    "id": 1,
    "name": "Premier Soccer League",
    "description": "The leading soccer organization",
    "createdAt": "2024-01-17T10:00:00",
    "updatedAt": "2024-01-17T10:00:00"
  }
]
```

### Create Organization
```http
POST /organizations
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "Regional Soccer Association",
  "description": "Community soccer organization"
}

Response: 201 Created
```

### Update Organization
```http
PUT /organizations/{id}
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "Updated Name",
  "description": "Updated description"
}

Response: 200 OK
```

### Delete Organization
```http
DELETE /organizations/{id}
Authorization: Bearer {token}

Response: 204 No Content
```

---

## Tournaments

### Get All Tournaments
```http
GET /tournaments
Authorization: Bearer {token}

# Filter by organization
GET /tournaments?organizationId=1
Authorization: Bearer {token}

Response: 200 OK
[
  {
    "id": 1,
    "name": "Spring Championship 2024",
    "organizationId": 1,
    "startDate": "2024-02-01",
    "endDate": "2024-05-01",
    "status": "UPCOMING",
    "createdAt": "2024-01-17T10:00:00",
    "updatedAt": "2024-01-17T10:00:00"
  }
]
```

### Create Tournament
```http
POST /tournaments
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "Summer Cup 2024",
  "organizationId": 1,
  "startDate": "2024-06-01",
  "endDate": "2024-08-31",
  "status": "UPCOMING"
}

Response: 201 Created
```

**Status values**: UPCOMING, IN_PROGRESS, COMPLETED, CANCELLED

---

## Divisions

### Get All Divisions
```http
GET /divisions
Authorization: Bearer {token}

# Filter by tournament
GET /divisions?tournamentId=1
Authorization: Bearer {token}

Response: 200 OK
[
  {
    "id": 1,
    "name": "Men's Premier Division",
    "tournamentId": 1,
    "createdAt": "2024-01-17T10:00:00",
    "updatedAt": "2024-01-17T10:00:00"
  }
]
```

### Create Division
```http
POST /divisions
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "Women's Division 1",
  "tournamentId": 1
}

Response: 201 Created
```

---

## Teams

### Get All Teams
```http
GET /teams
Authorization: Bearer {token}

# Filter by division
GET /teams?divisionId=1
Authorization: Bearer {token}

Response: 200 OK
[
  {
    "id": 1,
    "name": "FC Thunder",
    "divisionId": 1,
    "logo": null,
    "createdAt": "2024-01-17T10:00:00",
    "updatedAt": "2024-01-17T10:00:00"
  }
]
```

### Create Team
```http
POST /teams
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "United FC",
  "divisionId": 1,
  "logo": "https://example.com/logo.png"
}

Response: 201 Created
```

---

## Players

### Get All Players
```http
GET /players
Authorization: Bearer {token}

# Filter by team
GET /players?teamId=1
Authorization: Bearer {token}

Response: 200 OK
[
  {
    "id": 1,
    "firstName": "John",
    "lastName": "Smith",
    "teamId": 1,
    "jerseyNumber": 10,
    "position": "Forward",
    "createdAt": "2024-01-17T10:00:00",
    "updatedAt": "2024-01-17T10:00:00"
  }
]
```

### Create Player
```http
POST /players
Authorization: Bearer {token}
Content-Type: application/json

{
  "firstName": "Michael",
  "lastName": "Jordan",
  "teamId": 1,
  "jerseyNumber": 23,
  "position": "Midfielder"
}

Response: 201 Created
```

---

## Venues

### Get All Venues
```http
GET /venues
Authorization: Bearer {token}

Response: 200 OK
[
  {
    "id": 1,
    "name": "Central Stadium",
    "address": "123 Main Street",
    "city": "Springfield",
    "capacity": 50000,
    "createdAt": "2024-01-17T10:00:00",
    "updatedAt": "2024-01-17T10:00:00"
  }
]
```

### Create Venue
```http
POST /venues
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "City Arena",
  "address": "456 Park Avenue",
  "city": "Metro City",
  "capacity": 25000
}

Response: 201 Created
```

---

## Matches

### Get All Matches
```http
GET /matches
Authorization: Bearer {token}

# Filter by division
GET /matches?divisionId=1
Authorization: Bearer {token}

# Filter by team
GET /matches?teamId=1
Authorization: Bearer {token}

Response: 200 OK
[
  {
    "id": 1,
    "divisionId": 1,
    "homeTeamId": 1,
    "awayTeamId": 2,
    "venueId": 1,
    "matchDate": "2024-02-10T15:00:00",
    "homeScore": null,
    "awayScore": null,
    "status": "SCHEDULED",
    "refereeId": 3,
    "createdAt": "2024-01-17T10:00:00",
    "updatedAt": "2024-01-17T10:00:00"
  }
]
```

### Create Match
```http
POST /matches
Authorization: Bearer {token}
Content-Type: application/json

{
  "divisionId": 1,
  "homeTeamId": 1,
  "awayTeamId": 2,
  "venueId": 1,
  "matchDate": "2024-02-15T18:00:00",
  "status": "SCHEDULED",
  "refereeId": 3
}

Response: 201 Created
```

**Status values**: SCHEDULED, IN_PROGRESS, COMPLETED, POSTPONED, CANCELLED

### Update Match Result
```http
PATCH /matches/{id}/result
Authorization: Bearer {token}
Content-Type: application/json

{
  "homeScore": 2,
  "awayScore": 1
}

Response: 200 OK
```
**Note**: This automatically updates the standings for both teams!

---

## Standings

### Get Standings by Division
```http
GET /standings?divisionId=1

Response: 200 OK
[
  {
    "id": 1,
    "divisionId": 1,
    "teamId": 1,
    "played": 5,
    "won": 3,
    "drawn": 1,
    "lost": 1,
    "goalsFor": 10,
    "goalsAgainst": 6,
    "goalDifference": 4,
    "points": 10,
    "createdAt": "2024-01-17T10:00:00",
    "updatedAt": "2024-01-17T10:30:00"
  }
]
```
**Note**: Standings endpoint is public (no authentication required)

**Points System**:
- Win: 3 points
- Draw: 1 point
- Loss: 0 points

**Sorting**: By points (descending), then goal difference (descending)

---

## Error Responses

### 400 Bad Request
```json
{
  "timestamp": "2024-01-17T10:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Username is already taken",
  "path": "/api/auth/register"
}
```

### 401 Unauthorized
```json
{
  "timestamp": "2024-01-17T10:00:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Invalid username or password",
  "path": "/api/auth/login"
}
```

### 403 Forbidden
```json
{
  "timestamp": "2024-01-17T10:00:00",
  "status": 403,
  "error": "Forbidden",
  "message": "Access denied",
  "path": "/api/organizations"
}
```

### 404 Not Found
```json
{
  "timestamp": "2024-01-17T10:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Team not found with id: 99",
  "path": "/api/teams/99"
}
```

### Validation Errors
```json
{
  "username": "Username must be between 3 and 50 characters",
  "email": "Email should be valid",
  "password": "Password must be at least 6 characters"
}
```

---

## Quick Testing with cURL

### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

### Get Tournaments (with token)
```bash
TOKEN="your_jwt_token_here"

curl -X GET http://localhost:8080/api/tournaments \
  -H "Authorization: Bearer $TOKEN"
```

### Create Match
```bash
curl -X POST http://localhost:8080/api/matches \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "divisionId": 1,
    "homeTeamId": 1,
    "awayTeamId": 2,
    "venueId": 1,
    "matchDate": "2024-02-15T18:00:00",
    "status": "SCHEDULED",
    "refereeId": 3
  }'
```

### Update Match Result
```bash
curl -X PATCH http://localhost:8080/api/matches/1/result \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"homeScore": 3, "awayScore": 1}'
```

### Get Standings (public, no auth)
```bash
curl -X GET http://localhost:8080/api/standings?divisionId=1
```

---

## Access Control Summary

| Endpoint | Admin | Organizer | Referee | Public |
|----------|-------|-----------|---------|--------|
| /auth/** | ✓ | ✓ | ✓ | ✓ |
| /organizations/** | ✓ | ✓ | ✗ | ✗ |
| /tournaments/** | ✓ | ✓ | ✗ | ✗ |
| /divisions/** | ✓ | ✓ | ✗ | ✗ |
| /teams/** | ✓ | ✓ | ✗ | ✗ |
| /players/** | ✓ | ✓ | ✗ | ✗ |
| /venues/** | ✓ | ✓ | ✗ | ✗ |
| /matches/** | ✓ | ✓ | ✓ | ✗ |
| /standings/** | ✓ | ✓ | ✓ | ✓ |

---

## Tips

1. **Always include the JWT token** in the Authorization header for protected endpoints
2. **Token expires after 24 hours** by default - user needs to login again
3. **Match results automatically update standings** - no manual calculation needed
4. **Updating a completed match** will reverse the old standings and apply new ones
5. **Deleting a completed match** will reverse its standings impact
6. **Home and away teams must be different** - validation will fail otherwise
7. **Standings are sorted automatically** by points descending, then goal difference

---

## Development Notes

- Server runs on port 8080 by default
- CORS is enabled for all origins (configure in production)
- All timestamps are in ISO 8601 format
- Database schema is auto-created on first run
- Seed data is loaded if `SEED_ENABLED=true` (default)
