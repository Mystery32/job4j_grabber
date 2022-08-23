CREATE TABLE post (
	id serial primary key,
	name TEXT,
	text TEXT,
	link VARCHAR(255) UNIQUE,
	created TIMESTAMP
);