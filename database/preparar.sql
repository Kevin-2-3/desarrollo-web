-- Preparación inicial de PostgreSQL para Motostock:
-- Ejecutar manualmente desde WSL con psql solo para un entorno donde todavía no existen este rol ni esta base de datos
-- No usar BEGIN ni psql --single-transaction: CREATE DATABASE lo impide
\set ON_ERROR_STOP on

CREATE ROLE motostock_app LOGIN;
\echo Establezca y confirme la contraseña del usuario motostock_app.
\password motostock_app

CREATE DATABASE motostock OWNER motostock_app;
\echo Preparación terminada. Continúe con esquema.sql y datos-prueba.sql.
