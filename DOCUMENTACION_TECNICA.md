1. Estructura de la Información (Base de Datos)
   La aplicación utiliza SQLite como sistema de gestión de base de datos relacional para garantizar la persistencia de la información de forma local y ligera. El archivo de base de datos se denomina electricity.db.

Modelo Entidad-Relación
La base de datos se compone de tres entidades principales relacionadas entre sí para gestionar el flujo desde el alta de un abonado hasta la generación de sus consumos.

Diccionario de Datos
Tabla: CLIENTE
Almacena la información de los abonados al servicio eléctrico.

id (INTEGER, PK): Identificador único autoincremental.

nombre (TEXT): Nombre del cliente.

apellidos (TEXT): Apellidos del cliente.

nif (TEXT): Documento de identidad (clave única para evitar duplicados).

Tabla: FACTURA
Almacena la cabecera de las facturas emitidas.

id (INTEGER, PK): Número de factura único.

id_cliente (INTEGER, FK): Referencia al cliente que pertenece la factura.

fecha_emision (TEXT): Fecha en la que se genera el documento.

periodo_inicio (TEXT): Fecha de inicio del consumo facturado.

periodo_fin (TEXT): Fecha de fin del consumo facturado.

Tabla: DETALLE_FACTURA
Almacena el desglose de consumos por tramos horarios.

id (INTEGER, PK): Identificador único del detalle.

id_factura (INTEGER, FK): Relación con la factura correspondiente.

tramo (TEXT): Clasificación del consumo (Punta, Llano o Valle).

consumo_kwh (REAL): Cantidad de energía consumida en ese tramo.

precio_kwh (REAL): Precio aplicado por cada kWh según el tramo.

Relaciones y Reglas de Negocio
Relación Cliente-Factura (1:N): Un cliente puede tener múltiples facturas asociadas a lo largo del tiempo, pero cada factura pertenece exclusivamente a un único cliente.

Relación Factura-Detalle (1:N): Cada factura se desglosa en varios registros de detalle (típicamente tres: uno por cada tramo horario) para permitir el cálculo preciso del importe total basado en la discriminación horaria.

Integridad Referencial: El borrado de un cliente implica, por lógica de negocio, la gestión de sus facturas asociadas para evitar registros huérfanos.