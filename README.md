<div align="center">

# 🎉 Eventify-SpringSecurity

### 🔐 Application de Gestion d'Événements avec Authentification Basic

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Security](https://img.shields.io/badge/Spring%20Security-6.5.6-brightgreen.svg)](https://spring.io/projects/spring-security)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-12+-blue.svg)](https://www.postgresql.org/)
[![License](https://img.shields.io/badge/License-Educational-red.svg)](https://opensource.org/licenses/)

*API REST sécurisée pour la gestion complète des événements avec authentification Basic HTTP*

[Fonctionnalités](#-fonctionnalités) • [Installation](#-installation) • [API Documentation](#-api-rest) • [Sécurité](#-configuration-de-sécurité)

</div>

---

## 📋 Table des matières

- [À propos](#-à-propos)
- [Fonctionnalités](#-fonctionnalités)
- [Stack Technique](#-stack-technique)
- [Architecture](#-architecture)
- [Installation](#-installation)
- [API REST](#-api-rest)
- [Base de données](#-base-de-données)
- [Configuration](#-configuration)
- [Configuration de sécurité](#-configuration-de-sécurité)
- [Gestion des erreurs](#-gestion-des-erreurs)

---

## 🎯 À propos

**Eventify-SpringSecurity** est une application REST permettant de gérer des événements avec un système d'authentification basé sur Spring Security. L'application permet aux utilisateurs de s'inscrire à des événements, aux organisateurs de créer et gérer leurs événements, et aux administrateurs de superviser le système.

### 🏢 Contexte

Eventify est une application permettant de gérer des événements. Les utilisateurs peuvent s'inscrire, les organisateurs peuvent créer et gérer leurs événements, et l'administrateur supervise tout le système.

### 🎯 Objectif

Développer une **API REST sécurisée** avec authentification **Basic HTTP** permettant de gérer l'ensemble du cycle de vie des événements et des inscriptions, avec une **gestion des rôles** (USER, ORGANIZER, ADMIN) et une **architecture stateless**.

---

## ✨ Fonctionnalités

### 👥 Gestion des Utilisateurs

- ✅ Inscription publique (POST `/api/public/users`)
- ✅ Consultation du profil utilisateur
- ✅ Gestion complète par l'administrateur (CRUD)
- ✅ Modification des rôles par l'administrateur
- 📊 **Rôles disponibles** : `ROLE_USER`, `ROLE_ORGANIZER`, `ROLE_ADMIN`
- 🔐 **Sécurité** : Mots de passe encodés avec BCrypt

### 🎪 Gestion des Événements

- ✅ Création d'événements par les organisateurs
- ✅ Modification (uniquement par l'organisateur propriétaire)
- ✅ Suppression (par l'organisateur ou l'administrateur)
- ✅ Consultation publique de tous les événements
- ✅ Suivi de la capacité et des inscriptions
- 📊 **Informations gérées** : titre, description, lieu, date/heure, capacité, organisateur

### 📝 Gestion des Inscriptions

- ✅ Inscription à un événement (utilisateurs)
- ✅ Consultation de ses inscriptions
- ✅ Annulation d'inscription
- ✅ Vérification automatique de la capacité
- ✅ Statuts disponibles : `PENDING`, `REGISTERED`, `CANCELLED`
- 📊 **Traçabilité** : Horodatage automatique des inscriptions

### 🔐 Sécurité

- ✅ Authentification Basic HTTP
- ✅ Architecture stateless (pas de session)
- ✅ Protection par rôles (USER, ORGANIZER, ADMIN)
- ✅ Gestion personnalisée des erreurs 401/403
- ✅ Custom Authentication Provider
- ✅ Custom UserDetailsService

---

## 🛠 Stack Technique

### 🔧 Backend & Framework

| Technologie | Version | Description |
|------------|---------|-------------|
| ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-6DB33F?logo=spring-boot&logoColor=white) | 3.5.7 | Framework principal |
| ![Spring Security](https://img.shields.io/badge/Spring%20Security-6.5.6-6DB33F?logo=spring&logoColor=white) | 6.5.6 | Sécurité et authentification |
| ![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-3.5.7-6DB33F?logo=spring&logoColor=white) | 3.5.7 | Accès aux données |
| ![Java](https://img.shields.io/badge/Java-17-ED8B00?logo=openjdk&logoColor=white) | 17 | Langage de programmation |

### 🗄️ Base de données

| Technologie | Version | Description |
|------------|---------|-------------|
| ![PostgreSQL](https://img.shields.io/badge/PostgreSQL-12+-4169E1?logo=postgresql&logoColor=white) | 12+ | Base de données relationnelle |
| ![Liquibase](https://img.shields.io/badge/Liquibase-Latest-2962FF?logo=liquibase&logoColor=white) | Latest | Gestion des migrations |

### 📝 Mapping & Validation

| Technologie | Version | Description |
|------------|---------|-------------|
| ![MapStruct](https://img.shields.io/badge/MapStruct-1.5.5-FF6C37?logo=java&logoColor=white) | 1.5.5 | Mapping Entity ↔ DTO |
| ![Lombok](https://img.shields.io/badge/Lombok-1.18.30-BC4521?logo=lombok&logoColor=white) | 1.18.30 | Réduction du code boilerplate |
| ![Jakarta Validation](https://img.shields.io/badge/Jakarta%20Validation-3.0-007396?logo=java&logoColor=white) | 3.0 | Validation des données |

---

## 🏗 Architecture

### 📐 Architecture en couches

```
┌─────────────────────────────────────────────────┐
│         🌐 REST Controllers                     │
│    (Endpoints API - Authentification Basic)     │
└────────────────┬────────────────────────────────┘
                 │
                 ▼
┌─────────────────────────────────────────────────┐
│         🔐 Spring Security Filter Chain         │
│  (CustomAuthenticationProvider, EntryPoint)     │
└────────────────┬────────────────────────────────┘
                 │
                 ▼
┌─────────────────────────────────────────────────┐
│         📋 DTOs (Data Transfer)                 │
│  (UserDTO, EventDTO, RegistrationDTO...)       │
└────────────────┬────────────────────────────────┘
                 │
                 ▼ MapStruct
┌─────────────────────────────────────────────────┐
│         🔄 Mappers                              │
│  (Conversion automatique Entity ↔ DTO)         │
└────────────────┬────────────────────────────────┘
                 │
                 ▼
┌─────────────────────────────────────────────────┐
│         💼 Services                             │
│  (Logique métier - Validation, Autorisation)   │
└────────────────┬────────────────────────────────┘
                 │
                 ▼
┌─────────────────────────────────────────────────┐
│         🗃️ Repositories                         │
│         (Spring Data JPA)                       │
└────────────────┬────────────────────────────────┘
                 │
                 ▼
┌─────────────────────────────────────────────────┐
│         🐘 PostgreSQL Database                 │
│         (Tables + Liquibase)                    │
└─────────────────────────────────────────────────┘
```

### 📦 Structure des packages

```
com.eventify.springsecurity
├── 📂 controller/          # Contrôleurs REST
│   ├── PublicController.java
│   ├── UserController.java
│   ├── OrganizerController.java
│   └── AdminController.java
├── 📂 security/            # Composants Spring Security
│   ├── CustomUserDetailsService.java
│   ├── CustomAuthenticationProvider.java
│   ├── CustomAuthenticationEntryPoint.java
│   └── CustomAccessDeniedHandler.java
├── 📂 config/             # Configuration
│   └── SecurityConfig.java
├── 📂 service/            # Interfaces de services
│   ├── UserService.java
│   ├── EventService.java
│   ├── RegistrationService.java
│   └── 📂 impl/           # Implémentations des services
│       ├── UserServiceImpl.java
│       ├── EventServiceImpl.java
│       └── RegistrationServiceImpl.java
├── 📂 repository/         # Accès aux données
│   ├── UserRepository.java
│   ├── EventRepository.java
│   └── RegistrationRepository.java
├── 📂 entity/             # Entités JPA
│   ├── User.java
│   ├── Event.java
│   └── Registration.java
├── 📂 dto/                # Data Transfer Objects
│   ├── UserCreateDTO.java
│   ├── UserResponseDTO.java
│   ├── EventCreateDTO.java
│   ├── EventUpdateDTO.java
│   ├── EventResponseDTO.java
│   ├── RegistrationResponseDTO.java
│   ├── ChangeRoleRequest.java
│   └── ErrorResponseDTO.java
├── 📂 mapper/             # Mappers MapStruct
│   ├── UserMapper.java
│   ├── EventMapper.java
│   └── RegistrationMapper.java
├── 📂 enums/              # Énumérations
│   ├── Role.java
│   └── RegistrationStatus.java
└── 📂 exception/          # Gestion des exceptions
    ├── GlobalExceptionHandler.java
    ├── UsernameAlreadyExistsException.java
    ├── EventNotFoundException.java
    ├── UserNotFoundException.java
    ├── UnauthorizedActionException.java
    └── InvalidRoleException.java
```

---

## 🚀 Installation

### 📋 Prérequis

Avant de commencer, assurez-vous d'avoir installé :

- ☕ **Java 17** ou supérieur
- 🐘 **PostgreSQL 12** ou supérieur
- 📦 **Maven 3.8** ou supérieur
- 🔧 **Git** (pour cloner le projet)

### 📥 Étape 1 : Cloner le projet

```bash
git clone https://github.com/fatima-ezzahraalouane/Eventify-SpringSecurity.git
cd Eventify-SpringSecurity
```

### 🗄️ Étape 2 : Créer la base de données

Connectez-vous à PostgreSQL et exécutez :

```sql
CREATE DATABASE eventify;
```

### ⚙️ Étape 3 : Configuration

1. **Créer le fichier de configuration** :
   ```bash
   cp src/main/resources/application.properties.example src/main/resources/application.properties
   ```

2. **Modifier les paramètres** dans `src/main/resources/application.properties` :

   ```properties
   # Configuration PostgreSQL
   spring.datasource.url=jdbc:postgresql://localhost:5432/eventify
   spring.datasource.username=votre_username
   spring.datasource.password=votre_password
   ```

### 🔨 Étape 4 : Compiler et lancer

```bash
# Compiler le projet
mvn clean install

# Lancer l'application
mvn spring-boot:run
```

### ✅ Étape 5 : Vérifier l'installation

L'application sera accessible sur :

- 🌐 **API** : http://localhost:8080
- 📋 **Endpoints publics** : http://localhost:8080/api/public/events

---

## 🌐 API REST

### 🔌 Endpoints disponibles

#### 🌍 Endpoints Publics (sans authentification)

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| `POST` | `/api/public/users` | ➕ Inscription d'un nouvel utilisateur |
| `GET` | `/api/public/events` | 📋 Liste de tous les événements publics |

#### 👤 Endpoints USER (nécessite `ROLE_USER`)

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| `GET` | `/api/user/profile` | 👤 Profil de l'utilisateur authentifié |
| `POST` | `/api/user/events/{id}/register` | 📝 S'inscrire à un événement |
| `GET` | `/api/user/registrations` | 📋 Liste des inscriptions de l'utilisateur |

#### 🎪 Endpoints ORGANIZER (nécessite `ROLE_ORGANIZER`)

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| `POST` | `/api/organizer/events` | ➕ Créer un événement |
| `PUT` | `/api/organizer/events/{id}` | ✏️ Modifier un événement (propriétaire uniquement) |
| `DELETE` | `/api/organizer/events/{id}` | 🗑️ Supprimer un événement (propriétaire uniquement) |

#### 👑 Endpoints ADMIN (nécessite `ROLE_ADMIN`)

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| `GET` | `/api/admin/users` | 📋 Liste de tous les utilisateurs |
| `PUT` | `/api/admin/users/{id}/role` | 🔄 Modifier le rôle d'un utilisateur |
| `DELETE` | `/api/admin/events/{id}` | 🗑️ Supprimer n'importe quel événement |

### 📄 Exemples de requêtes

#### Inscription d'un utilisateur (Public)

```http
POST /api/public/users
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "password123"
}
```

**Réponse** : `201 Created`
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "role": "ROLE_USER"
}
```

#### Créer un événement (Organizer)

```http
POST /api/organizer/events
Authorization: Basic <base64(email:password)>
Content-Type: application/json

{
  "title": "Conférence Spring Security",
  "description": "Conférence sur la sécurité avec Spring",
  "location": "Paris",
  "dateTime": "2024-12-25T10:00:00",
  "capacity": 100
}
```

#### S'inscrire à un événement (User)

```http
POST /api/user/events/1/register
Authorization: Basic <base64(email:password)>
```

**Réponse** : `201 Created`
```json
{
  "id": 1,
  "user": {
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com"
  },
  "event": {
    "id": 1,
    "title": "Conférence Spring Security"
  },
  "registeredAt": "2024-11-21T10:00:00",
  "status": "REGISTERED"
}
```

#### Modifier le rôle d'un utilisateur (Admin)

```http
PUT /api/admin/users/1/role
Authorization: Basic <base64(email:password)>
Content-Type: application/json

{
  "role": "ROLE_ORGANIZER"
}
```

### 🔐 Authentification Basic HTTP

Tous les endpoints protégés utilisent l'**authentification Basic HTTP** :

**Format** : `Authorization: Basic <base64(email:password)>`

**Exemple** :
- Email : `user@example.com`
- Password : `password123`
- Encodé en Base64 : `dXNlckBleGFtcGxlLmNvbTpwYXNzd29yZDEyMw==`

**Requête** :
```http
GET /api/user/profile
Authorization: Basic dXNlckBleGFtcGxlLmNvbTpwYXNzd29yZDEyMw==
```

---

## 🗄️ Base de données

### 📊 Modèle de données

```mermaid
erDiagram
    USER ||--o{ EVENT : organise
    USER ||--o{ REGISTRATION : inscrit
    EVENT ||--o{ REGISTRATION : a_des_inscriptions
    
    USER {
        bigint id PK
        varchar name
        varchar email UK
        varchar password
        varchar role
    }
    
    EVENT {
        bigint id PK
        varchar title
        text description
        varchar location
        timestamp date_time
        int capacity
        bigint organizer_id FK
    }
    
    REGISTRATION {
        bigint id PK
        bigint user_id FK
        bigint event_id FK
        timestamp registered_at
        varchar status
    }
```

### 🔄 Migrations Liquibase

Les migrations de base de données sont gérées par **Liquibase** :

```
src/main/resources/db/changelog/
├── db.changelog-master.yaml       # Fichier principal
└── migrations/                    # Dossier des migrations
    ├── 001-create-users-table.yaml
    ├── 002-create-events-table.yaml
    └── 003-create-registrations-table.yaml
```

Liquibase s'exécute automatiquement au démarrage de l'application.

### 📋 Tables

#### Table `users`
- `id` : Identifiant unique (BIGSERIAL)
- `name` : Nom de l'utilisateur (VARCHAR, NOT NULL)
- `email` : Email unique (VARCHAR, UNIQUE, NOT NULL)
- `password` : Mot de passe encodé BCrypt (VARCHAR, NOT NULL)
- `role` : Rôle de l'utilisateur (VARCHAR, NOT NULL) - Valeurs : `ROLE_USER`, `ROLE_ORGANIZER`, `ROLE_ADMIN`

#### Table `events`
- `id` : Identifiant unique (BIGSERIAL)
- `title` : Titre de l'événement (VARCHAR, NOT NULL)
- `description` : Description (TEXT)
- `location` : Lieu (VARCHAR)
- `date_time` : Date et heure (TIMESTAMP, NOT NULL)
- `capacity` : Capacité maximale (INTEGER, NOT NULL)
- `organizer_id` : Référence à l'organisateur (BIGINT, FK, NOT NULL)

#### Table `registrations`
- `id` : Identifiant unique (BIGSERIAL)
- `user_id` : Référence à l'utilisateur (BIGINT, FK, NOT NULL)
- `event_id` : Référence à l'événement (BIGINT, FK, NOT NULL)
- `registered_at` : Date d'inscription (TIMESTAMP, NOT NULL)
- `status` : Statut de l'inscription (VARCHAR, NOT NULL) - Valeurs : `REGISTERED`, `CANCELLED`
- **Contrainte unique** : `(user_id, event_id)` - Un utilisateur ne peut s'inscrire qu'une fois par événement

---

## ⚙️ Configuration

### 📝 application.properties

```properties
# ========================================
# Configuration PostgreSQL
# ========================================
spring.datasource.url=jdbc:postgresql://localhost:5432/eventify
spring.datasource.username=postgres
spring.datasource.password=admin
spring.datasource.driver-class-name=org.postgresql.Driver

# ========================================
# Configuration JPA/Hibernate
# ========================================
spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# ========================================
# Configuration Liquibase
# ========================================
spring.liquibase.enabled=true
spring.liquibase.change-log=classpath:db/changelog/db.changelog-master.yaml

# ========================================
# Configuration Serveur
# ========================================
server.port=8080

# ========================================
# Configuration Logging
# ========================================
logging.level.com.eventify=DEBUG
logging.level.org.springframework.web=INFO
logging.level.org.springframework.security=DEBUG
logging.level.org.hibernate.SQL=DEBUG
```

---

## 🔐 Configuration de sécurité

### 🏗️ Architecture de sécurité

L'application utilise **Spring Security** avec une authentification **Basic HTTP** dans une architecture **stateless**.

#### Composants de sécurité

1. **CustomUserDetailsService** (`com.eventify.springsecurity.security.CustomUserDetailsService`)
   - Implémente `UserDetailsService`
   - Charge les utilisateurs depuis la base de données PostgreSQL
   - Convertit les rôles de l'entité `User` en `GrantedAuthority`

2. **CustomAuthenticationProvider** (`com.eventify.springsecurity.security.CustomAuthenticationProvider`)
   - Implémente `AuthenticationProvider`
   - Authentifie les utilisateurs en vérifiant :
     - L'existence de l'utilisateur via `UserDetailsService`
     - La correspondance du mot de passe avec `BCryptPasswordEncoder`

3. **CustomAuthenticationEntryPoint** (`com.eventify.springsecurity.security.CustomAuthenticationEntryPoint`)
   - Gère les erreurs **401 Unauthorized**
   - Retourne un format JSON standardisé

4. **CustomAccessDeniedHandler** (`com.eventify.springsecurity.security.CustomAccessDeniedHandler`)
   - Gère les erreurs **403 Forbidden**
   - Retourne un format JSON standardisé

### ⚙️ Configuration Spring Security

Le fichier `SecurityConfig` configure :

#### 1. Architecture stateless
```java
.sessionManagement(session -> session
    .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
```
- ✅ Aucune session n'est créée
- ✅ Chaque requête doit inclure les credentials

#### 2. Désactivation CSRF
```java
.csrf(csrf -> csrf.disable())
```
- ✅ Nécessaire pour une API stateless (REST)

#### 3. Authentification Basic HTTP
```java
.httpBasic(httpBasic -> httpBasic
    .authenticationEntryPoint(customAuthenticationEntryPoint))
```
- ✅ Utilise l'authentification Basic HTTP standard
- ✅ Format : `Authorization: Basic <base64(email:password)>`

#### 4. Règles d'autorisation
```java
.authorizeHttpRequests(auth -> auth
    .requestMatchers("/api/public/**").permitAll()
    .requestMatchers("/api/user/**").hasRole("USER")
    .requestMatchers("/api/organizer/**").hasRole("ORGANIZER")
    .requestMatchers("/api/admin/**").hasRole("ADMIN")
    .anyRequest().authenticated())
```

**Règles** :
- `/api/public/**` → ✅ Accessible sans authentification
- `/api/user/**` → ✅ Nécessite `ROLE_USER`
- `/api/organizer/**` → ✅ Nécessite `ROLE_ORGANIZER`
- `/api/admin/**` → ✅ Nécessite `ROLE_ADMIN`
- Toutes les autres requêtes → ✅ Nécessitent une authentification

#### 5. Encodage des mots de passe
```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```
- ✅ Utilise **BCrypt** pour encoder les mots de passe
- ✅ Force de hachage : 10 (par défaut)

### 👥 Rôles utilisés

- **ROLE_USER** : Rôle par défaut lors de l'inscription
  - Peut consulter son profil
  - Peut s'inscrire à des événements
  - Peut consulter ses inscriptions

- **ROLE_ORGANIZER** : Peut créer, modifier et supprimer ses propres événements
  - Peut créer des événements
  - Peut modifier uniquement ses événements
  - Peut supprimer uniquement ses événements

- **ROLE_ADMIN** : Accès complet
  - Peut consulter tous les utilisateurs
  - Peut modifier les rôles des utilisateurs
  - Peut supprimer n'importe quel événement

### 🧪 Profil de test

Un profil Spring `test` est disponible pour bypasser l'authentification dans les tests :

```java
@ActiveProfiles("test")
@SpringBootTest
class MyTest {
    // Les tests peuvent s'exécuter sans authentification
}
```

---

## 🚨 Gestion des erreurs

### 📄 Format d'erreur standardisé

L'API retourne des réponses JSON structurées :

```json
{
  "timestamp": "2024-11-21T10:00:00",
  "status": 403,
  "error": "Forbidden",
  "message": "Vous n'avez pas les droits pour accéder à cette ressource",
  "path": "/api/admin/users"
}
```

### 🔢 Codes HTTP

| Code | Description | Exemple |
|------|-------------|---------|
| `200` | Succès | Consultation réussie |
| `201` | Ressource créée | Utilisateur/Événement créé |
| `204` | Pas de contenu | Suppression réussie |
| `400` | Erreur de validation | Données invalides |
| `401` | Non authentifié | Credentials manquants ou invalides |
| `403` | Accès refusé | Rôle insuffisant |
| `404` | Ressource non trouvée | Utilisateur/Événement introuvable |
| `409` | Conflit | Email déjà utilisé |
| `500` | Erreur serveur | Erreur interne |

### 🎯 Exceptions personnalisées

| Exception | Code HTTP | Description |
|-----------|-----------|-------------|
| `UsernameAlreadyExistsException` | `409 Conflict` | Email déjà utilisé |
| `EventNotFoundException` | `404 Not Found` | Événement introuvable |
| `UserNotFoundException` | `404 Not Found` | Utilisateur introuvable |
| `UnauthorizedActionException` | `403 Forbidden` | Action non autorisée |
| `InvalidRoleException` | `400 Bad Request` | Rôle invalide |

### 📋 Exemples d'erreurs

#### 401 Unauthorized
```json
{
  "timestamp": "2024-11-21T10:00:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Authentification requise",
  "path": "/api/user/profile"
}
```

#### 403 Forbidden
```json
{
  "timestamp": "2024-11-21T10:00:00",
  "status": 403,
  "error": "Forbidden",
  "message": "Vous n'êtes pas autorisé à modifier cet événement",
  "path": "/api/organizer/events/1"
}
```

#### 404 Not Found
```json
{
  "timestamp": "2024-11-21T10:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Événement non trouvé avec l'ID : 999",
  "path": "/api/user/events/999/register"
}
```

#### 409 Conflict
```json
{
  "timestamp": "2024-11-21T10:00:00",
  "status": 409,
  "error": "Conflict",
  "message": "Un utilisateur avec cet email existe déjà",
  "path": "/api/public/users"
}
```

---

## 📮 Collection Postman

Une collection Postman complète est disponible pour tester tous les endpoints de l'API :

📁 **Fichiers** :
- `Eventify-API.postman_collection.json` - Collection avec tous les endpoints
- `Eventify-Environment.postman_environment.json` - Variables d'environnement

**Comment l'utiliser** :

1. Ouvrir Postman
2. Cliquer sur **Import**
3. Sélectionner les fichiers JSON
4. La collection contient tous les endpoints avec des exemples de requêtes

**Documentation complète** : Consultez `README-Postman.md` pour les instructions détaillées.

---

## 📚 Documentation complémentaire

### 📄 Fichiers de documentation

- 📦 **[README-Postman.md](README-Postman.md)** - Guide d'utilisation de la collection Postman
- 📊 **[Diagram/classDiagram.png](Diagram/classDiagram.png)** - Diagramme de classes UML

### 🔗 Ressources utiles

- 📖 [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- 📖 [Spring Security Documentation](https://spring.io/projects/spring-security)
- 📖 [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- 📖 [MapStruct Documentation](https://mapstruct.org/)
- 📖 [Liquibase Documentation](https://docs.liquibase.com/)

---

## 👨‍💻 Auteurs

Développé avec ❤️ en binôme par **Fatima-Ezzahra Alouane** et **Salma Hamdi** pour **Eventify**

### 🔗 Connectez-vous avec nous

#### Fatima-Ezzahra Alouane

[![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/fatima-ezzahra-alouane)
[![Portfolio](https://img.shields.io/badge/Portfolio-FF5722?style=for-the-badge&logo=google-chrome&logoColor=white)](https://fatima-ezzahra-alouane.vercel.app)

#### Salma Hamdi

[![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/salma-hamdi-4b66472a1/)

---

<div align="center">

### 🌟 Si ce projet vous a aidé, n'oubliez pas de lui donner une étoile ! ⭐

**[⬆ Retour en haut](#-eventify-springsecurity)**

</div>

