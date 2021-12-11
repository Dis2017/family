alter table user_role
    add create_time datetime not null comment '创建时间';

alter table user_role
    add modify_time datetime not null comment '更新时间';

alter table user_role
    add delete_flag boolean not null comment '删除标记';