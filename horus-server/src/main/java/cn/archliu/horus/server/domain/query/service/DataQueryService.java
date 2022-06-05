package cn.archliu.horus.server.domain.query.service;

import javax.validation.Valid;

import cn.archliu.horus.server.domain.query.web.dto.TaosQueryDTO;

/**
 * @Author: Arch
 * @Date: 2022-05-07 09:06:36
 * @Description: 数据查询服务
 */
public interface DataQueryService {

    /**
     * TDEngine 数据查询
     * 
     * @param taosQueryDTO
     * @return
     */
    Object taosQuery(TaosQueryDTO taosQueryDTO);

    /**
     * mysql 数据查询
     * 
     * @param taosQueryDTO
     * @return
     */
    Object mysqlQuery(@Valid TaosQueryDTO taosQueryDTO);

}
