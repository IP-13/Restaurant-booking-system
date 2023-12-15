--creates new tables without data for each test

drop table if exists visitor_grade;
drop table if exists restaurant_grade;
drop table if exists restaurant;
drop table if exists user_t;

create table if not exists user_t(
    username varchar(50) unique,
    num_of_grades int not null,
    sum_of_grades int not null
);

create table if not exists restaurant(
    restaurant_id int unique,
    num_of_grades int not null,
    sum_of_grades int not null
);

create table if not exists restaurant_grade (
    id int generated always as identity(start with 100 increment by 100) primary key,
    username varchar(50) references user_t(username),
    table_reserve_ticket_id int,
    -- ресторан, которому пользователь ставит оценку
    restaurant_id int references restaurant(restaurant_id),
    grade int not null,
    comment text
);

create table if not exists visitor_grade (
    id int generated always as identity(start with 100 increment by 100) primary key,
    manager_name varchar(50) references user_t(username),
    table_reserve_ticket_id int,
    -- пользователь, которому менеджер ставит оценку
    username varchar(50) references user_t(username),
    grade int not null,
    comment text
);