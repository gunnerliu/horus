package cn.archliu.horus.server.domain.collection.web;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.archliu.common.response.ComRes;
import cn.archliu.common.response.sub.CRUDData;
import cn.archliu.common.response.sub.ResData;
import cn.archliu.horus.infr.domain.collection.entity.HorusColGroovyFilter;
import cn.archliu.horus.server.domain.collection.service.CollectionService;
import cn.archliu.horus.server.util.PageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = { "数据采集结构相关接口" })
@RestController
@RequestMapping("/api/horus/colDesc")
public class ColDescWeb {

    @Autowired
    private CollectionService collectionService;

    @ApiOperation("添加 GroovyFilter")
    @PutMapping("/addGroovyFilter")
    public ComRes<ResData<Void>> addGroovyFilter(@RequestParam("metricsCode") String metricsCode,
            @RequestParam("groovyCode") String groovyCode) {
        collectionService.addGroovyFilter(metricsCode, groovyCode);
        return ComRes.success();
    }

    @ApiOperation("删除 GroovyFilter")
    @DeleteMapping("/removeGroovyFilter")
    public ComRes<ResData<Void>> removeGroovyFilter(@RequestParam("id") Long id) {
        collectionService.removeGroovyFilter(id);
        return ComRes.success();
    }

    @ApiOperation("分页查询 groovy filter 信息")
    @GetMapping("/pageGroovyFilter")
    public ComRes<CRUDData<HorusColGroovyFilter>> pageGroovyFilter(@RequestParam("pageIndex") Integer pageIndex,
            @RequestParam("pageSize") Integer pageSize) {
        Page<HorusColGroovyFilter> page = PageUtil.build(pageIndex, pageSize);
        IPage<HorusColGroovyFilter> data = collectionService.pageGroovyFilter(page);
        return ComRes
                .success(new CRUDData<HorusColGroovyFilter>().setItems(data.getRecords()).setTotal(data.getTotal()));
    }

}
