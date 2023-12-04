create table if not exists table_reserve_ticket (
    id int generated always as identity(start with 100 increment by 100) primary key,
    restaurant_id int,
    user_id int,
    -- дата и время создания заявки
    creation_date timestamp not null,
    -- дата и время в которое пользователь планирует прийти
    from_date timestamp not null,
    -- дата и время до которого пользователь планирует находиться в ресторане
    till_date timestamp not null,
    num_of_guests int not null,
    user_comment text,
    manager_id int,
    manager_comment text,
    status reserve_table_status
);

create table if not exists restaurant_grade (
    id int generated always as identity(start with 100 increment by 100) primary key,
    user_id int,
    table_reserve_ticket_id int references table_reserve_ticket(id),
    -- ресторан, которому пользователь ставит оценку
    restaurant_id int references restaurant(id),
    grade int not null,
    comment text
);

create table if not exists visitor_grade (
    id int generated always as identity(start with 100 increment by 100) primary key,
    manager_id int,
    table_reserve_ticket_id int references table_reserve_ticket(id),
    -- пользователь, которому менеджер ставит оценку
    user_id int,
    grade int not null,
    comment text
);

create table if not exists booking_constraint (
    id int generated always as identity(start with 100 increment by 100) primary key,
    restaurant_id int,
    manager_id int,
    reason text not null,
    from_date timestamp not null,
    till_date timestamp not null
);