INSERT INTO role (id, name) values(1,'ROLE_USER');
INSERT INTO role (id, name) values(2,'ROLE_ADMIN');

INSERT INTO user (id, first_name, last_name, email, password) VALUES (1, 'Dragutin', 'Horvat', 'dhorvat22@gmail.com', '$2a$10$cSfvQSXJCTpIUqriYudf6OxFtINUutF.M85UD94jrzUCLgm5LZK3K');

INSERT INTO user_role(user_id, role_id) VALUES (1,2);