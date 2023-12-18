create table if not exists file_info(
    bucket_name varchar(50),
    object_name varchar(50),
    username varchar(50),
    primary key (bucket_name, object_name)
);