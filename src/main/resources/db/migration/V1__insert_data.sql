-- Insertar usuarios con contraseñas en texto plano (en producción deben ir cifradas)
INSERT INTO users (username, email, password, gender, image_profile) VALUES
    ('Administrador', 'admin@example.com', '$2a$12$K2tWPqfRCIxdSUhBJEMDGOCQDZ/v/koKzFmfXMBBBOLr/YsPinMvm', 'MASCULINE', 'https://res.cloudinary.com/duzogl1l3/image/upload/v1725165057/anonimus_pzvrq1.jpg'),
    ('Manager', 'manager@example.com', '$2a$12$BuWzMP.RM4i855gdbNCYXO6mJKocVeGytnXfaahQjMZdAjagjeJp6', 'MASCULINE', 'https://res.cloudinary.com/duzogl1l3/image/upload/v1725165057/anonimus_pzvrq1.jpg'),
    ('Ricardo', 'ricardo@example.com', '$2a$12$9.YJ8h1nWXZCsQmzP.pdq.qIDAodqSTPLscrIl3bKkLS4FjyUpoBm', 'MASCULINE', 'https://res.cloudinary.com/duzogl1l3/image/upload/v1725165057/anonimus_pzvrq1.jpg'),
    ('Evelin', 'evelin@example.com', '$2a$12$LrJ.ybrJeyf2pvVywdHLcO0bY/GApQ0v.8do.iGEKy6DX3hcLR7fa', 'FEMENINE', 'https://res.cloudinary.com/duzogl1l3/image/upload/v1725165057/anonimus_pzvrq1.jpg');

-- Insertar roles
INSERT INTO roles (role_name) VALUES
    ('ROLE_ADMIN'),
    ('ROLE_USER'),
    ('ROLE_CLIENT'),
    ('ROLE_STUDENT');

-- Insertar permisos
INSERT INTO permissions (permission_name) VALUES
    ('CREATE_USER'),
    ('READ_USER'),
    ('UPDATE_USER'),
    ('DELETE_USER'),
-- Insertar permisos de PERMISSION
    ('CREATE_PERMISSION'),
    ('READ_PERMISSION'),
    ('UPDATE_PERMISSION'),
    ('DELETE_PERMISSION'),
-- Insertar permisos de ROLES
    ('CREATE_ROLE'),
    ('READ_ROLE'),
    ('UPDATE_ROLE'),
    ('DELETE_ROLE'),
-- Insertar permisos de CATEGORY
    ('CREATE_CATEGORY'),
    ('READ_CATEGORY'),
    ('UPDATE_CATEGORY'),
    ('DELETE_CATEGORY');

    -- Asignar roles a los usuarios
INSERT INTO user_roles (user_id, role_id) VALUES
    (1, 1),
    (2, 2),
    (3, 3),
    (4, 4);

-- Relacionar roles con permisos
INSERT INTO role_permissions (role_id, permission_id) VALUES
    (1, 1), -- ADMIN tiene ALL_PERMISSIONS
    (1, 2),
    (1, 3),
    (1, 4),
    (1, 5),
    (1, 6),
    (1, 7),
    (1, 8),
    (1, 9),
    (1, 10),
    (1, 11),
    (1, 12),
    (1, 13),
    (1, 14),
    (1, 15),
    (1, 16),
    (2, 1),
    (2, 2),
    (2, 3),
    (2, 4),
    (3, 2);