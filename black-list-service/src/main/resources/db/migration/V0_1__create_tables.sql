create table if not exists black_list (
    id int generated always as identity(start with 100 increment by 100) primary key,
    username varchar(50),
    from_date timestamp not null,
    till_date timestamp not null,
    reason text not null
);