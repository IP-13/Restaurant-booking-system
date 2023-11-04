create table if not exists user_t (
    id int generated always as identity(start with 100 increment by 100) primary key,
    username varchar(50) not null,
    password text not null,
    num_of_grades int not null,
    sum_of_grades int not null,
    roles text[]
);

create table if not exists admin (
    id int generated always as identity(start with 100 increment by 100) primary key,
    user_id int references user_t(id),
    is_active boolean
);

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
    user_id int references user_t(id),
    creation_date timestamp not null
);

create table if not exists restaurant_add_ticket_result (
    id int generated always as identity(start with 100 increment by 100) primary key,
    restaurant_add_ticket_id int references restaurant_add_ticket(id),
    admin_id int references admin(id),
    result restaurant_add_status,
    creation_date timestamp not null,
    admin_comment text
);

create table if not exists address (
    id int generated always as identity(start with 100 increment by 100) primary key,
    country text not null,
    city text not null,
    street text not null,
    building int not null,
    entrance int,
    floor int
);

create table if not exists restaurant (
    id int generated always as identity(start with 100 increment by 100) primary key,
    name varchar(50) not null,
    address_id int references address(id),
    restaurant_add_ticket_id int references restaurant_add_ticket(id),
    description text,
    num_of_grades int not null,
    sum_of_grades int not null
);

create table if not exists manager (
    id int generated always as identity(start with 100 increment by 100) primary key,
    user_id int references user_t(id),
    restaurant_id int references restaurant(id),
    is_active boolean
);

create table if not exists table_reserve_ticket (
    id int generated always as identity(start with 100 increment by 100) primary key,
    restaurant_id int references restaurant(id),
    user_id int references user_t(id),
    -- дата и время создания заявки
    creation_date timestamp not null,
    -- дата и время в которое пользователь планирует прийти
    from_date timestamp not null,
    -- дата и время до которого пользователь планирует находиться в ресторане
    till_date timestamp not null,
    num_of_guests int not null,
    user_comment text
);

create table if not exists table_reserve_ticket_result (
    id int generated always as identity(start with 100 increment by 100) primary key,
    manager_id int references manager(id),
    manager_comment text,
    status reserve_table_status
);

create table if not exists grade_after_visit_visitor (
    id int generated always as identity(start with 100 increment by 100) primary key,
    user_id int references user_t(id),
    table_reserve_ticket_result_id int references table_reserve_ticket_result(id),
    -- оценка, которую пользователь поставил ресторану после визита
    grade float,
    comment text
);

create table if not exists grade_after_visit_manager (
    id int generated always as identity(start with 100 increment by 100) primary key,
    manager_id int references manager(id),
    table_reserve_ticket_result_id int references table_reserve_ticket_result(id),
    -- оценка, которую менеджер поставил пользователю после визита
    grade float,
    comment text
);

create table if not exists booking_constraint (
    id int generated always as identity(start with 100 increment by 100) primary key,
    restaurant_id int references restaurant(id),
    manager_id int references manager(id),
    reason text not null,
    from_date timestamp not null,
    till_date timestamp not null
);

create table if not exists black_list (
    id int generated always as identity(start with 100 increment by 100) primary key,
    user_id int references user_t(id),
    from_date timestamp not null,
    till_date timestamp not null,
    reason text not null
);