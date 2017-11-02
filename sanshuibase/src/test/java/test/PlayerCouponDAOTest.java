package test;

import com.keke.sanshui.base.admin.dao.PlayerCouponDAO;
import com.keke.sanshui.base.admin.po.PlayerCouponPo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)  //使用junit4进行测试
@ContextConfiguration({"classpath:application-context.xml"})
public class PlayerCouponDAOTest {

    @Autowired
    private PlayerCouponDAO playerCouponDAO;

    @Before
    public void test(){
        System.setProperty("env","dev");
    }
    @Test
    public void testInsert(){
        PlayerCouponPo playerCouponPo = new PlayerCouponPo();
        playerCouponPo.setGoldCount(3);
        playerCouponPo.setPlayerId(1011);
        playerCouponPo.setLastUpdateTime(System.currentTimeMillis());
        playerCouponPo.setSilverCount(0);
        Assert.assertTrue(playerCouponDAO.insertPlayerCouponPo(playerCouponPo) > 0);
    }

    @Test
    public void testUpdate(){
        PlayerCouponPo playerCouponPo = new PlayerCouponPo();
        playerCouponPo.setGoldCount(33);
        playerCouponPo.setPlayerId(1011);
        playerCouponPo.setLastUpdateTime(System.currentTimeMillis());
        playerCouponPo.setSilverCount(2);
        Assert.assertTrue(playerCouponDAO.updatePlayerCouponPo(playerCouponPo) > 0);
    }
}


