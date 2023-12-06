create table user_t(
    username varchar(50) primary key,
    num_of_grades int not null,
    sum_of_grades int not null
);

create table restaurant(
    id int generated always as identity(start with 100 increment by 100) primary key,
    num_of_grades int not null,
    sum_of_grades int not null
);

create table if not exists restaurant_grade (
    id int generated always as identity(start with 100 increment by 100) primary key,
    username int references user_t(username),
    table_reserve_ticket_id int,
    -- ресторан, которому пользователь ставит оценку
    restaurant_id int references restaurant(id),
    grade int not null,
    comment text
);

create table if not exists visitor_grade (
    id int generated always as identity(start with 100 increment by 100) primary key,
    manager_name int references user_t(username),
    table_reserve_ticket_id int,
    -- пользователь, которому менеджер ставит оценку
    username int references user_t(username),
    grade int not null,
    comment text
);