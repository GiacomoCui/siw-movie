/*--- INSERT MOVIE ---*/
insert into movie (id, anno, titolo) values (nextval('hibernate_sequence'), 2020, 'Er pupone');

/*--- INSERT ARTIST ---*/
insert into artist (id, nome, cognome, data_nascita) values(nextval('hibernate_sequence'), 'Tim', 'Burton', '1958-08-25');
insert into artist (id, nome, cognome, data_nascita) values(nextval('hibernate_sequence'), 'Himesh', 'Patel', '1990-10-13');
insert into artist (id, nome, cognome, data_nascita) values(nextval('hibernate_sequence'), 'Danny', 'Boyle', '1956-10-20');
insert into artist (id, nome, cognome, data_nascita) values(nextval('hibernate_sequence'), 'Tim', 'Roth', '1961-05-14');
insert into artist (id, nome, cognome, data_nascita) values(nextval('hibernate_sequence'), 'Michael', 'Keaton', '1951-09-05');
insert into artist (id, nome, cognome, data_nascita) values(nextval('hibernate_sequence'), 'Stanley', 'Kubrick', '1928-07-26');
insert into artist (id, nome, cognome, data_nascita) values(nextval('hibernate_sequence'), 'Ronald Lee', 'Ermey', '1944-03-24');
insert into artist (id, nome, cognome, data_nascita) values(nextval('hibernate_sequence'), 'Claudio', 'Santamaria', '1974-07-22');
insert into artist (id, nome, cognome, data_nascita) values(nextval('hibernate_sequence'), 'Gabriele', 'Mainetti', '1976-11-07');

/*--- INSERT USERS ---*/
insert into users(id, cognome, email, nome) values (10, 'Admin', 'admin@gmail.com', 'Admin');
insert into credentials(id, password, ruolo, username, user_id) values (nextval('hibernate_sequence'), '$2a$10$3Cv16C474NgW.YCaw6LiWuiW.Na2PNf8gvzGpMeNmTZliKHzTFhtC', 'ADMIN', 'Admin', 10);

insert into users(id, cognome, email, nome) values (12, 'Default', 'default@gmail.com', 'Default');
insert into credentials(id, password, ruolo, username, user_id) values (nextval('hibernate_sequence'), '$2a$10$sQH25h7Q25EUcETWQWrXH.qR6FDJoE0AxJQVN0mXNAxkJAtRw1Rqu', 'DEFAULT', 'Default', 12);