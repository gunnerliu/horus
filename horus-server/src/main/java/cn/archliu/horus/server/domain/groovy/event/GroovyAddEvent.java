package cn.archliu.horus.server.domain.groovy.event;

import org.springframework.context.ApplicationEvent;

/**
 * @Author: Arch
 * @Date: 2022-05-25 17:06:37
 * @Description: groovy 脚本新增事件
 */
public class GroovyAddEvent extends ApplicationEvent {

    public GroovyAddEvent(Object source) {
        super(source);
    }
}
