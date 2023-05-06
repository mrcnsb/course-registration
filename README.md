# Course Registration Application

This is a Spring Boot application connected to a MySQL database, both running as Docker containers using Docker Compose.

## Prerequisites

- Docker
- Docker Compose

## Setup and Run

1. Clone this repository:

git clone https://github.com/mrcnsb/course-registration.git
cd course-registration

2. Build the application image (if not already built):

docker-compose build

3. Start the containers:

docker-compose up

The Spring Boot application will be available on http://localhost:8081, and the MySQL database will be accessible on port 3308.

4. To stop the containers, press `Ctrl+C` in the terminal, or run the following command in another terminal window:

docker-compose down

## Configuration


The Docker Compose configuration is in the `docker-compose.yml` file. You can customize the ports, database credentials, and other settings as needed.

### Services

- `mysql`: MySQL database container using the official MySQL image.
- `app`: Spring Boot application container using the custom `course-app-image`.

### Volumes

- `db_data`: Persistent volume for the MySQL database to store data between container restarts.

### Ports

- MySQL: 3308 (host) -> 3306 (container)
- Spring Boot app: 8081 (host) -> 8080 (container)
