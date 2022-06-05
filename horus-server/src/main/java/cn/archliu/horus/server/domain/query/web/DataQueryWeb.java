package cn.archliu.horus.server.domain.query.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.archliu.common.response.ComRes;
import cn.archliu.common.response.sub.ResData;
import cn.archliu.horus.common.exception.sub.ParamErrorException;
import cn.archliu.horus.common.util.SqlCheckUtils;
import cn.archliu.horus.server.domain.query.service.DataQueryService;
import cn.archliu.horus.server.domain.query.web.dto.TaosQueryDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Validated
@Api(tags = { "数据查询接口" })
@RestController
@RequestMapping("/api/horus/query")
public class DataQueryWeb {

    @Autowired
    private DataQueryService dataQueryService;

    @ApiOperation("TDEngine 数据查询")
    @PostMapping("/taosQuery")
    public ComRes<ResData<Object>> taosQuery(@Valid @RequestBody TaosQueryDTO taosQueryDTO) {
        // 查询 sql 语法检查
        if (!SqlCheckUtils.queryCheck(taosQueryDTO.getQuerySql())) {
            throw ParamErrorException.throwE("sql语法错误/仅支持查询语句！");
        }
        return ComRes.success(new ResData<>(dataQueryService.taosQuery(taosQueryDTO)));
    }

    @ApiOperation("mysql 数据查询")
    @PostMapping("/mysqlQuery")
    public ComRes<ResData<Object>> mysqlQuery(@Valid @RequestBody TaosQueryDTO taosQueryDTO) {
        // 查询 sql 语法检查
        if (!SqlCheckUtils.queryCheck(taosQueryDTO.getQuerySql())) {
            throw ParamErrorException.throwE("sql语法错误/仅支持查询语句！");
        }
        return ComRes.success(new ResData<>(dataQueryService.mysqlQuery(taosQueryDTO)));
    }

}
