BEGIN TRANSACTION;

insert into families (family_name) VALUES ('Smith');

-- the password for both users is "password"
INSERT INTO users (username,password_hash,role) VALUES ('user','$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC','ROLE_USER');
INSERT INTO users (username,password_hash,role) VALUES ('admin','$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC','ROLE_ADMIN');
INSERT into users (username,password_hash,role, family_id) VALUES ('parent','$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC','ROLE_PARENT', 1);
Insert into users (username,password_hash,role, family_id) VALUES ('child','$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC','ROLE_CHILD', 1);

Insert into books (title, author, isbn) VALUES ('Effective Java', 'Joshua Bloch', '978-0134686097');
Insert into books (title, author, isbn) VALUES ('Clean Code', 'Robert C. Martin', '978-0132350884');
Insert into books (title, author, isbn) VALUES ('Java Concurrency in Practice', 'Brian Goetz', '978-0321349606');

INSERT INTO reading_activity (reader_id, book_id, format, minutes, notes) VALUES (1, 1, 'EBOOK', 120, 'Great book on Java best practices');
INSERT INTO reading_activity (reader_id, book_id, format, minutes, notes) VALUES (2, 2, 'PAPERBACK', 90, 'Learned a lot about clean coding principles');


COMMIT TRANSACTION;
