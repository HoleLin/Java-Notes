DROP TABLE
    IF
        EXISTS `dicom_tag`;
CREATE TABLE `dicom_tag` (
                             `id` INT auto_increment NOT NULL,
                             `flag` VARCHAR ( 100 ) NOT NULL COMMENT 'Tag标识',
                             `coordinates` CHAR ( 11 ) NOT NULL COMMENT 'Tag标准坐标',
                             `tag_value` int NOT NULL COMMENT '程序对应的Tag值',
                             `need_check` TINYINT NOT NULL DEFAULT 0 COMMENT '是否需要校验,0-无需校验;1-需要校验',
                             `value_type` VARCHAR ( 50 ) NOT NULL COMMENT 'Tag值的数据类型',
                             `description` text NULL COMMENT 'Tag描述',
                             PRIMARY KEY ( `id` ),
                             UNIQUE KEY ( `flag` )
);