package com.pig4cloud.plugin.impl.postgresql;

import com.alibaba.nacos.plugin.datasource.mapper.TenantInfoMapper;
import com.pig4cloud.plugin.constants.DataSourceConstant;

public class TenantInfoMapperByPostgresql extends com.pig4cloud.plugin.impl.postgresql.PostgresqlAbstractMapper implements TenantInfoMapper {

	@Override
	public String getDataSource() {
		return DataSourceConstant.POSTGRESQL;
	}

}
