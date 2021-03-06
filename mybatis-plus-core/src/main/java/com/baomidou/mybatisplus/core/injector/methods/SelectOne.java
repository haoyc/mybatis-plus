/*
 * Copyright (c) 2011-2020, hubin (jobob@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.baomidou.mybatisplus.core.injector.methods;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;

/**
 * <p>
 * 根据 ID 删除
 * </p>
 *
 * @author hubin
 * @since 2018-04-06
 */
public class SelectOne extends AbstractMethod {

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        SqlMethod sqlMethod = SqlMethod.SELECT_ONE;
        DbType dbType = getGlobalConfig().getDbConfig().getDbType();
        String select = this.sqlSelectColumns(tableInfo, false);
        if (dbType == DbType.SQL_SERVER || dbType == DbType.SQL_SERVER2005) {
            select = "TOP 1 " + select;
        }
        String sql = String.format(sqlMethod.getSql(), select, tableInfo.getTableName(),
            this.sqlWhereEntityWrapper(tableInfo) + dbType.getLimit(1));
        if (dbType == DbType.ORACLE) {
            sql = String.format("SELECT * FROM (%s) ROWNUM <= 1");
        }
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        return this.addSelectMappedStatement(mapperClass, sqlMethod.getMethod(), sqlSource, modelClass, tableInfo);
    }
}
