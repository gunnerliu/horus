package cn.archliu.horus.server.domain.metrics.service.impl;

import java.io.IOException;
import java.util.Map;

import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.ClasspathResourceLoader;
import org.springframework.stereotype.Service;

import cn.archliu.horus.common.exception.sub.GenFailedException;
import cn.archliu.horus.server.domain.metrics.service.BeetlService;

@Service
public class BeetlServiceImpl implements BeetlService {

    @Override
    public String generateTemplate(String templatePath, Map<String, Object> params) {
        try {
            // 文件资源模板加载器
            ClasspathResourceLoader classpathResourceLoader = new ClasspathResourceLoader("templates/", "utf-8"); // 构造资源加载器，并指定字符集为utf-8
            /*
             * 使用Beetl默认的配置。
             * Beetl可以使用配置文件的方式去配置，但由于此处是直接上手的例子，
             * 我们不去管配置的问题，只需要基本的默认配置就可以了。
             */
            Configuration config = Configuration.defaultConfiguration();
            GroupTemplate groupTemplate = new GroupTemplate(classpathResourceLoader, config);
            Template template = groupTemplate.getTemplate(templatePath);
            // 需要导入的参数
            template.binding(params);
            return template.render();
        } catch (IOException e) {
            throw GenFailedException.throwE("模板生成异常！");
        }
    }

}
