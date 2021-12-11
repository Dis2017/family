create table user_role
(
    id          bigint auto_increment comment '映射编号'
        primary key,
    user_id     bigint     not null comment '角色编号',
    role_id     bigint     not null comment '角色编号',
    create_time datetime   not null comment '创建时间',
    modify_time datetime   not null comment '更新时间',
    delete_flag tinyint(1) not null comment '删除标记'
)
    comment '用户和角色的映射表';