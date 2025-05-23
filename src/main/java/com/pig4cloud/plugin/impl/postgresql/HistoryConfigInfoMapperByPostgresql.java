package com.pig4cloud.plugin.impl.postgresql;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.alibaba.nacos.plugin.datasource.constants.FieldConstant;
import com.alibaba.nacos.plugin.datasource.constants.TableConstant;
import com.alibaba.nacos.plugin.datasource.mapper.HistoryConfigInfoMapper;
import com.alibaba.nacos.plugin.datasource.model.MapperContext;
import com.alibaba.nacos.plugin.datasource.model.MapperResult;
import com.pig4cloud.plugin.constants.DataSourceConstant;

public class HistoryConfigInfoMapperByPostgresql extends PostgresqlAbstractMapper implements HistoryConfigInfoMapper {

	public MapperResult removeConfigHistory(MapperContext context) {
		String sql = "DELETE FROM his_config_info WHERE gmt_modified < ? LIMIT ?";
		return new MapperResult(sql, CollectionUtils.list(context.getWhereParameter(FieldConstant.START_TIME),
				context.getWhereParameter(FieldConstant.LIMIT_SIZE)));
	}

	public MapperResult pageFindConfigHistoryFetchRows(MapperContext context) {
		String sql = "SELECT nid,data_id,group_id,tenant_id,app_name,src_ip,src_user,op_type,ext_info,publish_type,gray_name,gmt_create,gmt_modified "
				+ "FROM his_config_info WHERE data_id = ? AND group_id = ? AND tenant_id = ? ORDER BY nid DESC "
				+ "LIMIT " + context.getPageSize() + " OFFSET " + context.getStartRow();
		return new MapperResult(sql, CollectionUtils.list(new Object[] { context.getWhereParameter("dataId"),
				context.getWhereParameter("groupId"), context.getWhereParameter("tenantId") }));
	}

	@Override
	public String getTableName() {
		return TableConstant.HIS_CONFIG_INFO;
	}

	@Override
	public String getDataSource() {
		return DataSourceConstant.POSTGRESQL;
	}

}
