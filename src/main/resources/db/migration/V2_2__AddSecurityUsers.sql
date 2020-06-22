insert into user (user_id, username, password, first_name, last_name, email) values
(1, 'csr_admin', '$2y$12$Je2nx4NYYQnZfUFqeEfWAekNwg0qBs1sZKA4ay70Ea39dItCxw6wW', 'admin', 'admin', 'admin@travel.com'),
(2, 'csr_tom', '$2y$12$mMgV9eakGVSZ3IxeSW7CR.5NrDmrvnqtsBzfz7uwZZU2PRJFa4L6a', 'Tom', 'Miles', 'tom.cook@travel.com'),
(3, 'jacky', '$2y$12$YkOfpb0hweNwYy3F2cQU4O0y7ZgH2JtfxtIcdj7Sd7lYraajutsey', 'Jacky', 'Huges', 'jacky.huges@travel.com');

insert into auth_user_group (auth_user_group_id, auth_group, description) values
(1, 'CSR_ADMIN', 'CSR Administrator group access'),
(2, 'CSR_USER', 'CSR User group access'),
(3, 'VIEWER', 'Viewer has only viewing access');

insert into user_auth_user_group (username, auth_user_group_id) values
('csr_admin', 1),
('csr_tom', 2),
('jacky', 3);
