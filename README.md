# ProblemHub API (Java / Spring Boot 3.5.16)

REST API for ProblemHub. Talks to your existing Supabase Postgres database directly
(via JDBC, not the Supabase client library) and verifies the JWTs that Supabase Auth
issues, so sign-up/login on the frontend stay exactly as they were.

## Endpoints

- `GET /api/problems` — list, newest first (public)
- `GET /api/problems/{id}` — single problem (public)
- `POST /api/problems` — submit a problem (requires `Authorization: Bearer <token>`)
- `POST /api/problems/{id}/support` — register "I have this problem too" (requires auth)

## Environment variables

Set these 4 in Render's Environment section:

- `DATABASE_URL` = `jdbc:postgresql://aws-0-ap-southeast-1.pooler.supabase.com:5432/postgres`
- `DATABASE_USERNAME` = `postgres.pawjavtapmncpkenasjv`
- `DATABASE_PASSWORD` = the database password from Supabase (Database → Settings → Reset Database Password)
- `SUPABASE_JWKS_URL` = `https://pawjavtapmncpkenasjv.supabase.co/auth/v1/.well-known/jwks.json`

The first two and the last one are fixed for this project — only `DATABASE_PASSWORD` is a secret you keep to yourself.

## Run locally

Needs Java 17 and Maven installed.

1. Set the four environment variables above in your shell (or your IDE's run config)
2. `mvn spring-boot:run`
3. API is live at `http://localhost:8080`

## Deploy to Render (free)

1. Push this folder to a GitHub repo
2. On Render: **New → Web Service**, connect the repo
3. Environment: **Docker** (Render will find the Dockerfile automatically)
4. Add the four environment variables from above in Render's dashboard
5. Deploy — first build takes a few minutes

Render's free tier sleeps after 15 minutes of no traffic, so the first request after
a quiet period can take 40-60 seconds to wake back up. That's normal, not a bug.

Once deployed, note the URL Render gives you (something like
`https://problemhub-api.onrender.com`) — the frontend will need it.
