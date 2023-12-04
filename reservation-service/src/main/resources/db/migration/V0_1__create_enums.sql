create type reserve_table_status as enum ('PROCESSING', 'ACCEPTED', 'REJECTED');
create cast (character varying as reserve_table_status) with inout as implicit;