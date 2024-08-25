package com.pig4cloud.plugin.impl.postgresql;

import com.alibaba.nacos.plugin.datasource.mapper.AbstractMapper;

import java.util.List;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/12/19 21:01
 */
public abstract class PostgresqlAbstractMapper extends AbstractMapper {

	@Override
	public String select(List<String> columns, List<String> where) {
		StringBuilder sql = new StringBuilder("SELECT ");

		for (int i = 0; i < columns.size(); i++) {
			sql.append(columns.get(i));
			if (i == columns.size() - 1) {
				sql.append(" ");
			}
			else {
				sql.append(",");
			}
		}
		sql.append("FROM ");
		sql.append(getTableName());
		sql.append(" ");

		if (where.size() == 0) {
			return sql.toString();
		}

		sql.append("WHERE ");
		for (int i = 0; i < where.size(); i++) {
			String column = where.get(i);

			// 租户列特殊处理 避免前端传空字符串是Oracle查询不到数据
			if ("tenant_id".equalsIgnoreCase(column)) {
				sql.append("(");
				sql.append(column).append(" = ").append("?");
				sql.append(" OR ");
				sql.append(column).append(" IS NULL ");
				sql.append(")");
			}
			else {
				sql.append(column).append(" = ").append("?");
			}

			if (i != where.size() - 1) {
				sql.append(" AND ");
			}
		}
		return sql.toString();
	}

	@Override
	public String update(List<String> columns, List<String> where) {
		StringBuilder sql = new StringBuilder();
		String method = "UPDATE ";
		sql.append(method);
		sql.append(getTableName()).append(" ").append("SET ");

		for (int i = 0; i < columns.size(); i++) {
			sql.append(columns.get(i)).append(" = ").append("?");
			if (i != columns.size() - 1) {
				sql.append(",");
			}
		}

		if (where.size() == 0) {
			return sql.toString();
		}

		sql.append(" WHERE ");

		for (int i = 0; i < where.size(); i++) {
			String column = where.get(i);
			if ("tenant_id".equalsIgnoreCase(column)) {
				sql.append("(");
				sql.append(column).append(" = ").append("?");
				sql.append(" OR ");
				sql.append(column).append(" IS NULL ");
				sql.append(")");
			}
			else {
				sql.append(column).append(" = ").append("?");
			}
			if (i != where.size() - 1) {
				sql.append(" AND ");
			}
		}
		return sql.toString();
	}

	@Override
	public String delete(List<String> params) {
		StringBuilder sql = new StringBuilder();
		String method = "DELETE ";
		sql.append(method).append("FROM ").append(getTableName()).append(" ").append("WHERE ");
		for (int i = 0; i < params.size(); i++) {
			String column = params.get(i);
			if ("tenant_id".equalsIgnoreCase(column)) {
				sql.append(" (");
				sql.append(column).append(" = ").append("?");
				sql.append(" OR ");
				sql.append(column).append(" IS NULL ");
				sql.append(")");
			}
			else {
				sql.append(column).append(" = ").append("?");
			}
			if (i != params.size() - 1) {
				sql.append(" AND ");
			}
		}

		return sql.toString();
	}

	@Override
	public String count(List<String> where) {
		StringBuilder sql = new StringBuilder();
		String method = "SELECT ";
		sql.append(method);
		sql.append("COUNT(*) FROM ");
		sql.append(getTableName());
		sql.append(" ");

		if (null == where || where.size() == 0) {
			return sql.toString();
		}

		sql.append("WHERE ");
		for (int i = 0; i < where.size(); i++) {
			String column = where.get(i);
			if ("tenant_id".equalsIgnoreCase(column)) {
				sql.append("(");
				sql.append(column).append(" = ").append("?");
				sql.append(" OR ");
				sql.append(column).append(" IS NULL ");
				sql.append(")");
			}
			else {
				sql.append(column).append(" = ").append("?");
			}
			if (i != where.size() - 1) {
				sql.append(" AND ");
			}
		}
		return sql.toString();
	}

	/**
	 * Get function by functionName.
	 * @param functionName functionName
	 * @return function
	 */
	@Override
	public String getFunction(String functionName) {
		return TrustedPgFunctionEnum.getFunctionByName(functionName);
	}

}
