DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

CREATE TABLE IF NOT EXISTS "users" (
  "id" INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  "email" varchar UNIQUE NOT NULL,
  "name" varchar NOT NULL
);

CREATE TABLE IF NOT EXISTS "items" (
  "id" INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  "name" varchar NOT NULL,
  "description" varchar NOT NULL,
  "available" boolean NOT NULL,
  "owner_id" int,
  "request_id" int
);

CREATE TABLE IF NOT EXISTS "requests" (
  "id" INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  "description" varchar NOT NULL,
  "requestor_id" int,
  "created" timestamp
);

CREATE TABLE IF NOT EXISTS "bookings" (
  "id" INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  "start_date" timestamp,
  "end_date" timestamp,
  "item_id" int,
  "booker_id" int,
  "status" varchar
);

CREATE TABLE IF NOT EXISTS "comments" (
  "id" INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  "text" varchar NOT NULL,
  "item_id" int,
  "author_id" int,
  "created" timestamp
);

ALTER TABLE "items" ADD FOREIGN KEY ("owner_id") REFERENCES "users" ("id");

ALTER TABLE "items" ADD FOREIGN KEY ("request_id") REFERENCES "requests" ("id");

ALTER TABLE "requests" ADD FOREIGN KEY ("requestor_id") REFERENCES "users" ("id");

ALTER TABLE "bookings" ADD FOREIGN KEY ("item_id") REFERENCES "items" ("id");

ALTER TABLE "bookings" ADD FOREIGN KEY ("booker_id") REFERENCES "users" ("id");

ALTER TABLE "comments" ADD FOREIGN KEY ("item_id") REFERENCES "items" ("id");

ALTER TABLE "comments" ADD FOREIGN KEY ("author_id") REFERENCES "users" ("id");
