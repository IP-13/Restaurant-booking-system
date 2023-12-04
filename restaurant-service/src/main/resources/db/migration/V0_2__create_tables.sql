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
    creation_date timestamp not null,
    status restaurant_add_status,
    admin_id int references user_t(id),
    processing_date timestamp,
    admin_comment text
);

create table if not exists restaurant (
    id int generated always as identity(start with 100 increment by 100) primary key,
    restaurant_add_ticket_id int references restaurant_add_ticket(id),
    manager_id int references user_t(id),
    name varchar(50) not null,
    country text not null,
    city text not null,
    street text not null,
    building int not null,
    entrance int,
    floor int,
    description text,
    num_of_grades int not null,
    sum_of_grades int not null
);