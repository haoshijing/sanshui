package com.keke.sanshui.pay.fuqianla;

import lombok.Data;

@Data
public class FuqianResponseVo {
  private Integer amount;
  private String receive_time;
  private String complete_time;
  private String merch_id;
  private String charge_id;
  private String order_no;
  private String ret_code;
  private String ret_info;
  private String optional;
  private String sign_type;
  private String version;
  private String sign_info;
}
