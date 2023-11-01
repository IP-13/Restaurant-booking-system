insert into user_t (username, password, rating, roles)
values ('mega_admin', '$2a$10$74v.SgHdDyr0fX.JbtK7G.TTJO35tyJoK7C3xXyHxQ0MbSm2BrnO6', 5.0, '{"ADMIN", "MANAGER"}');
insert into admin(user_id) values (100);