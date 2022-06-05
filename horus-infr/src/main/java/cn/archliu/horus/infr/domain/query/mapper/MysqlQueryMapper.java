package cn.archliu.horus.infr.domain.query.mapper;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * @Author: Arch
 * @Date: 2022-05-07 09:11:12
 * @Description: mysql 查询服务
 */
public interface MysqlQueryMapper {

    List<LinkedHashMap<Object, Object>> mysqlQuery(@Param("sqlStr") String sqlStr);

}
