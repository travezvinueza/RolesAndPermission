-- Insertar usuarios con contraseñas en texto plano (en producción deben ir cifradas)
INSERT INTO users (username, email, password, image_profile) VALUES
    ('Administrador', 'admin@example.com', '$2a$12$K2tWPqfRCIxdSUhBJEMDGOCQDZ/v/koKzFmfXMBBBOLr/YsPinMvm', 'https://res.cloudinary.com/duzogl1l3/image/upload/v1725165057/anonimus_pzvrq1.jpg'),
    ('Manager', 'manager@example.com', '$2a$12$BuWzMP.RM4i855gdbNCYXO6mJKocVeGytnXfaahQjMZdAjagjeJp6', 'https://res.cloudinary.com/duzogl1l3/image/upload/v1725165057/anonimus_pzvrq1.jpg'),
    ('Ricardo', 'ricardo@example.com', '$2a$12$9.YJ8h1nWXZCsQmzP.pdq.qIDAodqSTPLscrIl3bKkLS4FjyUpoBm', 'https://res.cloudinary.com/duzogl1l3/image/upload/v1725165057/anonimus_pzvrq1.jpg'),
    ('Evelin', 'evelin@example.com', '$2a$12$LrJ.ybrJeyf2pvVywdHLcO0bY/GApQ0v.8do.iGEKy6DX3hcLR7fa', 'https://res.cloudinary.com/duzogl1l3/image/upload/v1725165057/anonimus_pzvrq1.jpg');

-- Insertar roles
INSERT INTO roles (role_name) VALUES
    ('ROLE_ADMIN'),
    ('ROLE_MANAGER'),
    ('ROLE_USER'),
    ('ROLE_CLIENT');

-- Insertar permisos
INSERT INTO permissions (permission_name) VALUES
    ('ALL_PERMISSIONS'),
    ('MANAGER_CREATE'),
    ('MANAGER_READ'),
    ('MANAGER_DELETE'),
    ('MANAGER_UPDATE'),
    ('MANAGER_WRITE'),
    -- Insertar permisos de PERMISOS
    ('READ_PERMISSION'),
    ('WRITE_PERMISSION'),
    ('CREATE_PERMISSION'),
    ('UPDATE_PERMISSION'),
    ('EDIT_PERMISSION'),
    ('VIEW_PERMISSION'),
    ('DELETE_PERMISSION'),
-- Insertar permisos de ROLES
    ('READ_ROLE'),
    ('WRITE_ROLE'),
    ('CREATE_ROLE'),
    ('EDIT_ROLE'),
    ('VIEW_ROLE'),
    ('DELETE_ROLE'),
 -- Insertar permisos de USUARIOS
    ('READ_USER'),
    ('WRITE_USER'),
    ('CREATE_USER'),
    ('EDIT_USER'),
    ('VIEW_USER'),
    ('DELETE_USER');

    -- Asignar roles a los usuarios
INSERT INTO user_roles (user_id, role_id) VALUES
    (1, 1),
    (2, 2),
    (3, 3),
    (4, 4);

-- Relacionar roles con permisos
INSERT INTO role_permissions (role_id, permission_id) VALUES
    (1, 1), -- ADMIN tiene ALL_PERMISSIONS
    (2, 2), -- MANAGER tiene MANAGER_CREATE
    (2, 3), -- MANAGER tiene MANAGER_READ
    (2, 4), -- MANAGER tiene MANAGER_DELETE
    (2, 5),
    (2, 6),
    (3, 20),
    (3, 21),
    (3, 22),
    (3, 23),
    (3, 24),
    (3, 25);