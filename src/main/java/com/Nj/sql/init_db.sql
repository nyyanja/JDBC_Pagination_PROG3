
--utilisateur
CREATE DATABASE product_management_db;

-- Connexion
CREATE USER product_manager_user WITH PASSWORD '123456';

-- Droits
ALTER DATABASE product_management_db OWNER TO product_manager_user;

--Droits des tables
 GRANT ALL PRIVILEGES ON DATABASE product_management_db TO product_manager_user;
