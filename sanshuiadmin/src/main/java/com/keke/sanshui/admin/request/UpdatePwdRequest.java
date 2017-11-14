package com.keke.sanshui.admin.request;

import lombok.Data;

@Data
public class UpdatePwdRequest {
    private String oldPwd;
    private String newPwd;
}
