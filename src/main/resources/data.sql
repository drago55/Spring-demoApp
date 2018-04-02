INSERT INTO role (id, name) values(1,'ROLE_USER');
INSERT INTO role (id, name) values(2,'ROLE_ADMIN');

INSERT INTO user (id, first_name, last_name, email, password) VALUES (1, 'Dragutin', 'Horvat', 'dhorvat22@gmail.com', '$2a$10$cSfvQSXJCTpIUqriYudf6OxFtINUutF.M85UD94jrzUCLgm5LZK3K');

INSERT INTO user_role(user_id, role_id) VALUES (1,2);

INSERT INTO lat_lon(id, lat, lon) VALUES(1,44.86697,13.8642);
INSERT INTO location(id,city,postal_code,state_province,street_address) VALUES(1, 'Pula','52100','Hrvatska','4 Ulica Istarskog razvoda');
INSERT INTO marker (id, description, marker_type, name, lat_lon_id, location_id, user_id ) VALUES(1,'Marker event description','TYPE1','Marker 1',1,1,1);