create type role as enum ('manager', 'admin');
create type reserve_table_status as enum ('processing', 'accepted', 'rejected');
create type restaurant_add_result as enum ('accepted', 'rejected');

create cast (character varying as role) with inout as implicit;
create cast (character varying as reserve_table_status) with inout as implicit;
create cast (character varying as restaurant_add_result) with inout as implicit;