package com.pig4cloud.plugin.impl.postgresql;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.alibaba.nacos.common.utils.NamespaceUtil;
import com.alibaba.nacos.plugin.datasource.constants.FieldConstant;
import com.alibaba.nacos.plugin.datasource.mapper.GroupCapacityMapper;
import com.alibaba.nacos.plugin.datasource.model.MapperContext;
import com.alibaba.nacos.plugin.datasource.model.MapperResult;
import com.pig4cloud.plugin.constants.DataSourceConstant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GroupCapacityMapperByPostgresql extends PostgresqlAbstractMapper implements GroupCapacityMapper {

	@Override
	public MapperResult select(MapperContext context) {
		String sql = "SELECT id, quota, `usage`, max_size, max_aggr_count, max_aggr_size, group_id FROM group_capacity WHERE group_id = ?";
		return new MapperResult(sql, Collections.singletonList(context.getWhereParameter("groupId")));
	}

	@Override
	public MapperResult insertIntoSelect(MapperContext context) {
		List<Object> paramList = new ArrayList();
		paramList.add(context.getUpdateParameter("groupId"));
		paramList.add(context.getUpdateParameter("quota"));
		paramList.add(context.getUpdateParameter("maxSize"));
		paramList.add(context.getUpdateParameter("maxAggrCount"));
		paramList.add(context.getUpdateParameter("maxAggrSize"));
		paramList.add(context.getUpdateParameter("gmtCreate"));
		paramList.add(context.getUpdateParameter("gmtModified"));
		String sql = "INSERT INTO group_capacity (group_id, quota, `usage`, max_size, max_aggr_count, max_aggr_size,gmt_create, gmt_modified) SELECT ?, ?, count(*), ?, ?, ?, ?, ? FROM config_info";
		return new MapperResult(sql, paramList);
	}

	@Override
	public MapperResult insertIntoSelectByWhere(MapperContext context) {
		String sql = "INSERT INTO group_capacity (group_id, quota, `usage`, max_size, max_aggr_count, max_aggr_size, gmt_create, gmt_modified) SELECT ?, ?, count(*), ?, ?, ?, ?, ? FROM config_info WHERE group_id=? AND tenant_id = '"
				+ NamespaceUtil.getNamespaceDefaultId() + "'";
		List<Object> paramList = new ArrayList();
		paramList.add(context.getUpdateParameter("groupId"));
		paramList.add(context.getUpdateParameter("quota"));
		paramList.add(context.getUpdateParameter("maxSize"));
		paramList.add(context.getUpdateParameter("maxAggrCount"));
		paramList.add(context.getUpdateParameter("maxAggrSize"));
		paramList.add(context.getUpdateParameter("gmtCreate"));
		paramList.add(context.getUpdateParameter("gmtModified"));
		paramList.add(context.getWhereParameter("groupId"));
		return new MapperResult(sql, paramList);
	}

	@Override
	public MapperResult incrementUsageByWhereQuotaEqualZero(MapperContext context) {
		return new MapperResult(
				"UPDATE group_capacity SET `usage` = `usage` + 1, gmt_modified = ? WHERE group_id = ? AND `usage` < ? AND quota = 0",
				CollectionUtils.list(new Object[] { context.getUpdateParameter("gmtModified"),
						context.getWhereParameter("groupId"), context.getWhereParameter("usage") }));
	}

	@Override
	public MapperResult incrementUsageByWhereQuotaNotEqualZero(MapperContext context) {
		return new MapperResult(
				"UPDATE group_capacity SET `usage` = `usage` + 1, gmt_modified = ? WHERE group_id = ? AND `usage` < quota AND quota != 0",
				CollectionUtils.list(new Object[] { context.getUpdateParameter("gmtModified"),
						context.getWhereParameter("groupId") }));
	}

	@Override
	public MapperResult incrementUsageByWhere(MapperContext context) {
		return new MapperResult("UPDATE group_capacity SET `usage` = `usage` + 1, gmt_modified = ? WHERE group_id = ?",
				CollectionUtils.list(new Object[] { context.getUpdateParameter("gmtModified"),
						context.getWhereParameter("groupId") }));
	}

	@Override
	public MapperResult decrementUsageByWhere(MapperContext context) {
		return new MapperResult(
				"UPDATE group_capacity SET `usage` = `usage` - 1, gmt_modified = ? WHERE group_id = ? AND `usage` > 0",
				CollectionUtils.list(new Object[] { context.getUpdateParameter("gmtModified"),
						context.getWhereParameter("groupId") }));
	}

	@Override
	public MapperResult updateUsage(MapperContext context) {
		return new MapperResult(
				"UPDATE group_capacity SET `usage` = (SELECT count(*) FROM config_info), gmt_modified = ? WHERE group_id = ?",
				CollectionUtils.list(new Object[] { context.getUpdateParameter("gmtModified"),
						context.getWhereParameter("groupId") }));
	}

	@Override
	public MapperResult updateUsageByWhere(MapperContext context) {
		return new MapperResult(
				"UPDATE group_capacity SET `usage` = (SELECT count(*) FROM config_info WHERE group_id=? AND tenant_id = '"
						+ NamespaceUtil.getNamespaceDefaultId() + "'), gmt_modified = ? WHERE group_id= ?",
				CollectionUtils.list(new Object[] { context.getWhereParameter("groupId"),
						context.getUpdateParameter("gmtModified"), context.getWhereParameter("groupId") }));
	}

	@Override
	public MapperResult selectGroupInfoBySize(MapperContext context) {
		String sql = "SELECT id, group_id FROM group_capacity WHERE id > ? LIMIT ?";
		return new MapperResult(sql,
				CollectionUtils.list(context.getWhereParameter(FieldConstant.ID), context.getPageSize()));
	}

	@Override
	public String getDataSource() {
		return DataSourceConstant.POSTGRESQL;
	}

}
