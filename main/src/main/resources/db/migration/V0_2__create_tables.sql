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
    description text
);

create table if not exists user_t (
    id int generated always as identity(start with 100 increment by 100) primary key,
    username varchar(50) not null,
    password text not null
);

create table if not exists user_role (
    id int generated always as identity(start with 100 increment by 100) primary key,
    user_id int references user_t(id),
    role role
);

create table if not exists visitor (
    id int generated always as identity(start with 100 increment by 100) primary key,
    user_id int references user_t(id),
    rating float
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

create table if not exists reserve_table_ticket (
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
    reserve_table_ticket_id int references reserve_table_ticket(id),
    reason text not null,
    creation_date timestamp not null,
    expiration_date timestamp not null
);

create table if not exists add_restaurant_ticket (
    id int generated always as identity(start with 100 increment by 100) primary key,
    manager_id int references manager(id),
    admin_id int references admin(id),
    restaurant_id int references restaurant(id),
    status reserve_table_status,
    create_date timestamp not null,
    last_status_update timestamp not null,
    author_comment text,
    admin_comment text
);

create table if not exists black_list (
    id int generated always as identity(start with 100 increment by 100) primary key,
    visitor_id int references visitor(id),
    manager_id int references manager(id),
    from_date timestamp not null,
    expiration_date timestamp not null,
    reason text not null
);