# Learn Spring Boot - Microservice Branch

Welcome to the **Learn Spring Boot** project! This branch (`microservice`) focuses on building and understanding microservices using Spring Boot.

---

## 📝 Summary

This README is structured to help you understand the key aspects of the project:

1. **Microservices**  
   - Autonomous services collaborate to handle complex tasks.
   - Data is aggregated using **Composite Services** for unified responses.

2. **Spring Cloud**  
   - **Auth-Server** handles authentication and authorization for all requests coming to the **Gateway**.
   - **Gateway** verifies user permissions and forwards requests to the appropriate microservices.

3. **Shared Components**  
   - **Utils Library** provides reusable functions (e.g., logging, JWT handling).  
   - **API Library** ensures consistency in API contracts and error handling across services.

4. **Workflow**  
   - Describes the development and deployment lifecycle.  
   - Includes CI/CD automation using **GitHub Actions** and Docker-based deployments.

---

## 🌟 Key Areas

### 1️⃣ Microservices
- **What is it?**  
  Microservices is an architectural style that structures an application as a collection of small, autonomous services, modeled around a business domain.
  
- **How it's implemented in this project?**
  - Each microservice is designed to handle a specific business capability.
  - Services communicate with each other using REST APIs.
  - Multiple services collaborate to handle complex tasks, ensuring scalability and modularity.
  - Composite services act as aggregators, combining data from multiple services to provide a unified response for complex operations.

- **Key Features of Microservices in this Project:**
  - Independent deployments.
  - Decentralized data management with separate databases for each service.
  - Fault isolation to prevent cascading failures.
  - Support for task orchestration and data aggregation through composite services.

---

### 2️⃣ Spring Cloud
- **What is it?**  
  Spring Cloud provides tools for developers to quickly build distributed systems and microservices. It simplifies the complexities of service discovery, configuration management, and load balancing.

- **How it's implemented in this project?**
  - **Auth-Server**: 
    - Centralized service responsible for authenticating users and authorizing their requests.
    - Issues and validates **JWT tokens** to ensure secure communication between components.
    - Ensures that only authenticated and authorized users can access the **Gateway**.
  - **Gateway**:
    - Acts as the second layer of security by verifying user permissions based on the roles and claims in the **JWT token**.
    - Forwards authorized requests to the appropriate microservices for processing.
  - **Eureka Server**: Handles service discovery, allowing services to register and discover one another.
  - **Spring Cloud Config Server**: Centralized configuration management.

- **Spring Cloud Features in this Project:**
  - Dynamic load balancing for distributed systems.
  - Centralized and externalized configurations for easy management.
  - Resilience tools such as circuit breakers.

---

### 3️⃣ Shared Components
- **What is it?**  
  Shared components are libraries or modules that are used across all microservices to ensure consistency and reduce redundancy.

#### **Utils Library**
- **Purpose**:
  - Provides common reusable functions to simplify development and avoid code duplication.
- **Key Functionalities**:
  - Logging utilities.
  - Date and time formatting helpers.
  - String operations.
  - JWT utilities:
    - Generate JWT tokens with claims.
    - Set and manage JWT secret keys.
    - Validate tokens for authenticity, expiration, and claims.

#### **API Library**
- **Purpose**:
  - Ensures consistency in API contracts and error handling across all services.
- **Key Functionalities**:
  - Common request and response DTOs.
  - Standardized error handling and exception classes.

#### **Key Benefits**:
- Centralized management of common logic.
- Improved maintainability and reduced code duplication.
- Ensured consistency across microservices.

---

### 4️⃣ Workflow
- **What is it?**
  The workflow explains how the system moves from development to deployment and how tasks are automated using CI/CD pipelines.

- **How it's implemented in this project?**
  - **Local Development Workflow**:
    1. Develop and test microservices locally.
    2. Use Docker Compose (local environment) for integration testing.
  - **Production Workflow**:
    1. Push code changes to the repository.
    2. CI/CD pipelines build, test, and deploy changes automatically.
  - **CI/CD Automation**:
    - GitHub Actions is used for automating builds, tests, and deployments.
    - Docker images are built and pushed to a container registry before deployment.

- **Key Steps in the Workflow:**
  1. **Code Commit**: Developers push code to the repository.
  2. **Continuous Integration**:
     - Code is built and tested automatically.
     - Docker images are created.
  3. **Continuous Deployment**: Validated services are deployed to production environments using Docker Compose or Kubernetes.

---

## 🏗️ Tech Stack

- **Java**: Programming language
- **Spring Boot**: Framework for microservices
- **Spring Cloud**: Tools for distributed systems
- **H2/ MySQL**: Database integration
- **Gradle**: Dependency management
- **Docker Compose**: Container orchestration

---

## 📖 How to Use

### Setting up locally:
1. Clone the repository:
   ```bash
   git clone https://github.com/Chaudeptrai123456/learnSpringBoot.git
   ```
2. Checkout to the `microservice` branch:
   ```bash
   git checkout microservice
   ```
3. Import the project into your favorite IDE (e.g., IntelliJ, Eclipse).
4. Run the application:
   ```bash
   ./gradlew bootRun
   ```

### Using Docker Compose:
1. Navigate to the root directory of the project.
2. Start all services in the desired environment:
   - **Local**:
     ```bash
     docker-compose -f docker-compose-local.yml up
     ```
   - **Production**:
     ```bash
     docker-compose -f docker-compose-prod.yml up
     ```

---

## 🤝 Contributing

Contributions are welcome! Please follow these steps:
1. Fork the repository.
2. Create a new branch (`git checkout -b feature-name`).
3. Commit your changes (`git commit -m 'Add some feature'`).
4. Push to the branch (`git push origin feature-name`).
5. Open a Pull Request.

---

## 📜 License

This project is licensed under the MIT License. See the `LICENSE` file for more details.

---

Happy coding! 😊
