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
    id          bigserial                                          not null
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


-- 微信公众号表 start

create table q_and_a
(
    id          serial primary key,
    question    varchar(128)                        not null,
    answer      text                                not null,
    tagIds      int[],
    update_time timestamp                           not null,
    create_time timestamp default CURRENT_TIMESTAMP not null
);
comment on table q_and_a is '问题与答案';
comment on column q_and_a.question is '问题';
comment on column q_and_a.answer is '答案';
comment on column q_and_a.tagIds is '标签id';
create trigger q_and_a_update_timestamp
    before insert or update
    on q_and_a
    for each row
execute procedure update_timestamp_column();


create table qa_tag
(
    id          serial primary key,
    tag_name    varchar(64)                         not null,
    description text,
    create_time timestamp default CURRENT_TIMESTAMP not null
);
comment on table qa_tag is '问题标签';



create table accept_msg_record
(
    id             bigserial primary key,
    to_user_name   varchar(200)  not null,
    from_user_name varchar(200)  not null,
    msg_type       varchar(32)   not null,
    msg_id         bigint unique not null,
    content        text,
    pic_url        varchar(255),
    media_id       bigint,
    thumb_media_id bigint,
    msg_data_id    varchar(128),
    idx            varchar(128),
    create_time    timestamp     not null
);
comment on table accept_msg_record is '接受消息记录';
comment on column accept_msg_record.to_user_name is '开发者微信号';
comment on column accept_msg_record.from_user_name is '发送方账号（一个OpenID）';
comment on column accept_msg_record.msg_type is '消息类型';
comment on column accept_msg_record.msg_id is '消息id';
comment on column accept_msg_record.content is '文本消息内容';
comment on column accept_msg_record.pic_url is '图片链接（由系统生成）';
comment on column accept_msg_record.media_id is '消息媒体id，可以调用获取临时素材接口拉取数据';
comment on column accept_msg_record.thumb_media_id is '消息缩略图的媒体id，可以调用多媒体文件下载接口拉取数据';
comment on column accept_msg_record.msg_data_id is '消息的数据ID（消息如果来自文章时才有）';
comment on column accept_msg_record.msg_data_id is '多图文时第几篇文章，从1开始（消息如果来自文章时才有）';



create table auto_answer_record
(
    id            bigserial primary key,
    qa_id         int                                 not null,
    msg_record_id bigint                              not null,
    create_time   timestamp default CURRENT_TIMESTAMP not null,
    foreign key (qa_id) references q_and_a (id),
    foreign key (msg_record_id) references accept_msg_record (id)
);
comment on table auto_answer_record is '自动回答记录';
-- 微信公众号表 end

