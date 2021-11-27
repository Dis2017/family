create table user
(
    id          bigint auto_increment comment '用户编号'
        primary key,
    name        varchar(8)  not null comment '用户名称',
    password    varchar(32) not null comment '密码',
    sex         tinyint(1)  null comment '性别',
    birthday    date        null comment '出生日期',
    area        smallint    null comment '地区',
    email       text        null comment '电子邮箱',
    phone       varchar(11) null comment '手机号码',
    family_id   bigint      null comment '家庭编号',
    create_time datetime    not null comment '创建时间',
    modify_time datetime    not null comment '更新时间',
    delete_flag tinyint(1)  not null comment '删除标记'
)
    comment '用户表';

