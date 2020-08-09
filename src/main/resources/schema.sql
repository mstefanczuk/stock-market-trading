-- CREATE DATABASE stock_market_trading;
-- CREATE USER stock_market_trading WITH PASSWORD 'stock_market_trading';
-- GRANT ALL PRIVILEGES ON DATABASE stock_market_trading TO stock_market_trading;
--
-- \connect stock_market_trading
DROP TABLE IF EXISTS instrument CASCADE;
CREATE TABLE instrument
(
    id   serial NOT NULL,
    name text   NOT NULL,
    CONSTRAINT instrument_pkey PRIMARY KEY (id),
    CONSTRAINT instrument_name_key UNIQUE (name)
);
DROP INDEX IF EXISTS instrument_id_idx CASCADE;
CREATE INDEX instrument_id_idx ON instrument (id);

DROP TABLE IF EXISTS instrument_price CASCADE;
CREATE TABLE instrument_price
(
    id                             serial  NOT NULL,
    instrument_id                  integer NOT NULL REFERENCES instrument,
    price                          numeric NOT NULL,
    stock_service_last_update_time timestamp without time zone,
    last_update_time               timestamp without time zone,
    CONSTRAINT instrument_price_pkey PRIMARY KEY (id)
);
DROP INDEX IF EXISTS instrument_price_id_idx CASCADE;
CREATE INDEX instrument_price_id_idx ON instrument_price (id);

DROP TABLE IF EXISTS instrument_price_history CASCADE;
CREATE TABLE instrument_price_history
(
    id                        serial  NOT NULL,
    instrument_id             integer NOT NULL REFERENCES instrument,
    price                     numeric NOT NULL,
    stock_service_update_time timestamp without time zone,
    update_time               timestamp without time zone,
    CONSTRAINT instrument_price_history_pkey PRIMARY KEY (id)
);
DROP INDEX IF EXISTS instrument_price_history_id_idx CASCADE;
CREATE INDEX instrument_price_history_id_idx ON instrument_price_history (id);

DROP TABLE IF EXISTS "user" CASCADE;
CREATE TABLE "user"
(
    id    serial NOT NULL,
    login text   NOT NULL,
    CONSTRAINT user_pkey PRIMARY KEY (id),
    CONSTRAINT user_login_key UNIQUE (login)
);
DROP INDEX IF EXISTS user_id_idx CASCADE;
CREATE INDEX user_id_idx ON "user" (id);

DROP TABLE IF EXISTS order_type CASCADE;
CREATE TABLE order_type
(
    id   serial NOT NULL,
    name text   NOT NULL,
    CONSTRAINT order_type_pkey PRIMARY KEY (id),
    CONSTRAINT order_type_type_key UNIQUE (name)
);
DROP INDEX IF EXISTS order_type_id_idx CASCADE;
CREATE INDEX order_type_id_idx ON order_type (id);

DROP TABLE IF EXISTS "order" CASCADE;
CREATE TABLE "order"
(
    id                                   serial  NOT NULL,
    user_id                              integer NOT NULL REFERENCES "user",
    instrument_id                        integer NOT NULL REFERENCES instrument,
    amount                               numeric NOT NULL,
    type_id                              integer NOT NULL REFERENCES order_type,
    local_price                          numeric NOT NULL,
    local_price_update_time              numeric,
    stock_service_price_update_time      numeric,
    real_stock_service_price             numeric NOT NULL,
    real_stock_service_price_update_time numeric,
    request_date_time                    timestamp without time zone,
    response_date_time                   timestamp without time zone,
    CONSTRAINT order_pkey PRIMARY KEY (id)
);
DROP INDEX IF EXISTS order_id_idx CASCADE;
CREATE INDEX order_id_idx ON "order" (id);

DROP TABLE IF EXISTS user_instrument CASCADE;
CREATE TABLE user_instrument
(
    id             serial  NOT NULL,
    user_id        integer NOT NULL REFERENCES "user",
    instrument_id  integer NOT NULL REFERENCES instrument,
    trading_amount numeric NOT NULL default 0,
    amount         numeric NOT NULL default 0,
    balance        numeric NOT NULL default 0,
    CONSTRAINT user_instrument_pkey PRIMARY KEY (id)
);
DROP INDEX IF EXISTS user_instrument_id_idx CASCADE;
CREATE INDEX user_instrument_id_idx ON user_instrument (id);
