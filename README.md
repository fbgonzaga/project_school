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

![students_tb_img](https://user-images.githubusercontent.com/30641015/182293371-6c35b0dc-8e2e-4057-b525-d686c733b466.png)

**Courses:**

![courses_tb_img](https://user-images.githubusercontent.com/30641015/182293389-38e631ee-b877-4c89-aae3-30b73af31b71.png)

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

I suggest you use the Metadata.io Workspace using Postman. The file to be imported (in JSON format) is available at:

[Postman Metadata.io Workspace](https://www.getpostman.com/collections/607f2c3cc23901f0d31e)

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

You are now ready do execute the application using your JAVA IDE.

## Acknowledgements ##

Thank you very much for your time in evaluating my project.
Feel free to contact me and share your thoughts! :)
