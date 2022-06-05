package cn.archliu.horus.server.domain.groovy.manager;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import cn.archliu.horus.common.exception.sub.FileUploadFailedException;
import cn.archliu.horus.common.exception.sub.ParamErrorException;
import cn.archliu.horus.infr.domain.groovy.entity.HorusGroovyInfo;
import cn.archliu.horus.server.config.HorusServerProperties;
import cn.archliu.horus.server.domain.groovy.entity.UploadGroovyInfo;
import cn.archliu.horus.server.domain.groovy.service.HorusGroovyService;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: Arch
 * @Date: 2022-05-21 11:12:58
 * @Description: groovy 文件管理
 */
@Slf4j
@Component
public class HorusGroovyFileManager {

    @Autowired
    private HorusServerProperties horusServerProperties;

    @Autowired
    private HorusGroovyService horusGroovyService;

    /**
     * 上传 groovy 文件
     * 
     * @param uploadFile
     * @param uploadGroovyInfo
     */
    public void uploadGroovy(MultipartFile uploadFile, UploadGroovyInfo uploadGroovyInfo) {
        if (StrUtil.isBlank(horusServerProperties.getGroovyPath())) {
            log.warn("未配置 groovy 上传路径！");
            throw ParamErrorException.throwE("未配置 groovy 上传路径！");
        }
        String originalFilename = uploadFile.getOriginalFilename();
        File fileDir = new File(horusServerProperties.getGroovyPath());
        // 目录不存在则创建该目录
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }
        String filePath = StrUtil.endWith(horusServerProperties.getGroovyPath(), File.separator)
                ? horusServerProperties.getGroovyPath() + originalFilename
                : horusServerProperties.getGroovyPath() + File.separator + originalFilename;
        try {
            // 落地文件夹
            File groovyFile = new File(filePath);
            uploadFile.transferTo(groovyFile);
            // 脚本信息持久化
            horusGroovyService.installNewGroovy(new HorusGroovyInfo().setGroovyFileName(originalFilename)
                    .setFilePath(filePath).setGroovyCode(uploadGroovyInfo.getGroovyCode())
                    .setExecuteType(uploadGroovyInfo.getExecuteType().name())
                    .setLastModTime(groovyFile.lastModified()));
        } catch (Exception e) {
            log.error("groovy 文件上传失败: {} ", uploadGroovyInfo.toString(), e);
            throw FileUploadFailedException.throwE("groovy 文件上传失败!");
        }
    }

    /**
     * 卸载 groovy 脚本缓存
     * 
     * @param groovyCode
     */
    public void unInstallGroovy(String groovyCode) {
        horusGroovyService.unInstallGroovy(groovyCode);
    }

    /**
     * 删除脚本：删除文件、删除信息、卸载缓存
     * 
     * @param groovyCode
     */
    public void delGroovy(String groovyCode) {
        horusGroovyService.delGroovy(groovyCode);

    }

}
