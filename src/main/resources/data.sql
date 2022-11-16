INSERT INTO role (name)
VALUES ('USER');

INSERT INTO role (name)
VALUES ('ADMIN');

INSERT INTO users (username, password)
VALUES ('admin', '$2a$10$1TwY5z9L5E0ohJ5FgU7ANO3nHKQ9Q2YSE4eLFPuIuD4HSlCAauUEW');

INSERT INTO user_role (user_id, role_id)
SELECT users.id, role.id
from users,
     role
WHERE users.username = 'admin'
  and role.name = 'ADMIN';

INSERT INTO user_role (user_id, role_id)
SELECT users.id, role.id
from users,
     role
WHERE users.username = 'admin'
  and role.name = 'USER';