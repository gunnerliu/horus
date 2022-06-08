package cn.archliu.horus.server.domain.groovy.web;

import java.util.Set;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.archliu.common.response.ComRes;
import cn.archliu.common.response.sub.CRUDData;
import cn.archliu.common.response.sub.ResData;
import cn.archliu.horus.infr.domain.groovy.entity.HorusGroovyInfo;
import cn.archliu.horus.server.domain.groovy.service.HorusGroovyService;
import cn.archliu.horus.server.domain.groovy.web.convert.GroovyConvert;
import cn.archliu.horus.server.domain.groovy.web.dto.AddGroovyDTO;
import cn.archliu.horus.server.util.PageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Validated
@Api(tags = { "groovy 脚本相关接口" })
@RestController
@RequestMapping("/api/horus/groovy")
public class HorusGroovyWeb {

    @Autowired
    private HorusGroovyService groovyService;

    @ApiOperation("新增 groovy 脚本")
    @PutMapping("/addGroovy")
    public ComRes<ResData<Void>> addGroovy(@RequestBody AddGroovyDTO addGroovyDTO) {
        HorusGroovyInfo groovyInfo = GroovyConvert.INSTANCE.trans(addGroovyDTO)
                .setLastModTime(System.currentTimeMillis()).setFilePath("DB");
        groovyService.addGroovy(groovyInfo);
        return ComRes.success();
    }

    @ApiOperation("卸载 groovy 脚本缓存")
    @DeleteMapping("/unInstallGroovy")
    public ComRes<ResData<Void>> unInstallGroovy(@RequestParam("groovyCode") String groovyCode) {
        groovyService.unInstallGroovy(groovyCode);
        return ComRes.success();
    }

    @ApiOperation("删除 groovy 脚本：删除文件、删除信息、卸载缓存")
    @DeleteMapping("/delGroovy")
    public ComRes<ResData<Void>> delGroovy(@RequestParam("groovyCode") String groovyCode) {
        groovyService.delGroovy(groovyCode);
        return ComRes.success();
    }

    @ApiOperation("分页查询 groovy 脚本信息")
    @GetMapping("/pageGroovyInfo")
    public ComRes<CRUDData<HorusGroovyInfo>> pageGroovyInfo(@RequestParam("pageIndex") Integer pageIndex,
            @RequestParam("pageSize") Integer pageSize) {
        Page<HorusGroovyInfo> page = PageUtil.build(pageIndex, pageSize);
        IPage<HorusGroovyInfo> data = groovyService.pageQuery(page);
        return ComRes.success(new CRUDData<HorusGroovyInfo>().setItems(data.getRecords()).setTotal(data.getTotal()));
    }

    @ApiOperation("查询 groovy 缓存")
    @GetMapping("/loadGroovyCache")
    public ComRes<ResData<Set<String>>> loadGroovyCache() {
        return ComRes.success(new ResData<>(groovyService.loadGroovyCache()));
    }

    @ApiOperation("查看 groovy 脚本详情")
    @GetMapping("/groovyDetail")
    public ComRes<ResData<String>> groovyDetail(@RequestParam("groovyCode") String groovyCode) {
        return ComRes.success(new ResData<>(groovyService.groovyDetail(groovyCode)));
    }

    // TODO 编辑 groovy 脚本内容

}
