package com.keke.sanshui.syncdata.canal.full;

import com.alibaba.druid.pool.DruidDataSource;
import com.keke.sanshui.base.admin.dao.PlayerRelationDAO;
import com.keke.sanshui.base.admin.po.PlayerPo;
import com.keke.sanshui.base.admin.po.PlayerRelationPo;
import com.keke.sanshui.base.admin.service.PlayerService;
import com.keke.sanshui.syncdata.canal.util.PlayerDataParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class FullSyncDataService {

    JdbcTemplate jdbcTemplate = new JdbcTemplate();

    @Autowired
    private PlayerService playerService;

    @Autowired
    private PlayerRelationDAO playerRelationDAO;

    @Autowired
    private PlayerDataParser parser;

    @Value("${sync.db.ip}")
    private String syncDbIp;

    private final static String PLAYER_ID = "guid";
    public FullSyncDataService(){
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUsername("keke");
        druidDataSource.setPassword("123456");
        druidDataSource.setUrl("jdbc:mysql://"+syncDbIp+":3306/waterthirteen?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull");
        jdbcTemplate.setDataSource(druidDataSource);
    }

    @PostConstruct
    public void syncRelation(){
        String sql = " select data from world_records where type =1 ";
        List<Map<String,Object>> datas =  jdbcTemplate.queryForList(sql);
        datas.forEach(data->{
            byte[] bytes = (byte[])data.get("data");
            List<PlayerRelationPo> list =   parser.parseFromWorldData(bytes);
            list.forEach(playerRelationPo -> {
                boolean isInDb = playerRelationDAO.queryByAgentAndPlayerGuid(playerRelationPo.getAgentPlayerId().intValue(),
                        playerRelationPo.getPlayerId().intValue()) > 0;
                if(!isInDb){
                    playerRelationDAO.insertRelation(playerRelationPo);
                }
            });

        });
    }
    @PostConstruct
    public void syncCharacterData(){
        String sql = " select * from characters";
        List<Map<String,Object>> datas =  jdbcTemplate.queryForList(sql);
        log.info("会员信息进行数据同步");
        datas.forEach(data->{
            BigInteger playerId = (BigInteger)data.get(PLAYER_ID);
            data.put(PLAYER_ID,playerId.intValue());
            if(!playerService.checkPlayerExsist(playerId.intValue())) {
                PlayerDataParser.PlayerInfo playerInfo = parser.parseFromBaseData(data);
                if (playerInfo != null) {
                    playerService.insertPlayer(playerInfo.getPlayerPo());
                    playerService.insertPlayerCoupon(playerInfo.getPlayerCouponPo());
                }
            }
        });
    }
}
