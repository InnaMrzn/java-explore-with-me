# java-explore-with-me 

## Graduate work "Explore-With-Me" providing API for managing events 

## Project description
Application providers basic event management functionality. Events publication, registration of participants for event and search for events.

## Technologies
* Java 11
* Docker
* Spring Boot
* Lombok
* PostgreSQL 14
* Maven 3.8.4

## Launch

This application configured to runs in a local Docker Containers. Docker Desktop https://www.docker.com/products/docker-desktop/ may be used as an option for running containerized applications locally.

### In order to launch Explore-With-Me service:
* Clone the whole application from github
* Start your local Docker runtime environment
* Open command-line shell and move to application upper folder (java-explore-with-me).
* Run `docker compose up` command
* Check that containers `ewm-service`, `stats-service`, `stats-db` and `ewm-db` are up and running inside Docker

![](containers_running.jpg)

## Usage

There are three roles: admin, registered user and public user. Authorization and role mansgement are out of scope of the project.

###Admin API:

###Registered user API

###Public user API



