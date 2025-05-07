package com.pig4cloud.plugin.impl.postgresql;

import com.alibaba.nacos.plugin.datasource.mapper.ConfigMigrateMapper;
import com.pig4cloud.plugin.constants.DataSourceConstant;

/**
 * @author mrdaios
 */
public class ConfigMigrateMapperByPostgresql extends PostgresqlAbstractMapper implements ConfigMigrateMapper {

	@Override
	public String getDataSource() {
		return DataSourceConstant.POSTGRESQL;
	}

}
