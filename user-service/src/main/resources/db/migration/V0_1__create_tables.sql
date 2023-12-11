create table if not exists user_t (
    username varchar(50) primary key,
    password text not null,
    roles text[]
);