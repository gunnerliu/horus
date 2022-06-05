package cn.archliu.horus.server.domain.groovy.web;

import java.util.Set;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.archliu.common.response.ComRes;
import cn.archliu.common.response.sub.CRUDData;
import cn.archliu.common.response.sub.ResData;
import cn.archliu.horus.infr.domain.groovy.entity.HorusGroovyInfo;
import cn.archliu.horus.server.domain.groovy.entity.UploadGroovyInfo;
import cn.archliu.horus.server.domain.groovy.manager.HorusGroovyFileManager;
import cn.archliu.horus.server.domain.groovy.service.HorusGroovyService;
import cn.archliu.horus.server.domain.groovy.web.dto.UploadGroovyDTO;
import cn.archliu.horus.server.util.PageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Validated
@Api(tags = { "groovy 脚本相关接口" })
@RestController
@RequestMapping("/api/horus/groovy")
public class HorusGroovyWeb {

    @Autowired
    private HorusGroovyFileManager horusGroovyFileManager;

    @Autowired
    private HorusGroovyService groovyService;

    @ApiOperation("上传 groovy 脚本")
    @PostMapping("/uploadGroovy")
    public ComRes<ResData<Void>> uploadGroovy(@RequestParam("UploadGroovy") MultipartFile multipartFile,
            @RequestBody UploadGroovyDTO uploadGroovyDTO) {
        horusGroovyFileManager.uploadGroovy(multipartFile, new UploadGroovyInfo()
                .setGroovyCode(uploadGroovyDTO.getGroovyCode()).setExecuteType(uploadGroovyDTO.getExecuteType()));
        return ComRes.success();
    }

    @ApiOperation("卸载 groovy 脚本缓存")
    @DeleteMapping("/unInstallGroovy")
    public ComRes<ResData<Void>> unInstallGroovy(@RequestParam("groovyCode") String groovyCode) {
        horusGroovyFileManager.unInstallGroovy(groovyCode);
        return ComRes.success();
    }

    @ApiOperation("删除 groovy 脚本：删除文件、删除信息、卸载缓存")
    @DeleteMapping("/delGroovy")
    public ComRes<ResData<Void>> delGroovy(@RequestParam("groovyCode") String groovyCode) {
        horusGroovyFileManager.delGroovy(groovyCode);
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

}
