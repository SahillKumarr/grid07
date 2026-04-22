# Backend Assignment - Grid07

## How to run
- Add your PostgreSQL and Redis credentials in application.properties
- Run using mvn spring-boot:run


## Architecture

- **PostgreSQL** — Database (posts, comments)
- **Redis** — gatekeeper for all guardrails and real-time counters
- **Spring Boot** — completely stateless 


## What I built
A Spring Boot API with Redis guardrails that controls how bots can
interact with posts.

## How I handled thread safety (Phase 2)

The main challenge was to stop the bot comments at exactly 100 even when
200 requests come at the same time.

I have used Redis INCR operation for this. INCR is atomic - meaning even if
200 threads hit it at the same millisecond, each one gets a unique
incremented number. So thread 101 gets 101, sees its over the limit,
and rejects itself. No race condition possible.

For cooldowns I have used Redis keys with TTL - when bot interacts with human,
key gets set with 600 second expiry. Next request is to check if the key exists,
if yes then it blocks.

All state is in Redis and no hashmaps or static variables used.