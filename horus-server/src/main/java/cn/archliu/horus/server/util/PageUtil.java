package cn.archliu.horus.server.util;

import java.util.List;
import java.util.function.Function;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import cn.archliu.common.response.sub.CRUDData;

public class PageUtil {

    public static <T> Page<T> build(Integer pageIndex, Integer pageSize) {
        if (pageIndex == null || pageIndex <= 0) {
            pageIndex = 1;
        }
        if (pageSize == null || pageSize <= 0) {
            pageSize = 10;
        }
        return new Page<>(pageIndex, pageSize);
    }

    public static <T, R> CRUDData<R> build(IPage<T> data, Function<List<T>, List<R>> convert) {
        return new CRUDData<R>().setItems(convert.apply(data.getRecords())).setTotal(data.getTotal());
    }

    public static <T> CRUDData<T> build(IPage<T> data) {
        return new CRUDData<T>().setItems(data.getRecords()).setTotal(data.getTotal());
    }

    public static <T, R> CRUDData<R> build(List<T> data, long total, Function<List<T>, List<R>> convert) {
        return new CRUDData<R>().setItems(convert.apply(data)).setTotal(total);
    }

    private PageUtil() {
    }

}
