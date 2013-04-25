CREATE TABLE "skill" ("id" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , 
"charactor" VARCHAR,
"category" VARCHAR,
"name" VARCHAR,
"command" VARCHAR,
"judge" VARCHAR,
"damage" VARCHAR,
"f_init" VARCHAR,
"f_block" VARCHAR,
"f_hit" VARCHAR,
"f_ch" VARCHAR,
"memo" VARCHAR,
"isNew" VARCHAR
, "domContent" VARCHAR)

CREATE TABLE "language" ("id" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , "jp" VARCHAR NOT NULL  UNIQUE , "en" VARCHAR, "cn" VARCHAR)
