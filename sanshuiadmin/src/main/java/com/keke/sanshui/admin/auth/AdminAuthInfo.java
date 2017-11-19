package com.keke.sanshui.admin.auth;

import lombok.Data;

@Data
public class AdminAuthInfo {
    private String userName;
    private Integer level;
    private String token;
}
