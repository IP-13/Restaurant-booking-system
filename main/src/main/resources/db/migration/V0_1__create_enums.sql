create type role as enum ('manager', 'admin');
create type reserve_table_status as enum ('processing', 'accepted', 'rejected');
create type add_restaurant_result as enum ('accepted', 'rejected');

create cast (character varying as role) with inout as implicit;
create cast (character varying as reserve_table_status) with inout as implicit;
create cast (character varying as add_restaurant_result) with inout as implicit;