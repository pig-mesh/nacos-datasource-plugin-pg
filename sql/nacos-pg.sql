CREATE TABLE config_info (
    id SERIAL PRIMARY KEY,
    data_id varchar(255) NOT NULL,
    group_id varchar(128) DEFAULT NULL,
    content text NOT NULL,
    md5 varchar(32) DEFAULT NULL,
    gmt_create timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    gmt_modified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    src_user text,
    src_ip varchar(50) DEFAULT NULL,
    app_name varchar(128) DEFAULT NULL,
    tenant_id varchar(128) DEFAULT '',
    c_desc varchar(256) DEFAULT NULL,
    c_use varchar(64) DEFAULT NULL,
    effect varchar(64) DEFAULT NULL,
    type varchar(64) DEFAULT NULL,
    c_schema text,
    encrypted_data_key varchar(1024) DEFAULT ''
);

COMMENT ON TABLE config_info IS 'config_info';
COMMENT ON COLUMN config_info.id IS 'id';
COMMENT ON COLUMN config_info.data_id IS 'data_id';
COMMENT ON COLUMN config_info.content IS 'content';
COMMENT ON COLUMN config_info.md5 IS 'md5';
COMMENT ON COLUMN config_info.gmt_create IS '创建时间';
COMMENT ON COLUMN config_info.gmt_modified IS '修改时间';
COMMENT ON COLUMN config_info.src_user IS 'source user';
COMMENT ON COLUMN config_info.src_ip IS 'source ip';
COMMENT ON COLUMN config_info.tenant_id IS '租户字段';
COMMENT ON COLUMN config_info.encrypted_data_key IS '密钥';

CREATE UNIQUE INDEX IF NOT EXISTS uk_configinfo_datagrouptenant ON config_info (data_id, group_id, tenant_id);

CREATE TABLE config_info_gray (
    id SERIAL PRIMARY KEY,
    data_id varchar(255) NOT NULL,
    group_id varchar(128) NOT NULL,
    content text NOT NULL,
    md5 varchar(32) DEFAULT NULL,
    src_user text,
    src_ip varchar(100) DEFAULT NULL,
    gmt_create timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    gmt_modified timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    app_name varchar(128) DEFAULT NULL,
    tenant_id varchar(128) DEFAULT '',
    gray_name varchar(128) NOT NULL,
    gray_rule text NOT NULL,
    encrypted_data_key varchar(256) DEFAULT ''
);

COMMENT ON TABLE config_info_gray IS 'config_info_gray';

CREATE UNIQUE INDEX IF NOT EXISTS uk_configinfogray_datagrouptenantgray ON config_info_gray (data_id, group_id, tenant_id, gray_name);
CREATE INDEX IF NOT EXISTS idx_dataid_gmt_modified ON config_info_gray (data_id, gmt_modified);
CREATE INDEX IF NOT EXISTS idx_gmt_modified ON config_info_gray (gmt_modified);

CREATE TABLE config_info_beta (
    id SERIAL PRIMARY KEY,
    data_id varchar(255) NOT NULL,
    group_id varchar(128) NOT NULL,
    app_name varchar(128) DEFAULT NULL,
    content text NOT NULL,
    beta_ips varchar(1024) DEFAULT NULL,
    md5 varchar(32) DEFAULT NULL,
    gmt_create timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    gmt_modified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    src_user text,
    src_ip varchar(50) DEFAULT NULL,
    tenant_id varchar(128) DEFAULT '',
    encrypted_data_key varchar(1024) DEFAULT ''
);

COMMENT ON TABLE config_info_beta IS 'config_info_beta';

CREATE UNIQUE INDEX IF NOT EXISTS uk_configinfobeta_datagrouptenant ON config_info_beta (data_id, group_id, tenant_id);

CREATE TABLE config_info_tag (
    id SERIAL PRIMARY KEY,
    data_id varchar(255) NOT NULL,
    group_id varchar(128) NOT NULL,
    tenant_id varchar(128) DEFAULT '',
    tag_id varchar(128) NOT NULL,
    app_name varchar(128) DEFAULT NULL,
    content text NOT NULL,
    md5 varchar(32) DEFAULT NULL,
    gmt_create timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    gmt_modified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    src_user text,
    src_ip varchar(50) DEFAULT NULL
);

COMMENT ON TABLE config_info_tag IS 'config_info_tag';

CREATE UNIQUE INDEX IF NOT EXISTS uk_configinfotag_datagrouptenanttag ON config_info_tag (data_id, group_id, tenant_id, tag_id);

CREATE TABLE config_tags_relation (
    id bigint NOT NULL,
    tag_name varchar(128) NOT NULL,
    tag_type varchar(64) DEFAULT NULL,
    data_id varchar(255) NOT NULL,
    group_id varchar(128) NOT NULL,
    tenant_id varchar(128) DEFAULT '',
    nid SERIAL PRIMARY KEY
);

COMMENT ON TABLE config_tags_relation IS 'config_tag_relation';

CREATE UNIQUE INDEX IF NOT EXISTS uk_configtagrelation_configidtag ON config_tags_relation (id, tag_name, tag_type);
CREATE INDEX IF NOT EXISTS idx_tenant_id ON config_tags_relation (tenant_id);

CREATE TABLE group_capacity (
    id SERIAL PRIMARY KEY,
    group_id varchar(128) NOT NULL DEFAULT '',
    quota int NOT NULL DEFAULT '0',
    usage int NOT NULL DEFAULT '0',
    max_size int NOT NULL DEFAULT '0',
    max_aggr_count int NOT NULL DEFAULT '0',
    max_aggr_size int NOT NULL DEFAULT '0',
    max_history_count int NOT NULL DEFAULT '0',
    gmt_create timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    gmt_modified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE group_capacity IS '集群、各Group容量信息表';
COMMENT ON COLUMN group_capacity.id IS '主键ID';
COMMENT ON COLUMN group_capacity.group_id IS 'Group ID，空字符表示整个集群';
COMMENT ON COLUMN group_capacity.quota IS '配额，0表示使用默认值';
COMMENT ON COLUMN group_capacity.usage IS '使用量';
COMMENT ON COLUMN group_capacity.max_size IS '单个配置大小上限，单位为字节，0表示使用默认值';
COMMENT ON COLUMN group_capacity.max_aggr_count IS '聚合子配置最大个数，，0表示使用默认值';
COMMENT ON COLUMN group_capacity.max_aggr_size IS '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值';
COMMENT ON COLUMN group_capacity.max_history_count IS '最大变更历史数量';

CREATE UNIQUE INDEX IF NOT EXISTS uk_group_id ON group_capacity (group_id);

CREATE TABLE his_config_info (
    id bigint NOT NULL,
    nid SERIAL PRIMARY KEY,
    data_id varchar(255) NOT NULL,
    group_id varchar(128) NOT NULL,
    app_name varchar(128) DEFAULT NULL,
    content text NOT NULL,
    md5 varchar(32) DEFAULT NULL,
    gmt_create timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    gmt_modified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    src_user text,
    src_ip varchar(50) DEFAULT NULL,
    op_type char(10) DEFAULT NULL,
    tenant_id varchar(128) DEFAULT '',
    encrypted_data_key varchar(1024) DEFAULT '',
    publish_type varchar(50) DEFAULT 'formal',
    gray_name varchar(50) DEFAULT NULL,
    ext_info text DEFAULT NULL
);

COMMENT ON TABLE his_config_info IS '多租户改造';

CREATE INDEX IF NOT EXISTS idx_gmt_create ON his_config_info (gmt_create);
CREATE INDEX IF NOT EXISTS idx_gmt_modified ON his_config_info (gmt_modified);
CREATE INDEX IF NOT EXISTS idx_did ON his_config_info (data_id);

CREATE TABLE tenant_capacity (
    id SERIAL PRIMARY KEY,
    tenant_id varchar(128) NOT NULL DEFAULT '',
    quota int NOT NULL DEFAULT '0',
    usage int NOT NULL DEFAULT '0',
    max_size int NOT NULL DEFAULT '0',
    max_aggr_count int NOT NULL DEFAULT '0',
    max_aggr_size int NOT NULL DEFAULT '0',
    max_history_count int NOT NULL DEFAULT '0',
    gmt_create timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    gmt_modified timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE tenant_capacity IS '租户容量信息表';
COMMENT ON COLUMN tenant_capacity.id IS '主键ID';
COMMENT ON COLUMN tenant_capacity.tenant_id IS 'Tenant ID';
COMMENT ON COLUMN tenant_capacity.quota IS '配额，0表示使用默认值';
COMMENT ON COLUMN tenant_capacity.usage IS '使用量';
COMMENT ON COLUMN tenant_capacity.max_size IS '单个配置大小上限，单位为字节，0表示使用默认值';
COMMENT ON COLUMN tenant_capacity.max_aggr_count IS '聚合子配置最大个数';
COMMENT ON COLUMN tenant_capacity.max_aggr_size IS '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值';
COMMENT ON COLUMN tenant_capacity.max_history_count IS '最大变更历史数量';

CREATE UNIQUE INDEX IF NOT EXISTS uk_tenant_id ON tenant_capacity (tenant_id);

CREATE TABLE tenant_info (
    id SERIAL PRIMARY KEY,
    kp varchar(128) NOT NULL,
    tenant_id varchar(128) DEFAULT '',
    tenant_name varchar(128) DEFAULT '',
    tenant_desc varchar(256) DEFAULT NULL,
    create_source varchar(32) DEFAULT NULL,
    gmt_create bigint NOT NULL,
    gmt_modified bigint NOT NULL
);

COMMENT ON TABLE tenant_info IS 'tenant_info';

CREATE UNIQUE INDEX IF NOT EXISTS uk_tenant_info_kptenantid ON tenant_info (kp, tenant_id);
CREATE INDEX IF NOT EXISTS idx_tenant_id ON tenant_info (tenant_id);

CREATE TABLE users (
    username varchar(50) PRIMARY KEY,
    password varchar(500) NOT NULL,
    enabled boolean NOT NULL
);

COMMENT ON TABLE users IS 'users';
COMMENT ON COLUMN users.username IS 'username';
COMMENT ON COLUMN users.password IS 'password';
COMMENT ON COLUMN users.enabled IS 'enabled';

CREATE TABLE roles (
    username varchar(50) NOT NULL,
    role varchar(50) NOT NULL
);

COMMENT ON TABLE roles IS 'roles';
COMMENT ON COLUMN roles.username IS 'username';
COMMENT ON COLUMN roles.role IS 'role';

CREATE UNIQUE INDEX IF NOT EXISTS idx_user_role ON roles (username, role);

CREATE TABLE permissions (
    role varchar(50) NOT NULL,
    resource varchar(128) NOT NULL,
    action varchar(8) NOT NULL
);

COMMENT ON TABLE permissions IS 'permissions';
COMMENT ON COLUMN permissions.role IS 'role';
COMMENT ON COLUMN permissions.resource IS 'resource';
COMMENT ON COLUMN permissions.action IS 'action';

CREATE UNIQUE INDEX IF NOT EXISTS uk_role_permission ON permissions (role, resource, action);
