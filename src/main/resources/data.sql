-- #######Postgres
INSERT INTO roles
  (NAME)
VALUES
  ('ROLE_USER'),
  ('ROLE_ADMIN')
ON CONFLICT
(NAME) DO NOTHING;

INSERT INTO USERS
  (EMAIL, GENDER, NAME, PASSWORD, STATUS, TYPE, USERNAME, SITENAME)
VALUES
  ('email@yahoo.com', 'Male', 'admin', '$2a$10$kTFo5FPqHgBv7DmWJ6BnPuhZE6wL1WKcFw/2wyAQajy08QeaVPOE.', 'Active', 'User', 'admin', 'rhnl7chn'),
  ('test@yahoo.com', 'Male', '49lqd', '$2a$10$kTFo5FPqHgBv7DmWJ6BnPuhZE6wL1WKcFw/2wyAQajy08QeaVPOE.', 'Active', 'User', 'test', 'rhnl7chn')
ON CONFLICT
(EMAIL) DO NOTHING;

INSERT INTO USER_ROLES
VALUES
  ('1', '2'),
  ('2', '1')
ON CONFLICT
(USER_ID, ROLE_ID) DO NOTHING;

-- ########## OTHER DB
-- INSERT INTO roles
--   (name)
-- VALUES('ROLE_USER');
-- INSERT INTO roles
--   (name)
-- VALUES('ROLE_ADMIN');

-- INSERT INTO USERS
--   (EMAIL, GENDER, NAME, PASSWORD, STATUS, TYPE, USERNAME, SITENAME)
-- VALUES('email@yahoo.com', 'Male', 'admin', '$2a$10$kTFo5FPqHgBv7DmWJ6BnPuhZE6wL1WKcFw/2wyAQajy08QeaVPOE.', 'Active', 'User', 'admin', 'rhnl7chn');

-- INSERT INTO USER_ROLES
-- VALUES('1', '2');

-- INSERT INTO USERS
--   (EMAIL, GENDER, NAME, PASSWORD, STATUS, TYPE, USERNAME, SITENAME)
-- VALUES('test@yahoo.com', 'Male', '49lqd', '$2a$10$kTFo5FPqHgBv7DmWJ6BnPuhZE6wL1WKcFw/2wyAQajy08QeaVPOE.', 'Active', 'User', 'test', 'rhnl7chn');

-- INSERT INTO USER_ROLES
-- VALUES('2', '1');