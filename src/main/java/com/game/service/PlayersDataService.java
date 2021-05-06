package com.game.service;


import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.repository.CustomizedPlayersCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class PlayersDataService {

    private Long count;

    @Autowired
    private CustomizedPlayersCrudRepository playersCrudRepository;

    @Transactional
    public List<Player> findAllPlayers(String name,
                                       String title,
                                       Race race,
                                       Profession profession,
                                       Long after,
                                       Long before,
                                       Integer minExperience,
                                       Integer maxExperience,
                                       Integer minLevel,
                                       Integer maxLevel,
                                       Boolean banned,
                                       Pageable pageable) {

        String strRace = (race == null) ? "" : race.name();
        String strProfession = (profession == null) ? "" : profession.name();
        if (name == null) name = "";
        if (title == null) title = "";
        Date dateAfter = (after == null) ? new Date(0L) : new Date(after);
        Date dateBefore = (before == null) ? new Date() : new Date(before);
        int minExp = (minExperience == null) ? 0 : minExperience;
        int maxExp = (maxExperience == null) ? Integer.MAX_VALUE : maxExperience;
        int minLvl = (minLevel == null) ? 0 : minLevel;
        int maxLvl = (maxLevel == null) ? Integer.MAX_VALUE : maxLevel;
        Collection<Boolean> banneds = new ArrayList<>();
        if (banned == null) {
            banneds.add(true);
            banneds.add(false);
        } else {
            banneds.add(banned);
        }


        /*count = playersCrudRepository.countByPlayersByNameContainingAndTitleContainingAndRaceContainingAndProfessionContainingAndBirthdayBetweenAndExperienceBetweenAndLevelBetweenAndBannedInAllIgnoreCase(
                name,
                title,
                strRace,
                strProfession,
                dateAfter,
                dateBefore,
                minExp,
                maxExp,
                minLvl,
                maxLvl,
                banneds
        );*/

        List<Player> result = playersCrudRepository.findPlayersByNameContainingAndTitleContainingAndRaceContainingAndProfessionContainingAndBirthdayBetweenAndExperienceBetweenAndLevelBetweenAndBannedInAllIgnoreCase(
                name,
                title,
                strRace,
                strProfession,
                dateAfter,
                dateBefore,
                minExp,
                maxExp,
                minLvl,
                maxLvl,
                banneds,
                pageable);

        count = new Long(playersCrudRepository.findPlayersByNameContainingAndTitleContainingAndRaceContainingAndProfessionContainingAndBirthdayBetweenAndExperienceBetweenAndLevelBetweenAndBannedInAllIgnoreCase(
                name,
                title,
                strRace,
                strProfession,
                dateAfter,
                dateBefore,
                minExp,
                maxExp,
                minLvl,
                maxLvl,
                banneds,
                Pageable.unpaged()
        ).size());

        return result;
    }

    @Transactional
    public Long count(String name,
                      String title,
                      Race race,
                      Profession profession,
                      Long after,
                      Long before,
                      Integer minExperience,
                      Integer maxExperience,
                      Integer minLevel,
                      Integer maxLevel,
                      Boolean banned
    ) {
        return count;
    }

    @Transactional
    public Player createPLayer(Player player) {
        Integer lvl = calcLevel(player.getExperience());
        Integer untilNextLevel = calcUntilNextLevel(lvl, player.getExperience());
        player.setLevel(lvl);
        player.setUntilNextLevel(untilNextLevel);

        if (player.getBanned() == null) player.setBanned(false);

        return playersCrudRepository.save(player);
    }

    @Transactional
    public Optional<Player> findById(Long id) {
        return playersCrudRepository.findById(id);
    }

    public Player save(Player player) {
        return playersCrudRepository.save(player);
    }

    public void deleteById(Long id) {
        playersCrudRepository.deleteById(id);
    }

    public Player replacePlayer(Player newPlayer, Long id) {
        return playersCrudRepository.findById(id)
                .map(player -> {
                    player.setName(newPlayer.getName());
                    player.setTitle(newPlayer.getTitle());
                    player.setRace(newPlayer.getRace());
                    player.setProfession(newPlayer.getProfession());
                    player.setBirthday(newPlayer.getBirthday());
                    player.setExperience(newPlayer.getExperience());
                    Integer lvl = calcLevel(newPlayer.getExperience());
                    Integer untilNextLevel = calcUntilNextLevel(lvl, newPlayer.getExperience());
                    player.setLevel(lvl);
                    player.setUntilNextLevel(untilNextLevel);
                    player.setBanned(newPlayer.getBanned());
                    return playersCrudRepository.save(player);
                })
                .orElseGet(() -> {
                    newPlayer.setId(id);
                    return playersCrudRepository.save(newPlayer);
                });
    }

    private Integer calcLevel(Integer exp){
        return new Integer((int)((Math.sqrt(2500 + 200. * exp) - 50) / 100));// (Math.sqrt(2500 + 200 * experince) - 50) / 100;
    }

    private Integer calcUntilNextLevel(Integer lvl, Integer exp) {
        return 50 * (lvl + 1) * (lvl + 2) - exp;
    }


}
