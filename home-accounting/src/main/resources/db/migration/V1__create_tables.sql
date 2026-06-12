create table users
(
    id  bigserial primary key,
    username varchar(50) not null unique,
    email varchar(100) not null unique,
    password varchar(255) not null,
    create_at timestamp default now(),
    updated_at timestamp default now()
);

create table categories (
    id bigserial primary key,
    name varchar(100) not null,
    type varchar(10) not null check (type in ('INCOME', 'EXPENSE')),
    create_at timestamp default now(),
    updated_at timestamp default now(),
);

create table transaction (
    id bigserial primary key,
    user_id bigint not null,
    category_id bigint not null,
    type varchar(10) not null check(type in ('INCOME', 'EXPENSE')),
    amount decimal(15,2) not null,
    comment text,
    date timestamp default now(),
    create_at timestamp default now(),
    updated_at timestamp default now(),

    constraint fk_transaction_category
        foreign key (category_id) references categories (id)
);

create index idx_transaction_user_id on transaction (user_id);
create index idx_transaction_category_id on transaction (category_id);
create index idx_transaction_date on transaction (date);
create index idx_transaction_type on transaction (type);

