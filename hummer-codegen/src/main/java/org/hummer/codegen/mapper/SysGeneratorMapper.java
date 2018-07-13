package org.hummer.codegen.mapper;


import java.util.List;
import java.util.Map;

/**
 * 代码生成器
 *
 * @ClassName: SysGeneratorDao
 * @Description: TODO
 * @author: Jeff Zhou
 * @date: 2018年7月13日 下午9:54:43
 */
public interface SysGeneratorMapper {

    List<Map<String, Object>> queryList(Map<String, Object> map);

    int queryTotal(Map<String, Object> map);

    Map<String, String> queryTable(String tableName);

    List<Map<String, String>> queryColumns(String tableName);
}
