package cn.archliu.horus.server.domain.groovy.event;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;

/**
 * @Author: Arch
 * @Date: 2022-05-25 16:55:40
 * @Description: groovy 脚本删除事件
 */
public class GroovyDelEvent extends ApplicationEvent {

    /** 被删除 groovy 脚本的 code */
    @Getter
    private String groovyCode;

    public GroovyDelEvent(String groovyCode) {
        super(groovyCode);
        this.groovyCode = groovyCode;
    }

}
