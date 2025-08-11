# Bloggy — A Blog App

**Bloggy** is a modern, secure blogging platform built with Java 17 and Spring Boot 6. It features robust user authentication, content management, containerisation with Docker, and deployment readiness on AWS.

---

## Table of Contents

* [About](#about)
* [Features](#features)
* [Tech Stack](#tech-stack)
* [Getting Started](#getting-started)

  * [Prerequisites](#prerequisites)
  * [Installation](#installation)
  * [Running Locally](#running-locally)
* [Usage](#usage)
* [Screenshot](#screenshot)
* [Docker & Deployment](#docker--deployment)
* [Contributing](#contributing)
* [License](#license)
* [Acknowledgements](#acknowledgements)

---

## About

Bloggy is a full-featured blogging application providing a clean, secure, and scalable environment for users to create and manage content. It employs industry-standard patterns for authentication (using JWT and Spring Security), persists data efficiently through Spring Data JPA, and integrates Hibernate and MySQL under the hood. The application is fully containerized with Docker, making it deployment-ready for AWS infrastructure.

---

## Features

* **JWT-Based Authentication & Authorization** via Spring Security
* **CRUD Operations** for blog posts and user interactions (to be detailed further)
* **Persistent Storage** with relational database via Spring Data JPA and Hibernate
* **Containerized** deployment using Docker
* **Cloud-Ready** architecture for seamless deployment to AWS

---

## Tech Stack

| Layer            | Technologies                      |
| ---------------- | --------------------------------- |
| Backend          | Java 17, Spring Boot 6            |
| Security         | Spring Security, JWT              |
| Data Persistence | Spring Data JPA, Hibernate, MySQL |
| Containerisation | Docker                            |
| Deployment       | AWS (Docker-ready)                |


---

## Getting Started

### Prerequisites

* Java 17
* Maven (or use included `mvnw`)
* MySQL (or compatible relational DB)
* Docker (if using containerization)

### Installation

1. **Clone the repository**

   ```bash
   git clone https://github.com/AAdewunmi/blog-app-project.git
   cd blog-app-project
   ```

2. **Configure your environment**

   * Create a `JavaBlogDB` (or similarly named) database in MySQL.
   * Set up database connection parameters in your `application.properties` (e.g., URL, username, password).

3. **Build the project**

   ```bash
   mvn clean install
   ```

---

## Running Locally

Run the app using Maven or the bundled wrapper:

```bash
./mvnw spring-boot:run
```

Then, open your browser at `http://localhost:8080` to access Bloggy.

---

## Usage

* **Access the UI** at `http://localhost:8080`
* **Authentication**: Register and log in to create and manage blog posts
* **Admin Dashboard** (if applicable): Manage users or flagged content (details to be implemented)

---

## Screenshot (Swagger UI)

<img width="1815" height="869" alt="Image" src="https://github.com/user-attachments/assets/d192b000-2136-45af-bcaa-45a0b23021af" />

<img width="1446" height="828" alt="Image" src="https://github.com/user-attachments/assets/7123186a-65f7-4b47-aa3d-5b1f82cb433a" />

<img width="1447" height="816" alt="Image" src="https://github.com/user-attachments/assets/7eb7e31c-0421-4e36-8053-9a916649a50c" />

---

## Docker & Deployment

**Build Docker Image**

```bash
docker build -t bloggy:latest .
```

**Run with Docker**

```bash
docker run -d -p 8080:8080 --name bloggy-container bloggy:latest
```

Once dockerized, you can deploy the container image to AWS services like ECS, ECR, or AWS Elastic Beanstalk for scalable hosting.

---

## Contributing

Contributions are welcome! To propose changes:

1. Fork the repository
2. Create your feature branch (`git checkout -b feature-name`)
3. Commit your updates (`git commit -m "Add feature"`)
4. Push to your fork (`git push origin feature-name`)
5. Submit a Pull Request for review

---

## License

This project is licensed under the **MIT License**.

---

## Acknowledgements

* Based on concepts and structures from Udemy's Building Real-Time REST APIs with Spring Boot - Blog App By Ramesh Fadatare (Java Guides)
* Inspired by best practices in clean code, secure authentication patterns, containerization, and cloud deployment

---
