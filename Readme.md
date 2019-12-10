## Run app from container

1. Clone app and build image
```
docker build -t it4u-server .
```
2. Run app in container
```
docker run -p 5000:5000 -d it4u-server
```

3. Connect to <http://host:5000/swagger-ui.html> or <http://host:5000/h2-console>
