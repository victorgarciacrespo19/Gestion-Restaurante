# 🍽️ Gestión de Restaurante - Trabajo 2 (Sistemas de Base de Datos II)

Aplicación Java para el Trabajo 2 de **Sistemas de Bases de Datos II** (UPSA). Implementa la lógica de negocio y persistencia de datos (DAO) para gestionar un restaurante.

## 🚀 Características
- **Menús:** Registro con fechas de validez.
- **Platos:** Nombre, descripción y tipo (primero, segundo, postre).
- **Ingredientes:** Cantidades exactas por plato y sus unidades.

## 🏗️ Arquitectura
Estructura separada por capas (Dominio, Aplicación e Implementación) usando inyección de dependencias y el patrón **DAO**.

- `domain`: Entidades (`Menu`, `Plato`, `Ingrediente`...) y excepciones de negocio personalizadas.
- `application`: Interfaces de Casos de Uso (`UseCases`) y acceso a datos (`Dao`).
- `impl`: Implementación SQL/JDBC (`UseCasesImpl`, `DaoImpl`).

## 👨‍💻 Autor
Víctor García Crespo
