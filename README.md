# 📌 Mini App Gestión de Tickets  

Este proyecto consiste en una aplicación full-stack para la gestión de tickets internos.  
Incluye **backend en Java 21 (Spring Boot)** y **frontend en Angular 20**, con autenticación, autorización y un diseño moderno y responsivo.  

---

## 🚀 Funcionalidades  

- **Autenticación y roles**  
  - Login con JWT (Spring Security).  
  - `ADMIN`: puede crear, editar, cerrar y eliminar tickets.  
  - `USER`: puede crear, listar, filtrar, editar y cerrar tickets (no puede eliminar).  

- **Gestión de tickets**  
  - Crear, listar, editar, cerrar y eliminar tickets.  
  - Filtros por estado, prioridad y búsqueda por título/descrición.  
  - Cada ticket tiene:  
    - `id`  
    - `title`  
    - `description`  
    - `priority` (`LOW`, `MEDIUM`, `HIGH`)  
    - `status` (`OPEN`, `IN_PROGRESS`, `CLOSED`)  
    - `assignee`  
    - `tags[]`  
    - `createdAt` / `updatedAt`  

- **Extras**  
  - Semilla inicial con **20 tickets por defecto**.  
  - Sanitización de campos con **Jsoup**.  
  - Documentación con **Swagger UI**.  
  - **Rate limiting** con Bucket4j.  
  - Migraciones automáticas con **Flyway**.  
  - Base de datos **PostgreSQL**.  

---

## 🛠️ Tecnologías  

### Backend (Java 21)  
- Spring Boot 3  
- Spring Security + JWT  
- Flyway (migraciones)  
- Bucket4j (rate limiting)  
- Swagger (documentación)  
- JUnit y pruebas de integración  
- PostgreSQL  

### Frontend (Angular 20)  
- Angular Material  
- TailwindCSS  
- Signals API  
- Responsive design  
- Skeleton loaders  
- ESLint + Prettier (lint & formatter)  

---

## 🔑 Usuarios por defecto  

| Usuario   | Contraseña   | Rol   |
|-----------|-------------|-------|
| `admin`   | `admin123`  | ADMIN |
| `user1`   | `usuario123`| USER  |

---

## ⚙️ Instalación con Docker Compose  

1. Clonar el repositorio  
   ```bash
   git clone https://github.com/oalejandromendez/ticket-management.git
   cd a la carpeta
   ```
2. Construir e iniciar servicios
    ```bash
    docker compose up --build
   ```

3. 🌐 Aplicación disponible en:
- **Frontend (Angular 20)**: [http://localhost:3000](http://localhost:3000)  
- **Backend (Spring Boot 21 + Swagger UI)**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)  
- **Base de datos (PostgreSQL)**: `localhost:5432`  
  - Usuario: `admin`  
  - Contraseña: `admin123`

## 📖 Notas

- **Seguridad:** Se implementa autenticación básica con JWT y roles (`ADMIN` y `USER`).  
- **Rate limiting:** Control de peticiones usando `Bucket4j`.  
- **Validación y sanitización:** Todos los campos de entrada se sanitizan con `Jsoup` para prevenir inyecciones.  
- **Documentación:** Swagger disponible para el backend (`/swagger-ui.html`).  
- **Migraciones:** Flyway gestiona la creación y actualización de la base de datos PostgreSQL.  
- **UI:** La interfaz es responsiva, accesible y limpia, utilizando Angular Material y TailwindCSS.  
- **Performance y experiencia:** Se usan Signals, paginación y loaders tipo skeleton para mejorar la experiencia de usuario.  
- **Estándares de código:** ESLint y Prettier aplicados para mantener consistencia en el frontend.
   
