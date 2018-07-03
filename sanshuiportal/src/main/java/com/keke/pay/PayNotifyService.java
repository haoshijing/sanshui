package com.keke.pay;

import com.keke.pay.notify.NotifyRespBase;

/**
 * @author haoshijing
 * @version 2018年07月03日 13:29
 **/
public interface PayNotifyService {
    int handlerNotifyResp(NotifyRespBase resp);
}
