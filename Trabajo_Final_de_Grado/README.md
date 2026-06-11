# LoadSafe

Aplicación web para la gestión y organización eficiente de paquetería desarrollada como Trabajo de Fin de Grado (DAM).

Este repositorio contiene el backend de la aplicación. El frontend fue desarrollado por otro integrante del equipo y no está incluido aquí.

## Tecnologías

- Java + Spring Boot
- Spring Data JPA / Hibernate
- Spring Security + JWT
- MariaDB / MySQL
- Maven

## ¿Qué hace la app?

Permite gestionar clientes, usuarios, paquetes, camiones y envíos con dos roles diferenciados:

- **Administrador:** acceso completo a todas las entidades (CRUD)
- **Empleado:** selecciona un camión disponible y asigna paquetes al envío teniendo en cuenta fragilidad, dimensiones y peso

## Autenticación

La API usa autenticación basada en JWT. Para acceder a los endpoints protegidos hay que incluir el token en el header de cada petición:

```
Authorization: Bearer <token>
```

El token se obtiene haciendo login en `POST /usuarios/login`.

## Endpoints principales

### Usuarios
| Método | Ruta | Acceso |
|--------|------|--------|
| POST | `/usuarios/login` | Público |
| POST | `/usuarios/registrarUsuario` | Admin |
| GET | `/usuarios/listaUsuarios` | Admin |

### Camiones
| Método | Ruta | Acceso |
|--------|------|--------|
| POST | `/camiones/agregarVehiculo` | Admin |
| GET | `/camiones/listarVehiculos` | Autenticado |
| PATCH | `/camiones/actualizarVehiculo/{matricula}` | Admin |
| PATCH | `/camiones/cambiarEstado/{matricula}` | Autenticado |
| DELETE | `/camiones/eliminarVehiculo/{matricula}` | Admin |

### Clientes
| Método | Ruta | Acceso |
|--------|------|--------|
| POST | `/clientes/altaCliente` | Admin |
| GET | `/clientes/listar` | Autenticado |
| PATCH | `/clientes/modificarDatosCliente/{id}` | Admin |
| DELETE | `/clientes/bajaCliente` | Admin |

### Envíos
| Método | Ruta | Acceso |
|--------|------|--------|
| POST | `/envios/crearEnvio` | Autenticado |
| GET | `/envios/mostrarEnvios` | Autenticado |
| GET | `/envios/mostrarEnviosPorFecha` | Autenticado |
| POST | `/envios/previsualizarCarga` | Autenticado |
| PATCH | `/envios/concluirEnvio` | Autenticado |

### Paquetes
| Método | Ruta | Acceso |
|--------|------|--------|
| POST | `/paquetes/agregarPaquete` | Autenticado |
| GET | `/paquetes/listarTodos` | Autenticado |
| GET | `/paquetes/listarPendientes` | Autenticado |
| GET | `/paquetes/consultarPaquete` | Autenticado |
| PATCH | `/paquetes/modificarPaquete/{id}` | Autenticado |
| PATCH | `/paquetes/cambiarEstado/{id}` | Autenticado |
| DELETE | `/paquetes/eliminarPaquete/{id}` | Admin |

## Modelo de datos

![Diagrama de base de datos]<img width="911" height="590" alt="image" src="https://github.com/user-attachments/assets/a1ea6355-9079-4e50-9f7e-323a4fcab32b" />

## Cómo ejecutarlo

1. Copia el fichero de configuración y rellena tus credenciales:
   ```bash
   cp src/main/resources/application.properties.example src/main/resources/application.properties
   ```

2. Crea la base de datos:
   ```sql
   CREATE DATABASE loadsafe;
   ```

3. Arranca la aplicación:
   ```bash
   mvn spring-boot:run
   ```

La API queda disponible en `http://localhost:8080`.
