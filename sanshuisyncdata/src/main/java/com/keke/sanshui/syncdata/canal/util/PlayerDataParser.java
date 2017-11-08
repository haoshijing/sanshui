package com.keke.sanshui.syncdata.canal.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.keke.sanshui.base.admin.po.PlayerCouponPo;
import com.keke.sanshui.base.admin.po.PlayerPo;
import com.keke.sanshui.base.admin.po.PlayerRelationPo;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.BytesContentProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Repository
@Slf4j
public final class PlayerDataParser {

    @Autowired
    private HttpClient httpClient;

    @Data
    public static class PlayerInfo {
        private PlayerPo playerPo;
        private PlayerCouponPo playerCouponPo;
    }

    public PlayerInfo parseFromBaseData(Map<String, Object> data) {
        Integer playerId = (Integer) data.get("guid");
        byte[] sourceData = (byte[]) data.get("base_data");
        byte[] deEncryptByteData = deEncrypt(sourceData);
        PlayerInfo playerInfo = getPlayerInfo(playerId.intValue(), deEncryptByteData);
        return playerInfo;
    }

    public List<PlayerRelationPo> parseFromWorldData(byte[] sourceData) {
        byte[] deEncryptByteData = deEncrypt(sourceData);
        List<PlayerRelationPo> playerRelationPos = getPlayRelations(deEncryptByteData);
        return playerRelationPos;
    }

    private List<PlayerRelationPo> getPlayRelations(byte[] deEncryptByteData) {
        List<PlayerRelationPo> relationPos = Lists.newArrayList();
        Map<Integer,Set<PlayerRelationPo>> relationMaps = Maps.newHashMap();
        ByteBuf byteBuf = Unpooled.buffer(deEncryptByteData.length);
        byteBuf.writeBytes(deEncryptByteData);
        byte curVersion = byteBuf.readByte();
        int count = byteBuf.readIntLE();
        byteBuf.writeBytes(deEncryptByteData);
        try {
            while (count-- > 0) {
                PlayerRelationPo playerRelationPo = new PlayerRelationPo();
                byte curPlayerVersion = byteBuf.readByte();
                Long guid = byteBuf.readLongLE();
                String name = readString(byteBuf);
                String otherName = readString(byteBuf);
                String headId = readString(byteBuf);
                byte sex = byteBuf.readByte();
                Integer costMoney = byteBuf.readIntLE();
                Long invitedGuid = byteBuf.readLongLE();
                int orderCount = byteBuf.readIntLE();
                while (orderCount-- > 0) {
                    String orderId = readString(byteBuf);
                    log.info("orderId = {}",orderId);
                }
                int childrenCount = byteBuf.readIntLE();
                while (childrenCount-- > 0) {
                    Long childrenId = byteBuf.readLongLE();
                    log.info("childrenId = {}",childrenId);
                }
                boolean isAgent = byteBuf.readBoolean();
                if(curPlayerVersion >= 2){
                    byte chooseType = byteBuf.readByte();
                }
                playerRelationPo.setAgentPlayerId(invitedGuid.intValue());
                playerRelationPo.setLastUpdateTime(System.currentTimeMillis());
                playerRelationPo.setPlayerId(guid.intValue());
                Set<PlayerRelationPo> playerRelationPos =relationMaps.get(invitedGuid.intValue());
                if(playerRelationPos == null){
                    playerRelationPos = Sets.newHashSet();
                    relationMaps.put(invitedGuid.intValue(),playerRelationPos);
                }
                playerRelationPos.add(playerRelationPo);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        relationMaps.forEach((inviteId,relationPoSet)->{
            relationPos.addAll(relationPoSet);
        });
        return relationPos;
    }

    private PlayerInfo getPlayerInfo(Integer playerId, byte[] data) {

        ByteBuf byteBuf = Unpooled.buffer(data.length);
        byteBuf.writeBytes(data);
        byte curVersion = byteBuf.readByte();
        String name = readString(byteBuf);

        Long goldCount = byteBuf.readLongLE();
        Long money = byteBuf.readLongLE();

        PlayerCouponPo playerCouponPo = new PlayerCouponPo();
        playerCouponPo.setSilverCount(money.intValue());
        playerCouponPo.setGoldCount(goldCount.intValue());
        playerCouponPo.setPlayerId(playerId);
        playerCouponPo.setLastUpdateTime(System.currentTimeMillis());

        PlayerPo playerPo = new PlayerPo();
        playerPo.setPlayerId(playerId);
        playerPo.setStatus(1);
        playerPo.setName(name);

        playerPo.setInsertTime(System.currentTimeMillis());
        playerPo.setLastUpdateTime(System.currentTimeMillis());


        PlayerInfo playerInfo = new PlayerInfo();
        playerInfo.setPlayerCouponPo(playerCouponPo);
        playerInfo.setPlayerPo(playerPo);
        return playerInfo;
    }

    private String readString(ByteBuf byteBuf) {
        int startIdx = byteBuf.readerIndex(), endIdx = startIdx;
        while (byteBuf.readByte() != 0) {
        }
        endIdx = byteBuf.readerIndex();

        String str = new String(byteBuf.array(), startIdx, endIdx - 1-startIdx);
        return str;
    }

    private byte[] deEncrypt(byte[] sourceData) {
        byte[] responseData = sourceData;
        Request request = httpClient.POST("http://121.196.195.248:97/DataDecompressWeb/ClientQuery.aspx?method=DeCompressData");
        request.timeout(5000, TimeUnit.MILLISECONDS).content(new BytesContentProvider(sourceData));
        try {
            ContentResponse response = request.send();
            responseData = response.getContent();

        } catch (Exception e) {

        }
        return responseData;
    }
}
