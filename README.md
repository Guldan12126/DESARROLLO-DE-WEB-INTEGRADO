# 🍜 Sistema Web de Gestión Integral — Salón Xin Yan

> Proyecto Final — Desarrollo Web Integrado |  Ingeniería de Sistemas e Informática

---

## 📋 Descripción

Sistema web de gestión integral para el **Salón Xin Yan**, un restaurante de comida chino-peruana (chifa) ubicado en el distrito de San Borja, Lima. El sistema digitaliza y optimiza los procesos operativos del negocio, integrando la gestión de pedidos, inventario, ventas, cobros y administración en una sola plataforma web.

La atención es **100% presencial**: los clientes acuden al local, son atendidos por los mozos, su pedido es preparado por cocina y finalmente se acercan a caja para realizar el pago antes de retirarse.

El proyecto aplica una arquitectura **cliente-servidor**, con **Spring Boot** en el back-end y **Angular** en el front-end, siguiendo buenas prácticas de desarrollo web moderno.

---

## 🏢 Datos de la Empresa

| Campo         | Detalle                              |
|---------------|--------------------------------------|
| **Nombre**    | Salón Xin Yan                        |
| **RUC**       | 20609104822                          |
| **Rubro**     | Restaurante de comida chino-peruana  |
| **Ubicación** | Distrito de San Borja, Lima, Perú    |
| **Modalidad** | Atención presencial                  |

---

## 🚨 Problemática

El Salón Xin Yan operaba completamente de forma **manual**, lo que generaba:

- ❌ Errores frecuentes en la toma de pedidos (anotaciones en papel o verbal)
- ❌ Pérdida de comandas y confusiones entre sala y cocina
- ❌ Demoras en la preparación y entrega de platos
- ❌ Control deficiente de caja y ventas sin registros digitales
- ❌ Cobros lentos y propensos a errores al momento de pagar
- ❌ Imposibilidad de generar reportes confiables para la toma de decisiones

---

## ✅ Solución Propuesta

Desarrollo de un **Sistema Web de Gestión Integral** que cubre todo el flujo de atención presencial del restaurante:

- 📲 Gestión de pedidos en tiempo real desde cualquier dispositivo
- 🍳 Tablero de cocina para seguimiento de órdenes en preparación
- 💵 Módulo de caja para cobro presencial (efectivo o tarjeta)
- 🧾 Generación de boletas y comprobantes de pago
- 📦 Control de inventario y alertas de stock bajo
- 📊 Reportes de ventas, ingresos y cierre de caja diario
- 👤 Autenticación con roles diferenciados (Admin, Mozo, Cocina, Cajero)

---

## 🔄 Flujo de Atención Presencial

```
 👤 Cliente llega al local
        │
        ▼
 🪑 Mozo asigna mesa y toma el pedido
   (ej: 1 Chaufa, 1 Wantán frito, 1 limonada)
        │
        ▼
 📲 Pedido se registra en el sistema
        │
        ▼
 🍳 Cocina recibe el pedido en el tablero
        │
        ▼
 ✅ Cocina marca el pedido como listo
        │
        ▼
 🍽️ Mozo lleva los platos a la mesa
        │
        ▼
 💰 Cliente termina y se acerca a Caja
        │
        ▼
 🧾 Cajero consulta el pedido de la mesa
    y muestra el total a cobrar
        │
        ▼
 💳 Cliente elige forma de pago:
   [ Efectivo ]  o  [ Tarjeta ]
        │
        ▼
 🧮 Efectivo → Cajero calcula el vuelto
 💳 Tarjeta  → Cajero registra el pago
        │
        ▼
 🖨️ Se genera boleta / comprobante
        │
        ▼
 ✅ Mesa se libera y queda disponible
```

---

## 🛠️ Tecnologías Utilizadas

### Back-end
- ![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=flat&logo=spring-boot&logoColor=white) **Spring Boot** — Lógica de negocio y servicios RESTful

### Front-end
- ![Angular](https://img.shields.io/badge/Angular-DD0031?style=flat&logo=angular&logoColor=white) **Angular** — Interfaz de usuario dinámica e intuitiva

### Arquitectura
- Cliente-Servidor
- API RESTful

---

## 👥 Roles del Sistema

| Rol | Descripción |
|-----|-------------|
| 🔑 **Administrador** | Gestión total del sistema, usuarios, reportes y configuración |
| 🧑‍🍳 **Mozo** | Asignación de mesas y registro de pedidos presenciales |
| 👨‍🍳 **Cocina** | Visualización y seguimiento de órdenes en preparación |
| 💰 **Cajero** | Cobro presencial, registro de pagos y cierre de mesas |

---

## 📦 Módulos del Sistema

### 🔑 Módulo Administrador
| Pantalla | Descripción |
|----------|-------------|
| Login | Autenticación con selección de rol |
| Dashboard | Vista general del negocio en tiempo real |
| Gestión de Mesas | Estado de todas las mesas del local |
| Gestión de Pedidos | Seguimiento de todos los pedidos activos |
| Nuevo Pedido | Creación y asignación de pedidos a mesas |
| Control de Productos | Inventario y stock de productos |
| Reportes de Ventas | Estadísticas, ingresos y ventas por producto |
| Administración de Usuarios | Gestión del personal y sus roles |

### 🧑‍🍳 Módulo Mozo
| Pantalla | Descripción |
|----------|-------------|
| Vista de Mesas | Estado de mesas disponibles y ocupadas |
| Registro de Pedidos | Toma de pedido por mesa (presencial) |
| Seguimiento de Pedido | Verificación del estado del pedido en cocina |

### 👨‍🍳 Módulo Cocina
| Pantalla | Descripción |
|----------|-------------|
| Dashboard de Cocina | Vista general de pedidos pendientes |
| Tablero de Órdenes | Pedidos en preparación en tiempo real |
| Gestión de Productos | Control de insumos disponibles en cocina |

### 💰 Módulo Cajero
| Pantalla | Descripción |
|----------|-------------|
| Mesas Pendientes de Pago | Lista de mesas listas para cobrar |
| Detalle de Pedido | Resumen de platos consumidos y total a pagar |
| Registro de Pago | Selección de método: efectivo o tarjeta |
| Cálculo de Vuelto | Ingreso del monto recibido y cálculo automático del vuelto |
| Generación de Boleta | Emisión del comprobante de pago |
| Cierre de Mesa | Liberación de la mesa tras el cobro |
| Cierre de Caja | Resumen de ingresos al final del turno |

---

## 👨‍💻 Equipo de Desarrollo

| Nombre | Apellidos |
|--------|-----------|
| Roberto Alexander | Anton Zapata |
| Dario Milton | Arroyo Nuñez |
| Victor Hugo | La Madrid Pacherres |
| Gael Sabit | Vasquez Risco |

---

## 👨‍🏫 Docente

**Ing. Edy Javier García Córdova**

---

## 📚 Información Académica

| Campo | Detalle |
|-------|---------|
| **Curso** | Desarrollo Web Integrado |
| **Ciclo** | 7mo Ciclo |
| **Carrera** | Ingeniería de Sistemas e Informática |
| **Año** | 2026 |
| **Sede** | Piura, Perú |

---

## 🚀 Instalación y Ejecución

> ⚠️ *Las instrucciones de instalación se actualizarán conforme avance el proyecto.*

### Requisitos previos
- Java 17+
- Node.js 18+
- Angular CLI
- Maven

### Back-end (Spring Boot)
```bash
# Clonar el repositorio
git clone https://github.com/usuario/xin-yan-sistema.git

# Navegar al directorio del back-end
cd xin-yan-sistema/backend

# Ejecutar con Maven
mvn spring-boot:run
```

### Front-end (Angular)
```bash
# Navegar al directorio del front-end
cd xin-yan-sistema/frontend

# Instalar dependencias
npm install

# Iniciar la aplicación
ng serve
```

La aplicación estará disponible en `http://localhost:4200`

---

## 📁 Estructura del Proyecto

```
xin-yan-sistema/
├── backend/                        # Spring Boot - API REST
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/xinyan/
│   │   │   │       ├── controllers/
│   │   │   │       ├── services/
│   │   │   │       ├── models/
│   │   │   │       └── repositories/
│   │   │   └── resources/
│   │   │       └── application.properties
│   └── pom.xml
│
├── frontend/                       # Angular - Interfaz de usuario
│   ├── src/
│   │   ├── app/
│   │   │   ├── modules/
│   │   │   │   ├── admin/          # Módulo Administrador
│   │   │   │   ├── mozo/           # Módulo Mozo
│   │   │   │   ├── cocina/         # Módulo Cocina
│   │   │   │   └── cajero/         # Módulo Cajero
│   │   │   ├── core/
│   │   │   └── shared/
│   │   └── environments/
│   └── package.json
│
└── README.md
```

---

## 📄 Licencia

Proyecto académico — Salon Xin Yan © 2026 Equipo de Desarrollo — Ingeniería de Sistemas e Informática.
