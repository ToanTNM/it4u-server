INSERT INTO roles(name) VALUES('ROLE_USER');
INSERT INTO roles(name) VALUES('ROLE_ADMIN');

INSERT INTO USERS(EMAIL, GENDER, NAME, PASSWORD, STATUS, TYPE, USERNAME, SITENAME) VALUES('email@yahoo.com', 'Male', 'admin', '$2a$10$fnEjJgyjJzrYlEmfDuufxOM.Evjmhocmu.2JaQ3R8r31sTUHujFgS', 'Active', 'User', 'admin', 'rhnl7chn');

INSERT INTO USER_ROLES VALUES('1','2');

INSERT INTO USERS(EMAIL, GENDER, NAME, PASSWORD, STATUS, TYPE, USERNAME, SITENAME) VALUES('test@yahoo.com', 'Male', '49lqd', '$2a$10$fnEjJgyjJzrYlEmfDuufxOM.Evjmhocmu.2JaQ3R8r31sTUHujFgS', 'Active', 'User', 'test', 'rhnl7chn');

INSERT INTO USER_ROLES VALUES('2','1');