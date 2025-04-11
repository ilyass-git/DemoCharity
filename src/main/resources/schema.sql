-- Suppression des tables si elles existent (dans l'ordre inverse des dépendances)
DROP TABLE IF EXISTS utilisateur_interets;
DROP TABLE IF EXISTS media;
DROP TABLE IF EXISTS don;
DROP TABLE IF EXISTS action_de_charite;
DROP TABLE IF EXISTS organisation;
DROP TABLE IF EXISTS utilisateur;
DROP TABLE IF EXISTS categorie;

-- Création de la table categorie
CREATE TABLE IF NOT EXISTS categorie (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(1000) NOT NULL
);

-- Création de la table utilisateur
CREATE TABLE IF NOT EXISTS utilisateur (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    prenom VARCHAR(255) NOT NULL,
    nom VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    mot_de_passe VARCHAR(255) NOT NULL,
    numero_telephone VARCHAR(20) NOT NULL,
    adresse VARCHAR(255) NOT NULL,
    ville VARCHAR(255) NOT NULL,
    pays VARCHAR(255) NOT NULL,
    code_postal VARCHAR(20) NOT NULL,
    langue VARCHAR(10) NOT NULL,
    notifications_email_activees BOOLEAN NOT NULL DEFAULT TRUE,
    date_creation DATETIME NOT NULL,
    date_modification DATETIME NOT NULL
);

-- Création de la table organisation
CREATE TABLE IF NOT EXISTS organisation (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    adresse_legale VARCHAR(255) NOT NULL,
    numero_identification_fiscale VARCHAR(50) NOT NULL UNIQUE,
    nom_contact_principal VARCHAR(255) NOT NULL,
    email_contact_principal VARCHAR(255) NOT NULL,
    telephone_contact_principal VARCHAR(20) NOT NULL,
    url_logo VARCHAR(255) NOT NULL,
    description_mission VARCHAR(1000) NOT NULL,
    est_approuvee BOOLEAN NOT NULL DEFAULT FALSE,
    date_creation DATETIME NOT NULL,
    date_modification DATETIME NOT NULL,
    admin_id BIGINT,
    FOREIGN KEY (admin_id) REFERENCES utilisateur(id)
);

-- Création de la table action_de_charite
CREATE TABLE IF NOT EXISTS action_de_charite (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titre VARCHAR(255) NOT NULL,
    description VARCHAR(2000) NOT NULL,
    date_debut DATETIME NOT NULL,
    date_fin DATETIME NOT NULL,
    lieu VARCHAR(255) NOT NULL,
    objectif_collecte DECIMAL(10, 2) NOT NULL,
    montant_actuel DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    est_archivee BOOLEAN NOT NULL DEFAULT FALSE,
    date_creation DATETIME NOT NULL,
    date_modification DATETIME NOT NULL,
    organisation_id BIGINT NOT NULL,
    categorie_id BIGINT NOT NULL,
    FOREIGN KEY (organisation_id) REFERENCES organisation(id),
    FOREIGN KEY (categorie_id) REFERENCES categorie(id)
);

-- Création de la table don
CREATE TABLE IF NOT EXISTS don (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    montant DECIMAL(10, 2) NOT NULL,
    methode_paiement VARCHAR(20) NOT NULL,
    id_transaction VARCHAR(100) NOT NULL,
    date_don DATETIME NOT NULL,
    est_anonyme BOOLEAN NOT NULL DEFAULT FALSE,
    utilisateur_id BIGINT NOT NULL,
    action_de_charite_id BIGINT NOT NULL,
    FOREIGN KEY (utilisateur_id) REFERENCES utilisateur(id),
    FOREIGN KEY (action_de_charite_id) REFERENCES action_de_charite(id)
);

-- Création de la table media
CREATE TABLE IF NOT EXISTS media (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(20) NOT NULL,
    url VARCHAR(255) NOT NULL,
    description VARCHAR(1000) NOT NULL,
    date_upload DATETIME NOT NULL,
    action_de_charite_id BIGINT NOT NULL,
    FOREIGN KEY (action_de_charite_id) REFERENCES action_de_charite(id)
);

-- Création de la table de liaison utilisateur_interets
CREATE TABLE IF NOT EXISTS utilisateur_interets (
    utilisateur_id BIGINT NOT NULL,
    categorie_id BIGINT NOT NULL,
    PRIMARY KEY (utilisateur_id, categorie_id),
    FOREIGN KEY (utilisateur_id) REFERENCES utilisateur(id),
    FOREIGN KEY (categorie_id) REFERENCES categorie(id)
); 