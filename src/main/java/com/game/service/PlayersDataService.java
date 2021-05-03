package com.game.service;


import com.game.entity.Player;
import com.game.repository.CustomizedPlayersCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PlayersDataService {

    @Autowired
    private CustomizedPlayersCrudRepository playersCrudRepository;

    @Transactional
    public List<Player> findAllPlayers(Pageable pageable) {
        List<Player> result = new ArrayList<Player>();
        for (Player plr : playersCrudRepository.findAll(pageable)) {
            result.add(plr);
        }
        return result;
    }

    @Transactional
    public Long count() {
        return playersCrudRepository.count();
    }


}
