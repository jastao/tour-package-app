insert into user (user_id, username, password, first_name, last_name, email) values
(1, 'csr_admin', '$2a$10$O7P841p7ui75riNDdGuMMurxVosky3sTE19j9eK2E6Wf.m59teNHG', 'admin', 'admin', 'admin@travel.com'),
(2, 'csr_tom', '$2y$12$mMgV9eakGVSZ3IxeSW7CR.5NrDmrvnqtsBzfz7uwZZU2PRJFa4L6a', 'Tom', 'Miles', 'tom.cook@travel.com'),
(3, 'jacky', '$2a$10$DvSji4zakHEB4oixZqrWuk0mBU2WuXcFpMvFqnSj5/9vgUEM8LCi', 'Jacky', 'Huges', 'jacky.huges@travel.com');

insert into auth_user_group (auth_user_group_id, auth_group, description) values
(1, 'CSR_ADMIN', 'CSR Administrator group access'),
(2, 'CSR_USER', 'CSR User group access'),
(3, 'VIEWER', 'Viewer has only viewing access');

insert into user_auth_user_group (username, auth_user_group_id) values
('csr_admin', 1),
('csr_tom', 2),
('jacky', 3);
