package com.pig4cloud.plugin.impl.postgresql;

import com.alibaba.nacos.plugin.datasource.constants.TableConstant;
import com.alibaba.nacos.plugin.datasource.mapper.AbstractMapper;
import com.alibaba.nacos.plugin.datasource.mapper.TenantInfoMapper;
import com.pig4cloud.plugin.constants.DataSourceConstant;

public class TenantInfoMapperByPostgresql extends AbstractMapper implements TenantInfoMapper {
    @Override
    public String getTableName() {
        return TableConstant.TENANT_INFO;
    }

    @Override
    public String getDataSource() {
        return DataSourceConstant.POSTGRESQL;
    }
}
