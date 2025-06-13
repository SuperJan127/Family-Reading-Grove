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

insert into prizes (prize_name, description, minutes_required, prizes_available, start_date, end_date, user_group)
VALUES 
('Book Voucher', 'A voucher for a free book of your choice.', 300, 2, '2023-01-01', '2023-12-31', 'CHILD'),
('Extra Screen Time', 'An additional hour of screen time.', 150, 3, '2023-01-01', '2023-12-31', 'CHILD'),
('Family Game Night', 'A family game night with snacks.', 200, 1, '2023-01-01', '2023-12-31', 'BOTH');

INSERT INTO reading_activity (reader_id, book_id, format, minutes, notes) VALUES (4, 1, 'EBOOK', 120, 'Great book on Java best practices');
INSERT INTO reading_activity (reader_id, book_id, format, minutes, notes) VALUES (3, 2, 'PAPERBACK', 90, 'Learned a lot about clean coding principles');

INSERT INTO user_book (user_id, book_id) VALUES (4, 1);
INSERT INTO user_book (user_id, book_id) VALUES (3, 2);
INSERT INTO user_book (user_id, book_id) VALUES (3, 1);

COMMIT TRANSACTION;
