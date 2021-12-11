create table url
(
    id          bigint auto_increment comment '编号'
        primary key,
    url         varchar(512)  not null comment '地址',
    method      varchar(16)   not null comment '请求方式',
    description varchar(1024) null comment '描述',
    create_time datetime      not null comment '创建时间',
    modify_time datetime      not null comment '更新时间',
    delete_flag tinyint(1)    not null comment '删除标记'
)
    comment '存储了项目中所有api的地址';