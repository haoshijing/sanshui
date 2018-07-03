package com.keke.pay;

/**
 * @author haoshijing
 * @version 2018年07月03日 09:21
 **/
public interface PaySignCreator<Req> {

    String genertorSign(Req req);
}
