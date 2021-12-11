create table roles
(
    id bigint auto_increment comment '角色编号'
        primary key,
    role varchar(128) not null comment '角色名',
    description varchar(1024) not null comment '描述',
    create_time datetime not null comment '创建时间',
    modify_time datetime not null comment '更新时间',
    delete_flag tinyint(1) not null comment '删除标记',
    constraint role_role_uindex
        unique (role)
)
    comment '角色表';