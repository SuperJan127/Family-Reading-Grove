BEGIN TRANSACTION;

DROP TABLE IF EXISTS reading_activity;
DROP TABLE IF EXISTS books;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS families;

CREATE TABLE families (
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

CREATE TABLE reading_activity (
	id SERIAL PRIMARY KEY,
	reader_id INT NOT NULL,
	book_id INT NOT NULL,
	format varchar(50) NOT NULL,
	notes TEXT,
	start_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	end_time TIMESTAMP,
	minutes_read INTEGER,
	CONSTRAINT FK_reader FOREIGN KEY (reader_id) REFERENCES users(user_id) ON DELETE CASCADE,
	CONSTRAINT FK_book FOREIGN KEY (book_id) REFERENCES books(book_id) ON DELETE CASCADE
);

CREATE INDEX idx_reading_activity_reader ON reading_activity (reader_id);
CREATE INDEX idx_reading_activity_book ON reading_activity (book_id);

COMMIT TRANSACTION;
