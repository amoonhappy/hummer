package org.hummer.codegen.controller;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import org.apache.commons.io.IOUtils;
import org.hummer.codegen.service.SysGeneratorService;
import org.hummer.codegen.util.JsonResponse;
import org.hummer.codegen.util.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 代码生成器
 *
 * @ClassName: SysGeneratorController
 * @Description: TODO
 * @author: Jason
 * @date: 2017年12月25日 下午9:54:43
 */
@Controller
@Api(tags = "Hummer Framework - CodeGeneration类")
@RequestMapping("/sys/generator")
public class SysGeneratorController {
    private SysGeneratorService sysGeneratorService;

    @Autowired(required = true)
    public void setSysGeneratorService(SysGeneratorService sysGeneratorService) {
        this.sysGeneratorService = sysGeneratorService;
    }

    @RequestMapping("/list.html")
    public String index() {
        return "system/generator.html";
    }

    /**
     * 列表
     */
    @ResponseBody
    @RequestMapping("/list")
    public JsonResponse list(String tableName, Integer page, Integer limit) {
        Map<String, Object> map = new HashMap<>();
        map.put("tableName", tableName);
        map.put("offset", (page - 1) * limit);
        map.put("limit", limit);

        //查询列表数据
        List<Map<String, Object>> list = sysGeneratorService.queryList(map);
        int total = sysGeneratorService.queryTotal(map);

        PageUtils pageUtil = new PageUtils(list, total, limit, page);

        return JsonResponse.ok().put("page", pageUtil);
    }

    /**
     * 生成代码
     */
    @RequestMapping("/code")
    public void code(String tables, HttpServletResponse response) throws IOException {
        String[] tableNames = new String[]{};
        tableNames = JSON.parseArray(tables).toArray(tableNames);

        byte[] data = sysGeneratorService.generatorCode(tableNames);

        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"generated_cod.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");

        IOUtils.write(data, response.getOutputStream());
    }
}
