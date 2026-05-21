-- Base de datos: Componente 2 - Gestión de Pagos (lado Command)
-- Motor: PostgreSQL

CREATE TABLE IF NOT EXISTS pagos_command (
    id             BIGSERIAL PRIMARY KEY,
    reserva_id     BIGINT         NOT NULL,
    tour_id        VARCHAR(50)    NOT NULL,
    email_cliente  VARCHAR(150)   NOT NULL,
    monto_total    NUMERIC(12, 2) NOT NULL,
    metodo_pago    VARCHAR(30),
    -- Métodos válidos: TARJETA_CREDITO | TARJETA_DEBITO | PSE | EFECTIVO
    estado         VARCHAR(20)    NOT NULL DEFAULT 'PENDIENTE',
    -- Estados: PENDIENTE | CONFIRMADO | RECHAZADO
    fecha_pago     TIMESTAMP,
    comprobante    VARCHAR(50)
);

-- Índice para detección de pagos duplicados
CREATE UNIQUE INDEX IF NOT EXISTS idx_pagos_reserva_confirmado
    ON pagos_command (reserva_id)
    WHERE estado = 'CONFIRMADO';
