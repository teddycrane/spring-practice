create table "event" (
  "id" varchar(255) not null,
  "end_date" timestamp(6) default null,
  "name" varchar(255) default null,
  "start_date" timestamp(6) default null,
  "is_active" boolean not null,
  primary key ("id")
  -- constraint "UK_mt8ulcc4k7fnc56rxaeu1sa33"
  --   unique ("name")
);
create table "race" (
  "id" varchar(255) not null,
  "category" varchar(8) default null,
  "name" varchar(255) default null,
  "end_time" timestamp(6) default null,
  "start_time" timestamp(6) default null,
  primary key ("id")
  -- constraint "UK_34ay6n28b5fe2cycvw3qx8r4e"
  --   unique ("name")
);
create table "racer" (
  "id" varchar(255) not null,
  "category" varchar(8) default null,
  "first_name" varchar(255) default null,
  "is_deleted" boolean not null,
  "last_name" varchar(255) default null,
  "birth_date" timestamp(6) default null,
  primary key ("id")
);
create table "user" (
  "id" varchar(255) not null,
  "first_name" varchar(255) default null,
  "is_deleted" boolean not null,
  "last_name" varchar(255) default null,
  "password" varchar(255) not null,
  "type" varchar(255) default null,
  "username" varchar(255) not null,
  "email" varchar(255) not null,
  "status" varchar(255) default null,
  primary key ("id")
  -- constraint "UK_sb8bbouer5wak8vyiiy4pf2bx"
  --   unique ("username")
);
create table "event_races" (
  "event_id" varchar(255) not null,
  "races_id" varchar(255) not null
  -- constraint "UK_t4u5abfwhf2bre440schtjq3"
  --   unique ("races_id"),
  -- constraint "FK7f0yppk5qpggkqs8168iy9yqo"
  --   foreign key ("races_id")
  --   references "race" ("id"),
  -- constraint "FKsn3xqchgpc4krqdlpf0gpjgh0"
  --   foreign key ("event_id")
  --   references "event" ("id"),
  -- index "FKsn3xqchgpc4krqdlpf0gpjgh0" ("event_id")
);
create table "race_racers" (
  "race_id" varchar(255) not null,
  "racers_id" varchar(255) not null
  -- constraint "UK_rdmw93dcho5bnks5hy0rdt40e"
  --   unique ("racers_id"),
  -- constraint "FK8k14l732x67t5kulic5aibb8b"
  --   foreign key ("racers_id")
  --   references "racer" ("id"),
  -- constraint "FKl5c63waba1wbrk7567lqt2dq7"
  --   foreign key ("race_id")
  --   references "race" ("id"),
  -- index "FKl5c63waba1wbrk7567lqt2dq7" ("race_id")
);
create table "race_finish_order" (
  "race_id" varchar(255) not null,
  "finish_order" timestamp(6) default null,
  "finish_order_key" varchar(255) not null
  -- primary key ("race_id", "finish_order_key"),
  -- constraint "FK52epfrofb2cu900s9i9l6cdcx"
  --   foreign key ("finish_order_key")
  --   references "racer" ("id"),
  -- constraint "FKkmis1de9ss55kklqh380amkb1"
  --   foreign key ("race_id")
  --   references "race" ("id"),
  -- index "FK52epfrofb2cu900s9i9l6cdcx" ("finish_order_key")
);
