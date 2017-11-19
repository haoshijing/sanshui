package com.keke.sanshui.admin.response.index;

import lombok.Data;

@Data
public class PickDataResponse {
    private Long daySuccessTotal;
    private Long dayPickTotal;
    private Integer successCount;
    private Integer totalCount;
}
