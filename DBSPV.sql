use db_sistema_biomercado;

INSERT INTO rol (nombre) VALUES ('USER');      -- Rol de usuario normal


INSERT INTO rol (nombre) VALUES ('ADMIN');     -- Rol de administrador


INSERT INTO usuario (nombre, apellido, curp, direccion, telefono, email, password, activo)
VALUES ('personal', 'encargado', 'JUAP123456HDFRRS09', 'Calle Falsa 123', '5555555555', 'personal.ususario@example.com', 'password123', b'1');

---------------------------

-- 2. Obtener el ID del usuario insertado
SET @usuario_id = LAST_INSERT_ID();

-- 3. Obtener el ID del rol "USER"
SET @rol_id = (SELECT id FROM rol WHERE nombre = 'USER');

-- 4. Insertar la relación entre el usuario y el rol
INSERT INTO relacion_usuario_rol (usuario_id, rol_id)
VALUES (@usuario_id, @rol_id);


SET SQL_SAFE_UPDATES = 0;

UPDATE usuario
SET password = '$2a$12$mOWlWa9KuUHBmrHxD2TXoOy7IXbfG9FGzov10O7LYIS8mK60kjgda'
WHERE password = 'password123';

SET SQL_SAFE_UPDATES = 1;


----------------------------------------
-- 1. Insertar el usuario administrador y obtener su ID en una sola consulta.
INSERT INTO usuario (nombre, apellido, curp, direccion, telefono, email, password, activo)
VALUES ('Admin', 'avendaño', 'test', 'Calle Principal 456', '5555555556', 'personaladmin@example.com', '$2a$12$9TgOl4v./piTvi64PGC8Q.fJoVXAMPWuMm4cJgMlqFhFBs/aQXJyW', b'1');

-- 2. Insertar la relación de rol "ADMIN" para el nuevo usuario, utilizando el ID del último usuario insertado y el ID del rol "ADMIN"
INSERT INTO relacion_usuario_rol (usuario_id, rol_id)
SELECT LAST_INSERT_ID(), id
FROM rol
WHERE nombre = 'ADMIN';