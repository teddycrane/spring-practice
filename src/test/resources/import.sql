create table event (
  id varchar(255) not null,
  end_date timestamp(6) default null,
  name varchar(255) default null,
  start_date timestamp(6) default null,
  is_active boolean not null,
  primary key (id),
  constraint "UK_mt8ulcc4k7fnc56rxaeu1sa33"
    unique (name)
);
create table race (
  id varchar(255) not null,
  category varchar(8) default null,
  name varchar(255) default null,
  end_time timestamp(6) default null,
  start_time timestamp(6) default null,
  primary key (id),
  constraint "UK_34ay6n28b5fe2cycvw3qx8r4e"
    unique (name)
);
create table racer (
  id varchar(255) not null,
  category varchar(8) default null,
  first_name varchar(255) default null,
  is_deleted boolean not null,
  last_name varchar(255) default null,
  birth_date timestamp(6) default null,
  primary key (id)
);
create table user (
  id varchar(255) not null,
  first_name varchar(255) default null,
  is_deleted boolean not null,
  last_name varchar(255) default null,
  password varchar(255) not null,
  type varchar(255) default null,
  username varchar(255) not null,
  email varchar(255) not null,
  status varchar(255) default null,
  primary key (id),
  constraint "UK_sb8bbouer5wak8vyiiy4pf2bx"
    unique (username)
);
create table event_races (
  event_id varchar(255) not null,
  races_id varchar(255) not null,
  constraint "UK_t4u5abfwhf2bre440schtjq3"
    unique (races_id),
  constraint "FK7f0yppk5qpggkqs8168iy9yqo"
    foreign key (races_id)
    references race (id),
  constraint "FKsn3xqchgpc4krqdlpf0gpjgh0"
    foreign key (event_id)
    references event (id),
  index "FKsn3xqchgpc4krqdlpf0gpjgh0" (event_id)
);
create table race_racers (
  race_id varchar(255) not null,
  racers_id varchar(255) not null,
  constraint "UK_rdmw93dcho5bnks5hy0rdt40e"
    unique (racers_id),
  constraint "FK8k14l732x67t5kulic5aibb8b"
    foreign key (racers_id)
    references racer (id),
  constraint "FKl5c63waba1wbrk7567lqt2dq7"
    foreign key (race_id)
    references race (id),
  index "FKl5c63waba1wbrk7567lqt2dq7" (race_id)
);
create table race_finish_order (
  race_id varchar(255) not null,
  finish_order timestamp(6) default null,
  finish_order_key varchar(255) not null,
  primary key (race_id, finish_order_key),
  constraint "FK52epfrofb2cu900s9i9l6cdcx"
    foreign key (finish_order_key)
    references racer (id),
  constraint "FKkmis1de9ss55kklqh380amkb1"
    foreign key (race_id)
    references race (id),
  index "FK52epfrofb2cu900s9i9l6cdcx" (finish_order_key)
);

INSERT INTO event(id, end_date, name, start_date, is_active) VALUES ('02fd6b48-33f0-4bdc-af52-ba4bbd802db7','2021-11-07 11:58:43.933000','Active Event','2021-11-06 12:58:43.933000',false);
INSERT INTO event(id, end_date, name, start_date, is_active) VALUES ('6a4868cd-cdc9-4eef-9318-2f07ccc3e24e','2021-11-07 11:58:43.933000','Deleted Event','2021-11-06 12:58:43.933000',false);

INSERT INTO race(id, category, name, end_time, start_time) VALUES ('008e44be-3d2a-4633-896d-8e2a50bdff5b','CAT3','Test Race 8',NULL,'2021-11-24 12:33:05.572000');
INSERT INTO race(id, category, name, end_time, start_time) VALUES ('1884c898-ff32-40f6-8647-fcc2f7894dd9','CAT3','Test Race 6','2021-11-24 12:30:15.532000','2021-11-24 12:30:15.532000');
INSERT INTO race(id, category, name, end_time, start_time) VALUES ('292155e3-29cf-42e2-b5f0-a5003f4f08c0','CAT3','Test Race','2021-11-24 11:46:40.548000','2021-11-24 11:14:18.479000');
INSERT INTO race(id, category, name, end_time, start_time) VALUES ('5846a11a-e628-4c44-a8b0-f0ab73b31e68','CAT5','D.P',NULL,NULL);
INSERT INTO race(id, category, name, end_time, start_time) VALUES ('5f921909-25d7-433d-a966-7f4c43982d9c','CAT3','Test Race 7','2021-11-24 12:31:56.869000','2021-11-24 12:31:56.869000');
INSERT INTO race(id, category, name, end_time, start_time) VALUES ('77b5aacc-d841-4949-8207-5a737ffa23aa','CAT5','Carl Brutananadilewski',NULL,NULL);

INSERT INTO racer(id, category, first_name, is_deleted, last_name, birth_date) VALUES ('08668f04-0a72-4753-adda-c261025c3d1b','CAT1','Catheryn',false,'Treutel','2005-08-25 12:26:20.102000');
INSERT INTO racer(id, category, first_name, is_deleted, last_name, birth_date)  VALUES ('0e596a03-76c3-4c52-9cbe-b560c58ef750','CAT1','Test',false,'User2',NULL);
INSERT INTO racer(id, category, first_name, is_deleted, last_name, birth_date)  VALUES ('0edab7f4-1ca6-4fa2-8da7-ad1f6ef1fef0','CAT2','Vivien',false,'Turcotte','1927-01-30 04:01:44.663000');
INSERT INTO racer(id, category, first_name, is_deleted, last_name, birth_date)  VALUES ('123ddca6-111d-4f3e-b2ec-ad72847a9c32','CAT3','Ling',false,'Thiel','2009-03-19 11:07:52.560000');
INSERT INTO racer(id, category, first_name, is_deleted, last_name, birth_date)  VALUES ('14a535d2-b41c-4c02-a111-3916870c4aea','CAT4','Otilia',false,'Lesch','1997-03-19 14:47:07.206000');
INSERT INTO racer(id, category, first_name, is_deleted, last_name, birth_date)  VALUES ('2dcd7ff6-7c77-41b9-b818-fc3e813e8b65','CAT2','Test',true,'User1',NULL);
INSERT INTO racer(id, category, first_name, is_deleted, last_name, birth_date)  VALUES ('5c81eed8-acf5-4630-bca2-30ab15684449','CAT5','Brook',false,'Torp','1947-08-05 19:44:10.496000');
INSERT INTO racer(id, category, first_name, is_deleted, last_name, birth_date)  VALUES ('66f97578-3595-4f19-86c2-7e4dd4000167','CAT1','Test',false,'User',NULL);
INSERT INTO racer(id, category, first_name, is_deleted, last_name, birth_date)  VALUES ('ce851d5a-df8b-4bf5-ab41-ef0c95a30236','CAT1','Teddy',false,'Crane',NULL);

INSERT INTO user(id, first_name, is_deleted, last_name, password, type, username, email, status) VALUES ('1023c9e1-6900-4520-b8ec-7753a5cdf120','Test',false,'User','ee26b0dd4af7e749aa1a8ee3c10ae9923f618980772e473f8819a5d4940e0db27ac185f8a0e1d5f84f88bc887fd67b143732c304cc5fa9ad8e6f57f50028a8ff','USER','testuser2','test2@fake.fake','ACTIVE');
INSERT INTO user(id, first_name, is_deleted, last_name, password, type, username, email, status) VALUES ('5e0c215b-4309-4902-97cf-01e7fc2a17b1','Test',false,'Admin','ee26b0dd4af7e749aa1a8ee3c10ae9923f618980772e473f8819a5d4940e0db27ac185f8a0e1d5f84f88bc887fd67b143732c304cc5fa9ad8e6f57f50028a8ff','ADMIN','admin','admintestuser@teddycrane.com','ACTIVE');
INSERT INTO user(id, first_name, is_deleted, last_name, password, type, username, email, status) VALUES ('7825ff10-d79e-494c-bfc4-a0184ae7badf','Test',false,'User2','58ed8cdf67334eec66c872644262a921f2e77b4ada5891af0664bf3078dcf9d400564bb8aff3e37aad2b6529efb557d2dd2d61daf54fb9e3cbb47f3348dfd821','USER','testuser1','test1@fake.fake','ACTIVE');

INSERT INTO race_finish_order(race_id, finish_order, finish_order_key) VALUES ('008e44be-3d2a-4633-896d-8e2a50bdff5b','2021-11-24 12:53:58.633000','ce851d5a-df8b-4bf5-ab41-ef0c95a30236');

INSERT INTO race_racers(race_id, racers_id) VALUES ('008e44be-3d2a-4633-896d-8e2a50bdff5b','0e596a03-76c3-4c52-9cbe-b560c58ef750');
INSERT INTO race_racers(race_id, racers_id) VALUES ('008e44be-3d2a-4633-896d-8e2a50bdff5b','66f97578-3595-4f19-86c2-7e4dd4000167');
INSERT INTO race_racers(race_id, racers_id) VALUES ('008e44be-3d2a-4633-896d-8e2a50bdff5b','ce851d5a-df8b-4bf5-ab41-ef0c95a30236');
