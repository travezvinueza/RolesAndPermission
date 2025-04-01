-- Insertar usuarios con contraseñas en texto plano (en producción deben ir cifradas)
INSERT INTO users (username, email, password, image_profile, account_locked) VALUES
    ('adminUser', 'admin@example.com', '$2a$12$K2tWPqfRCIxdSUhBJEMDGOCQDZ/v/koKzFmfXMBBBOLr/YsPinMvm', 'https://res.cloudinary.com/duzogl1l3/image/upload/v1725165057/anonimus_pzvrq1.jpg', FALSE),
    ('normalUser', 'user@example.com', '$2a$12$3aV2WjBWhBTjigkj/dMz4.5y/PKaeGpjkujqR0lyftXM6oRwUG2OK', 'https://res.cloudinary.com/duzogl1l3/image/upload/v1725165057/anonimus_pzvrq1.jpg', FALSE);

-- Insertar roles
INSERT INTO roles (role_name) VALUES
    ('ADMIN'),
    ('USER');

-- Insertar permisos
INSERT INTO permissions (permission_name) VALUES
    ('READ_POSTS'),
    ('WRITE_POSTS');

    -- Asignar roles a los usuarios
INSERT INTO user_roles (user_id, role_id) VALUES
    (1, 1), -- adminUser es ADMIN
    (2, 2); -- normalUser es USER


-- Relacionar roles con permisos
INSERT INTO role_permissions (role_id, permission_id) VALUES
    (1, 1), -- ADMIN tiene READ_POSTS
    (1, 2), -- ADMIN tiene WRITE_POSTS
    (2, 1); -- USER solo tiene READ_POSTS


