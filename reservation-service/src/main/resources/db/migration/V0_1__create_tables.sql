create table if not exists user_t (
    id int generated always as identity(start with 100 increment by 100) primary key,
    username varchar(50) not null unique,
    password text not null,
    num_of_grades int not null,
    sum_of_grades int not null,
    roles text[]
);