create table if not exists user_t (
    id int generated always as identity(start with 100 increment by 100) primary key,
    username varchar(50) not null,
    password text not null,
    rating float
);

create table if not exists user_role (
    id int generated always as identity(start with 100 increment by 100) primary key,
    user_id int references user_t(id),
    role role
);

create table if not exists admin (
    id int generated always as identity(start with 100 increment by 100) primary key,
    user_id int references user_t(id)
);

create table if not exists manager (
    id int generated always as identity(start with 100 increment by 100) primary key,
    user_id int references user_t(id),
    restaurant_id int references restaurant(id)
);

create table if not exists table_reserve_ticket (
    id int generated always as identity(start with 100 increment by 100) primary key,
    restaurant_id int references restaurant(id),
    visitor_id int references visitor(id),
    manager_id int references manager(id),
    status reserve_table_status,
    creation_date timestamp not null,
    last_status_update timestamp not null,
    visitor_comment text,
    manager_comment text
);

create table if not exists booking_constraint (
    id int generated always as identity(start with 100 increment by 100) primary key,
    restaurant_id int references restaurant(id),
    manager_id int references manager(id),
    reason text not null,
    creation_date timestamp not null,
    expiration_date timestamp not null
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
    create_date timestamp not null
);

create table if not exists restaurant_add_ticket_result (
    id int generated always as identity(start with 100 increment by 100) primary key,
    admin_id int references admin(id),
    result add_restaurant_result,
    create_date timestamp not null,
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
    description text
);

create table if not exists black_list (
    id int generated always as identity(start with 100 increment by 100) primary key,
    visitor_id int references visitor(id),
    manager_id int references manager(id),
    from_date timestamp not null,
    expiration_date timestamp not null,
    reason text not null
);