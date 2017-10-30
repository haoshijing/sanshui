package test;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.keke.sanshui.base.admin.dao.PayLinkDAO;
import com.keke.sanshui.base.admin.po.PayLink;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.BytesContentProvider;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)  //使用junit4进行测试
@ContextConfiguration({"classpath:application-context.xml"})
public class PayLinkDAOTest {

    @Autowired
    private PayLinkDAO payLinkDAO;

    @Test
    public void testInsert(){
        PayLink payLink = new PayLink();
        payLink.setCIdNo("http://www.baidu.com");
        payLink.setPickCouponVal(8);
        payLink.setPickRmb(20);
        int insertRet = payLinkDAO.insert(payLink);
        Assert.assertTrue(insertRet == 1);
    }

    @Test
    public void testBatchInsert() throws Exception{

        List<PayLink> payLinkList = Lists.newArrayList();
        {
            PayLink payLink = new PayLink();
            payLink.setPickRmb(298 * 100);
            payLink.setPickCouponVal(4112);
            payLink.setCIdNo("InTfIygzhw");
            payLinkList.add(payLink);
        }
        {
            PayLink payLink = new PayLink();
            payLink.setPickRmb(128 * 100);
            payLink.setPickCouponVal(1510);
            payLink.setCIdNo("0BA7HmcarO");
            payLinkList.add(payLink);
        }
        {
            PayLink payLink = new PayLink();
            payLink.setPickRmb(50 * 100);
            payLink.setPickCouponVal(540);
            payLink.setCIdNo("H3ZzXORrdF");
            payLinkList.add(payLink);
        }
        {
            PayLink payLink = new PayLink();
            payLink.setPickRmb(30 * 100);
            payLink.setPickCouponVal(300);
            payLink.setCIdNo("vC1taJ2jRi");
            payLinkList.add(payLink);
        }
        {
            PayLink payLink = new PayLink();
            payLink.setPickRmb(8 * 100);
            payLink.setPickCouponVal(80);
            payLink.setCIdNo("NE42anAhzj");
            payLinkList.add(payLink);
        }
        {
            PayLink payLink = new PayLink();
            payLink.setPickRmb(1*100);
            payLink.setPickCouponVal(10);
            payLink.setCIdNo("F066yjemS8");
            payLinkList.add(payLink);
        }
        {
            PayLink payLink = new PayLink();
            payLink.setPickRmb(1);
            payLink.setPickCouponVal(1000);
            payLink.setCIdNo("ypKE8hhKVq");
            payLinkList.add(payLink);
        }

        HttpClient httpClient = new HttpClient();
        httpClient.start();
        Request request = httpClient.newRequest("http://game.youthgamer.com:8080/sanshui/add");

        ContentResponse contentResponse = request.content(new BytesContentProvider(JSON.toJSONBytes(payLinkList)),"application/json").send();

        System.out.println(contentResponse.getContentAsString());

    }

    @Test
    public void testQuery(){
        PayLink payLink = payLinkDAO.getById(1);
        Assert.assertTrue(payLink != null);
    }
}
