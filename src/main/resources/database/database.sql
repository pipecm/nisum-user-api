-- H2 2.2.224; 
SET DB_CLOSE_DELAY -1;         
;              
CREATE USER IF NOT EXISTS "NISUMUSERAPI" SALT '564b65b3b8a8e957' HASH '2f77b81ec2bc179970699b935fe1d5d49e70550bae11ae339c020d39fdf4deec' ADMIN;
CREATE MEMORY TABLE "PUBLIC"."API_USER"(
    "USER_ACTIVE" BOOLEAN,
    "CREATED_AT" TIMESTAMP(6),
    "LAST_UPDATED_AT" TIMESTAMP(6),
    "USER_ID" UUID NOT NULL,
    "USER_EMAIL" CHARACTER VARYING(255),
    "USER_NAME" CHARACTER VARYING(255),
    "USER_PASSWORD" CHARACTER VARYING(255)
);             
ALTER TABLE "PUBLIC"."API_USER" ADD CONSTRAINT "PUBLIC"."CONSTRAINT_6" PRIMARY KEY("USER_ID"); 
-- 0 +/- SELECT COUNT(*) FROM PUBLIC.API_USER; 
CREATE MEMORY TABLE "PUBLIC"."API_USER_LOGIN"(
    "LOGIN_TS" TIMESTAMP(6),
    "LOGIN_ID" UUID NOT NULL,
    "USER_ID" UUID NOT NULL,
    "LOGIN_TOKEN" CHARACTER VARYING(255)
);             
ALTER TABLE "PUBLIC"."API_USER_LOGIN" ADD CONSTRAINT "PUBLIC"."CONSTRAINT_1" PRIMARY KEY("LOGIN_ID");          
-- 0 +/- SELECT COUNT(*) FROM PUBLIC.API_USER_LOGIN;           
CREATE MEMORY TABLE "PUBLIC"."API_USER_PHONE"(
    "PHONE_ID" UUID NOT NULL,
    "USER_ID" UUID NOT NULL,
    "CITY_CODE" CHARACTER VARYING(255),
    "COUNTRY_CODE" CHARACTER VARYING(255),
    "PHONE_NUMBER" CHARACTER VARYING(255)
);      
ALTER TABLE "PUBLIC"."API_USER_PHONE" ADD CONSTRAINT "PUBLIC"."CONSTRAINT_1B" PRIMARY KEY("PHONE_ID");         
-- 0 +/- SELECT COUNT(*) FROM PUBLIC.API_USER_PHONE;           
ALTER TABLE "PUBLIC"."API_USER_PHONE" ADD CONSTRAINT "PUBLIC"."FK46L3YF48DFYHUCIU86QWOHSH8" FOREIGN KEY("USER_ID") REFERENCES "PUBLIC"."API_USER"("USER_ID") NOCHECK;          
ALTER TABLE "PUBLIC"."API_USER_LOGIN" ADD CONSTRAINT "PUBLIC"."FKFX5S8T5L1UGFSFBEYI7BDPHJX" FOREIGN KEY("USER_ID") REFERENCES "PUBLIC"."API_USER"("USER_ID") NOCHECK;          
