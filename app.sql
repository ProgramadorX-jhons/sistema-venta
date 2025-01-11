use db_sistema;

INSERT INTO rol (nombre) VALUES ('USER');      -- Rol de usuario normal
INSERT INTO rol (nombre) VALUES ('ADMIN');     -- Rol de administrador


INSERT INTO usuario (nombre, apellido, curp, direccion, telefono, email, password, activo)
VALUES ('Junior', 'avendano', 'JUAP123456HDFRRS09', 'Calle Falsa 123', '5555555555', 'junior.avendano@example.com', 'password123', b'1');


SET @usuario_id = LAST_INSERT_ID();

SET @rol_id = (SELECT id FROM rol WHERE nombre = 'USER');


INSERT INTO relacion_usuario_rol (usuario_id, rol_id)
VALUES (@usuario_id, @rol_id);



SET SQL_SAFE_UPDATES = 0;

UPDATE usuario
SET password = '$2a$12$mOWlWa9KuUHBmrHxD2TXoOy7IXbfG9FGzov10O7LYIS8mK60kjgda'
WHERE password = 'password123';

SET SQL_SAFE_UPDATES = 1;


-- 1. Insertar el usuario administrador y obtener su ID en una sola consulta.
INSERT INTO usuario (nombre, apellido, curp, direccion, telefono, email, password, activo)
VALUES ('Admin', 'avendano', 'test', 'Calle Principal 456', '5555555556', 'personal.admin@example.com', '$2a$12$9TgOl4v./piTvi64PGC8Q.fJoVXAMPWuMm4cJgMlqFhFBs/aQXJyW', b'1');

-- 2. Insertar la relación de rol "ADMIN" para el nuevo usuario, utilizando el ID del último usuario insertado y el ID del rol "ADMIN"
INSERT INTO relacion_usuario_rol (usuario_id, rol_id)
SELECT LAST_INSERT_ID(), id
FROM rol
WHERE nombre = 'ADMIN';