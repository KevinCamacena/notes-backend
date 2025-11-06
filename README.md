# Notes API Backend

This is the backend service for a simple notes application. It uses a SpringBoot API, a MongoDB database, and Mongo Express for database management. The entire stack is orchestrated using Docker Compose.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

*   [Docker](https://docs.docker.com/get-docker/)
*   [Docker Compose](https://docs.docker.com/compose/install/) (usually included with Docker Desktop)

### Installation

1.  **Clone the repository**

    ```sh
    git clone git@github.com:KevinCamacena/notes-backend.git
    cd notes-backend
    ```

2.  **Environment Configuration**

    Before you can run the application, you need to set up your environment variables. Create a file named `.env` in the root directory of the project.

    Copy the following content into your `.env` file. This file is used by `docker-compose` to inject secrets and configuration into the services.

    ```env
    # MongoDB Credentials
    DB_NAME=notesdb
    DB_USER=root
    DB_PASSWORD=my_secret_db_password
    
    # Mongo Express web login
    ME_USER=admin
    ME_PASSWORD=your_secret_web_password
    
    # Notes API
    DOCKER_USERNAME=your_docker_username
    MONGO_DB_URI=mongodb://root:my_secret_db_password@mongo:27017/notesdb?authSource=admin
    ```

    **Important:**
    *   Replace `your_docker_username` with your actual Docker Hub username. This is used for tagging the Docker image.
    *   You can change the passwords (`DB_PASSWORD`, `ME_PASSWORD`) to something more secure if you wish. Just ensure the `MONGO_DB_URI` is updated accordingly if you change `DB_PASSWORD`.

## Running the Application

Once the `.env` file is created, you can start all the services using Docker Compose:

```sh
docker-compose up -d --build
```

The services will be available at:
*   **Notes API**: `http://localhost:8080/swagger-ui.html`
*   **Mongo Express**: `http://localhost:8081` (Log in with `ME_USER` and `ME_PASSWORD` from your `.env` file)

To stop the services, run:
```sh
docker-compose down
```