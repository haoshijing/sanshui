package test;

import com.keke.sanshui.base.admin.dao.OperLogDAO;
import com.keke.sanshui.base.admin.po.log.OperLogPo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)  //使用junit4进行测试
@ContextConfiguration({"classpath:application-context.xml"})
public class LogDaoTest {

    @Autowired
    private OperLogDAO operLogDAO;
    @Before
    public void setup(){

    }

    @Test
    public void testInsert(){
        OperLogPo operLogPo = new OperLogPo();
        operLogPo.setMark("222");
        operLogPo.setOperTarget(111);
        operLogPo.setOperType(3);
        operLogPo.setInsertTime(System.currentTimeMillis());

        Assert.assertTrue(operLogDAO.insertLog(operLogPo) > 0);
    }

}
