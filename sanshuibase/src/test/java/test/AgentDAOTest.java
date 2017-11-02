package test;

import com.keke.sanshui.base.admin.dao.AgentDAO;
import com.keke.sanshui.base.admin.po.AgentPo;
import com.keke.sanshui.base.admin.service.AgentService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)  //使用junit4进行测试
@ContextConfiguration({"classpath:application-context.xml"})
public class AgentDAOTest {

    @Autowired
    private AgentDAO agentDAO;

    @Autowired
    private AgentService agentService;

    @Test
    public void testSelectPlayer(){
       Set<Integer> agents = agentService.getAllBranchAgent(1,true);
       for(Integer ret:agents){
           System.out.println(ret);
       }
    }

    @Test
    public void testInsert(){
        AgentPo agentPo = new AgentPo();
        agentPo.setAgentName("代理名称2");
        agentPo.setAgentWeChartNo("13968185435");
        agentPo.setInsertTime(System.currentTimeMillis());
        agentPo.setLastUpdateTime(System.currentTimeMillis());
        agentPo.setAgentNickName("郝仕兢22");
        agentPo.setLevel(3);
        agentPo.setPlayerId(1015);
        agentPo.setStatus(1);
        agentPo.setParentId(3);

        agentDAO.insert(agentPo);

        Assert.assertTrue(agentPo.getId() > 0);
    }
}


