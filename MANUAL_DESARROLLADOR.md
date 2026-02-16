# MANUAL TÉCNICO Y DEL DESARROLLADOR - ELECTROFACTURA
**Autor:** Jose Marquez
**Proyecto:** ElectroFactura - Práctica 6.1
**Versión:** 1.0

---

## 1. Introducción
ElectroFactura es una aplicación de escritorio desarrollada en **Java** utilizando el framework **JavaFX** para la gestión de clientes y facturación eléctrica. Este documento sirve como guía integral para la instalación, configuración, administración y extensión del proyecto.

---

## 2. Requisitos y Entorno de Desarrollo
Para poner en marcha el proyecto y continuar su desarrollo, es necesario disponer del siguiente software:

* **JDK (Java Development Kit):** Versión 17 o superior (Recomendado JDK 21 LTS).
* **Sistema de Construcción:** Gradle (incluido mediante wrapper `gradlew`).
* **IDE Recomendado:** IntelliJ IDEA (Community o Ultimate).
* **Base de Datos:** SQLite.
* **Plugins IDE:** Se recomienda tener instalado el plugin de "JavaFX" en IntelliJ para facilitar la edición de vistas `.fxml`.

---

## 3. Instalación y Despliegue (Manual de Instalación)
Pasos para ejecutar la aplicación desde cero en un entorno limpio:

1.  **Obtener el código:**
    Descomprimir el archivo del proyecto o clonar el repositorio en una carpeta local.

2.  **Importar en el IDE:**
    * Abrir IntelliJ IDEA.
    * Seleccionar `File > Open` y buscar la carpeta raíz `Jose_Pr51`.
    * **Importante:** Seleccionar el archivo `build.gradle.kts` y elegir "Open as Project".

3.  **Instalar Dependencias:**
    Al abrir el proyecto, Gradle debería sincronizarse automáticamente. Si no ocurre, pulsar el botón "Reload All Gradle Projects" en la pestaña de Gradle. Esto descargará JavaFX y el driver de SQLite.

4.  **Ejecución:**
    Desde la pestaña de Gradle en el IDE, navegar a `Tasks > application > run` o ejecutar el siguiente comando en la terminal integrada:
    ```bash
    ./gradlew run
    ```

---

## 4. Estructura del Proyecto
El proyecto sigue el patrón de arquitectura **MVC (Modelo-Vista-Controlador)** para separar la lógica de negocio de la interfaz de usuario.

### Paquetes Principales (`src/main/java/org/example/jose_pr51`)
* **`/model` (Modelo):** Contiene las clases POJO (`Cliente`, `Factura`) y las clases de acceso a datos (`ClienteDAO`, `FacturaDAO`). Aquí reside la lógica SQL y la conexión con la base de datos.
* **`/controller` (Controlador):** Contiene `MainController.java`. Gestiona los eventos de la interfaz (botones, tablas), valida los datos de entrada y comunica la vista con el modelo.
* **Raíz:** Contiene la clase `HelloApplication.java` que inicia el ciclo de vida de JavaFX y carga el FXML.

### Recursos (`src/main/resources`)
* **`vista_principal.fxml`:** Archivo XML que define la estructura visual de la interfaz.
* **`ayuda.html`:** Manual de usuario y tutoriales integrados (cargados mediante WebView).
* **`electricity.db`:** Archivo físico de la base de datos (se genera automáticamente si no existe).
* **`javadoc/`:** (Opcional) Carpeta con la documentación técnica generada.

---

## 5. Estructura de la Base de Datos
El sistema utiliza **SQLite**. A continuación se detalla el esquema de datos:

### Diagrama E-R (Visual)

```
### Diagrama E-R (Visual)

+-----------------+             +-----------------+             +---------------------+
|     CLIENTE     |             |     FACTURA     |             |   DETALLE_FACTURA   |
+-----------------+             +-----------------+             +---------------------+
| PK  id          |---(1:N)----<| PK  id          |---(1:N)----<| PK  id              |
|     nombre      |             | FK  id_cliente  |             | FK  id_factura      |
|     apellidos   |             |     fecha       |             |     tramo           |
|     nif         |             |     periodo     |             |     consumo_kwh     |
+-----------------+             +-----------------+             +---------------------+
```

### Diccionario de Datos

#### Tabla: `CLIENTE`
Almacena la información de los abonados.
* `id` (INTEGER, PK): Identificador único.
* `nombre` (TEXT): Nombre de pila.
* `apellidos` (TEXT): Apellidos completos.
* `nif` (TEXT): Documento único de identidad.

#### Tabla: `FACTURA`
Cabeceras de facturación.
* `id` (INTEGER, PK): Número de factura.
* `id_cliente` (INTEGER, FK): Cliente asociado.
* `fecha_emision`, `periodo_inicio`, `periodo_fin` (TEXT): Fechas relevantes.

#### Tabla: `DETALLE_FACTURA`
Desglose de consumos.
* `id` (INTEGER, PK): Identificador de línea.
* `id_factura` (INTEGER, FK): Factura asociada.
* `tramo` (TEXT): 'Punta', 'Llano' o 'Valle'.
* `consumo_kwh` (REAL): Energía consumida.
* `precio_kwh` (REAL): Coste unitario aplicado.

---

## 6. Manual de Administración
La administración de la aplicación se centra en el mantenimiento de la integridad de los datos y la configuración operativa.

### Gestión de la Base de Datos
La aplicación utiliza un archivo local `electricity.db` situado en la raíz de ejecución.
* **Auditoría y Reparación:** En caso de corrupción de datos o necesidad de una auditoría manual, se recomienda utilizar la herramienta externa gratuita **"DB Browser for SQLite"**.
* **Credenciales:** El archivo no está cifrado por defecto, por lo que no requiere usuario ni contraseña para su apertura externa.
* **Copias de Seguridad:** Se recomienda copiar el archivo `electricity.db` periódicamente a una ubicación segura.

### Configuración de Parámetros
Actualmente, los parámetros de facturación (tramos horarios y precios) se introducen manualmente en cada generación de factura o están definidos en el código.
* **Futuras Versiones:** Para facilitar la administración, se recomienda externalizar estos valores (precios del kWh) a un archivo `config.properties` que la aplicación lea al inicio, permitiendo cambiar las tarifas sin recompilar el código.

---

## 7. Generación de Documentación (Javadoc)
Para generar la documentación técnica actualizada de la API:

1.  En IntelliJ IDEA, ir al menú `Tools > Generate JavaDoc`.
2.  Configurar los argumentos para evitar errores de caracteres:
    `-encoding UTF-8 -charset UTF-8 -docencoding UTF-8`
3.  Seleccionar el directorio de salida (por defecto `/javadoc` en la raíz del proyecto).
4.  Abrir el archivo `index.html` generado para navegar por la estructura de clases.

---
