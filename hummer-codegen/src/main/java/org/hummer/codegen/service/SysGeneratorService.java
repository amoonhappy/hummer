package org.hummer.codegen.service;


import java.util.List;
import java.util.Map;

/**
 * 代码生成器
 *
 * @ClassName: SysGeneratorService
 * @Description: TODO
 * @author: Jason
 * @date: 2017年12月25日 下午9:53:03
 */
public interface SysGeneratorService {

    List<Map<String, Object>> queryList(Map<String, Object> map);

    int queryTotal(Map<String, Object> map);

    Map<String, String> queryTable(String tableName);

    List<Map<String, String>> queryColumns(String tableName);

    /**
     * 生成代码
     */
    byte[] generatorCode(String[] tableNames);
}
