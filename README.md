
# 🖥️ Sistema Punto de Venta (SPV)

El Sistema de Punto de Venta está diseñado para facilitar y optimizar las operaciones de venta, la gestión de inventario, y los ingresos a almacén en una Jarcería o negocio parecido. Este sistema permitirá:

- Gestionar productos
- Realizar ventas
- Controlar inventario
- Generar tickets de venta
- Generar reporte de artículos
- Administrar la información de los clientes, proveedores y empleados

El sistema está orientado a mejorar la eficiencia operativa, reducir errores humanos y proporcionar una experiencia de usuario fluida y segura tanto para los empleados como para los clientes.
## 🔗 Diagrma E-R

## Configuración inicial de la base de datos

### Creación de roles y usuarios base

```sql
use db_sistema;

-- Crear roles básicos
INSERT INTO rol (nombre) VALUES ('USER');      -- Rol de usuario normal
INSERT INTO rol (nombre) VALUES ('ADMIN');     -- Rol de administrador

-- Crear usuario básico
INSERT INTO usuario (nombre, apellido, curp, direccion, telefono, email, password, activo)
VALUES ('Empleado', 'manana', 'JUAP123456HDFRRS09', 'Calle Falsa 123', '5555555555', 'empleado@example.com', 'password123', b'1');

-- Obtener ID del último usuario insertado
SET @usuario_id = LAST_INSERT_ID();

-- Obtener ID del rol USER
SET @rol_id = (SELECT id FROM rol WHERE nombre = 'USER');

-- Asignar rol al usuario
INSERT INTO relacion_usuario_rol (usuario_id, rol_id)
VALUES (@usuario_id, @rol_id);

-- Actualizar contraseña con hash
SET SQL_SAFE_UPDATES = 0;
UPDATE usuario
SET password = '$2a$12$mOWlWa9KuUHBmrHxD2TXoOy7IXbfG9FGzov10O7LYIS8mK60kjgda'
WHERE password = 'password123';
SET SQL_SAFE_UPDATES = 1;

-- Crear usuario administrador
INSERT INTO usuario (nombre, apellido, curp, direccion, telefono, email, password, activo)
VALUES ('Admin', 'avendano', 'test', 'Calle Principal 456', '5555555556', 'admin@example.com', '$2a$12$9TgOl4v./piTvi64PGC8Q.fJoVXAMPWuMm4cJgMlqFhFBs/aQXJyW', b'1');

-- Asignar rol ADMIN al usuario administrador
INSERT INTO relacion_usuario_rol (usuario_id, rol_id)
SELECT LAST_INSERT_ID(), id
FROM rol
WHERE nombre = 'ADMIN';
 
