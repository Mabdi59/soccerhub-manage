# SoccerHub Backend

This is the backend service for the SoccerHub Soccer Tournament Management System.

## Technology Stack

- Java 21
- Spring Boot 3.2.0
- PostgreSQL
- JWT Authentication
- Maven

## Prerequisites

- Java 21 or higher
- Maven 3.6 or higher
- PostgreSQL 12 or higher

## Configuration

The application can be configured using environment variables:

### Database Configuration
- `DATABASE_URL`: PostgreSQL connection URL (default: `jdbc:postgresql://localhost:5432/soccerhub`)
- `DATABASE_USERNAME`: Database username (default: `postgres`)
- `DATABASE_PASSWORD`: Database password (default: `postgres`)

### JWT Configuration
- `JWT_SECRET`: Secret key for JWT token generation (default: provided in application.properties)
- `JWT_EXPIRATION`: Token expiration time in milliseconds (default: `86400000` - 24 hours)

### Application Configuration
- `SEED_ENABLED`: Enable/disable data seeding on startup (default: `true`)

## Running the Application

### Local Development

1. Ensure PostgreSQL is running and create a database named `soccerhub`:
   ```bash
   createdb soccerhub
   ```

2. Build the application:
   ```bash
   mvn clean package
   ```

3. Run the application:
   ```bash
   java -jar target/backend-1.0.0.jar
   ```

   Or using Maven:
   ```bash
   mvn spring-boot:run
   ```

The application will start on `http://localhost:8080`

### Using Docker

1. Build the Docker image:
   ```bash
   docker build -t soccerhub-backend .
   ```

2. Run the container:
   ```bash
   docker run -p 8080:8080 \
     -e DATABASE_URL=jdbc:postgresql://host.docker.internal:5432/soccerhub \
     -e DATABASE_USERNAME=postgres \
     -e DATABASE_PASSWORD=postgres \
     soccerhub-backend
   ```

## API Endpoints

### Authentication
- `POST /api/auth/register` - Register a new user
- `POST /api/auth/login` - Login and receive JWT token

### Organizations
- `GET /api/organizations` - Get all organizations
- `GET /api/organizations/{id}` - Get organization by ID
- `POST /api/organizations` - Create organization
- `PUT /api/organizations/{id}` - Update organization
- `DELETE /api/organizations/{id}` - Delete organization

### Tournaments
- `GET /api/tournaments` - Get all tournaments
- `GET /api/tournaments?organizationId={id}` - Get tournaments by organization
- `GET /api/tournaments/{id}` - Get tournament by ID
- `POST /api/tournaments` - Create tournament
- `PUT /api/tournaments/{id}` - Update tournament
- `DELETE /api/tournaments/{id}` - Delete tournament

### Divisions
- `GET /api/divisions` - Get all divisions
- `GET /api/divisions?tournamentId={id}` - Get divisions by tournament
- `GET /api/divisions/{id}` - Get division by ID
- `POST /api/divisions` - Create division
- `PUT /api/divisions/{id}` - Update division
- `DELETE /api/divisions/{id}` - Delete division

### Teams
- `GET /api/teams` - Get all teams
- `GET /api/teams?divisionId={id}` - Get teams by division
- `GET /api/teams/{id}` - Get team by ID
- `POST /api/teams` - Create team
- `PUT /api/teams/{id}` - Update team
- `DELETE /api/teams/{id}` - Delete team

### Players
- `GET /api/players` - Get all players
- `GET /api/players?teamId={id}` - Get players by team
- `GET /api/players/{id}` - Get player by ID
- `POST /api/players` - Create player
- `PUT /api/players/{id}` - Update player
- `DELETE /api/players/{id}` - Delete player

### Venues
- `GET /api/venues` - Get all venues
- `GET /api/venues/{id}` - Get venue by ID
- `POST /api/venues` - Create venue
- `PUT /api/venues/{id}` - Update venue
- `DELETE /api/venues/{id}` - Delete venue

### Matches
- `GET /api/matches` - Get all matches
- `GET /api/matches?divisionId={id}` - Get matches by division
- `GET /api/matches?teamId={id}` - Get matches by team
- `GET /api/matches/{id}` - Get match by ID
- `POST /api/matches` - Create match
- `PUT /api/matches/{id}` - Update match
- `PATCH /api/matches/{id}/result` - Update match result (automatically updates standings)
- `DELETE /api/matches/{id}` - Delete match

### Standings
- `GET /api/standings?divisionId={id}` - Get standings by division

## Default Seed Data

When `SEED_ENABLED=true`, the application creates:

- 3 users:
  - Admin: username=`admin`, password=`admin123`
  - Organizer: username=`organizer`, password=`organizer123`
  - Referee: username=`referee`, password=`referee123`
- 2 organizations
- 2 tournaments
- 2 divisions
- 4 teams
- 4 players
- 2 venues
- 2 matches

## Security

The API uses JWT-based authentication. To access protected endpoints:

1. Login or register to get a JWT token
2. Include the token in the `Authorization` header: `Bearer <token>`

### Role-Based Access:
- **ADMIN**: Full access to all endpoints
- **ORGANIZER**: Access to manage organizations, tournaments, divisions, teams, players, venues, and matches
- **REFEREE**: Access to view and update match results
- Public access to standings (no authentication required)

## Development

### Project Structure
```
src/main/java/com/soccerhub/backend/
├── config/          # Configuration classes
├── controller/      # REST controllers
├── dto/            # Data Transfer Objects
├── entity/         # JPA entities
├── exception/      # Exception handling
├── repository/     # JPA repositories
├── security/       # Security configuration
└── service/        # Business logic
```

### Building
```bash
mvn clean install
```

### Testing
```bash
mvn test
```

## License

Copyright © 2024 SoccerHub
