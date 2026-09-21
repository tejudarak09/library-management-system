# Deploying the Library Management System (public URL, free)

This guide puts the app on the public internet using **Render's free tier**
(Docker web service) plus a free MySQL-compatible database. No localhost needed.

Total time: ~20–30 minutes, most of it waiting for builds.

---

## Step 0 — What you'll need (all free)

1. A **GitHub** account
2. A **Render** account (sign up with GitHub — easiest): https://render.com
3. A free MySQL-compatible database. Two good options:
   - **TiDB Cloud** — free tier, MySQL-compatible, easiest: https://tidbcloud.com
     (create a free Serverless cluster)
   - **Aiven** — free tier MySQL: https://aiven.io

> Render itself no longer offers free managed MySQL, which is why we use TiDB/Aiven.

---

## Step 1 — Push the project to GitHub

In a terminal inside the project folder (`library-management-system`):

```bash
git init
git add .
git commit -m "Library Management System"
```

Then on GitHub.com: **New repository** → name it `library-management-system`
→ **Create repository** (don't add a README/license — the project has one).

Back in the terminal (replace `YOUR-USERNAME`):

```bash
git branch -M main
git remote add origin https://github.com/YOUR-USERNAME/library-management-system.git
git push -u origin main
```

---

## Step 2 — Create the free database (TiDB Cloud example)

1. Sign in to TiDB Cloud → **Create Cluster** → choose the **Serverless** (free) tier.
2. Once it's running, open the cluster → **Connect** → pick your connection info.
3. Note down:
   - **Host** (e.g. `gateway01.ap-southeast-1.prod.aws.tidbcloud.com`)
   - **Port** (usually `4000`)
   - **Username** (usually `root`)
   - **Password** (the one you set when creating the cluster)
   - **Database name** — create one called `library_db` (SQL editor: `CREATE DATABASE library_db;`)
4. Build your JDBC URL (TiDB requires SSL):

```
jdbc:mysql://HOST:4000/library_db?useSSL=true&serverTimezone=UTC&allowPublicKeyRetrieval=true
```

Example:

```
jdbc:mysql://gateway01.ap-southeast-1.prod.aws.tidbcloud.com:4000/library_db?useSSL=true&serverTimezone=UTC&allowPublicKeyRetrieval=true
```

> Using Aiven instead? Same idea — copy its host/port/user/password from the
> Aiven console and build the JDBC URL the same way.

---

## Step 3 — Create the Web Service on Render

1. Render dashboard → **New +** → **Web Service**.
2. **Connect** your `library-management-system` GitHub repo (grant access if asked).
3. Configure:
   - **Name:** `library-management-system` (or anything)
   - **Runtime:** Docker (auto-detected from the `Dockerfile`)
   - **Instance type:** **Free**
4. Under **Environment Variables**, click **Add Environment Variable** and add
   exactly these three:

   | Key | Value |
   |-----|-------|
   | `SPRING_DATASOURCE_URL` | your JDBC URL from Step 2 |
   | `SPRING_DATASOURCE_USERNAME` | your DB username (TiDB: `root`) |
   | `SPRING_DATASOURCE_PASSWORD` | your DB password |

   (`PORT` is set automatically by Render — the app already respects it.)
5. Click **Create Web Service**. Render builds the Docker image (~5–10 min first time).

---

## Step 4 — Open your public app 🎉

When the deploy goes **Live**, Render gives you a URL like:

```
https://library-management-system-xxxx.onrender.com
```

Open it — that's your library system, live on the internet. Share that link on LinkedIn!

### Health check note

Render's default health check just verifies the service answers on its port —
no special endpoint needed. The app serves the UI at `/`, so the default check
passes as soon as Spring Boot finishes starting. (First boot can take
30–60 seconds while Hibernate connects to the database.)

### Free-tier quirks to know

- The free service **sleeps after ~15 minutes of inactivity** — the first visit
  after sleep takes ~30–60 seconds to wake up. Totally normal.
- If you see a database connection error in the Render logs, double-check the
  three env vars (typos in the JDBC URL are the #1 cause).

---

## Local development still works unchanged

With no env vars set, the app falls back to `localhost:3306` MySQL exactly as
before — `mvn spring-boot:run` needs no changes.
