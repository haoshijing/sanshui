package com.keke.sanshui.base.admin.service;

import com.keke.sanshui.base.admin.dao.PlayerDAO;
import com.keke.sanshui.base.admin.po.PlayerPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {
    @Autowired
    private PlayerDAO playerDAO;

    public List<PlayerPo> selectList(Integer maxPlayerId, Integer limit){
       return playerDAO.queryPlayerList(maxPlayerId,limit);
    }
}
