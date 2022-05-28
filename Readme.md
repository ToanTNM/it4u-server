## Run app from container

1. Clone app and build image

```docker build -t it4u-server .
```

2. Run app in container

```docker run -p 5000:5000 -d it4u-server
```

If using PostgresSQL, run DB container

```docker run --name it4u-db -t \
      -e POSTGRES_USER="it4u" \
      -e POSTGRES_PASSWORD="123456" \
      -e POSTGRES_DB="it4uDb" \
      --restart=always \
	  -p 5433:5433 \
	  -d postgres:9.5
```

Config application-prod.yml following the DB parameters

3. Connect to <http://host:5000/swagger-ui.html> or <http://host:5000/h2-console>
