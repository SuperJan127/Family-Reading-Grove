BEGIN TRANSACTION;

DROP TABLE IF EXISTS reading_activity;
DROP TABLE IF EXISTS books;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS families;
DROP TABLE IF EXISTS prizes;
DROP TABLE IF EXISTS user_book;

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
    reader_id INTEGER NOT NULL,        
    book_id INTEGER NOT NULL,          
    format VARCHAR(50) NOT NULL,       
    minutes INTEGER NOT NULL CHECK (minutes >= 0),  
    notes TEXT,    
	CONSTRAINT FK_reader FOREIGN KEY (reader_id) REFERENCES users(user_id) ON DELETE CASCADE,
	CONSTRAINT FK_book FOREIGN KEY (book_id) REFERENCES books(book_id) ON DELETE CASCADE                  
);



Create table prizes(
	prize_id SERIAL PRIMARY KEY,
	prize_name varchar(100) NOT NULL UNIQUE,
	description varchar (255) NOT NULL,
	minutes_required INTEGER NOT NULL,
	prizes_available INTEGER NOT NULL DEFAULT 0,
	start_date date NOT NULL,
	end_date date NOT NULL,
	user_group varchar(50) NOT NULL,
	family_id int,
	CONSTRAINT FK_family_prize FOREIGN KEY (family_id) REFERENCES families(family_id) ON DELETE SET NULL
);

CREATE TABLE user_book (
    id SERIAL PRIMARY KEY,
    user_id int NOT NULL,
    book_id int NOT NULL,
    currently_reading BOOLEAN DEFAULT TRUE,
    date_started DATE DEFAULT CURRENT_DATE,
	date_completed DATE DEFAULT null,
	notes varchar(255) DEFAULT NULL,
	rating int CHECK (rating >= 0 AND rating <= 5),
	CONSTRAINT check_date_started CHECK (date_started <= CURRENT_DATE),
	CONSTRAINT fk_users FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_books FOREIGN KEY (book_id) REFERENCES books(book_id) ON DELETE CASCADE,
    CONSTRAINT unique_user_book UNIQUE (user_id, book_id)
);

COMMIT TRANSACTION;
