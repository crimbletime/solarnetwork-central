/* ============================================================================
 * Sample script to create a new user and database for the SolarNet
 * application. Use this as a guide only, and modify names and passwords as
 * appropriate.
 * ============================================================================
 */
CREATE ROLE solarnet LOGIN ENCRYPTED PASSWORD 'solarnet' VALID UNTIL 'infinity';

CREATE DATABASE solarnet WITH ENCODING='UTF8' OWNER=solarnet TEMPLATE=template0;

\connect solarnet

-- pgpgsql is included by default in Postgres 9.x now
-- CREATE LANGUAGE plpgsql;
