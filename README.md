# Metadata.io

**Metadata.io Hiring Process**

These endpoints are related to the "School registration system" project.

The system is part of the hiring process at Metadata.io.

Technology stack:
 - Java
 - Maven
 - Spring Boot
 - Docker (docker-compose)
 - JUnit
 - MySQL

Author: Flavio B. Gonzaga

fbgonzaga@gmail.com

## Available endpoints ##

The following tables summarize the available endpoints and HTTP methods for both (students and courses).

**Students:**

![students_tb_img](https://user-images.githubusercontent.com/30641015/182287758-7b75d6a6-c16e-448f-9715-4b74b0d18117.png)

**Courses:**

![courses_tb_img](https://user-images.githubusercontent.com/30641015/182287871-5ec02e42-9e4d-4707-bb9b-dd4a64ffe406.png)

## Complete endpoints documentation ##

The complete endpoints documentation is available at:

[Postman documentation](https://documenter.getpostman.com/view/19854346/Uze1x5Ac)

## Running the project ##

### docker compose ###

Assuming you have already installed the Docker Desktop on Linux, you can pull the containers from the Docker Hub.

`docker pull fbgonzaga/school-application:latest`

`docker pull fbgonzaga/school-database:latest`

You can find in the root path of this repository the file **docker-compose.yml**.

Running the application:

`docker compose up`

The application must be available at:

`http://localhost:8080/`

I suggest you use Metadata.io Workspace (shared with you on Postman) to interact with the endpoints. :)

### "manually" ###

Assuming you have already installed and configured:
 - OpenJDK (version 11) with your prefered JAVA IDE
 - MariaDB

1. Create the school schema.
2. Create the user school_admin.
3. Grant privileges for the school_admin user on the school schema.

`CREATE DATABASE school;`

`CREATE USER 'school_admin'@'%' IDENTIFIED BY 'school_admin';`

`GRANT ALL PRIVILEGES ON school.* TO 'school_admin'@localhost IDENTIFIED BY 'school_admin';`

You are now ready do execute the application using your JAVA IDE. :)

## Acknowledgements ##

Thank you very much for your time in evaluating my project.
Feel free to contact me and share your thoughts! :)
