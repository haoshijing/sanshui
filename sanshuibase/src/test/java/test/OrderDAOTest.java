package test;

import com.google.common.collect.Lists;
import com.keke.sanshui.base.admin.dao.OrderDAO;
import com.keke.sanshui.base.admin.po.order.Order;
import com.keke.sanshui.base.admin.po.order.QueryOrderPo;
import com.keke.sanshui.base.admin.service.OrderService;
import com.keke.sanshui.base.util.WeekUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)  //使用junit4进行测试
@ContextConfiguration({"classpath:application-context.xml"})
public class OrderDAOTest {

    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private OrderService orderService;

    @Test
    public void testInsert() {
        Order order = new Order();
        order.setClientGuid(33);
        order.setMoney("8");
        order.setTitle("支付标题");
        order.setPrice("1");
        order.setPayState(1);
        order.setPayTime("2017-10-28 00:29");
        order.setOrderNo("YJ8154545785154");
        order.setPayType("tpay");
        order.setSendStatus(1);
        order.setInsertTime(System.currentTimeMillis());
        int insertRet = orderDAO.insert(order);

        Assert.assertTrue(insertRet == 1);
    }

    @Test
    public void testQuerySum() {
        Long sum = orderDAO.queryPickupSum(1000013, WeekUtil.getWeekStartTimestamp(), WeekUtil.getWeekEndTimestamp());
        Assert.assertTrue(sum > 0l);
    }

    @Test
    public void testQuery() {
        Assert.assertTrue(orderService.queryOrderByNo("YJ8154545785154") != null);
    }

    @Test
    public void testUpdateSend() {

    }


    @Test
    public void testSelectList(){
        QueryOrderPo queryOrderPo = new QueryOrderPo();
        queryOrderPo.setOffset(1);
        queryOrderPo.setLimit(20);
        queryOrderPo.setClientGuids(Lists.newArrayList(1002156));
        queryOrderPo.setOrderStatus(2);
        List<Order> orderList = orderDAO.selectList(queryOrderPo);

        orderList.forEach(order -> {
            System.out.println(order.getSelfOrderNo());
        });
    }
}
