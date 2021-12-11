create table url_role
(
    id          bigint auto_increment comment '编号'
        primary key,
    url_id      bigint     not null comment 'URL编号',
    role_id     bigint     not null comment '角色编号',
    create_time datetime   not null comment '创建时间',
    modify_time datetime   not null comment '更新时间',
    delete_flag tinyint(1) not null comment '删除标记'
)
    comment 'api地址和角色的映射
决定什么角色可以访问什么api';

