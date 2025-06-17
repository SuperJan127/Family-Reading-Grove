BEGIN TRANSACTION;

insert into families (family_name) VALUES ('Smith');
insert into families (family_name) VALUES ('Johnson');

-- the password for all users is "password"
INSERT INTO users (username,password_hash,role) VALUES ('user','$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC','ROLE_USER');
INSERT INTO users (username,password_hash,role) VALUES ('admin','$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC','ROLE_ADMIN');
INSERT into users (username,password_hash,role, family_id) VALUES ('parent','$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC','ROLE_PARENT', 1);
Insert into users (username,password_hash,role, family_id) VALUES ('child','$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC','ROLE_CHILD', 1);
INSERT INTO users (username, password_hash, role, family_id) VALUES ('emily', '$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC', 'ROLE_CHILD', 2);
INSERT INTO users (username, password_hash, role, family_id) VALUES ('michael', '$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC', 'ROLE_PARENT', 2);
INSERT INTO users (username, password_hash, role, family_id) VALUES ('alice', '$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC', 'ROLE_PARENT', 2);
INSERT INTO users (username, password_hash, role, family_id) VALUES ('jason', '$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC', 'ROLE_CHILD', 2);

Insert into books (title, author, isbn) VALUES ('Effective Java', 'Joshua Bloch', '978-0134686097');
Insert into books (title, author, isbn) VALUES ('Clean Code', 'Robert C. Martin', '978-0132350884');
INSERT INTO books (title, author, isbn) VALUES ('Where the Wild Things Are', 'Maurice Sendak', '978-0064431781');
INSERT INTO books (title, author, isbn) VALUES ('The Very Hungry Caterpillar', 'Eric Carle', '978-0399226908');
INSERT INTO books (title, author, isbn) VALUES ('The Cat in the Hat', 'Dr. Seuss', '978-0394800011');

insert into prizes (prize_name, description, minutes_required, prizes_available, start_date, end_date, user_group, family_id)
VALUES 
('Book Voucher', 'A voucher for a free book of your choice.', 120, 2, '2025-02-07', '2025-08-31', 'INDIVIDUAL', 1),
('Extra Screen Time', 'An additional hour of screen time.', 150, 3, '2025-04-06', '2025-07-31', 'INDIVIDUAL', 1),
('Family Game Night', 'A family game night with snacks.', 200, 1, '2025-01-01', '2025-12-31', 'FAMILY', 1),
('Book Voucher', 'A voucher for a free book of your choice.', 120, 2, '2025-07-07', '2025-08-31', 'INDIVIDUAL', 2),
('Extra Screen Time', 'An additional hour of screen time.', 150, 3, '2025-04-06', '2025-07-31', 'INDIVIDUAL', 2),
('Family Game Night', 'A family game night with snacks.', 200, 1, '2025-05-01', '2025-12-31', 'FAMILY', 2);

INSERT INTO reading_activity (reader_id, book_id, format, minutes, notes, date) VALUES (4, 1, 'DIGITAL', 120, 'Great book on Java best practices', '2025-05-01');
INSERT INTO reading_activity (reader_id, book_id, format, minutes, notes, date) VALUES (3, 2, 'PAPER', 90, 'Learned a lot about clean coding principles', '2025-06-02');
INSERT INTO reading_activity (reader_id, book_id, format, minutes, notes, date) VALUES (3, 1, 'DIGITAL', 60, 'Interesting insights on Java concurrency', '2025-06-03');
INSERT INTO reading_activity (reader_id, book_id, format, minutes, notes, date) VALUES (7, 3, 'PAPER', 30, 'Loved the illustrations', '2025-06-04');
INSERT INTO reading_activity (reader_id, book_id, format, minutes, notes, date) VALUES (5, 4, 'PAPER', 45, 'A delightful story for children', '2025-06-05');
INSERT INTO reading_activity (reader_id, book_id, format, minutes, notes, date) VALUES (7, 5, 'PAPER', 50, 'A classic childrens book', '2025-03-06');
insert into reading_activity (reader_id, book_id, format, minutes, notes, date) VALUES (8, 1, 'DIGITAL', 100, 'A must-read for Java developers', '2025-05-07');
insert into reading_activity (reader_id, book_id, format, minutes, notes, date) VALUES (8, 2, 'PAPER', 80, 'Great tips on writing clean code', '2025-06-08');
insert into reading_activity (reader_id, book_id, format, minutes, notes, date) VALUES (6, 3, 'DIGITAL', 70, 'A fun and engaging read for kids', '2025-07-09');


INSERT INTO user_book (user_id, book_id) VALUES (4, 1);
INSERT INTO user_book (user_id, book_id) VALUES (3, 2);
INSERT INTO user_book (user_id, book_id) VALUES (3, 1);
INSERT INTO user_book (user_id, book_id) VALUES (7, 3);
INSERT INTO user_book (user_id, book_id) VALUES (5, 4);
INSERT INTO user_book (user_id, book_id) VALUES (7, 5);

COMMIT TRANSACTION;
