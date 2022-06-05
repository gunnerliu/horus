package cn.archliu.horus.server.domain.query.service.impl;

import java.util.LinkedHashMap;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.archliu.horus.infr.domain.query.mapper.MysqlQueryMapper;
import cn.archliu.horus.infr.domain.query.mapper.TaosQueryMapper;
import cn.archliu.horus.server.domain.query.service.DataQueryService;
import cn.archliu.horus.server.domain.query.web.dto.TaosQueryDTO;
import cn.hutool.json.JSONUtil;

@Service
public class DataQueryServiceImpl implements DataQueryService {

    @Autowired
    private TaosQueryMapper taosQueryMapper;

    @Autowired
    private MysqlQueryMapper mysqlQueryMapper;

    @Override
    public Object taosQuery(TaosQueryDTO taosQueryDTO) {
        List<LinkedHashMap<Object, Object>> dataQuery = taosQueryMapper.taosQuery(taosQueryDTO.getQuerySql());
        return JSONUtil.parseArray(dataQuery);
    }

    @Override
    public Object mysqlQuery(@Valid TaosQueryDTO taosQueryDTO) {
        List<LinkedHashMap<Object, Object>> dataQuery = mysqlQueryMapper.mysqlQuery(taosQueryDTO.getQuerySql());
        return JSONUtil.parseArray(dataQuery);
    }

}
