-- Base de datos: Componente 3 - Control de Ingreso al Tour (lado Command)
-- Motor: PostgreSQL

CREATE TABLE IF NOT EXISTS ingresos_command (
    id              BIGSERIAL PRIMARY KEY,
    reserva_id      BIGINT      NOT NULL,
    tour_id         VARCHAR(50) NOT NULL,
    email_cliente   VARCHAR(150) NOT NULL,
    codigo_qr       VARCHAR(50) UNIQUE NOT NULL,
    fecha_tour      TIMESTAMP,
    momento_ingreso TIMESTAMP,
    estado          VARCHAR(20) NOT NULL DEFAULT 'HABILITADO',
    -- Estados: HABILITADO | INGRESADO | RECHAZADO
    motivo_rechazo  TEXT
);

-- Índice para búsqueda rápida por QR en la entrada
CREATE INDEX IF NOT EXISTS idx_ingresos_qr ON ingresos_command (codigo_qr);

-- Índice para reportes por tour
CREATE INDEX IF NOT EXISTS idx_ingresos_tour ON ingresos_command (tour_id);
