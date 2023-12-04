create type restaurant_add_status as enum ('PROCESSING', 'ACCEPTED', 'REJECTED');
create cast (character varying as restaurant_add_status) with inout as implicit;