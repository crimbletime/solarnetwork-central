CREATE SCHEMA solardatum;

CREATE SCHEMA solaragg;

CREATE TABLE solardatum.da_datum (
  ts solarcommon.ts NOT NULL,
  node_id solarcommon.node_id,
  source_id solarcommon.source_id,
  posted solarcommon.ts NOT NULL,
  jdata json NOT NULL,
  CONSTRAINT sn_consum_datum_pkey PRIMARY KEY (node_id, ts, source_id) DEFERRABLE INITIALLY IMMEDIATE
);

CREATE TABLE solaragg.agg_stale_datum (
  ts_start timestamp with time zone NOT NULL,
  node_id solarcommon.node_id,
  source_id solarcommon.source_id,
  agg_kind char(1) NOT NULL,
  created timestamp NOT NULL DEFAULT now(),
  CONSTRAINT agg_stale_datum_pkey PRIMARY KEY (agg_kind, node_id, ts_start, source_id)
);

CREATE TABLE solaragg.agg_datum_hourly (
  ts_start timestamp with time zone NOT NULL,
  local_date date NOT NULL,
  local_time time without time zone NOT NULL,
  node_id solarcommon.node_id,
  source_id solarcommon.source_id,
  jdata json NOT NULL,
 CONSTRAINT agg_datum_hourly_pkey PRIMARY KEY (node_id, ts_start, source_id)
);

CREATE TABLE solaragg.agg_datum_daily (
  ts_start timestamp with time zone NOT NULL,
  local_date date NOT NULL,
  node_id solarcommon.node_id,
  source_id solarcommon.source_id,
  jdata json NOT NULL,
 CONSTRAINT agg_datum_daily_pkey PRIMARY KEY (node_id, ts_start, source_id)
);

CREATE OR REPLACE FUNCTION solardatum.trigger_agg_stale_datum()
  RETURNS trigger AS
$BODY$BEGIN
	CASE TG_OP
		WHEN 'INSERT', 'UPDATE' THEN
			BEGIN
				INSERT INTO solaragg.agg_stale_datum (ts_start, node_id, source_id, agg_kind)
				VALUES (date_trunc('hour', NEW.ts), NEW.node_id, NEW.source_id, 'h');
			EXCEPTION WHEN unique_violation THEN
				-- Nothing to do, just continue
			END;
			RETURN NEW;
		ELSE
			BEGIN
				INSERT INTO solaragg.agg_stale_datum (ts_start, node_id, source_id, agg_kind)
				VALUES (date_trunc('hour', OLD.ts), OLD.node_id, OLD.source_id, 'h');
			EXCEPTION WHEN unique_violation THEN
				-- Nothing to do, just continue
			END;
			RETURN OLD;
	END CASE;
END;$BODY$
  LANGUAGE plpgsql VOLATILE;

CREATE TRIGGER populate_agg_stale_datum
  AFTER INSERT OR UPDATE OR DELETE
  ON solardatum.da_datum
  FOR EACH ROW
  EXECUTE PROCEDURE solardatum.trigger_agg_stale_datum();


CREATE OR REPLACE FUNCTION solardatum.store_datum(
	ts solarcommon.ts, 
	node_id solarcommon.node_id, 
	source_id solarcommon.source_id, 
	posted solarcommon.ts, 
	jdata text)
  RETURNS void AS
$BODY$
DECLARE
	ts_post solarcommon.ts := (CASE WHEN posted IS NULL THEN now() ELSE posted END);
	jdata_json json := jdata::json;
BEGIN
	BEGIN
		INSERT INTO solardatum.da_datum(ts, node_id, source_id, posted, jdata)
		VALUES (ts, node_id, source_id, ts_post, jdata_json);
	EXCEPTION WHEN unique_violation THEN
		-- We mostly expect inserts, but we allow updates
		UPDATE solardatum.da_datum SET 
			jdata = jdata_json, 
			posted = ts_post
		WHERE
			node_id = node_id
			AND ts = ts
			AND source_id = source_id;
	END;
END;$BODY$
  LANGUAGE plpgsql VOLATILE;

CREATE OR REPLACE FUNCTION solardatum.find_available_sources(
	IN node solarcommon.node_id, 
	IN st solarcommon.ts DEFAULT NULL, 
	IN en solarcommon.ts DEFAULT NULL)
  RETURNS TABLE(source_id solarcommon.source_id) AS
$BODY$
BEGIN
	CASE
		WHEN st IS NULL AND en IS NULL THEN
			RETURN QUERY SELECT DISTINCT d.source_id
			FROM solaragg.agg_datum_daily d
			WHERE d.node_id = node;
		
		WHEN st IS NULL THEN
			RETURN QUERY SELECT DISTINCT d.source_id
			FROM solaragg.agg_datum_daily d
			WHERE d.node_id = node
				AND d.ts_start >= st;
				
		ELSE
			RETURN QUERY SELECT DISTINCT d.source_id
			FROM solaragg.agg_datum_daily d
			WHERE d.node_id = node
				AND d.ts_start >= st
				AND d.ts_end <= en;
	END CASE;	
END;$BODY$
  LANGUAGE plpgsql STABLE;

