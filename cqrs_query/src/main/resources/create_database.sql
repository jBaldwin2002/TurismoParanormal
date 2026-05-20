-- Turismo Paranormal — base de datos del módulo QUERY (MongoDB)
-- Ejecutar en mongo shell:
use bd_query

db.createCollection("reservas")

db.reservas.createIndex({ usuario: 1 })
db.reservas.createIndex({ tourId: 1 })
db.reservas.createIndex({ estado: 1 })
db.reservas.createIndex({ reservaId: 1 }, { unique: true })
