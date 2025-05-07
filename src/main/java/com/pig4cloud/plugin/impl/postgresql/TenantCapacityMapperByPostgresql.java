package com.pig4cloud.plugin.impl.postgresql;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.alibaba.nacos.plugin.datasource.constants.FieldConstant;
import com.alibaba.nacos.plugin.datasource.constants.TableConstant;
import com.alibaba.nacos.plugin.datasource.mapper.TenantCapacityMapper;
import com.alibaba.nacos.plugin.datasource.model.MapperContext;
import com.alibaba.nacos.plugin.datasource.model.MapperResult;
import com.pig4cloud.plugin.constants.DataSourceConstant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TenantCapacityMapperByPostgresql extends PostgresqlAbstractMapper implements TenantCapacityMapper {

	@Override
	public MapperResult select(MapperContext context) {
		String sql = "SELECT id, quota, `usage`, max_size, max_aggr_count, max_aggr_size, tenant_id FROM tenant_capacity WHERE tenant_id = ?";
		return new MapperResult(sql, Collections.singletonList(context.getWhereParameter("tenantId")));
	}

	@Override
	public MapperResult getCapacityList4CorrectUsage(MapperContext context) {
		String sql = "SELECT id, tenant_id FROM tenant_capacity WHERE id>? LIMIT ?";
		return new MapperResult(sql, CollectionUtils.list(context.getWhereParameter(FieldConstant.ID),
				context.getWhereParameter(FieldConstant.LIMIT_SIZE)));
	}

	@Override
	public MapperResult incrementUsageWithDefaultQuotaLimit(MapperContext context) {
		return new MapperResult(
				"UPDATE tenant_capacity SET `usage` = `usage` + 1, gmt_modified = ? WHERE tenant_id = ? AND `usage` < ? AND quota = 0",
				CollectionUtils.list(new Object[] { context.getUpdateParameter("gmtModified"),
						context.getWhereParameter("tenantId"), context.getWhereParameter("usage") }));
	}

	@Override
	public MapperResult incrementUsageWithQuotaLimit(MapperContext context) {
		return new MapperResult(
				"UPDATE tenant_capacity SET `usage` = `usage` + 1, gmt_modified = ? WHERE tenant_id = ? AND `usage` < quota AND quota != 0",
				CollectionUtils.list(new Object[] { context.getUpdateParameter("gmtModified"),
						context.getWhereParameter("tenantId") }));
	}

	@Override
	public MapperResult incrementUsage(MapperContext context) {
		return new MapperResult(
				"UPDATE tenant_capacity SET `usage` = `usage` + 1, gmt_modified = ? WHERE tenant_id = ?",
				CollectionUtils.list(new Object[] { context.getUpdateParameter("gmtModified"),
						context.getWhereParameter("tenantId") }));
	}

	@Override
	public MapperResult decrementUsage(MapperContext context) {
		return new MapperResult(
				"UPDATE tenant_capacity SET `usage` = `usage` - 1, gmt_modified = ? WHERE tenant_id = ? AND `usage` > 0",
				CollectionUtils.list(new Object[] { context.getUpdateParameter("gmtModified"),
						context.getWhereParameter("tenantId") }));
	}

	@Override
	public MapperResult correctUsage(MapperContext context) {
		return new MapperResult(
				"UPDATE tenant_capacity SET `usage` = (SELECT count(*) FROM config_info WHERE tenant_id = ?), gmt_modified = ? WHERE tenant_id = ?",
				CollectionUtils.list(new Object[] { context.getWhereParameter("tenantId"),
						context.getUpdateParameter("gmtModified"), context.getWhereParameter("tenantId") }));
	}

	@Override
	public MapperResult insertTenantCapacity(MapperContext context) {
		List<Object> paramList = new ArrayList();
		paramList.add(context.getUpdateParameter("tenantId"));
		paramList.add(context.getUpdateParameter("quota"));
		paramList.add(context.getUpdateParameter("maxSize"));
		paramList.add(context.getUpdateParameter("maxAggrCount"));
		paramList.add(context.getUpdateParameter("maxAggrSize"));
		paramList.add(context.getUpdateParameter("gmtCreate"));
		paramList.add(context.getUpdateParameter("gmtModified"));
		paramList.add(context.getWhereParameter("tenantId"));
		return new MapperResult(
				"INSERT INTO tenant_capacity (tenant_id, quota, `usage`, max_size, max_aggr_count, max_aggr_size, gmt_create, gmt_modified) SELECT ?, ?, count(*), ?, ?, ?, ?, ? FROM config_info WHERE tenant_id=?",
				paramList);
	}

	@Override
	public String getTableName() {
		return TableConstant.TENANT_CAPACITY;
	}

	@Override
	public String getDataSource() {
		return DataSourceConstant.POSTGRESQL;
	}

}
