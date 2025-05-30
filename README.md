# HopeShare - Plateforme de Charité

## Présentation

HopeShare est une plateforme web dédiée à la promotion et au soutien des actions de charité. Elle permet aux utilisateurs de découvrir des actions caritatives, de faire des dons, et aux organisations de présenter leurs initiatives.

## Architecture

L'application est construite selon une architecture en couches, suivant les principes de l'architecture hexagonale (ou en ports et adaptateurs) :

### Couches de l'application

1. **Couche Présentation** : Contrôleurs REST et templates Thymeleaf
2. **Couche Service** : Logique métier et règles de gestion
3. **Couche Persistance** : Accès aux données via JPA/Hibernate
4. **Couche Sécurité** : Authentification et autorisation

## Technologies utilisées

### Backend
- **Java 17** : Langage de programmation
- **Spring Boot 3.x** : Framework pour le développement d'applications Java
- **Spring Security** : Gestion de la sécurité et de l'authentification
- **Spring Data JPA** : Accès aux données via JPA
- **Hibernate** : ORM (Object-Relational Mapping)
- **JWT (JSON Web Tokens)** : Authentification sans état
- **Maven** : Gestion des dépendances et build

### Frontend
- **HTML5/CSS3** : Structure et style
- **JavaScript** : Interactivité côté client
- **Bootstrap 5** : Framework CSS pour le design responsive
- **Thymeleaf** : Moteur de template pour le rendu côté serveur
- **Font Awesome** : Icônes

### Base de données
- **H2** : Base de données en mémoire pour le développement
- **PostgreSQL** : Base de données relationnelle pour la production

## Structure du projet

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── charityapp/
│   │           ├── controllers/       # Contrôleurs REST et MVC
│   │           ├── entities/          # Entités JPA
│   │           ├── repositories/      # Repositories Spring Data
│   │           ├── services/          # Services métier
│   │           ├── security/          # Configuration de sécurité
│   │           ├── dto/               # Objets de transfert de données
│   │           └── CharityBackendApplication.java  # Point d'entrée
│   └── resources/
│       ├── static/                    # Ressources statiques (CSS, JS, images)
│       ├── templates/                 # Templates Thymeleaf
│       └── application.properties     # Configuration
└── test/                              # Tests unitaires et d'intégration
```

## Entités principales

### Utilisateur
Représente un utilisateur de la plateforme avec ses informations personnelles et d'authentification.

### ActionDeCharite
Représente une action caritative avec son titre, description, objectif de collecte, etc.

### Organisation
Représente une organisation caritative avec ses informations de contact et sa mission.

### Categorie
Représente une catégorie d'actions caritatives (éducation, santé, environnement, etc.).

### Don
Représente un don fait par un utilisateur à une action caritative.

## Fonctionnalités principales

### Authentification et autorisation
- Inscription et connexion des utilisateurs
- Gestion des rôles (utilisateur, admin, organisation)
- Authentification par JWT

### Gestion des actions caritatives
- Création et modification d'actions caritatives
- Affichage des actions avec filtrage par catégorie
- Suivi de la progression des collectes

### Gestion des dons
- Possibilité de faire des dons aux actions caritatives
- Historique des dons par utilisateur
- Confirmation et remerciement automatiques

### Interface utilisateur
- Design responsive adapté à tous les appareils
- Navigation intuitive
- Affichage des actions en vedette
- Filtres et recherche

## Sécurité

L'application utilise Spring Security avec JWT pour l'authentification :
- Les tokens JWT sont générés lors de la connexion
- Les tokens sont validés à chaque requête
- Les routes sensibles sont protégées
- Les mots de passe sont hashés avec BCrypt

## API REST

L'application expose plusieurs endpoints REST :

- `/api/auth/register` : Inscription d'un nouvel utilisateur
- `/api/auth/login` : Connexion et génération de token JWT
- `/api/actions` : Gestion des actions caritatives
- `/api/categories` : Gestion des catégories
- `/api/organisations` : Gestion des organisations
- `/api/utilisateurs` : Gestion des utilisateurs
- `/api/dons` : Gestion des dons

## Configuration du Projet

### Prérequis
- Java 17 ou supérieur
- Maven
- MySQL
- Compte Google Developer (pour OAuth2)

### Installation

1. Cloner le projet
```bash
git clone https://github.com/votre-username/hopeshare.git
cd hopeshare
```

2. Configuration de la base de données
- Créer une base de données MySQL nommée `hopeshare`
- Copier `src/main/resources/application.properties.example` vers `src/main/resources/application.properties`
- Modifier les paramètres de connexion dans `application.properties`

3. Configuration OAuth2 Google
- Aller sur [Google Cloud Console](https://console.cloud.google.com)
- Créer un nouveau projet
- Activer l'API Google+ API
- Créer des identifiants OAuth2
- Copier le Client ID et Client Secret dans `application.properties`

4. Compiler et exécuter
```bash
mvn clean install
mvn spring-boot:run
```

### Structure du Projet
```
src/
├── main/
│   ├── java/
│   │   └── com/charityapp/
│   │       ├── config/      # Configuration Spring
│   │       ├── controllers/ # Contrôleurs REST
│   │       ├── entities/    # Entités JPA
│   │       ├── repositories/# Repositories Spring Data
│   │       ├── services/    # Services métier
│   │       └── security/    # Configuration sécurité
│   └── resources/
│       ├── static/         # Ressources statiques
│       ├── templates/      # Templates Thymeleaf
│       └── application.properties # Configuration
```

### Sécurité
- Ne jamais commiter `application.properties` avec des valeurs réelles
- Utiliser des variables d'environnement pour les secrets en production
- Changer régulièrement les clés JWT et les secrets OAuth2
- Activer HTTPS en production

### Déploiement
1. Configurer les variables d'environnement
2. Construire le JAR
```bash
mvn clean package
```
3. Exécuter le JAR
```bash
java -jar target/hopeshare-0.0.1-SNAPSHOT.jar
```

## Contribution
1. Fork le projet
2. Créer une branche pour votre fonctionnalité
3. Commiter vos changements
4. Pousser vers la branche
5. Créer une Pull Request

## Licence
Ce projet est sous licence MIT. Voir le fichier `LICENSE` pour plus de détails. 