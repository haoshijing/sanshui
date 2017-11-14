package test;

import com.keke.sanshui.base.util.MD5Util;
import com.keke.sanshui.base.vo.PayVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)  //使用junit4进行测试
public class TestSign {


    @Test
    public  void testSign(){
        System.out.println(MD5Util.md5(MD5Util.md5("1234")+"123"));

    }
}
