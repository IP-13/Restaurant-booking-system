create type role as enum ('visitor', 'manager', 'admin');
create type reserve_table_status as enum ('created', 'processing', 'accepted', 'rejected');
create type add_restaurant_status as enum ('created', 'processing', 'accepted', 'rejected');