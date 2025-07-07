<div dir="rtl">

# תרגיל מסכם - Java מתקדם חלק ב
## מערכת ניהול ספרייה עם JWT

### מטרת התרגיל

בניית מערכת REST API פשוטה לניהול ספרייה הכוללת:
- אימות JWT בסיסי
- ניהול ספרים ומשתמשים
- Spring Boot + MySQL + JPA
- בדיקות עם IntelliJ HTTP Client

### המבנה הכללי שעליך לבנות

</div>

```
library-management/
├── src/main/java/com/library/
│   ├── LibraryApplication.java
│   ├── config/
│   │   ├── JwtProperties.java      
│   │   ├── JwtUtil.java           
│   │   ├── SecurityConfig.java     
│   │   └── JwtAuthenticationFilter.java
│   ├── controller/
│   │   ├── AuthController.java     
│   │   └── BookController.java     
│   ├── entity/
│   │   ├── User.java              
│   │   ├── Role.java              
│   │   └── Book.java              
│   ├── repository/
│   │   ├── UserRepository.java     
│   │   ├── RoleRepository.java     
│   │   └── BookRepository.java     
│   ├── service/
│   │   ├── CustomUserDetailsService.java 
│   │   └── BookService.java        
│   ├── dto/
│   │   ├── LoginRequest.java       
│   │   ├── LoginResponse.java      
│   │   └── BookDto.java           
│   └── DataLoader.java            
├── src/main/resources/
│   └── application.properties      
└── http-tests/
    ├── auth.http
    └── books.http
```

<div dir="rtl">

## הוראות התרגיל - לפי שלבים

### שלב 1: הכנת הפרויקט ב-IntelliJ Ultimate (10 נקודות)
- צור פרויקט Spring Boot חדש
- הוסף dependencies: web, security, jpa, mysql, jwt, lombok
- הגדר `application.properties` עם חיבור למסד נתונים
- הגדר database בשם `library_db`

### שלב 2: יצירת Entities (15 נקודות)
- **User Entity**: id, username, password, roles
- **Role Entity**: id, name
- **Book Entity**: id, title, author, isbn, category, available, borrowedBy
- הגדר קשרים: User ↔ Role (Many-to-Many)

### שלב 3: שכבת Repository (10 נקודות)
- **UserRepository**: findByUsername
- **RoleRepository**: findByName
- **BookRepository**:
    - findByAuthor
    - findByAvailable
    - findByTitleContaining
    - findByCategory

### שלב 4: אבטחה ו-JWT (20 נקודות)
- **JwtProperties**: קבועים לזמן תוקף וסוד
- **JwtUtil**: יצירה ואימות של tokens
- **JwtAuthenticationFilter**: סינון requests
- **SecurityConfig**: הגדרת נתיבים מוגנים
- **CustomUserDetailsService**: טעינת משתמשים

### שלב 5: שכבת Service (15 נקודות)
- **BookService**:
    - getAllBooks()
    - saveBook()
    - deleteBook()
    - borrowBook()
    - returnBook()
    - searchBooks()

### שלב 6: Controllers (20 נקודות)
- **AuthController**:
    - POST /api/auth/login
- **BookController**:
    - GET /api/books (כל הספרים)
    - POST /api/books (הוספת ספר)
    - PUT /api/books/{id} (עדכון)
    - DELETE /api/books/{id} (מחיקה)
    - POST /api/books/{id}/borrow (השאלה)
    - POST /api/books/{id}/return (החזרה)

### שלב 7: DataLoader (5 נקודות)
- צור משתמשי admin ו-user בסיסיים
- צור תפקידים: ADMIN, USER
- הוסף כמה ספרים לדוגמה

### שלב 8: IntelliJ HTTP Client Tests (5 נקודות)
- **auth.http**: בדיקת login
- **books.http**: בדיקת כל הפעולות על ספרים

## דרישות מיוחדות

### הרשאות גישה:
- **כל המשתמשים**: יכולים לראות ספרים ולהשאיל
- **ADMIN בלבד**: יכולים להוסיף/לערוך/למחוק ספרים

### פונקציונליות נדרשת:
1. **אימות**: login עם username/password → קבלת JWT
2. **ניהול ספרים**: CRUD מלא עם הרשאות
3. **השאלות**: מעקב אחר מי השאיל איזה ספר
4. **חיפוש**: לפי שם, מחבר, קטגוריה

### בדיקות נדרשות:
- login מוצלח/כושל
- קבלת רשימת ספרים (עם token)
- הוספת ספר (admin בלבד)
- השאלת ספר והחזרה
- חיפוש ספרים

## קריטריונים לציון

### שלמות הפתרון (60%)
- כל הendpoints עובדים
- אימות JWT תקין
- פעולות CRUD מלאות
- הרשאות נכונות

### איכות קוד (25%)
- מבנה תיקיות נכון
- קוד נקי וברור
- שימוש נכון ב-annotations
- טיפול בשגיאות

### בדיקות ותיעוד (15%)
- HTTP Client files עובדים
- README עם הוראות הרצה
- screenshots של בדיקות

### דרישות הגשה
1. פרויקט מלא עם כל הקבצים
2. קובץ README עם הוראות הרצה
3. קבצי .http לבדיקות
4. צילומי מסך של הבדיקות

המטרה: ליצור מערכת פשוטה אבל מלאה שמדגימה את כל הרכיבים של Spring Boot עם JWT ו-JPA.

</div>