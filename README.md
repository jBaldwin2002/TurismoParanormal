# 🦇 Plataforma de Reservas de Turismo Paranormal
### Arquitectura CQRS + Apache Kafka — Spring Boot 3.3.5 / Java 17

---

## 1. Descripción del Producto

Plataforma de microservicios para gestionar reservas de **recorridos turísticos paranormales** en diferentes ciudades. El sistema administra el ciclo completo: reserva → pago → acceso físico al tour, usando el patrón **CQRS** con **Apache Kafka** como broker de eventos.

---

## 2. Broker: Apache Kafka

**¿Por qué Kafka?**
Kafka es un broker de mensajería de alto rendimiento basado en un log distribuido e inmutable. Es ideal para este proyecto porque:

- **Desacopla** los microservicios: Reservas no necesita saber que Pagos e Ingresos existen.
- **Garantiza orden** de los eventos dentro de una partición.
- **Permite replay**: si un consumidor falla, puede releer los eventos desde el offset que quedó pendiente.
- **Escalable**: varios consumidores en el mismo `group-id` distribuyen la carga automáticamente.

### Topics Kafka utilizados

| Topic | Productor | Consumidores | Evento |
|-------|-----------|-------------|--------|
| `reserva-event-topic` | Reservas (Command) | Reservas (Query), Pagos (Command) | `ReservaCreada`, `ReservaCancelada` |
| `pago-event-topic` | Pagos (Command) | Pagos (Query), Ingresos (Command) | `PagoConfirmado`, `PagoRechazado` |
| `ingreso-event-topic` | Ingresos (Command) | Ingresos (Query) | `IngresoRegistrado` |

---

## 3. Implementación CQRS

El patrón **Command Query Responsibility Segregation** separa las operaciones de escritura (Commands) de las de lectura (Queries).

### Flujo general

```
[Cliente HTTP]
      │
      ├─→ Command Controller  ──→  Command Service  ──→  PostgreSQL (escritura)
      │                                   │
      │                                   └──→  Kafka (publica evento)
      │                                              │
      └─→ Query Controller   ──→  Query Service  ←──┘  (consume evento)
                                       │
                                       └──→  MongoDB (lectura)
```

### Por componente

| Componente | Command | Query | BD Escritura | BD Lectura |
|-----------|---------|-------|-------------|-----------|
| Reservas | `CrearReservaCommand` | `ConsultarReservaQuery` | PostgreSQL | MongoDB |
| Pagos | `ProcesarPagoCommand` | `ConsultarPagoQuery` | PostgreSQL | MongoDB |
| Ingresos | `RegistrarIngresoCommand` | `ConsultarIngresoQuery` | PostgreSQL | MongoDB |

---

## 4. Componentes

### Componente 1 — Gestión de Reservas (puerto 8630)
- Registra reservas de tours paranormales en PostgreSQL.
- Publica `ReservaCreada` en Kafka.
- El Query Service escucha el topic y proyecta en MongoDB.

**Endpoints:**
```
POST   /api/reservas/commands/          → Crear reserva
DELETE /api/reservas/commands/{id}      → Cancelar reserva
GET    /api/reservas/queries/           → Listar todas
GET    /api/reservas/queries/{id}       → Obtener por ID
GET    /api/reservas/queries/tour/{tourId}
GET    /api/reservas/queries/cliente/{email}
```

### Componente 2 — Gestión de Pagos (puerto 8632)
- Escucha `ReservaCreada` y crea un registro de pago PENDIENTE.
- Expone endpoint para confirmar el pago.
- Publica `PagoConfirmado` en Kafka.

**Endpoints:**
```
POST   /api/pagos/commands/{reservaId}?metodoPago=PSE  → Procesar pago
GET    /api/pagos/queries/                              → Listar todos
GET    /api/pagos/queries/reserva/{reservaId}
GET    /api/pagos/queries/cliente/{email}
GET    /api/pagos/queries/estado/{estado}
```

### Componente 3 — Control de Ingreso al Tour (puerto 8634)
- Escucha `PagoConfirmado` y genera un código QR de acceso.
- Valida QR, fecha/hora y unicidad del ingreso.
- Publica `IngresoRegistrado`.

**Endpoints:**
```
POST   /api/ingresos/commands/registrar?codigoQr=QR-XXX  → Registrar ingreso
GET    /api/ingresos/queries/                             → Listar todos
GET    /api/ingresos/queries/qr/{codigoQr}
GET    /api/ingresos/queries/tour/{tourId}
GET    /api/ingresos/queries/estado/{estado}
GET    /api/ingresos/queries/cliente/{email}
```

---

## 5. Flujo completo de negocio

```
1. Cliente crea reserva  →  POST /api/reservas/commands/
2. Kafka publica ReservaCreada
3. Pagos recibe evento → crea pago PENDIENTE en PostgreSQL
4. Cliente paga         →  POST /api/pagos/commands/{reservaId}?metodoPago=PSE
5. Kafka publica PagoConfirmado
6. Ingresos recibe evento → genera código QR → guarda en PostgreSQL (HABILITADO)
7. El día del tour, staff escanea QR → POST /api/ingresos/commands/registrar?codigoQr=QR-XXX
8. Sistema valida QR, fecha, no duplicado → registra INGRESADO
9. Kafka publica IngresoRegistrado → MongoDB se actualiza
```

---

## 6. Cómo ejecutar

### Pre-requisitos
- Java 17+
- Apache Kafka corriendo en `localhost:9092`
- PostgreSQL (Neon) — credenciales ya configuradas
- MongoDB Atlas — URI ya configurada

### Levantar Kafka (Docker)
```bash
docker run -d --name kafka -p 9092:9092 \
  -e KAFKA_CFG_NODE_ID=0 \
  -e KAFKA_CFG_PROCESS_ROLES=controller,broker \
  -e KAFKA_CFG_LISTENERS=PLAINTEXT://:9092,CONTROLLER://:9093 \
  -e KAFKA_CFG_LISTENER_SECURITY_PROTOCOL_MAP=CONTROLLER:PLAINTEXT,PLAINTEXT:PLAINTEXT \
  -e KAFKA_CFG_CONTROLLER_QUORUM_VOTERS=0@kafka:9093 \
  -e KAFKA_CFG_CONTROLLER_LISTENER_NAMES=CONTROLLER \
  bitnami/kafka:latest
```

### Ejecutar cada microservicio
```bash
# Componente 1
cd reservas && ./mvnw spring-boot:run

# Componente 2
cd pagos && ./mvnw spring-boot:run

# Componente 3
cd ingresos && ./mvnw spring-boot:run
```

---

## 7. Estructura del proyecto

```
turismo_paranormal/
├── reservas/          # Componente 1 — Puerto 8630
│   └── src/main/java/co/paranormal/cqrs/
│       ├── controller/  ReservaCommandController, ReservaQueryController
│       ├── service/     ReservaCommandService, ReservaQueryService
│       ├── dto/         ReservaEvent
│       └── persistence/ entity/ ReservaCommand (PG), ReservaQuery (Mongo)
│                        repository/
├── pagos/             # Componente 2 — Puerto 8632
│   └── src/main/java/co/paranormal/cqrs/
│       ├── controller/  PagoCommandController, PagoQueryController
│       ├── service/     PagoCommandService, PagoQueryService
│       ├── dto/         ReservaEvent, PagoEvent
│       └── persistence/ entity/ PagoCommand (PG), PagoQuery (Mongo)
└── ingresos/          # Componente 3 — Puerto 8634
    └── src/main/java/co/paranormal/cqrs/
        ├── controller/  IngresoCommandController, IngresoQueryController
        ├── service/     IngresoCommandService, IngresoQueryService
        ├── dto/         PagoEvent, IngresoEvent
        └── persistence/ entity/ IngresoCommand (PG), IngresoQuery (Mongo)
```
