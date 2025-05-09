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
    ('DELETE_CATEGORY'),
-- Insertar permisos de PRODUCT
    ('CREATE_PRODUCT'),
    ('READ_PRODUCT'),
    ('UPDATE_PRODUCT'),
    ('DELETE_PRODUCT'),
-- Insertar permisos de ORDER
    ('CREATE_ORDER'),
    ('READ_ORDER'),
    ('UPDATE_ORDER'),
    ('DELETE_ORDER'),
-- Insertar permisos de ORDER_DETAIL
    ('READ_ORDER_DETAIL'),
    ('UPDATE_ORDER_DETAIL'),
    ('DELETE_ORDER_DETAIL');

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
    (1, 17),
    (1, 18),
    (1, 19),
    (1, 20),
    (2, 1),
    (2, 2),
    (2, 3),
    (2, 4),
    (3, 2),
    (3, 21),
    (3, 22);

-- Insertar algunas categorías
INSERT INTO categories (category_name)
VALUES
('Electrónica'),
('Ropa'),
('Hogar');

-- Insertar algunos productos
INSERT INTO products (product_code, product_name, price, stock, image_product, category_id)
VALUES
('PRODUCT-1A2B3C', 'Smartphone X', 500.99, 50, 'smartphone.png', 1),
('PRODUCT-3K4L2R', 'Chaqueta de Cuero', 100.99, 20, 'chaqueta.png', 2),
('PRODUCT-8F2C1D', 'Aspiradora', 150.00, 15, 'aspiradora.png', 3);

-- Insertar algunas órdenes
INSERT INTO orders (order_code, order_state, description, user_id)
VALUES
('ORDER-174563', 'PENDING', 'Compra de un Smartphone', 3),
('ORDER-367641', 'PROCESSING', 'Compra de ropa', 3);

-- Insertar detalles de órdenes
INSERT INTO order_details (order_id, product_id, quantity, unit_price, total_price)
VALUES
(1, 1, 1, 500.99, 500.99),  -- Orden 1: 1 Smartphone
(2, 2, 2, 100.99, 201.98);  -- Orden 2: 2 Chaquetas