# BFHL Hardcoded JAR (runs with your webhook + JWT)

This builds a **single runnable JAR** that already contains your **webhook** and **JWT**.
Just paste your full JWT once in `src/Main.java`, build, and run — it will print the server's response and HTTP status.

## Files
- `src/Main.java` — contains hardcoded `WEBHOOK` + `ACCESS_TOKEN` constants.
- `final_query.sql` — your MySQL answer (read at runtime and sent as JSON).

## Paste your token
Open `src/Main.java` and replace:
```java
private static final String ACCESS_TOKEN = "PASTE_YOUR_FULL_JWT_TOKEN_HERE";
```
with your actual JWT token:
```java
private static final String ACCESS_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...";
```

## Build and Run

### Compile
```bash
javac -d . src/Main.java
```

### Run
```bash
java Main
```

### Create JAR (optional)
```bash
jar cfm bfhl.jar manifest.txt Main.class final_query.sql
```

## What it does
1. Reads the SQL query from `final_query.sql`
2. Sends it as JSON to the hardcoded webhook URL
3. Uses your hardcoded JWT for authentication
4. Prints the server response and HTTP status

## Requirements
- Java 11+ (for `Files.readString()`)
- Network access to the webhook URL 