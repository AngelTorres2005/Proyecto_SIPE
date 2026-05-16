# SIPE - Sistema de Indicadores Psicológicos Especializados

SIPE es una plataforma web integral y segura diseñada para profesionales de la salud mental que permite optimizar la gestión de expedientes clínicos y la aplicación de evaluaciones psicológicas. El sistema automatiza el levantamiento de instrumentos clínicos (como la Escala de Ideación Suicida de Paykel), realiza el cálculo inmediato de puntuaciones e interpreta los niveles de riesgo psicométrico para asistir en la toma de decisiones clínicas prioritarias.

---

## Características Principales

* **Autenticación y Control de Accesos:** Sistema de seguridad robusto respaldado por **Spring Security**. Implementa encriptación de contraseñas mediante **BCrypt** y un manejo de sesiones a través de `CustomUserDetailsService`, garantizando la privacidad de las cuentas médicas.
* **Manejo de Expedientes Clínicos Privados:** Panel médico parametrizado que aísla los expedientes de los pacientes por cada doctor. Permite la gestión completa (CRUD) de registros de pacientes de manera totalmente segura y asíncrona mediante JavaScript (Fetch API).
* **Asistente Dinámico de Evaluaciones (Multi-step Wizard):** Interfaz fluida para la aplicación de test psicológicos que implementa lógica condicional en el frontend. Las secciones de frecuencia (escalas de 1 a 7) se despliegan de forma interactiva únicamente cuando el usuario valida una respuesta dicotómica positiva ("Sí").
* **Motor Clínico de Calificación Automated:** Algoritmo integrado en el backend (`CuestionarioController`) que procesa los formularios POST, calcula la sumatoria aritmética de los reactivos y clasifica dinámicamente el nivel de riesgo del paciente (Sin Riesgo, Riesgo Leve, Riesgo Moderado, Riesgo Alto) bajo métricas psicométricas estandarizadas.
* **Persistencia Relacional Transaccional:** Estructura de persistencia robusta que separa los metadatos generales de una aplicación (`evaluaciones`) del desglose específico de las respuestas del reactivo (`respuestas_detalle`), asegurando la integridad referencial y auditorías históricas confiables.
* **Diseño Ejecutivo y Limpio:** Interfaz minimalista basada en componentes con bordes cuadrados (`rounded-0`) construida sobre **Bootstrap 5** y **Thymeleaf**, optimizada para la lectura rápida de datos clínicos y respuestas visuales interactivas manejadas con SweetAlert2.

---

## Tecnologías Utilizadas

### Backend
* **Java 21**
* **Spring Boot**
* **Spring Security** (Manejo de sesiones, roles y encriptación)
* **Spring Data JPA** (Persistencia e inyección de consultas JPQL relacionales)

### Frontend
* **Thymeleaf** (Renderizado y paso dinámico de variables de servidor a cliente)
* **Bootstrap 5** (Layout responsivo y estilos de componentes de formulario)
* **JavaScript (ES6+)** (Consumo de API interna, validaciones dinámicas de formularios y control de modales)
* **SweetAlert2** (Mensajería intermitente y confirmaciones de eventos críticos)

### Base de Datos
* **PostgreSQL** (Almacenamiento de datos con soporte completo de integridad referencial y secuencias de identidad)

---

## Arquitectura de la Base de Datos

El diseño relacional mapea entidades normalizadas administradas por Hibernate:
* `usuarios`: Almacena las cuentas e información de acceso de los especialistas clínicos.
* `pacientes`: Expediente personal de los individuos evaluados, enlazado directamente a su usuario médico.
* `instrumentos` y `preguntas`: Catálogo parametrizado de los test psicológicos y los enunciados específicos por orden de reactivo.
* `evaluaciones`: Cabecera transaccional que documenta la puntuación total, fecha de aplicación y nivel de gravedad determinado.
* `respuestas_detalle`: Detalle pormenorizado de cada evaluación, guardando los valores booleanos dicotómicos y los ponderados de frecuencia por reactivo.

---

## Instalación y Configuración

### Requisitos Previos
* Java Development Kit (JDK) 21 o superior instalado.
* Maven 3.x o superior instalado (o usar el ejecutable `./mvnw` integrado).
* Servidor PostgreSQL en ejecución.

### 1. Clonar el repositorio localmente
Ejecute los siguientes comandos en su terminal (desarrollado y testeado utilizando IntelliJ IDEA):
```bash
git clone https://github.com/AngelTorres2005/Proyecto_SIPE.git
```

### 2. Exportar la base de datos

Hay que exportar la base de datos a un motor (preferencia PostgreSQL que es el que se configura en el application.properties)

Una vez exportado de se configura la conexion en el archivo ya mencionado, una vez realizado la aplicacion correria sin problema alguno.