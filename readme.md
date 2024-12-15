# CareConnect - Health Management System

CareConnect is a comprehensive health management system built using a microservices architecture. This system integrates multiple healthcare services to provide seamless management for users, healthcare professionals, and administrators. The project is designed to simplify and automate healthcare processes such as user management, appointments, prescriptions, billing, payments, ambulance booking, and notifications.

---

## Features

### 1. **User Service**
- Manage patient, doctor, and admin user accounts.
- Role-based access (Admin, Doctor, Patient).
- Admin approval workflow for doctor registration.
- Retrieve user information, roles, and statuses.

### 2. **Appointment Service**
- Schedule, update, and cancel appointments.
- View appointment history for users.
- Notification support for upcoming deadlines.

### 3. **Prescription and Medication Service**
- Create and manage prescriptions.
- Link medications with prescriptions.
- Retrieve prescription history for patients.

### 4. **Billing Service**
- Generate bills for medical consultations and treatments.
- Store bill details including breakdown of costs.
- Link bills to appointments for transparency.

### 5. **Payment Service**
- Record payment details such as mode of payment, transaction number, and payment date.
- Store payment history for reference.
- Integration-ready for online payment systems in future.

### 6. **Mailing Service**
- Send email notifications to users.
- Reminders for upcoming appointments (one day before).
- Customizable email templates for notifications.

### 7. **Ambulance Booking Service**
- Book ambulances on-demand.
- Track and manage bookings.
- Maintain booking history for reference.

---

## Technology Stack

### Backend
- **Java** (Spring Boot framework)
- **Hibernate/JPA** for ORM
- **MySQL** for database
- **Spring Security** for authentication and authorization
- **ModelMapper** for object mapping

### Frontend
- Not included in this repository (Recommended: React, Angular).

### Tools and Libraries
- **JUnit** and **Mockito** for testing.
- **Swagger** for API documentation.
- **Lombok** for boilerplate code reduction.

### Messaging and Notifications
- **JavaMailSender** for email notifications.

---

## Microservices Architecture

### Service Communication
- REST APIs are used for communication between microservices.
- **Service Registry**: Spring Cloud Netflix Eureka.
- **API Gateway**: Common endpoint for all the APIs.

### Independent Services
Each microservice is independently developed to ensure modularity and scalability. Below are the core services:

| Service Name               | Description                              |
|----------------------------|------------------------------------------|
| **User Service**           | Manages user authentication and roles.  |
| **Appointment Service**    | Handles appointment scheduling.         |
| **Prescription Service**   | Maintains medical records and prescriptions. |
| **Billing Service**        | Tracks billing and payments.            |
| **Payment Service**        | Records payment details.                |
| **Mailing Service**        | Sends notifications and reminders.      |
| **Ambulance Booking**      | Manages ambulance bookings.             |

---

## Prerequisites

1. **Java 17+**
2. **Spring Boot 3+**
3. **MySQL**
4. **Maven** for dependency management
5. Frontend technologies like **React** (optional).

---

## Setup and Installation

### 1. Clone the Repository
```bash
$ git clone https://github.com/alokpandeygzp/CareConnect-Health-Management.git
$ cd CareConnect
```

### 2. Build and Run Individual Services
Each microservice resides in its own directory. Navigate to the respective service directory and build the service using Maven:
```bash
$ cd user-service
$ mvn clean install
$ mvn spring-boot:run
```
Repeat this process for other services.

### 3. Database Setup
- Import the SQL schema for each service into your database.
- Update the `application.yml` or `application.properties` files with your database credentials.

### 4. Test the Application
Run JUnit tests for each service:
```bash
$ mvn test
```

### 5. API Documentation
Visit the Swagger UI for each service to explore APIs:
```
http://localhost:<port>/swagger-ui.html
```

---

## API Endpoints Overview

### User Service
| HTTP Method | Endpoint                      | Description                  |
|-------------|-------------------------------|------------------------------|
| POST        | `/api/users/`                | Create a new user.           |
| GET         | `/api/users/{userId}`        | Retrieve user details.       |
| DELETE      | `/api/users/{userId}`        | Delete a user.               |
| GET         | `/api/users/pending/doctors` | List pending doctor approvals. |
| POST        | `/api/users/approve/doctor/{id}` | Approve a doctor.        |

### Appointment Service
| HTTP Method | Endpoint                    | Description                  |
|-------------|-----------------------------|------------------------------|
| POST        | `/api/appointments/`       | Create an appointment.       |
| GET         | `/api/appointments/{id}`   | Get appointment details.     |
| DELETE      | `/api/appointments/{id}`   | Cancel an appointment.       |

(Additional endpoints for other services can be added similarly.)

---

## Future Enhancements

1. **Online Payment Integration**
2. **Analytics Dashboard**
3. **Telemedicine Support**
4. **Integration with Wearable Devices**
5. **Kubernetes Deployment**

---

## Contributors
Feel free to contribute to this project by submitting issues or pull requests. For major changes, please open an issue first to discuss what you would like to change.

---


