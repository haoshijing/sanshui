package test;

import com.keke.sanshui.base.admin.dao.PlayerDAO;
import com.keke.sanshui.base.admin.po.PlayerPo;
import com.keke.sanshui.base.admin.service.PlayerService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

public class PlayerDAOTest extends BaseTest{
    @Autowired
    private PlayerDAO playerDAO;

    @Autowired
    private PlayerService playerService;
    @Test
    public void testInsert(){
        for(int i = 5 ; i < 6;i++) {
            PlayerPo playerPo = new PlayerPo();
            playerPo.setInsertTime(System.currentTimeMillis());
            playerPo.setStatus(1);
            playerPo.setPlayerId(300000);
            playerPo.setLastUpdateTime(System.currentTimeMillis());
            playerPo.setOpenId(UUID.randomUUID().toString().replace("0",""));

            Assert.assertTrue(playerDAO.insertPlayer(playerPo) > 0);
        }
    }
    @Test
    public void testUpdate(){
        for(int i = 5 ; i < 6;i++) {
            PlayerPo playerPo = new PlayerPo();
            playerPo.setStatus(2);
            playerPo.setLastUpdateTime(System.currentTimeMillis());
            playerPo.setOpenId(UUID.randomUUID().toString().replace("-",""));
            playerPo.setPlayerId(300000);
            Assert.assertTrue(playerDAO.updatePlayer(playerPo) > 0);
        }
    }


    @Test
    public void testList(){
        List<PlayerPo> playerPoList = playerService.selectList(0,15);
        int count = 0 ;
        Integer nextMaxId = 0;
        do{
            playerPoList.forEach(playerPo -> {
                System.out.println(playerPo.getOpenId());
            });
            playerPoList = playerService.selectList(nextMaxId,15);
            if(playerPoList.size() > 0){
                nextMaxId = playerPoList.get(playerPoList.size()-1).getId();
            }
            count++;
        }while (playerPoList.size() == 0 || count < 3);

    }
}
