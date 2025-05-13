-- Insertion des catégories
INSERT INTO categorie (nom, description) VALUES 
('Éducation', 'Actions caritatives liées à l''éducation et à la formation'),
('Santé', 'Actions caritatives liées à la santé et aux soins médicaux'),
('Environnement', 'Actions caritatives liées à la protection de l''environnement'),
('Pauvreté', 'Actions caritatives liées à la lutte contre la pauvreté'),
('Culture', 'Actions caritatives liées à la promotion de la culture');

-- Insertion du super admin en premier
INSERT INTO utilisateur (id, email, mot_de_passe, nom, prenom, numero_telephone, adresse, ville, pays, code_postal, langue, notifications_email_activees, date_creation, date_modification)
VALUES (1, 'admin@hopeshare.com', 'admin123', 'Admin', 'Super', '+33600000000', '123 Admin Street', 'Paris', 'France', '75000', 'fr', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Attribution du rôle ROLE_SUPER_ADMIN
INSERT INTO utilisateur_roles (utilisateur_id, role) VALUES (1, 'ROLE_SUPER_ADMIN');

-- Insertion des autres utilisateurs (mot de passe: password123)
INSERT INTO utilisateur (prenom, nom, email, mot_de_passe, numero_telephone, adresse, ville, pays, code_postal, langue, notifications_email_activees, date_creation, date_modification) VALUES 
('Jean', 'Dupont', 'jean.dupont@email.com', '$2a$10$rDmFN6ZJvwFqMz1qkqkqUOqkqkqkqkqkqkqkqkqkqkqkqkqkqkqk', '+33123456789', '123 Rue de Paris', 'Paris', 'France', '75001', 'FR', true, NOW(), NOW()),
('Marie', 'Martin', 'marie.martin@email.com', '$2a$10$rDmFN6ZJvwFqMz1qkqkqUOqkqkqkqkqkqkqkqkqkqkqkqkqkqkqk', '+33123456790', '456 Avenue des Champs-Élysées', 'Paris', 'France', '75008', 'FR', true, NOW(), NOW()),
('Ahmed', 'Benali', 'ahmed.benali@email.com', '$2a$10$rDmFN6ZJvwFqMz1qkqkqUOqkqkqkqkqkqkqkqkqkqkqkqkqkqkqk', '+212123456789', '789 Boulevard Mohammed V', 'Casablanca', 'Maroc', '20000', 'AR', true, NOW(), NOW());

-- Insertion des rôles utilisateurs
INSERT INTO utilisateur_roles (utilisateur_id, role) VALUES
(2, 'ROLE_USER'),
(3, 'ROLE_USER'),
(4, 'ROLE_USER');

-- Insertion des organisations
INSERT INTO organisation (nom, adresse_legale, numero_identification_fiscale, nom_contact_principal, email_contact_principal, telephone_contact_principal, url_logo, description_mission, est_approuvee, date_creation, date_modification, admin_id) VALUES 
('Fondation Éducation Pour Tous', '10 Rue de l''Éducation, Paris', 'FR12345678901', 'Sophie Bernard', 'sophie.bernard@education.org', '0123456789', 'https://example.com/logo1.png', 'Promouvoir l''éducation pour tous les enfants', true, NOW(), NOW(), 2),
('Association Santé Sans Frontières', '20 Avenue de la Santé, Lyon', 'FR98765432109', 'Pierre Dubois', 'pierre.dubois@sante.org', '0987654321', 'https://example.com/logo2.png', 'Améliorer l''accès aux soins de santé dans les pays en développement', true, NOW(), NOW(), 3),
('ONG Environnement Durable', '30 Boulevard de l''Environnement, Marseille', 'FR45678912305', 'Leila Benali', 'leila.benali@environnement.org', '0555555555', 'https://example.com/logo3.png', 'Protéger l''environnement et promouvoir le développement durable', false, NOW(), NOW(), 4);

-- Insertion des actions de charité
INSERT INTO action_de_charite (titre, description, date_debut, date_fin, lieu, objectif_collecte, montant_actuel, est_archivee, date_creation, date_modification, organisation_id, categorie_id) VALUES 
('Campagne de Scolarisation', 'Aide à la scolarisation des enfants défavorisés', '2023-09-01', '2023-12-31', 'Paris, France', 50000.00, 25000.00, false, NOW(), NOW(), 1, 1),
('Médical Mobile', 'Unité médicale mobile pour les zones rurales', '2023-10-01', '2024-03-31', 'Lyon, France', 75000.00, 30000.00, false, NOW(), NOW(), 2, 2),
('Reforestation Urbaine', 'Plantation d''arbres dans les zones urbaines', '2023-11-01', '2024-04-30', 'Marseille, France', 30000.00, 15000.00, false, NOW(), NOW(), 3, 3);

-- Insertion des dons
INSERT INTO don (montant, methode_paiement, id_transaction, date_don, est_anonyme, utilisateur_id, action_de_charite_id) VALUES 
(100.00, 'PAYPAL', 'PAY-123456789', NOW(), false, 2, 1),
(50.00, 'STRIPE', 'STR-987654321', NOW(), false, 3, 1),
(200.00, 'PAYPAL', 'PAY-456789123', NOW(), true, 4, 2),
(75.00, 'STRIPE', 'STR-789123456', NOW(), false, 2, 3);

-- Insertion des médias
INSERT INTO media (type, url, description, date_upload, action_de_charite_id) VALUES 
('IMAGE', 'https://example.com/image1.jpg', 'Photo des enfants à l''école', NOW(), 1),
('VIDEO', 'https://example.com/video1.mp4', 'Vidéo de présentation du projet', NOW(), 1),
('IMAGE', 'https://example.com/image2.jpg', 'Photo de l''unité médicale', NOW(), 2),
('IMAGE', 'https://example.com/image3.jpg', 'Photo des arbres plantés', NOW(), 3); 