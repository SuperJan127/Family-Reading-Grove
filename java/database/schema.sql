BEGIN TRANSACTION;

DROP TABLE IF EXISTS books;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS families;

Create table families (
	family_id SERIAL,
	family_name varchar(100) NOT NULL UNIQUE,
	CONSTRAINT PK_family PRIMARY KEY (family_id)
);

CREATE TABLE users (
	user_id SERIAL,
	username varchar(50) NOT NULL UNIQUE,
	password_hash varchar(200) NOT NULL,
	role varchar(50) NOT NULL,
	family_id int,
	CONSTRAINT PK_user PRIMARY KEY (user_id),
	constraint FK_family FOREIGN KEY (family_id) REFERENCES families(family_id) ON DELETE SET NULL
);

CREATE TABLE books (
	book_id SERIAL PRIMARY KEY,
	title varchar(255) NOT NULL,
	author varchar(255) NOT NULL,
	isbn varchar(20) UNIQUE
);

COMMIT TRANSACTION;
