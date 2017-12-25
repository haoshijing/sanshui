package com.keke.sanshui.syncdata.canal.full;

import com.alibaba.druid.pool.DruidDataSource;
import com.keke.sanshui.base.admin.dao.AgentDAO;
import com.keke.sanshui.base.admin.dao.PlayerRelationDAO;
import com.keke.sanshui.base.admin.po.PlayerCouponPo;
import com.keke.sanshui.base.admin.po.PlayerRelationPo;
import com.keke.sanshui.base.admin.po.agent.AgentPo;
import com.keke.sanshui.base.admin.service.PlayerService;
import com.keke.sanshui.syncdata.canal.util.PlayerDataParser;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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

    @Autowired
    private AgentDAO agentDAO;


    private ScheduledExecutorService scheduledExecutorService;

    @Value("${sync.db.ip}")
    private String syncDbIp;

    @Value("${sync.db.name}")
    private String syncDbName;

    @Value("${sync.db.password}")
    private String syncDbPassword;

    private static final String PLAYER_ID = "guid";

    public FullSyncDataService() {

    }

    @PostConstruct
    public void init() {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUsername(syncDbName);
        druidDataSource.setPassword(syncDbPassword);
        druidDataSource.setUrl("jdbc:mysql://" + syncDbIp + ":3306/waterthirteen?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull");
        jdbcTemplate.setDataSource(druidDataSource);
    }

    @EventListener
    public void startWork(EmbeddedServletContainerInitializedEvent event) {
        scheduledExecutorService = Executors.newScheduledThreadPool(1, new DefaultThreadFactory("SyncDataThread"));
        scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                try {
                    syncCharacterData();
                    syncRelation();
                } catch (Exception e) {
                    log.error("", e);
                }
            }
        }, 10000, 60000, TimeUnit.MILLISECONDS);
    }

    public void syncRelation() {
        log.info("关系开始进行同步");
        String sql = " select data from world_records where type =1 ";
        List<Map<String, Object>> datas = jdbcTemplate.queryForList(sql);
        datas.forEach(data -> {
            byte[] bytes = (byte[]) data.get("data");
            PlayerDataParser.PlayerAndAgentData playerAndAgentData = parser.parseFromWorldData(bytes);
            playerAndAgentData.getPlayerRelationPos().forEach(playerRelationPo -> {
                Integer parentId = playerRelationPo.getParentPlayerId().intValue();
                Integer playerId = playerRelationPo.getPlayerId().intValue();
                PlayerRelationPo playerRelationPo1 = playerRelationDAO.selectByPlayerId(playerId);
                if (playerRelationPo1 != null) {
                    PlayerRelationPo updatePlayerRelationPo = new PlayerRelationPo();
                    updatePlayerRelationPo.setPlayerId(playerId);
                    updatePlayerRelationPo.setId(playerRelationPo1.getId());
                    updatePlayerRelationPo.setParentPlayerId(parentId);
                    playerRelationDAO.updatePlayerRelation(updatePlayerRelationPo);
                } else {
                    playerRelationDAO.insertRelation(playerRelationPo);
                }
            });
            playerAndAgentData.getAgentPos().forEach(agentPo -> {
                //去找这个人的上级
                Integer playerId = agentPo.getPlayerId();
                AgentPo queryPo = agentDAO.selectByPlayerId(playerId);
                if (queryPo == null) {
                    try {
                        agentDAO.insert(agentPo);
                    } catch (Exception e) {
                        log.error("{}", e);
                    }
                } else {
                    AgentPo updatePo = new AgentPo();
                    updatePo.setId(queryPo.getId());
                    updatePo.setIsNeedAreaCal(agentPo.getIsNeedAreaCal());
                    agentDAO.updateAgent(updatePo);
                }
            });
        });
    }

    public void syncCharacterData() {
        String sql = " select * from characters";
        List<Map<String, Object>> datas = jdbcTemplate.queryForList(sql);
        log.info("会员信息进行数据同步");
        datas.forEach(data -> {
            BigInteger playerId = (BigInteger) data.get(PLAYER_ID);
            data.put(PLAYER_ID, playerId.intValue());
            try {
                PlayerDataParser.PlayerInfo playerInfo = parser.parseFromBaseData(data);
                boolean exist = playerService.checkPlayerExsist(playerId.intValue());
                if (playerInfo != null) {
                    if (!exist) {
                        try {
                            playerService.insertPlayer(playerInfo.getPlayerPo());
                            playerService.insertPlayerCoupon(playerInfo.getPlayerCouponPo());
                        } catch (Exception e) {
                            log.error("", e);
                        }
                    } else {
                        PlayerCouponPo updatePlayerCouponPo = new PlayerCouponPo();
                        updatePlayerCouponPo.setPlayerId(playerInfo.getPlayerCouponPo().getPlayerId());
                        updatePlayerCouponPo.setSilverCount(playerInfo.getPlayerCouponPo().getSilverCount());
                        playerService.updatePlayerCoupon(updatePlayerCouponPo);
                    }
                }
            } catch (Exception e) {
                log.error("", e);
                e.printStackTrace();
            }
        });
    }
}
