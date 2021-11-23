alter table user
    add create_time datetime not null comment '创建时间';

alter table user
    add modify_time datetime not null comment '更新时间';