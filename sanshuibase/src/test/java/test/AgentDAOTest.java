package test;

import com.keke.sanshui.base.admin.dao.AgentDAO;
import com.keke.sanshui.base.admin.po.AgentPo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)  //使用junit4进行测试
@ContextConfiguration({"classpath:application-context.xml"})
public class AgentDAOTest {

    @Autowired
    private AgentDAO agentDAO;

    @Test
    public void testInsert(){
        AgentPo agentPo = new AgentPo();
        agentPo.setAgentName("代理名称");
        agentPo.setAgentWeChartNo("13968185435");
        agentPo.setInsertTime(System.currentTimeMillis());
        agentPo.setLastUpdateTime(System.currentTimeMillis());
        agentPo.setAgentNickName("郝仕兢");
        agentPo.setLevel(1);
        agentPo.setPlayerId(1011);
        agentPo.setStatus(1);
        agentPo.setParentId(0);

        agentDAO.insert(agentPo);

        Assert.assertTrue(agentPo.getId() > 0);
    }
}


