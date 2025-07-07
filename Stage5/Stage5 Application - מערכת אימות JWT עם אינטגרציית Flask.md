
<div dir="rtl">

# Stage5 Application - מערכת אימות JWT עם אינטגרציית Flask

## סקירה כללית

זוהי מערכת אימות מתקדמת המשלבת שלוש טכנולוגיות:
- **React Frontend** - ממשק משתמש מודרני עם Vite
- **Spring Boot Backend** - שרת Java עם אבטחת JWT
- **Flask Python Service** - שירות עיבוד נתונים נוסף
- **MySQL Database** - בסיס נתונים יחסי

## ארכיטקטורה כללית

```mermaid
graph TB
    subgraph "Client Layer"
        A[React Vite App]
        A1[AuthContext Provider]
        A2[Protected Routes]
        A3[UI Components]
    end
    
    subgraph "Spring Boot Backend"
        B[Spring Security]
        B1[JWT Authentication Filter]
        B2[Controllers Layer]
        B3[Services Layer]
        B4[Repository Layer]
    end
    
    subgraph "External Services"
        C[Flask Python Service]
        C1[REST API Endpoints]
        C2[Data Processing]
    end
    
    subgraph "Database Layer"
        D[MySQL Database]
        D1[User Table]
        D2[Role Table]
        D3[Join Tables]
    end
    
    subgraph "Security Components"
        E[JWT Tokens]
        E1[Access Token]
        E2[Refresh Token]
        E3[Token Blacklist]
    end
    
    A --> B
    B --> C
    B --> D
    B --> E
    
    A -.->|sessionStorage| F[Browser Storage]
```

## זרימת אימות מלאה

```mermaid
sequenceDiagram
    participant U as User Browser
    participant R as React App
    participant S as Spring Security
    participant DB as MySQL Database
    participant F as Flask Service
    
    Note over U,F: Login Process
    U->>R: הזנת פרטי התחברות
    R->>S: POST /api/login
    S->>DB: בדיקת משתמש וסיסמה
    DB->>S: אישור פרטים
    S->>S: יצירת JWT Tokens עם IP
    S->>R: החזרת Access + Refresh tokens
    R->>R: שמירה ב-sessionStorage
    
    Note over U,F: Protected API Call
    R->>S: API request עם JWT token
    S->>S: אימות טוקן + IP validation
    S->>F: Forward request ל-Flask
    F->>F: עיבוד נתונים
    F->>S: Response עם נתונים
    S->>R: החזרת תגובה ל-React
    R->>U: הצגת תוצאות
    
    Note over U,F: Token Refresh
    R->>S: Refresh token request
    S->>S: אימות refresh token
    S->>S: Token rotation - blacklist old token
    S->>R: החזרת טוקנים חדשים
```

## מבנה Frontend - React

### Component Hierarchy

```mermaid
graph TD
    A[main.jsx] --> B[App.jsx]
    B --> C[AuthProvider]
    C --> D[ProtectedRoute]
    D --> E[Home.jsx]
    
    E --> F[Dashboard Tab]
    E --> G[Flask Test Tab]
    
    F --> H[User Information]
    F --> I[Admin Panel]
    F --> J[Token Display]
    
    G --> K[SimpleFlask.jsx]
    K --> L[GET Test Button]
    K --> M[POST Test Button]
    
    N[Login.jsx] --> C
    O[TokenDisplay.jsx] --> F
```

### State Management Flow

```mermaid
graph LR
    A[AuthContext] --> B[useState Hooks]
    A --> C[useEffect Hooks]
    A --> D[Custom Functions]
    
    B --> B1[user state]
    B --> B2[accessToken state]
    B --> B3[refreshToken state]
    B --> B4[loading state]
    
    D --> D1[login function]
    D --> D2[logout function]
    D --> D3[authenticatedFetch]
    D --> D4[handleRefreshToken]
    
    E[sessionStorage] --> F[Token Persistence]
    D3 --> G[Auto Token Refresh]
```

### Authentication Flow in React

```mermaid
stateDiagram-v2
    [*] --> Loading: App מתחיל
    Loading --> CheckingTokens: בדיקת sessionStorage
    
    CheckingTokens --> NoTokens: אין טוקנים
    CheckingTokens --> ValidTokens: טוקנים קיימים ותקפים
    CheckingTokens --> ExpiredTokens: טוקנים פגו תוקף
    
    NoTokens --> ShowLogin: הצגת Login
    ExpiredTokens --> ShowLogin: ניקוי storage
    
    ShowLogin --> LoginAttempt: משתמש מזין פרטים
    LoginAttempt --> LoginSuccess: Spring Boot מאמת
    LoginAttempt --> LoginError: שגיאת אימות
    
    LoginError --> ShowLogin: הצגת שגיאה
    LoginSuccess --> LoggedIn: שמירת טוקנים
    ValidTokens --> LoggedIn: מעבר לדף בית
    
    LoggedIn --> APICall: בקשת API
    APICall --> TokenCheck: בדיקת תוקף טוקן
    TokenCheck --> TokenValid: עוד יותר מ-2 דקות
    TokenCheck --> TokenExpiring: פחות מ-2 דקות
    
    TokenValid --> APISuccess: בקשה עם טוקן נוכחי
    TokenExpiring --> AutoRefresh: רענון אוטומטי
    AutoRefresh --> RefreshSuccess: טוקנים חדשים
    AutoRefresh --> RefreshFailed: רענון נכשל
    
    RefreshSuccess --> APISuccess: בקשה עם טוקן חדש
    RefreshFailed --> ShowLogin: חזרה להתחברות
    APISuccess --> LoggedIn: המשך פעילות
    
    LoggedIn --> UserLogout: לחיצה על Logout
    UserLogout --> ShowLogin: ניקוי מלא
```

## Backend Architecture - Spring Boot

### Layer Structure

```mermaid
graph TB
    subgraph "Controller Layer"
        A[AuthenticationController]
        B[UserController]
        C[SimpleController]
    end
    
    subgraph "Service Layer"
        D[AuthenticationService]
        E[RefreshTokenService]
        F[SimpleFlaskService]
        G[TokenBlacklistService]
        H[CustomUserDetailsService]
    end
    
    subgraph "Repository Layer"
        I[UserRepository]
        J[RoleRepository]
    end
    
    subgraph "Entity Layer"
        K[User Entity]
        L[Role Entity]
    end
    
    subgraph "Security Layer"
        M[SecurityConfig]
        N[JwtAuthenticationFilter]
        O[JwtUtil]
        P[CustomLogoutHandler]
    end
    
    subgraph "Configuration"
        Q[RestTemplate Config]
        R[DataInitializer]
    end
    
    A --> D
    B --> H
    C --> F
    D --> G
    E --> G
    F --> Q
    H --> I
    I --> K
    J --> L
    M --> N
    N --> O
```

### JWT Security Flow

```mermaid
graph TD
    A[HTTP Request] --> B[JwtAuthenticationFilter]
    B --> C{Is Public Endpoint?}
    C -->|Yes| D[Skip Filter]
    C -->|No| E[Extract JWT Token]
    E --> F{Token Present?}
    F -->|No| G[401 Unauthorized]
    F -->|Yes| H[Check Token Blacklist]
    H --> I{Token Blacklisted?}
    I -->|Yes| J[401 Token Revoked]
    I -->|No| K[Extract Username & IP]
    K --> L[Load UserDetails]
    L --> M[Validate Token + IP]
    M --> N{Valid?}
    N -->|No| O[401 Invalid Token]
    N -->|Yes| P[Set Authentication Context]
    P --> Q[Continue to Controller]
    D --> Q
```

### Database Schema

```mermaid
erDiagram
    User {
        BIGINT id PK
        VARCHAR username UK
        VARCHAR password
    }
    
    Role {
        BIGINT id PK
        VARCHAR role_name UK
    }
    
    users_roles {
        BIGINT user_id FK
        BIGINT role_id FK
    }
    
    User ||--o{ users_roles : has
    Role ||--o{ users_roles : belongs_to
```

### Token Management

```mermaid
graph LR
    A[Token Generation] --> B[Access Token]
    A --> C[Refresh Token]
    
    B --> D[3 minutes TTL]
    B --> E[User Claims]
    B --> F[IP Address Claim]
    B --> G[Roles Claim]
    
    C --> H[8 minutes TTL]
    C --> I[Refresh Purpose]
    C --> J[IP Address Claim]
    
    K[Token Blacklist] --> L[ConcurrentHashMap]
    L --> M[Token String]
    L --> N[Expiration Time]
    
    O[Token Rotation] --> P[Old Token Blacklisted]
    O --> Q[New Tokens Generated]
```

## Flask Integration

### Flask Service Structure

```mermaid
graph TB
    A[Flask App] --> B[CORS Configuration]
    A --> C[Route Handlers]
    
    C --> D[GET /hello]
    C --> E[POST /data]
    
    D --> F[Simple JSON Response]
    E --> G[Process Request Data]
    G --> H[Return Processed Result]
    
    I[Error Handling] --> J[404 Not Found]
    I --> K[500 Server Error]
```

### Communication Flow

```mermaid
sequenceDiagram
    participant R as React Frontend
    participant S as Spring Boot
    participant F as Flask Service
    
    Note over R,F: GET Request Flow
    R->>S: GET /api/simple/hello (with JWT)
    S->>S: Validate JWT token
    S->>F: GET /hello
    F->>F: Generate response
    F->>S: JSON response
    S->>R: Forward response
    
    Note over R,F: POST Request Flow
    R->>S: POST /api/simple/data (with JWT + data)
    S->>S: Validate JWT token
    S->>F: POST /data (forward data)
    F->>F: Process data
    F->>S: Processed result
    S->>R: Forward result
```

## Security Implementation

### JWT Token Structure

```mermaid
graph TD
    A[JWT Token] --> B[Header]
    A --> C[Payload]
    A --> D[Signature]
    
    B --> B1[Algorithm: HS256]
    B --> B2[Type: JWT]
    
    C --> C1[sub: username]
    C --> C2[roles: user authorities]
    C --> C3[exp: expiration timestamp]
    C --> C4[iat: issued at timestamp]
    C --> C5[ipAddress: client IP]
    C --> C6[tokenType: access/refresh]
    C --> C7[issuedBy: server identifier]
    
    D --> D1[HMAC SHA256 Signature]
```

### Security Layers

```mermaid
graph LR
    A[Request] --> B[CORS Check]
    B --> C[JWT Validation]
    C --> D[IP Address Verification]
    D --> E[Token Blacklist Check]
    E --> F[Role Authorization]
    F --> G[Method Security]
    G --> H[Access Granted]
    
    I[Security Failures] --> J[Token Expired]
    I --> K[Invalid Signature]
    I --> L[IP Mismatch]
    I --> M[Blacklisted Token]
    I --> N[Insufficient Permissions]
```

## Development Setup

### Prerequisites

```mermaid
graph TD
    A[Development Environment] --> B[Java 17+]
    A --> C[Node.js 18+]
    A --> D[Python 3.8+]
    A --> E[MySQL 8.0+]
    A --> F[IntelliJ IDEA Ultimate]
    
    G[Required Dependencies] --> H[Spring Boot 3.3+]
    G --> I[React 18]
    G --> J[Vite]
    G --> K[Flask]
    G --> L[MySQL Connector/J]
```

### Service Ports

```mermaid
graph LR
    A[React Dev Server] --> B[Port 5173]
    C[Spring Boot] --> D[Port 8080]
    E[Flask Service] --> F[Port 5000]
    G[MySQL Database] --> H[Port 3306]
```

## API Endpoints

### Spring Boot Endpoints

```mermaid
graph TD
    A[Public Endpoints] --> B[POST /api/login]
    A --> C[POST /api/refresh_token]
    
    D[Protected Endpoints] --> E[GET /api/protected-message]
    D --> F[POST /api/logout]
    D --> G[GET /api/simple/hello]
    D --> H[POST /api/simple/data]
    
    I[Admin Only] --> J[GET /actuator/health]
    I --> K[GET /actuator/metrics]
```

### Flask Endpoints

```mermaid
graph TD
    A[Flask Service] --> B[GET /hello]
    A --> C[POST /data]
    
    B --> D[Returns greeting message]
    B --> E[Includes timestamp]
    
    C --> F[Accepts JSON data]
    C --> G[Processes and returns result]
    C --> H[Includes processing metadata]
```

## Error Handling Strategy

### Frontend Error Handling

```mermaid
graph TD
    A[API Response] --> B{Status Code?}
    
    B -->|200-299| C[Success Handling]
    B -->|401| D[Clear tokens + Redirect to login]
    B -->|403| E[Show access denied message]
    B -->|404| F[Show not found error]
    B -->|500| G[Show server error]
    B -->|Network Error| H[Show connection error]
    
    I[Token Validation Error] --> J[Clear sessionStorage]
    J --> K[Reset authentication state]
    K --> L[Redirect to login]
```

### Backend Error Handling

```mermaid
graph TD
    A[Exception Handling] --> B[JWT Exceptions]
    A --> C[Authentication Exceptions]
    A --> D[Authorization Exceptions]
    A --> E[Flask Communication Errors]
    
    B --> F[ExpiredJwtException --> 401]
    B --> G[MalformedJwtException --> 401]
    B --> H[SignatureException --> 401]
    
    C --> I[BadCredentialsException --> 401]
    C --> J[UsernameNotFoundException  --> 401]
    
    D --> K[AccessDeniedException  --> 403]
    
    E --> L[ConnectionException  --> 503]
    E --> M[TimeoutException  --> 504]
```

## Performance Optimizations

### Token Management Optimization

```mermaid
graph TD
    A[Token Optimization] --> B[Automatic Refresh]
    A --> C[Token Blacklist Cleanup]
    A --> D[Connection Pooling]
    
    B --> E[2-minute threshold]
    B --> F[Seamless user experience]
    
    C --> G[Remove expired tokens]
    C --> H[Memory management]
    
    D --> I[HikariCP configuration]
    D --> J[MySQL connection reuse]
```

### Caching Strategy

```mermaid
graph TD
    A[Caching Layers] --> B[Browser sessionStorage]
    A --> C[Spring Boot in-memory cache]
    A --> D[MySQL query optimization]
    
    B --> E[JWT tokens]
    B --> F[User session data]
    
    C --> G[Token blacklist]
    C --> H[User details cache]
    
    D --> I[Connection pooling]
    D --> J[Query result caching]
```

## Testing Strategy

### Test Pyramid

```mermaid
graph TD
    A[Testing Strategy] --> B[Unit Tests]
    A --> C[Integration Tests]
    A --> D[End-to-End Tests]
    
    B --> E[React Component Tests]
    B --> F[Spring Boot Service Tests]
    B --> G[Flask Function Tests]
    
    C --> H[API Integration Tests]
    C --> I[Database Integration Tests]
    C --> J[Security Filter Tests]
    
    D --> K[Full Application Flow]
    D --> L[User Journey Tests]
    D --> M[Cross-Service Communication]
```

## Production Deployment

### Deployment Architecture

```mermaid
graph TB
    subgraph "Load Balancer"
        A[Nginx/Apache]
    end
    
    subgraph "Frontend"
        B[React Build]
        C[Static Files]
    end
    
    subgraph "Backend Services"
        D[Spring Boot Instance 1]
        E[Spring Boot Instance 2]
        F[Flask Service]
    end
    
    subgraph "Database"
        G[MySQL Primary]
        H[MySQL Replica]
    end
    
    subgraph "Monitoring"
        I[Application Logs]
        J[Health Checks]
        K[Metrics Collection]
    end
    
    A --> B
    A --> D
    A --> E
    D --> F
    E --> F
    D --> G
    E --> G
    G --> H
```

### Environment Configuration

```mermaid
graph TD
    A[Environment Configs] --> B[Development]
    A --> C[Staging]
    A --> D[Production]
    
    B --> E[H2 Database]
    B --> F[Debug Logging]
    B --> G[CORS Enabled]
    
    C --> H[MySQL Database]
    C --> I[Info Logging]
    C --> J[Limited CORS]
    
    D --> K[MySQL Cluster]
    D --> L[Error Logging Only]
    D --> M[Strict Security]
    D --> N[SSL/TLS Enabled]
```

## Monitoring and Observability

### Health Monitoring

```mermaid
graph TD
    A[Health Monitoring] --> B[Application Health]
    A --> C[Database Health]
    A --> D[Service Integration Health]
    
    B --> E[Spring Boot Actuator]
    B --> F[React App Status]
    B --> G[Flask Service Status]
    
    C --> H[MySQL Connection Pool]
    C --> I[Query Performance]
    C --> J[Storage Utilization]
    
    D --> K[Spring-Flask Communication]
    D --> L[Authentication Flow]
    D --> M[Token Validation]
```

### Logging Strategy

```mermaid
graph TD
    A[Logging Levels] --> B[DEBUG]
    A --> C[INFO]
    A --> D[WARN]
    A --> E[ERROR]
    
    F[Log Categories] --> G[Authentication Events]
    F --> H[API Requests/Responses]
    F --> I[Database Operations]
    F --> J[Security Events]
    F --> K[Flask Communication]
    
    L[Log Destinations] --> M[Console Output]
    L --> N[File Logs]
    L --> O[Centralized Logging]
```

## הוראות הפעלה מלאות

### סדר הפעלה

```mermaid
graph TD
    A[Start Services] --> B[1. Start MySQL]
    B --> C[2. Start Flask Service]
    C --> D[3. Start Spring Boot]
    D --> E[4. Start React Dev Server]
    
    F[Verification] --> G[Check MySQL Connection]
    G --> H[Test Flask Endpoints]
    H --> I[Verify Spring Boot Health]
    I --> J[Access React Application]
```

### Database Setup

```sql
-- Create database
CREATE DATABASE IF NOT EXISTS schema_jwt_2024 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

-- Use database
USE schema_jwt_2024;

-- Tables will be created automatically by Hibernate
-- with spring.jpa.hibernate.ddl-auto=update
```

### Environment Variables

```properties
# application.properties
spring.application.name=Stage5
spring.datasource.url=jdbc:mysql://localhost:3306/schema_jwt_2024?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=admin
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
flask.server.url=http://localhost:5000
server.port=8080
```

### Flask Dependencies

```bash
# requirements.txt
Flask==2.3.3
Flask-CORS==4.0.0
```

### React Dependencies

```json
{
  "dependencies": {
    "react": "^18.2.0",
    "react-dom": "^18.2.0"
  },
  "devDependencies": {
    "@vitejs/plugin-react": "^4.0.3",
    "vite": "^4.4.5"
  }
}
```

## סיכום טכני

מערכת Stage5 מדגימה ארכיטקטורה מודרנית עם:

### חוזקות המערכת
- **אבטחה מתקדמת** עם JWT וvalidation מבוסס IP
- **ארכיטקטורה מודולרית** עם הפרדה ברורה של שכבות
- **אוטומציה מלאה** של ניהול טוקנים ורענון
- **אינטגרציה רב-שירותית** בין React, Spring Boot ו-Flask
- **ניהול מצב מתקדם** עם React Context API
- **טיפול מקיף בשגיאות** ברמת המערכת
- **תמיכה בהרשאות** מבוססות תפקידים

### טכנולוגיות מרכזיות
- **Frontend**: React 18, Vite, Context API, sessionStorage
- **Backend**: Spring Boot 3.3+, Spring Security, JWT
- **Database**: MySQL 8.0+ עם JPA/Hibernate
- **Integration**: RESTful APIs, CORS, HTTP clients
- **Security**: JWT tokens, IP validation, token blacklisting

המערכת מספקת בסיס איתן לאפליקציות enterprise עם דרישות אבטחה גבוהות ואינטגרציה מורכבת.

</div>