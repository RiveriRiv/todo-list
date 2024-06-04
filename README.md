# todo_list application

# How tu use

1. Start the spring-boot application by building with 'gradle clean build' and running Application.class
   or running docker container
2. Go to localhost:8080/item in browser

# Docker
To run docker container use:
1. docker load < item.tar
2. docker run -p 127.0.0.1:8080:8080 localhost:8080/item:v1
