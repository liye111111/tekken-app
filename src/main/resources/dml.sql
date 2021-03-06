CREATE TABLE "skill" ("id" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , 
"role" VARCHAR,
"cate" VARCHAR,
"cmdName" VARCHAR,
"groupName" VARCHAR,
"command" VARCHAR,
"judge" VARCHAR,
"damage" VARCHAR,
"distance" VARCHAR,
"f_init" VARCHAR,
"f_block" VARCHAR,
"f_hit" VARCHAR,
"f_ch" VARCHAR,
"memo" VARCHAR,
"isNew" VARCHAR, 
"domContent" VARCHAR)


CREATE TABLE "language" ("id" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , "jp" VARCHAR NOT NULL  UNIQUE , "en" VARCHAR, "cn" VARCHAR)

select * from skill where id in (select min(id) from skill group by command)  order by command