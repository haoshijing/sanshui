package test;

import com.keke.sanshui.base.admin.dao.OrderDAO;
import com.keke.sanshui.base.admin.po.Order;
import com.keke.sanshui.base.admin.po.Order;
import com.keke.sanshui.base.admin.service.OrderService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)  //使用junit4进行测试
@ContextConfiguration({"classpath:application-context.xml"})
public class OrderDAOTest {

    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private OrderService orderService;
    @Test
    public void testInsert(){
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
    public void testQuery(){
        Assert.assertTrue(orderService.queryOrderByNo("YJ8154545785154") != null);
    }

    @Test
    public void testUpdateSend(){

    }
}
