package test.service;

import com.keke.sanshui.base.admin.service.AdminService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)  //使用junit4进行测试
@ContextConfiguration({"classpath:application-context.xml"})
public class AdminServiceTest {

    @Resource
    AdminService adminService;

    @Test
    public void testCheck(){
        Assert.assertTrue(adminService.checkUser("superadmin","kong198818",""));
    }

}
