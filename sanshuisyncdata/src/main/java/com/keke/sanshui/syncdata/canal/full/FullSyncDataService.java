package com.keke.sanshui.syncdata.canal.full;

import com.alibaba.druid.pool.DruidDataSource;
import com.keke.sanshui.base.admin.po.PlayerPo;
import com.keke.sanshui.base.admin.service.PlayerService;
import com.keke.sanshui.syncdata.canal.util.PlayerDataParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    private PlayerDataParser playerDataParser;

    public FullSyncDataService(){
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUsername("keke");
        druidDataSource.setPassword("123456");
        druidDataSource.setUrl("jdbc:mysql://183.131.78.116:3306/waterthirteen?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull");
        jdbcTemplate.setDataSource(druidDataSource);
    }

    @PostConstruct
    public void syncRelation(){
        String sql = " select data from characters";
        List<Map<String,Object>> datas =  jdbcTemplate.queryForList(sql);
        datas.forEach(data->{
            byte[] bytes = (byte[])data.get("data");

        });
    }
    @PostConstruct
    public void syncCharacterData(){
        String sql = " select * from characters";
        List<Map<String,Object>> datas =  jdbcTemplate.queryForList(sql);
        log.info("会员信息进行数据同步");
        datas.forEach(data->{
            BigInteger playerId = (BigInteger)data.get("guid");
            data.put("guid",playerId.intValue());
            if(!playerService.checkPlayerExsist(playerId.intValue())) {
                PlayerDataParser.PlayerInfo playerInfo = playerDataParser.parseFromBaseData(data);
                if (playerInfo != null) {
                    playerService.insertPlayer(playerInfo.getPlayerPo());
                    playerService.insertPlayerCoupon(playerInfo.getPlayerCouponPo());
                }
            }
        });
    }


}
