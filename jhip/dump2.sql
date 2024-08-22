-- H2 2.2.224; 
SET DB_CLOSE_DELAY -1;         
;              
CREATE USER IF NOT EXISTS "JHIPSTER" SALT '0f3a55b1f1491843' HASH '0e7a475d6527b631e58288aa766c1afa31388a270e9991738e288dd4a80b84d7' ADMIN;    
CREATE CACHED TABLE "PUBLIC"."DATABASECHANGELOG"(
    "ID" CHARACTER VARYING(255) NOT NULL,
    "AUTHOR" CHARACTER VARYING(255) NOT NULL,
    "FILENAME" CHARACTER VARYING(255) NOT NULL,
    "DATEEXECUTED" TIMESTAMP NOT NULL,
    "ORDEREXECUTED" INTEGER NOT NULL,
    "EXECTYPE" CHARACTER VARYING(10) NOT NULL,
    "MD5SUM" CHARACTER VARYING(35),
    "DESCRIPTION" CHARACTER VARYING(255),
    "COMMENTS" CHARACTER VARYING(255),
    "TAG" CHARACTER VARYING(255),
    "LIQUIBASE" CHARACTER VARYING(20),
    "CONTEXTS" CHARACTER VARYING(255),
    "LABELS" CHARACTER VARYING(255),
    "DEPLOYMENT_ID" CHARACTER VARYING(10)
);   
-- 6 +/- SELECT COUNT(*) FROM PUBLIC.DATABASECHANGELOG;        
INSERT INTO "PUBLIC"."DATABASECHANGELOG" VALUES
('00000000000001', 'jhipster', 'config/liquibase/changelog/00000000000000_initial_schema.xml', TIMESTAMP '2024-08-17 16:08:35.468701', 1, 'EXECUTED', '9:3d15ce8389bddb1666f01b768d03e89b', 'createTable tableName=jhi_user; createTable tableName=jhi_authority; createTable tableName=jhi_user_authority; addPrimaryKey tableName=jhi_user_authority; addForeignKeyConstraint baseTableName=jhi_user_authority, constraintName=fk_authority_name, ...', '', NULL, '4.27.0', NULL, NULL, '3925315383'),
('20240817192811-1', 'jhipster', 'config/liquibase/changelog/20240817192811_added_entity_Customers.xml', TIMESTAMP '2024-08-17 16:08:35.471415', 2, 'EXECUTED', '9:9b4e1b250063cf900e52a7bdebe9faff', 'createTable tableName=customers', '', NULL, '4.27.0', NULL, NULL, '3925315383'),
('20240817192811-1-data', 'jhipster', 'config/liquibase/changelog/20240817192811_added_entity_Customers.xml', TIMESTAMP '2024-08-17 16:08:35.47601', 3, 'EXECUTED', '9:b188ba7554dc4c4234abdfe7f00478c6', 'loadData tableName=customers', '', NULL, '4.27.0', 'faker', NULL, '3925315383'),
('20240817192812-1', 'jhipster', 'config/liquibase/changelog/20240817192812_added_entity_Posts.xml', TIMESTAMP '2024-08-17 16:08:35.485006', 4, 'EXECUTED', '9:6fe9999c0145b2b9ca4538c09ad4c5f0', 'createTable tableName=posts; dropDefaultValue columnName=availability, tableName=posts', '', NULL, '4.27.0', NULL, NULL, '3925315383'),
('20240817192812-1-data', 'jhipster', 'config/liquibase/changelog/20240817192812_added_entity_Posts.xml', TIMESTAMP '2024-08-17 16:08:35.493838', 5, 'EXECUTED', '9:245fb6770dfb3f64aab17236c43ccb6f', 'loadData tableName=posts', '', NULL, '4.27.0', 'faker', NULL, '3925315383'),
('20240817192812-2', 'jhipster', 'config/liquibase/changelog/20240817192812_added_entity_constraints_Posts.xml', TIMESTAMP '2024-08-17 16:08:35.496561', 6, 'EXECUTED', '9:dcd84c68f366e7613c93ab1b8f900e2e', 'addForeignKeyConstraint baseTableName=posts, constraintName=fk_posts__customers_id, referencedTableName=customers', '', NULL, '4.27.0', NULL, NULL, '3925315383'); 
CREATE CACHED TABLE "PUBLIC"."DATABASECHANGELOGLOCK"(
    "ID" INTEGER NOT NULL,
    "LOCKED" BOOLEAN NOT NULL,
    "LOCKGRANTED" TIMESTAMP,
    "LOCKEDBY" CHARACTER VARYING(255)
);          
ALTER TABLE "PUBLIC"."DATABASECHANGELOGLOCK" ADD CONSTRAINT "PUBLIC"."PK_DATABASECHANGELOGLOCK" PRIMARY KEY("ID");             
-- 1 +/- SELECT COUNT(*) FROM PUBLIC.DATABASECHANGELOGLOCK;    
INSERT INTO "PUBLIC"."DATABASECHANGELOGLOCK" VALUES
(1, FALSE, NULL, NULL);    
CREATE CACHED TABLE "PUBLIC"."JHI_USER"(
    "ID" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1050) DEFAULT ON NULL NOT NULL,
    "LOGIN" CHARACTER VARYING(50) NOT NULL,
    "PASSWORD_HASH" CHARACTER VARYING(60) NOT NULL,
    "FIRST_NAME" CHARACTER VARYING(50),
    "LAST_NAME" CHARACTER VARYING(50),
    "EMAIL" CHARACTER VARYING(191),
    "IMAGE_URL" CHARACTER VARYING(256),
    "ACTIVATED" BOOLEAN NOT NULL,
    "LANG_KEY" CHARACTER VARYING(10),
    "ACTIVATION_KEY" CHARACTER VARYING(20),
    "RESET_KEY" CHARACTER VARYING(20),
    "CREATED_BY" CHARACTER VARYING(50) NOT NULL,
    "CREATED_DATE" TIMESTAMP DEFAULT NULL,
    "RESET_DATE" TIMESTAMP,
    "LAST_MODIFIED_BY" CHARACTER VARYING(50),
    "LAST_MODIFIED_DATE" TIMESTAMP
);         
ALTER TABLE "PUBLIC"."JHI_USER" ADD CONSTRAINT "PUBLIC"."PK_JHI_USER" PRIMARY KEY("ID");       
-- 2 +/- SELECT COUNT(*) FROM PUBLIC.JHI_USER; 
INSERT INTO "PUBLIC"."JHI_USER" VALUES
(1, 'admin', '$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC', 'Administrator', 'Administrator', 'admin@localhost', '', TRUE, 'en', NULL, NULL, 'system', NULL, NULL, 'system', NULL),
(2, 'user', '$2a$10$VEjxo0jq2YG9Rbk2HmX9S.k1uZBGYUHdUcid3g/vfiEl7lwWgOH/K', 'User', 'User', 'user@localhost', '', TRUE, 'en', NULL, NULL, 'system', NULL, NULL, 'system', NULL);   
CREATE CACHED TABLE "PUBLIC"."JHI_AUTHORITY"(
    "NAME" CHARACTER VARYING(50) NOT NULL
);     
ALTER TABLE "PUBLIC"."JHI_AUTHORITY" ADD CONSTRAINT "PUBLIC"."PK_JHI_AUTHORITY" PRIMARY KEY("NAME");           
-- 2 +/- SELECT COUNT(*) FROM PUBLIC.JHI_AUTHORITY;            
INSERT INTO "PUBLIC"."JHI_AUTHORITY" VALUES
('ROLE_ADMIN'),
('ROLE_USER');     
CREATE CACHED TABLE "PUBLIC"."JHI_USER_AUTHORITY"(
    "USER_ID" BIGINT NOT NULL,
    "AUTHORITY_NAME" CHARACTER VARYING(50) NOT NULL
);       
ALTER TABLE "PUBLIC"."JHI_USER_AUTHORITY" ADD CONSTRAINT "PUBLIC"."CONSTRAINT_E" PRIMARY KEY("USER_ID", "AUTHORITY_NAME");     
-- 3 +/- SELECT COUNT(*) FROM PUBLIC.JHI_USER_AUTHORITY;       
INSERT INTO "PUBLIC"."JHI_USER_AUTHORITY" VALUES
(1, 'ROLE_ADMIN'),
(1, 'ROLE_USER'),
(2, 'ROLE_USER');        
CREATE CACHED TABLE "PUBLIC"."CUSTOMERS"(
    "ID" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1500) DEFAULT ON NULL NOT NULL,
    "UID" BIGINT NOT NULL,
    "EMAIL" CHARACTER VARYING(255),
    "USER_NAME" CHARACTER VARYING(255) NOT NULL,
    "PASSWORD" CHARACTER VARYING(255) NOT NULL,
    "PROFILE_PIC_I_URL" CHARACTER VARYING(255)
);        
ALTER TABLE "PUBLIC"."CUSTOMERS" ADD CONSTRAINT "PUBLIC"."PK_CUSTOMERS" PRIMARY KEY("ID");     
-- 10 +/- SELECT COUNT(*) FROM PUBLIC.CUSTOMERS;               
INSERT INTO "PUBLIC"."CUSTOMERS" VALUES
(1, 5200, 'Raphael.Cormier@gmail.com', 'meanwhile alongside', 'movie fearful', 'yellow whoever'),
(2, 8806, 'Hank_Schultz@gmail.com', 'however', 'inasmuch', 'third or fondly'),
(3, 19510, 'Sallie_MacGyver30@gmail.com', 'huzzah meh often', 'tremendously versus woot', 'apropos dry ha'),
(4, 21873, 'Jayne.Cruickshank@gmail.com', 'er overlie', 'vivaciously', 'wisely into since'),
(5, 9704, 'Jarod_Smith@gmail.com', 'truly nuke whenever', 'yet when', 'whoever'),
(6, 12471, 'Wilson1@gmail.com', 'work pfft whose', 'inasmuch drat as', 'along accusation qua'),
(7, 29170, 'Bobby67@gmail.com', 'phooey kibitz', 'whose embossing before', 'famously tepid pointless'),
(8, 30142, 'Billy.Wolff@yahoo.com', 'enroll split', 'innocently traduce', 'pfft plot'),
(9, 22989, 'Kara.Bahringer@gmail.com', 'terrific', 'backpack', 'webbed'),
(10, 903, 'Vern_Wisozk@hotmail.com', 'airbag brr', 'hence um meanwhile', 'tick teeming');       
CREATE CACHED TABLE "PUBLIC"."POSTS"(
    "ID" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1500) DEFAULT ON NULL NOT NULL,
    "POST_ID" BIGINT NOT NULL,
    "PRICE" DOUBLE PRECISION,
    "TITLE" CHARACTER VARYING(255),
    "LOCATION" CHARACTER VARYING(255),
    "AVAILABILITY" TIMESTAMP DEFAULT NULL,
    "RATING" INTEGER,
    "TAG" CHARACTER VARYING(255),
    "CUSTOMERS_ID" BIGINT
);      
ALTER TABLE "PUBLIC"."POSTS" ADD CONSTRAINT "PUBLIC"."PK_POSTS" PRIMARY KEY("ID");             
-- 10 +/- SELECT COUNT(*) FROM PUBLIC.POSTS;   
INSERT INTO "PUBLIC"."POSTS" VALUES
(1, 8227, 27399.56, 'frilly supposing', 'finally upside-down greatly', TIMESTAMP '2024-08-16 19:54:23', 11339, 'READY_MADE', NULL),
(2, 12071, 29213.04, 'hm', 'reciprocity dismantle', TIMESTAMP '2024-08-17 18:34:12', 16607, 'READY_MADE', NULL),
(3, 27076, 12509.29, 'true given concerning', 'gopher articulate', TIMESTAMP '2024-08-17 00:02:59', 28116, 'READY_MADE', NULL),
(4, 31268, 4084.52, 'regarding shrilly', 'physically', TIMESTAMP '2024-08-16 23:38:12', 20026, 'READY_MADE', NULL),
(5, 19081, 18387.55, 'abnormality close', 'ouch cooperation', TIMESTAMP '2024-08-17 13:10:40', 5470, 'PRODUCE', NULL),
(6, 8700, 1418.29, 'blah flank', 'ah incidentally', TIMESTAMP '2024-08-17 03:35:22', 1203, 'READY_MADE', NULL),
(7, 21364, 23310.4, 'sublimate', 'finally', TIMESTAMP '2024-08-16 20:08:40', 283, 'PRODUCE', NULL),
(8, 8886, 2402.26, 'although warlord spice', 'canine', TIMESTAMP '2024-08-17 04:45:43', 13225, 'OTHER', NULL),
(9, 13300, 30075.39, 'er', 'physically warmly upon', TIMESTAMP '2024-08-17 19:15:49', 21851, 'OTHER', NULL),
(10, 21421, 20158.96, 'whack', 'instead', TIMESTAMP '2024-08-16 20:34:53', 1290, 'READY_MADE', NULL);      
ALTER TABLE "PUBLIC"."JHI_USER" ADD CONSTRAINT "PUBLIC"."UX_USER_LOGIN" UNIQUE("LOGIN");       
ALTER TABLE "PUBLIC"."JHI_USER" ADD CONSTRAINT "PUBLIC"."UX_USER_EMAIL" UNIQUE("EMAIL");       
ALTER TABLE "PUBLIC"."POSTS" ADD CONSTRAINT "PUBLIC"."FK_POSTS__CUSTOMERS_ID" FOREIGN KEY("CUSTOMERS_ID") REFERENCES "PUBLIC"."CUSTOMERS"("ID") NOCHECK;       
ALTER TABLE "PUBLIC"."JHI_USER_AUTHORITY" ADD CONSTRAINT "PUBLIC"."FK_USER_ID" FOREIGN KEY("USER_ID") REFERENCES "PUBLIC"."JHI_USER"("ID") NOCHECK;            
ALTER TABLE "PUBLIC"."JHI_USER_AUTHORITY" ADD CONSTRAINT "PUBLIC"."FK_AUTHORITY_NAME" FOREIGN KEY("AUTHORITY_NAME") REFERENCES "PUBLIC"."JHI_AUTHORITY"("NAME") NOCHECK;       