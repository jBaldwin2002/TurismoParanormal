-- Turismo Paranormal — base de datos del módulo COMMAND (PostgreSQL)
DROP DATABASE IF EXISTS bd_command;
DROP USER IF EXISTS user_command;
CREATE USER user_command WITH PASSWORD '123456';
CREATE DATABASE bd_command OWNER user_command;

-- Conectar a bd_command antes de ejecutar lo siguiente:
CREATE TABLE IF NOT EXISTS reservas (
    id            BIGSERIAL PRIMARY KEY,
    usuario       VARCHAR(100) NOT NULL,
    tour_id       VARCHAR(100) NOT NULL,
    nombre_tour   VARCHAR(200) NOT NULL,
    fecha         DATE         NOT NULL,
    horario       VARCHAR(50)  NOT NULL,
    personas      INTEGER      NOT NULL CHECK (personas > 0),
    valor_total   DOUBLE PRECISION NOT NULL,
    estado        VARCHAR(30)  NOT NULL DEFAULT 'PENDIENTE',
    fecha_creacion TIMESTAMP   DEFAULT NOW(),
    CONSTRAINT uq_usuario_tour_fecha UNIQUE (usuario, tour_id, fecha)
);
