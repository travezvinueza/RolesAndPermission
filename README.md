![rolesAndPermission](https://github.com/user-attachments/assets/94730766-5688-4819-bd3a-fe732deab0eb)
# JWT SpringSecurity Project

Este proyecto demuestra una API REST segura desarrollada con Spring Boot, que utiliza tokens web JSON (JWT) para la autenticación y la autorización.

## Features

* **Registro de usuario:** Permite que se registren con nombre de usuario, email, contraseña y su rol, por defecto se guarda con el rol `ROLE_USER`.
* **Inicio de sesión:** Al iniciar sesión se puede autenticar con nombre de usuario o email.
* **Actualización de token:** Proporciona un mecanismo para actualizar los tokens de acceso mediante tokens de actualización.
* **Autenticación basada en JWT:** Protege los endpoints de la API mediante JWT.
* **Autorización basada en roles y permisos:** Admite diferentes roles con diferentes permisos para diferentes endpoints.
* **Gestión de productos:** Operaciones CRUD básicas para productos (crear, leer, actualizar, eliminar).
* **Carga de imagenes:** Permite la carga de imágenes en la carpeta `uploads` con formato PNG, JPG y JPEG.
* **Validación:** Implementa la validación de entrada para las cargas útiles de las solicitudes.
* 
## Technologies

*   **Spring Boot:** The core framework for building the application.
*   **Spring Security:** Handles authentication and authorization.
*   **JSON Web Tokens (JWT):** Used for secure authentication.
*   **Spring Data JPA:** Simplifies database interactions.
*   **PostgreSQL:** The relational database management system.
*   **Flyway:** Database migration tool.
*   **Spring Mail:** Sends emails.
*   **PayPal:** Handles payments.
*   **Springdoc OpenAPI:** Generates API documentation in HTML format.
*   **JasperReports:** Generates PDF reports. 
*   **Lombok:** Reduces boilerplate code.
*   **Maven:** Build tool for dependency management and project building.
*   **Java 21:** The programming language used for the application.

## Setup and Installation

1.  **Prerequisites:**
    *   Java Development Kit (JDK) 21 or higher
    *   Maven
    *   PostgreSQL database installed and running.

2.  **Clone the repository:**
    ```bash
    git clone https://github.com/travezvinueza/RolesAndPermission.git
    ```

3.  **Email smtp setup Configuration:**
    *   Open `src/main/resources/application.properties`.
    ```properties
    spring.mail.username=<EMAIL_USERNAME>
    spring.mail.password=<EMAIL_PASSWORD>
    ```

4.  **Paypal Configuration:**
     ```properties
     paypal.client.id=<YOUR_PAYPAL_CLIENT_ID>
     paypal.client.secret=<YOUR_PAYPAL_CLIENT_SECRET>
     ```

5. **Build and Run:**
    ```bash
    mvn clean install
    mvn spring-boot:run
    ```

   The application will start on `http://localhost:8081` (default Spring Boot port).

## Further Development

*   **More detailed error handling:** Improve error handling and provide more informative error messages.
*   **Comprehensive testing:** Add unit and integration tests.
*   **Enhanced security:** Consider additional security measures, such as input sanitization and rate limiting.
*   **Role based authorization**


