package com.keke.sanshui.admin.service;


import com.google.common.collect.Lists;
import com.keke.sanshui.admin.agent.response.UnderAgentResponseVo;
import com.keke.sanshui.admin.agent.response.UnderProxyVo;
import com.keke.sanshui.admin.auth.AdminAuthCacheService;
import com.keke.sanshui.admin.request.agent.AgentQueryVo;
import com.keke.sanshui.admin.request.player.PlayerQueryVo;
import com.keke.sanshui.admin.response.agent.AgentExportVo;
import com.keke.sanshui.admin.response.agent.UnderAgentVo;
import com.keke.sanshui.admin.response.agent.UnderPlayerVo;
import com.keke.sanshui.admin.util.CSVUtils;
import com.keke.sanshui.admin.vo.AgentVo;
import com.keke.sanshui.base.admin.dao.AgentPickTotalDAO;
import com.keke.sanshui.base.admin.dao.PlayerDAO;
import com.keke.sanshui.base.admin.dao.PlayerPickTotalDAO;
import com.keke.sanshui.base.admin.dao.PlayerRelationDAO;
import com.keke.sanshui.base.admin.po.*;
import com.keke.sanshui.base.admin.po.agent.AgentPo;
import com.keke.sanshui.base.admin.po.agent.AgentQueryPo;
import com.keke.sanshui.base.admin.service.AgentService;
import com.keke.sanshui.base.admin.service.PlayerCouponService;
import com.keke.sanshui.base.util.MD5Util;
import com.keke.sanshui.base.util.WeekUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class AdminAgentReadService {

    @Value("${saltEncrypt}")
    private String saltEncrypt;

    @Autowired
    AgentService agentService;

    @Autowired
    PlayerCouponService playerCouponService;

    @Autowired
    AgentPickTotalDAO agentPickTotalDAO;

    @Autowired
    PlayerPickTotalDAO playerPickTotalDAO;

    @Autowired
    PlayerRelationDAO playerRelationDAO;

    @Autowired
    PlayerDAO playerDAO;

    @Autowired
    AdminAuthCacheService adminAuthCacheService;


    public List<AgentVo> selectAgentVoList(AgentQueryVo agentQueryVo) {
        AgentQueryPo queryAgentPo = new AgentQueryPo();
        queryAgentPo.setPlayerId(agentQueryVo.getGuid());
        queryAgentPo.setLimit(agentQueryVo.getLimit());
        queryAgentPo.setLevel(agentQueryVo.getLevel());
        queryAgentPo.setAgentName(agentQueryVo.getUserName());
        queryAgentPo.setAgentNickName(agentQueryVo.getNickName());
        queryAgentPo.setAgentWeChartNo(agentQueryVo.getWechartNo());
        queryAgentPo.setStatus(1);
        Integer page = agentQueryVo.getPage();
        if (page == 0) {
            page = 1;
        }
        queryAgentPo.setOffset((page - 1) * agentQueryVo.getLimit());
        List<AgentPo> agentPos = agentService.selectList(queryAgentPo);
        Integer week = agentQueryVo.getWeek();
        List<AgentVo> agentVos = agentPos.stream().map(eachAgentPo -> {
            AgentVo agentVo = new AgentVo();
            agentVo.setGameId(eachAgentPo.getPlayerId());
            agentVo.setName(eachAgentPo.getAgentName());
            agentVo.setType(eachAgentPo.getLevel());
            agentVo.setNickName(eachAgentPo.getAgentNickName());
            agentVo.setWeChartNo(eachAgentPo.getAgentWeChartNo());
            agentVo.setAgentId(eachAgentPo.getId());
            agentVo.setParentAgentId(eachAgentPo.getParentId());
            agentVo.setMemo(eachAgentPo.getMemo());
            agentVo.setWeek(agentQueryVo.getWeek().toString());
            return agentVo;

        }).map(agentVo -> {
            PlayerCouponPo playerCouponPo = playerCouponService.selectByPlayerId(agentVo.getGameId());
            if (playerCouponPo != null) {
                agentVo.setGoldCount(playerCouponPo.getGoldCount());
                agentVo.setSliverCount(playerCouponPo.getSilverCount());
            } else {
                agentVo.setGoldCount(0);
                agentVo.setSliverCount(0);
            }
            return agentVo;
        }).map(agentVo -> {
                    PlayerPickTotalPo playerPickTotalPo = playerPickTotalDAO.selectByPlayerId(agentVo.getGameId(), week);
                    if (playerPickTotalPo != null) {
                        agentVo.setAgentTotalPickUp(playerPickTotalPo.getTotalMoney());
                    } else {
                        agentVo.setAgentTotalPickUp(0L);
                    }
                    AgentPickTotalPo agentPickTotalPo = agentPickTotalDAO.selectByAgentId(agentVo.getAgentId(), week);
                    if (agentPickTotalPo != null) {
                        agentVo.setAgentUnderTotalPickUp(agentPickTotalPo.getTotalMoney());
                        agentVo.setAreaAgentUnderTotalPickUp(agentPickTotalPo.getTotalUnderMoney());
                    } else {
                        agentVo.setAgentUnderTotalPickUp(0L);
                        agentVo.setAreaAgentUnderTotalPickUp(0L);
                    }
                    return agentVo;
                }
        ).map(agentVo -> {
            Integer count = playerRelationDAO.selectUnderByPlayerId(agentVo.getGameId()).size();
            String underAgentCount = "-";
            agentVo.setMemberCount(count);
            if (agentVo.getType() == 2) {
                AgentQueryPo tmpQueryVo = new AgentQueryPo();
                tmpQueryVo.setParentId(agentVo.getAgentId());
                underAgentCount = String.valueOf(agentService.selectCount(tmpQueryVo));
            }
            agentVo.setUnderAgentCount(underAgentCount);
            return agentVo;
        }).collect(Collectors.toList());
        return agentVos;
    }

    public Long selectCount(AgentQueryVo agentQueryVo) {
        AgentQueryPo queryAgentPo = new AgentQueryPo();
        queryAgentPo.setPlayerId(agentQueryVo.getGuid());
        queryAgentPo.setLimit(agentQueryVo.getLimit());
        queryAgentPo.setLevel(agentQueryVo.getLevel());
        queryAgentPo.setAgentName(agentQueryVo.getUserName());
        queryAgentPo.setAgentNickName(agentQueryVo.getNickName());
        queryAgentPo.setAgentWeChartNo(agentQueryVo.getWechartNo());
        queryAgentPo.setStatus(1);
        return agentService.selectCount(queryAgentPo);
    }

    public List<UnderPlayerVo> obtainUnderPlayer(Integer agentGuid) {
        List<PlayerRelationPo> playerRelationPos = playerRelationDAO.selectUnderByPlayerId(agentGuid);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Integer week = WeekUtil.getCurrentWeek();
        return playerRelationPos.stream().map(playerRelationPo -> {
            UnderPlayerVo underPlayerVo = new UnderPlayerVo();
            Integer playerId = playerRelationPo.getPlayerId();
            PlayerPo playerPo = playerDAO.selectByPlayId(playerId);
            if (playerPo != null) {
                underPlayerVo.setName(playerPo.getOtherName());
            } else {
                underPlayerVo.setName("");
            }
            if (playerPo.getGameInsertTime() != null) {
                underPlayerVo.setCreateTime(format.format(new Date(playerPo.getGameInsertTime())));
            }
            underPlayerVo.setPlayerGuid(playerRelationPo.getPlayerId());
            PlayerPickTotalPo playerPickTotalPo = playerPickTotalDAO.selectByPlayerId(playerId, week);
            Long pickTotal = 0L;
            if (playerPickTotalPo != null) {
                pickTotal = playerPickTotalPo.getTotalMoney();
            }
            underPlayerVo.setPlayerPickUp(pickTotal);
            return underPlayerVo;
        }).collect(Collectors.toList());
    }

    public List<UnderAgentVo> obtainUnderAgent(Integer agentId,Integer week) {
        AgentQueryPo agentQueryPo = new AgentQueryPo();
        agentQueryPo.setParentId(agentId);
        agentQueryPo.setStatus(1);
        agentQueryPo.setLimit(10000);
        List<AgentPo> agentPos = agentService.selectList(agentQueryPo);
        return agentPos.stream().map(agentPo -> {
            UnderAgentVo underAgentVo = new UnderAgentVo();
            underAgentVo.setAgentGuid(agentPo.getPlayerId());
            underAgentVo.setWechartNo(agentPo.getAgentWeChartNo());
            underAgentVo.setNickname(agentPo.getAgentNickName());
            underAgentVo.setAgentId(agentPo.getId());
            underAgentVo.setUsername(agentPo.getAgentName());

            PlayerPickTotalPo playerPickTotalPo = playerPickTotalDAO.selectByPlayerId(agentPo.getPlayerId(), week);
            if (playerPickTotalPo != null) {
                underAgentVo.setProxyPickTotal(playerPickTotalPo.getTotalMoney());
            } else {
                underAgentVo.setProxyPickTotal(0L);
            }
            AgentPickTotalPo agentPickTotalPo = agentPickTotalDAO.selectByAgentId(agentPo.getId(), week);
            if (agentPickTotalPo != null) {
                underAgentVo.setProxyAgentTotal(agentPickTotalPo.getTotalMoney());
            } else {
                underAgentVo.setProxyAgentTotal(0L);
            }
            return underAgentVo;
        }).collect(Collectors.toList());
    }

    public List<AgentExportVo> exportAgentPick(String weeks, HttpServletResponse response) {
        List<AgentExportVo> agentExportVos = Lists.newArrayList();
        Arrays.stream(weeks.split(",")).forEach(week -> {
            Integer w = Integer.valueOf(week);
            agentExportVos.addAll(getExportFromWeek(w));
        });
        String fileName = UUID.randomUUID().toString().replace("-", "");
        File file = new File("e:/export/" + fileName + ".csv");
        List<String> strList = agentExportVos.stream().map(agentExportVo -> {
            StringBuilder stringBuilder = new StringBuilder(256);
            stringBuilder.append(agentExportVo.getWeek()).append(",")
                    .append(agentExportVo.getGuid()).append(",")
                    .append(agentExportVo.getName()).append(",")
                    .append(agentExportVo.getUnderMonery()).append(",")
                    .append(agentExportVo.getSelfPickTotal());
            return stringBuilder.toString();
        }).collect(Collectors.toList());

        CSVUtils.exportCsv(file, strList);
        FileInputStream in = null;
        OutputStream out = null;
        try {
            response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
            //创建字节输入流以读取本地文件
            in = new FileInputStream(file);
            //创建字节输出流，将文件数据写出给浏览器
            out = response.getOutputStream();
            byte[] buf = new byte[1024];
            int len = 0;
            //边读边写
            while ((len = in.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {

                }
            }
        }
        return agentExportVos;
    }

    private List<AgentExportVo> getExportFromWeek(Integer week) {
        List<AgentExportVo> agentExportVos = agentPickTotalDAO.exportAgent(week)
                .stream().map(agentPickTotalPo -> {
                    AgentExportVo agentExportVo = new AgentExportVo();
                    agentExportVo.setUnderAgentMoney(agentPickTotalPo.getTotalUnderMoney() == null ? 0L : agentPickTotalPo.getTotalUnderMoney());
                    agentExportVo.setUnderMonery(agentPickTotalPo.getTotalMoney() == null ? 0L : agentPickTotalPo.getTotalMoney());
                    agentExportVo.setWeek(week.toString());
                    AgentPo agentPo = agentService.selectById(agentPickTotalPo.getAgentId());
                    PlayerPickTotalPo playerPickTotalPo = playerPickTotalDAO.selectByPlayerId(agentPo.getPlayerId(), week);
                    if (agentPo != null) {
                        agentExportVo.setGuid(agentPo.getPlayerId());
                        agentExportVo.setName(agentPo.getAgentWeChartNo());
                    }
                    if (playerPickTotalPo != null) {
                        agentExportVo.setSelfPickTotal(playerPickTotalPo.getTotalMoney());
                    } else {
                        agentExportVo.setSelfPickTotal(0L);
                    }
                    return agentExportVo;
                }).collect(Collectors.toList());
        return agentExportVos;
    }

    public Pair<Boolean, Integer> checkUser(String name, String password) {
        try {
            AgentPo agentPo = agentService.findByGuid(Integer.valueOf(name));
            if (agentPo != null) {
                String dbPwd = agentPo.getPassword();
                String encryptPwd = MD5Util.md5(MD5Util.md5(password) + saltEncrypt);
                if (StringUtils.equals(dbPwd, encryptPwd)) {
                    return Pair.of(true, agentPo.getLevel());
                }
            }
        } catch (Exception e) {

        }
        return Pair.of(false, 0);
    }

    public long getWeekMoney(Integer playerId, Integer week) {

        AgentPo agentPo = agentService.findByGuid(playerId);
        if (agentPo != null) {
            AgentPickTotalPo agentPickTotalPo =
                    agentPickTotalDAO.selectByAgentId(agentPo.getId(), week);
            if (agentPickTotalPo == null) {
                return 0l;
            }
            return agentPickTotalPo.getTotalMoney();
        }
        return 0L;
    }

    public UnderAgentResponseVo queryUnderAgentList(Integer areaAgentGuid, PlayerQueryVo playerQueryVo) {
        UnderAgentResponseVo underAgentResponseVo = new UnderAgentResponseVo();
        Integer week = playerQueryVo.getWeek();
        if (week == null) {
            week = WeekUtil.getCurrentWeek();
        }
        final Integer currentWeek = week;
        AgentPo agentPo = agentService.findByGuid(areaAgentGuid);
        if (agentPo != null) {
            AgentPickTotalPo agentPickTotalPo = agentPickTotalDAO.selectByAgentId(agentPo.getId(), week);
            underAgentResponseVo.setWeekAgentPickTotal(agentPickTotalPo.getTotalUnderMoney());
            AgentQueryPo agentQueryPo = new AgentQueryPo();
            agentQueryPo.setLimit(playerQueryVo.getLimit());
            agentQueryPo.setOffset((playerQueryVo.getPage() - 1) * playerQueryVo.getLimit());
            agentQueryPo.setParentId(agentPo.getId());
            List<AgentPo> agentPos = agentService.selectList(agentQueryPo);
            List<UnderProxyVo> underProxyVos = agentPos.stream().map((dbAgentPo) -> {
                UnderProxyVo underProxyVo = new UnderProxyVo();
                underProxyVo.setWeek(currentWeek);
                underProxyVo.setIsNotCal(dbAgentPo.getIsNeedAreaCal());
                underProxyVo.setGuid(dbAgentPo.getPlayerId());
                PlayerPo playerPo = playerDAO.selectByPlayId(dbAgentPo.getPlayerId());
                underProxyVo.setOtherName(playerPo.getOtherName());
                //这里改为批量的
                AgentPickTotalPo agentPickTotalPo1 = agentPickTotalDAO.selectByAgentId(dbAgentPo.getId(), currentWeek);
                if (agentPickTotalPo1 != null &&
                        agentPickTotalPo1.getTotalMoney() != null) {
                    underProxyVo.setAgentTotal(agentPickTotalPo1.getTotalMoney());
                }
                PlayerPickTotalPo playerPickTotalPo = playerPickTotalDAO.selectByPlayerId(dbAgentPo.getPlayerId(), currentWeek);
                if (playerPickTotalPo != null) {
                    underProxyVo.setPickTotal(playerPickTotalPo.getTotalMoney());
                } else {
                    underProxyVo.setPickTotal(0L);
                }

                return underProxyVo;
            }).collect(Collectors.toList());
            List<Integer> agentPlayerGuids = agentService.getAllBranchAgent(agentPo.getId(), 1);
            Long money1 = playerPickTotalDAO.sumPickUp(agentPlayerGuids, currentWeek);
            if (money1 == null) {
                money1 = 0L;
            }

            List<Integer> notNeedCalPlayerGuids = agentService.getAllBranchAgent(agentPo.getId(), 2);
            for (Integer guid : notNeedCalPlayerGuids) {
                log.info("guid = {}", guid);
            }
            Long money2 = 0L;
            if (notNeedCalPlayerGuids.size() > 0) {
                money2 = playerPickTotalDAO.sumPickUp(notNeedCalPlayerGuids, currentWeek);
                if (money2 == null) {
                    money2 = 0L;
                }
            }
            underAgentResponseVo.setUnderAgentSelfTotal(money1);
            underAgentResponseVo.setNotBelongToSelfPickTotal(money2);
            underAgentResponseVo.setUnderProxyVos(underProxyVos);
        }

        return underAgentResponseVo;
    }

    public Long queryUnderAgentCount(Integer areaAgentGuid, PlayerQueryVo playerQueryVo) {
        UnderAgentResponseVo underAgentResponseVo = new UnderAgentResponseVo();
        Integer week = playerQueryVo.getWeek();
        if (week == null) {
            week = WeekUtil.getCurrentWeek();
        }
        AgentPo agentPo = agentService.findByGuid(areaAgentGuid);

        if (agentPo != null) {
            AgentPickTotalPo agentPickTotalPo = agentPickTotalDAO.selectByAgentId(agentPo.getId(), week);
            if(agentPickTotalPo != null) {
                underAgentResponseVo.setWeekAgentPickTotal(agentPickTotalPo.getTotalUnderMoney());
                AgentQueryPo agentQueryPo = new AgentQueryPo();
                agentQueryPo.setParentId(agentPo.getId());
                Long count = agentService.selectCount(agentQueryPo);
                return count;
            }else{
                log.warn(" agentId = {}",agentPo.getId());
            }
        }
        return 0L;
    }
}
