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
    id          bigserial                            not null
        constraint user_pkey primary key,
    name        varchar(30)                          not null,
    birthday    date,
    password    varchar(128)                         not null,
    gender      smallint,
    address     varchar(50),
    email       varchar(128),
    update_time timestamp                            not null,
    create_time timestamp default CURRENT_TIMESTAMP  not null,
    role        user_role default 'Admin'::user_role not null
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
    update_time timestamp                           not null,
    create_time timestamp default CURRENT_TIMESTAMP not null
);
comment on table q_and_a is '问题与答案';
comment on column q_and_a.question is '问题';
comment on column q_and_a.answer is '答案';
create trigger q_and_a_update_timestamp
    before insert or update
    on q_and_a
    for each row
execute procedure update_timestamp_column();


create table qa_tag
(
    id          serial primary key,
    tag_name    varchar(64) unique                  not null,
    description text,
    update_time timestamp                           not null,
    create_time timestamp default CURRENT_TIMESTAMP not null
);
comment on table qa_tag is '问题标签';
create trigger qa_tag_update_timestamp
    before insert or update
    on qa_tag
    for each row
execute procedure update_timestamp_column();

create table qa_tag_relation
(
    qa_id       int                                 not null
        constraint qa_tag_relation_qa references q_and_a,
    tag_id      int                                 not null
        constraint qa_tag_relation_tag references qa_tag,
    constraint qa_and_tag unique (qa_id, tag_id),
    create_time timestamp default current_timestamp not null
);

comment on table qa_tag_relation is '问答标签关联表';
comment on constraint qa_tag_relation_qa on qa_tag_relation is '问答表外键';
comment on constraint qa_tag_relation_tag on qa_tag_relation is '标签表外键';



create table accept_msg_record
(
    id             bigserial primary key,
    to_user_name   varchar(200) not null,
    from_user_name varchar(200) not null,
    msg_type       varchar(32)  not null,
    event          varchar(32),
    msg_id         bigint unique,
    content        text,
    pic_url        varchar(255),
    media_id       bigint,
    thumb_media_id bigint,
    msg_data_id    varchar(128),
    idx            varchar(128),
    create_time    timestamp    not null
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


-- 文件 start
create table file_path
(
    id          serial primary key,
    parent_id   int references file_path (id) on delete cascade,
    path        varchar(128)                        not null,
    create_time timestamp default current_timestamp not null,
    constraint file_key unique (parent_id, path)
);
comment on column file_path.parent_id is '为空表示根目录，对象存储里用作桶';



create table file_object
(
    id          bigserial primary key,
    path_id     int references file_path (id)       not null,
    name        varchar(128)                        not null,
    size        bigint                              not null,
    mime_type   varchar(64)                         not null,
    md5         varchar(128)                        not null,
    metadata    jsonb,
    update_time timestamp                           not null,
    create_time timestamp default current_timestamp not null
);

create trigger file_object_update_timestamp
    before insert or update
    on file_object
    for each row
execute procedure update_timestamp_column();

-- 文件 end
