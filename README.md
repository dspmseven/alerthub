# AlertHub

Alert notification service that receives alerts and routes them to different channels (Telegram, SMS, Email) based on severity level.

## Tech Stack

- Java 21
- Spring Boot 3.5
- Apache Camel
- Apache ActiveMQ Artemis
- PostgreSQL 17

## Requirements

- JDK 21
- Docker & Docker Compose

## Quick Start

1. Start infrastructure:
```bash
docker-compose up -d
```

2. Run the application:
```bash
./gradlew bootRun
```

## API

### Send Notification

```
POST /api/v1/notification
Content-Type: application/json

{
  "name": "High CPU Usage",
  "message": "CPU usage exceeded 90%",
  "date": "2026-01-27T10:00:00",
  "severity": "CRITICAL",
  "source": "zabbix",
  "tags": "infrastructure,cpu",
  "resolved": false
}
```

### Severity Levels

- `CRITICAL` - Telegram + SMS + Email
- `HIGH` - Telegram + Email
- `MEDIUM` - Email
- `LOW` - Email

## Configuration

Environment variables (with defaults):

| Variable | Default |
|----------|---------|
| `ARTEMIS_URL` | `tcp://localhost:61616` |
| `ARTEMIS_USER` | `admin` |
| `ARTEMIS_PASSWORD` | `admin` |
| `DB_URL` | `jdbc:postgresql://localhost:5432/alerthub` |
| `DB_USER` | `user` |
| `DB_PASSWORD` | `password` |

## Roadmap

- [ ] Telegram integration
- [ ] SMS integration
- [ ] Email integration
- [ ] Web UI
