-- Base de datos: Componente 1 - Gestión de Reservas (lado Command)
-- Motor: PostgreSQL

CREATE TABLE IF NOT EXISTS reservas_command (
    id                BIGSERIAL PRIMARY KEY,
    tour_id           VARCHAR(50)  NOT NULL,
    nombre_cliente    VARCHAR(150) NOT NULL,
    email_cliente     VARCHAR(150) NOT NULL,
    cantidad_personas INT          NOT NULL CHECK (cantidad_personas > 0),
    fecha_tour        TIMESTAMP    NOT NULL,
    estado            VARCHAR(30)  NOT NULL DEFAULT 'PENDIENTE_PAGO'
    -- Estados: PENDIENTE_PAGO | PAGADA | CANCELADA
);

-- Índice para búsquedas por tour y fecha (validación de cupos)
CREATE INDEX IF NOT EXISTS idx_reservas_tour_fecha ON reservas_command (tour_id, fecha_tour);

-- Índice para búsqueda por cliente
CREATE INDEX IF NOT EXISTS idx_reservas_email ON reservas_command (email_cliente);
