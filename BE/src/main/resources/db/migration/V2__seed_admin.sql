
INSERT INTO roles (name, is_admin) VALUES ('ADMIN', TRUE), ('USER', FALSE);

INSERT INTO users (username, password, enabled)
VALUES ('admin', '$2a$10$MgZQ3nj7Ms5ShwxT9851AeO6RhPo4xcdXX4HNCoAqcatHe3gZN2X6', true);

INSERT INTO users (username, password, enabled)
VALUES ('user1', '$2a$10$EMHfrKssinqVDA63dMVXGOrlJgPmVfJqVgeCM9L6v2vs3wQHAjkC.', true);

INSERT INTO user_roles (id_user, id_role)
VALUES ((SELECT id FROM users WHERE username = 'admin'), (SELECT id FROM roles WHERE name = 'ADMIN'));

INSERT INTO user_roles (id_user, id_role)
VALUES ((SELECT id FROM users WHERE username = 'user1'), (SELECT id FROM roles WHERE name = 'USER'));
