package com.keke.sanshui.admin.service;

import com.google.common.collect.Lists;
import com.keke.sanshui.admin.response.index.PickDataResponse;
import com.keke.sanshui.admin.response.index.PickLastWeekData;
import com.keke.sanshui.base.admin.po.order.Order;
import com.keke.sanshui.base.admin.po.order.QueryOrderPo;
import com.keke.sanshui.base.admin.service.OrderService;
import com.keke.sanshui.base.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Repository
public class IndexService {

    @Autowired
    private OrderService orderService;

    public PickDataResponse getCurrentDayPick() {
        return getCurrentDayPick(0);
    }

    public List<PickLastWeekData> getLast7DayPick(){
        List<PickLastWeekData> list = Lists.newArrayList();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for(int i = -7; i < 0 ;i++){
            PickLastWeekData pickLastWeekData = new PickLastWeekData();
            PickDataResponse pickDataResponse = getCurrentDayPick(i);
            pickLastWeekData.setDayPickTotal(pickDataResponse.getDayPickTotal());
            pickLastWeekData.setDaySuccessTotal(pickDataResponse.getDaySuccessTotal());
            Long timestamp = TimeUtil.getDayStartTimestamp(i);
            pickLastWeekData.setDateStr(simpleDateFormat.format(new Date(timestamp)));
            list.add(pickLastWeekData);
        }
        return list;
    }

    private PickDataResponse getCurrentDayPick(int day) {
        PickDataResponse pickDataResponse = new PickDataResponse();

        QueryOrderPo queryOrderPo = new QueryOrderPo();
        queryOrderPo.setOrderStatus(2);
        queryOrderPo.setStartTimestamp(TimeUtil.getDayStartTimestamp(day));
        queryOrderPo.setEndTimestamp(TimeUtil.getDayEndTimestamp(day));
        queryOrderPo.setLimit(1000);
        queryOrderPo.setOffset(0);
        List<Order> orderList = orderService.selectList(queryOrderPo);
        int sum = orderList.stream().mapToInt(order->{
            return Integer.valueOf(order.getPrice());
        }).sum();
        pickDataResponse.setDaySuccessTotal(Long.valueOf(sum/100));
        QueryOrderPo newQueryPo = new QueryOrderPo();
        newQueryPo.setStartTimestamp(TimeUtil.getDayStartTimestamp(day));
        newQueryPo.setEndTimestamp(TimeUtil.getDayEndTimestamp(day));
        newQueryPo.setLimit(1000);
        newQueryPo.setOffset(0);
        orderList = orderService.selectList(newQueryPo);
        int sumTotal = orderList.stream().mapToInt(order->{
            return Integer.valueOf(order.getPrice());
        }).sum();
        pickDataResponse.setDayPickTotal(Long.valueOf(sumTotal/100));
        return pickDataResponse;
    }

}
