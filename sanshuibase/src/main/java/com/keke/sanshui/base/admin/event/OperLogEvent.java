package com.keke.sanshui.base.admin.event;

import com.keke.sanshui.base.admin.po.log.OperLogPo;
import lombok.Data;
import org.springframework.context.ApplicationEvent;

@Data
public class OperLogEvent extends ApplicationEvent {

    private OperLogPo operLogPo;
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public OperLogEvent(Object source, OperLogPo operLogPo) {
        super(source);
        this.operLogPo = operLogPo;
    }
}
