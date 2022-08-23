CREATE TABLE post (
	id serial primary key,
	name TEXT,
	text TEXT,
	lint TEXT UNIQUE,
	created TIMESTAMP
);