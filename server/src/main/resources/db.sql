-- 更新或添加时自动更新时间
create or replace function update_timestamp_column() returns trigger as
'
    begin
        new.update_time := current_timestamp;
        return new;
    end
' language plpgsql;


-- 创建枚举 start
create type user_role as enum ('Admin', 'User');
alter type user_role owner to postgres;
-- 创建枚举 end


-- 用户表 start
create table users
(
    id          bigint    default nextval('user_id_seq'::regclass) not null
        constraint user_pkey primary key,
    name        varchar(30)                                        not null,
    birthday    date,
    password    varchar(128)                                       not null,
    gender      smallint,
    address     varchar(50),
    email       varchar(128),
    update_time timestamp                                          not null,
    create_time timestamp default CURRENT_TIMESTAMP                not null,
    role        user_role default 'Admin'::user_role               not null
);

alter table users
    owner to postgres;

create trigger user_update_timestamp
    before insert or update
    on users
    for each row
execute procedure update_timestamp_column();
-- 用户表 end


-- 省市区表 start
create table region
(
    id        integer     not null
        constraint id primary key,
    parent_id integer     not null,
    name      varchar(20) not null
);

comment on table region is '省市县';

alter table region
    owner to postgres;

create index parent_id
    on region (parent_id);
-- 省市区表 end

