package cn.archliu.horus.infr.domain.query.mapper;

import java.util.LinkedHashMap;
import java.util.List;

import com.baomidou.dynamic.datasource.annotation.DS;

import org.apache.ibatis.annotations.Param;

/**
 * @Author: Arch
 * @Date: 2022-05-07 09:09:40
 * @Description: TDEngine 数据查询
 */
@DS("td")
public interface TaosQueryMapper {

    List<LinkedHashMap<Object, Object>> taosQuery(@Param("sqlStr") String sqlStr);

}
