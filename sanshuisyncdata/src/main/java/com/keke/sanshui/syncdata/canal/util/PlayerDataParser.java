package com.keke.sanshui.syncdata.canal.util;

import com.keke.sanshui.base.admin.po.PlayerCouponPo;
import com.keke.sanshui.base.admin.po.PlayerPo;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.Data;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.BytesContentProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Repository
public final class PlayerDataParser {

    @Autowired
    private HttpClient httpClient;
    @Data
    public static class PlayerInfo{
        private PlayerPo playerPo;
        private PlayerCouponPo playerCouponPo;
    }
    public PlayerInfo parseFromBaseData(Map<String,Object> data){
        Integer playerId = (Integer)data.get("guid");
        byte[] baseData = (byte[])data.get("base_data");

        byte[] responseData = baseData;
        Request request = httpClient.POST("http://121.196.195.248:97/DataDecompressWeb/ClientQuery.aspx?method=DeCompressData");
        request.timeout(5000, TimeUnit.MILLISECONDS).content(new BytesContentProvider(baseData));
        try {
            ContentResponse response = request.send();
            responseData = response.getContent();

        }catch (Exception e){

        }
        PlayerInfo playerInfo = getPlayerInfo(playerId.intValue(),responseData);
        return playerInfo;
    }
    private PlayerInfo getPlayerInfo(Integer playerId,byte[]data){
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        byte curVersion = byteBuffer.get();
        String name = readString(byteBuffer);
        Long goldCount = byteBuffer.getLong();
        Long money = byteBuffer.getLong();

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

    private String readString(ByteBuffer byteBuffer){
        int startIdx =  byteBuffer.position(),endIdx = startIdx;
        while (byteBuffer.get() != 0){
            endIdx++;
        }
        String str = new String(byteBuffer.array(),startIdx,endIdx-1);
        return str;
    }
}
