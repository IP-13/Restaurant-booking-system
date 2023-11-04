create type role as enum ('MANAGER', 'ADMIN');
create type reserve_table_status as enum ('PROCESSING', 'ACCEPTED', 'REJECTED');
create type restaurant_add_status as enum ('PROCESSING', 'ACCEPTED', 'REJECTED');

create cast (character varying as role) with inout as implicit;
create cast (character varying as reserve_table_status) with inout as implicit;
create cast (character varying as restaurant_add_result) with inout as implicit;