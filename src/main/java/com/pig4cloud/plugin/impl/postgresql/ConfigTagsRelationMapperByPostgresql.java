package com.pig4cloud.plugin.impl.postgresql;

import com.alibaba.nacos.common.utils.ArrayUtils;
import com.alibaba.nacos.common.utils.StringUtils;
import com.alibaba.nacos.plugin.datasource.constants.FieldConstant;
import com.alibaba.nacos.plugin.datasource.mapper.ConfigTagsRelationMapper;
import com.alibaba.nacos.plugin.datasource.mapper.ext.WhereBuilder;
import com.alibaba.nacos.plugin.datasource.model.MapperContext;
import com.alibaba.nacos.plugin.datasource.model.MapperResult;
import com.pig4cloud.plugin.constants.DataSourceConstant;

import java.util.ArrayList;
import java.util.List;

public class ConfigTagsRelationMapperByPostgresql extends PostgresqlAbstractMapper implements ConfigTagsRelationMapper {

	@Override
	public MapperResult findConfigInfo4PageFetchRows(MapperContext context) {
		final String appName = (String) context.getWhereParameter(FieldConstant.APP_NAME);
		final String dataId = (String) context.getWhereParameter(FieldConstant.DATA_ID);
		final String group = (String) context.getWhereParameter(FieldConstant.GROUP_ID);
		final String content = (String) context.getWhereParameter(FieldConstant.CONTENT);
		final String tenantId = (String) context.getWhereParameter(FieldConstant.TENANT_ID);
		final String[] tagArr = (String[]) context.getWhereParameter(FieldConstant.TAG_ARR);

		List<Object> paramList = new ArrayList<>();
		StringBuilder where = new StringBuilder(" WHERE ");
		final String baseSql = "SELECT a.id,a.data_id,a.group_id,a.tenant_id,a.app_name,a.content FROM config_info  a LEFT JOIN "
				+ "config_tags_relation b ON a.id=b.id";

		where.append(" a.tenant_id=? ");
		paramList.add(tenantId);

		if (StringUtils.isNotBlank(dataId)) {
			where.append(" AND a.data_id=? ");
			paramList.add(dataId);
		}
		if (StringUtils.isNotBlank(group)) {
			where.append(" AND a.group_id=? ");
			paramList.add(group);
		}
		if (StringUtils.isNotBlank(appName)) {
			where.append(" AND a.app_name=? ");
			paramList.add(appName);
		}
		if (!StringUtils.isBlank(content)) {
			where.append(" AND a.content LIKE ? ");
			paramList.add(content);
		}
		where.append(" AND b.tag_name IN (");
		for (int i = 0; i < tagArr.length; i++) {
			if (i != 0) {
				where.append(", ");
			}
			where.append('?');
			paramList.add(tagArr[i]);
		}
		where.append(") ");
		String sql = baseSql + where + " OFFSET " + context.getStartRow() + " ROWS FETCH NEXT " + context.getPageSize()
				+ " ROWS ONLY";
		return new MapperResult(sql, paramList);
	}

	@Override
	public MapperResult findConfigInfoLike4PageFetchRows(MapperContext context) {
		final String appName = (String) context.getWhereParameter(FieldConstant.APP_NAME);
		final String dataId = (String) context.getWhereParameter(FieldConstant.DATA_ID);
		final String group = (String) context.getWhereParameter(FieldConstant.GROUP_ID);
		final String content = (String) context.getWhereParameter(FieldConstant.CONTENT);
		final String tenantId = (String) context.getWhereParameter(FieldConstant.TENANT_ID);
		final String[] tagArr = (String[]) context.getWhereParameter(FieldConstant.TAG_ARR);
		final String[] types = (String[]) context.getWhereParameter(FieldConstant.TYPE);

		WhereBuilder where = new WhereBuilder(
				"SELECT a.ID,a.data_id,a.group_id,a.tenant_id,a.app_name,a.content,a.type FROM config_info a LEFT JOIN "
						+ "config_tags_relation b ON a.id=b.id");

		where.like("a.tenant_id", tenantId);

		if (StringUtils.isNotBlank(dataId)) {
			where.and().like("a.data_id", dataId);
		}
		if (StringUtils.isNotBlank(group)) {
			where.and().like("a.group_id", group);
		}
		if (StringUtils.isNotBlank(appName)) {
			where.and().eq("a.app_name", appName);
		}
		if (StringUtils.isNotBlank(content)) {
			where.and().like("a.content", content);
		}
		if (!ArrayUtils.isEmpty(tagArr)) {
			where.and().startParentheses();
			for (int i = 0; i < tagArr.length; i++) {
				if (i != 0) {
					where.or();
				}
				where.like("b.tag_name", tagArr[i]);
			}
			where.endParentheses();
		}
		if (!ArrayUtils.isEmpty(types)) {
			where.and().in("a.type", types);
		}

		where.offset(context.getStartRow(), context.getPageSize());
		return where.build();
	}

	@Override
	public String getDataSource() {
		return DataSourceConstant.POSTGRESQL;
	}

}
