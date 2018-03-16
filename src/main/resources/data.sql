INSERT INTO role (id, name) values(1,'ROLE_USER');
INSERT INTO role (id, name) values(2,'ROLE_ADMIN');

INSERT INTO user (id, first_name, last_name, email, password) VALUES (1, 'Dragutin', 'Horvat', 'dhorvat22@gmail.com', 'LCwuEtYWGKXJ7PBIo2Ze86gXnqe7iURj5mb7WCpHEHNQwcZ2lfa');

INSERT INTO user_role(user_id, role_id) VALUES (1,2);