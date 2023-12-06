create table if not exists restaurant_add_ticket (
    id int generated always as identity(start with 100 increment by 100) primary key,
    name varchar(50) not null,
    country text not null,
    city text not null,
    street text not null,
    building int not null,
    entrance int,
    floor int,
    description text,
    user_id int,
    creation_date timestamp not null,
    status restaurant_add_status,
    admin_id int,
    processing_date timestamp,
    admin_comment text
);

create table if not exists restaurant (
    id int generated always as identity(start with 100 increment by 100) primary key,
    restaurant_add_ticket_id int references restaurant_add_ticket(id),
    manager_id int,
    name varchar(50) not null,
    country text not null,
    city text not null,
    street text not null,
    building int not null,
    entrance int,
    floor int,
    description text,
);

create table if not exists table_reserve_ticket (
    id int generated always as identity(start with 100 increment by 100) primary key,
    restaurant_id int references restaurant(id),
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

create table if not exists booking_constraint (
    id int generated always as identity(start with 100 increment by 100) primary key,
    restaurant_id int references restaurant(id),
    manager_id int,
    reason text not null,
    from_date timestamp not null,
    till_date timestamp not null
);