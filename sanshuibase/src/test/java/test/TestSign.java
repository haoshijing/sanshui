package test;

import com.keke.sanshui.base.util.SignUtil;
import com.keke.sanshui.base.vo.PayVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)  //使用junit4进行测试
public class TestSign {


    @Test
    public  void testSign(){
        //P_address=, P_attach=10021561509233199429,
        // P_city=杭州, P_country=中国,
        // P_email=, P_mobile=,
        // P_money=1.00, P_name=,
        // P_no=2017102921001004320250926770,
        // P_num=1, P_postcode=,
        // P_price=1.00, P_province=浙江,
        // P_qq=, P_remarks=,
        // P_state=2, P_time=2017/10/29 7:26:54,
        // P_title=10银豆, P_type=alipay,
        // P_url=http://sss.cn-newworld.com:8080/sanshui/goPayPage
        PayVo payVo = new PayVo();
        payVo.setP_attach("10000241509418833398");
        payVo.setP_no("2017102921001004320250926770");
        payVo.setP_title("测试商品");
        payVo.setP_city("杭州");
        payVo.setP_country("中国");
        payVo.setP_num("1");
        payVo.setP_price("0.01");
        payVo.setP_money("0.01");
        payVo.setP_province("浙江");
        payVo.setP_type("alipay");
        payVo.setP_state("2");
        payVo.setP_time("2017/10/31 11:00:48");
        payVo.setP_url("http://game.youthgamer.com:8080/sanshui/goPayPage");

        System.out.println(SignUtil.createPaySign(payVo,"1dfXbJl2wyz1IAiAEdmjTR5q"));

    }
}
