alter table url_security_code_validator
    add create_time datetime not null comment '创建时间';

alter table url_security_code_validator
    add modify_time datetime not null comment '更新时间';

alter table url_security_code_validator
    add delete_flag boolean not null comment '删除标记';