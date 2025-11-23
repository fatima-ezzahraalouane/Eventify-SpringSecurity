# Guide d'utilisation de la Collection Postman Eventify

## 📦 Fichiers fournis

1. **Eventify-API.postman_collection.json** - Collection Postman avec tous les endpoints
2. **Eventify-Environment.postman_environment.json** - Variables d'environnement (optionnel)

## 🚀 Installation

### Étape 1 : Importer la collection
1. Ouvrez Postman
2. Cliquez sur **Import** (en haut à gauche)
3. Sélectionnez le fichier `Eventify-API.postman_collection.json`
4. Cliquez sur **Import**

### Étape 2 : Importer l'environnement (optionnel mais recommandé)
1. Cliquez sur **Import**
2. Sélectionnez le fichier `Eventify-Environment.postman_environment.json`
3. Cliquez sur **Import**
4. Sélectionnez l'environnement "Eventify Local Environment" dans le menu déroulant en haut à droite

## 📝 Configuration initiale

### 1. Créer des utilisateurs de test

Avant de tester les endpoints protégés, vous devez créer des utilisateurs avec différents rôles :

#### a) Créer un utilisateur USER (par défaut)
- Utilisez **POST /api/public/users** avec :
```json
{
  "name": "User Test",
  "email": "user@example.com",
  "password": "password123"
}
```
- Notez l'ID de l'utilisateur créé

#### b) Créer un utilisateur ORGANIZER
- Créez d'abord un utilisateur via **POST /api/public/users**
- Puis modifiez son rôle en base de données ou utilisez l'endpoint admin (si vous avez un admin)

#### c) Créer un utilisateur ADMIN
- Créez d'abord un utilisateur via **POST /api/public/users**
- Puis modifiez son rôle en base de données :
```sql
UPDATE users SET role = 'ROLE_ADMIN' WHERE email = 'admin@example.com';
```

### 2. Mettre à jour les variables d'environnement

Dans Postman, modifiez les variables selon vos utilisateurs créés :
- `user_email` : Email de votre utilisateur USER
- `user_password` : Mot de passe de votre utilisateur USER
- `organizer_email` : Email de votre utilisateur ORGANIZER
- `organizer_password` : Mot de passe de votre utilisateur ORGANIZER
- `admin_email` : Email de votre utilisateur ADMIN
- `admin_password` : Mot de passe de votre utilisateur ADMIN
- `event_id` : ID d'un événement existant (après en avoir créé un)
- `user_id` : ID d'un utilisateur (pour tester le changement de rôle)

## 🧪 Ordre de test recommandé

### 1. Tests publics (sans authentification)
1. ✅ **POST /api/public/users** - Créer un utilisateur
2. ✅ **GET /api/public/events** - Lister les événements

### 2. Tests USER
1. ✅ **GET /api/user/profile** - Voir son profil
2. ✅ **POST /api/user/events/{id}/register** - S'inscrire à un événement
3. ✅ **GET /api/user/registrations** - Voir ses inscriptions

### 3. Tests ORGANIZER
1. ✅ **POST /api/organizer/events** - Créer un événement
2. ✅ **PUT /api/organizer/events/{id}** - Modifier son événement
3. ✅ **DELETE /api/organizer/events/{id}** - Supprimer son événement

### 4. Tests ADMIN
1. ✅ **GET /api/admin/users** - Lister tous les utilisateurs
2. ✅ **PUT /api/admin/users/{id}/role** - Modifier le rôle d'un utilisateur
3. ✅ **DELETE /api/admin/events/{id}** - Supprimer n'importe quel événement

## 🔐 Authentification Basic

Tous les endpoints protégés utilisent l'authentification Basic HTTP. Dans Postman :
- Les credentials sont automatiquement configurés via les variables d'environnement
- Si vous modifiez manuellement, utilisez :
  - **Type** : Basic Auth
  - **Username** : Email de l'utilisateur
  - **Password** : Mot de passe en clair

## ⚠️ Notes importantes

1. **Mots de passe** : Les mots de passe sont encodés avec BCrypt en base de données. Utilisez le mot de passe en clair dans Postman.

2. **Rôles** : 
   - Les rôles doivent être au format `ROLE_XXX` (ROLE_USER, ROLE_ORGANIZER, ROLE_ADMIN)
   - Par défaut, un nouvel utilisateur a le rôle `ROLE_USER`

3. **IDs dynamiques** : 
   - Après avoir créé un événement, mettez à jour la variable `event_id` avec l'ID retourné
   - Après avoir créé un utilisateur, mettez à jour la variable `user_id` avec l'ID retourné

4. **Erreurs** : 
   - **401 Unauthorized** : Identifiants invalides ou manquants
   - **403 Forbidden** : Rôle insuffisant pour accéder à la ressource
   - **404 Not Found** : Ressource non trouvée
   - **409 Conflict** : Email déjà utilisé

## 🎯 Scénarios de test

### Scénario 1 : Utilisateur s'inscrit et consulte les événements
1. Créer un utilisateur (POST /api/public/users)
2. Lister les événements (GET /api/public/events)
3. Voir son profil (GET /api/user/profile)
4. S'inscrire à un événement (POST /api/user/events/{id}/register)
5. Voir ses inscriptions (GET /api/user/registrations)

### Scénario 2 : Organisateur crée et gère un événement
1. Créer un événement (POST /api/organizer/events)
2. Modifier l'événement (PUT /api/organizer/events/{id})
3. Supprimer l'événement (DELETE /api/organizer/events/{id})

### Scénario 3 : Admin gère les utilisateurs
1. Lister tous les utilisateurs (GET /api/admin/users)
2. Modifier le rôle d'un utilisateur (PUT /api/admin/users/{id}/role)
3. Supprimer un événement (DELETE /api/admin/events/{id})

## 📊 Codes de réponse attendus

- **200 OK** : Requête réussie
- **201 Created** : Ressource créée avec succès
- **204 No Content** : Suppression réussie
- **400 Bad Request** : Erreur de validation
- **401 Unauthorized** : Authentification requise
- **403 Forbidden** : Accès refusé (rôle insuffisant)
- **404 Not Found** : Ressource non trouvée
- **409 Conflict** : Conflit (ex: email déjà utilisé)

