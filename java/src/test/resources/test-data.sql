BEGIN TRANSACTION;

INSERT INTO users (username,password_hash,role) VALUES ('user1','user1','ROLE_USER');
INSERT INTO users (username,password_hash,role) VALUES ('user2','user2','ROLE_USER');
INSERT INTO users (username,password_hash,role) VALUES ('user3','user3','ROLE_USER');

insert into families (family_name) values ('test1');
insert into families (family_name) values ('test2');
insert into families (family_name) values ('test3');

COMMIT TRANSACTION;
