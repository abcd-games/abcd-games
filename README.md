

## Setup

### PostgreSQL

- Run command to start postgeSQL in Docker
```
docker run --name some-postgres -p 5432:5432 -e POSTGRES_PASSWORD=mysecretpassword -d postgres
```

- Use IntelliJ Database-Tool for manual connection to you database.
- Make sure docker is available while running Integration-Tests
