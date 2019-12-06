INSERT INTO roles(name) VALUES('ROLE_USER');
INSERT INTO roles(name) VALUES('ROLE_ADMIN');

INSERT INTO USERS(EMAIL, GENDER, NAME, PASSWORD, STATUS, TYPE, USERNAME) VALUES('email@yahoo.com', 'Male', 'admin', '$2a$10$fnEjJgyjJzrYlEmfDuufxOM.Evjmhocmu.2JaQ3R8r31sTUHujFgS', 'Active', 'User', 'admin');

INSERT INTO USER_ROLES VALUES('1','2');