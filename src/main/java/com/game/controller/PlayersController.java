package com.game.controller;


import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayersDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/rest/players")
public class PlayersController {


    @Autowired
    private final PlayersDataService service;

    PlayersController(PlayersDataService service) {
        this.service = service;
    }

    @GetMapping()
    List<Player> all(@RequestParam(required = false) String name,
                     @RequestParam(required = false) String title,
                     @RequestParam(required = false) Race race,
                     @RequestParam(required = false) Profession profession,
                     @RequestParam(required = false) Long after,
                     @RequestParam(required = false) Long before,
                     @RequestParam(required = false) Boolean banned,
                     @RequestParam(required = false) Integer minExperience,
                     @RequestParam(required = false) Integer maxExperience,
                     @RequestParam(required = false) Integer minLevel,
                     @RequestParam(required = false) Integer maxLevel,
                     @RequestParam(required = false) PlayerOrder order,
                     @RequestParam(required = false) Integer pageNumber,
                     @RequestParam(required = false) Integer pageSize) {
        int pgN = (pageNumber == null) ? 0 : pageNumber;
        int pgS = (pageSize == null) ? 3 : pageSize;
        String ord = (order == null) ? "id" : order.getFieldName();
        Pageable sorted = PageRequest.of(pgN, pgS, Sort.by(ord));
        List<Player> result = service.findAllPlayers(sorted);
        return result;
    }

    @GetMapping("/count")
    Long count(@RequestParam(required = false) String name,
                  @RequestParam(required = false) String title,
                  @RequestParam(required = false) Race race,
                  @RequestParam(required = false) Profession profession,
                  @RequestParam(required = false) Long after,
                  @RequestParam(required = false) Long before,
                  @RequestParam(required = false) Boolean banned,
                  @RequestParam(required = false) Integer minExperience,
                  @RequestParam(required = false) Integer maxExperience,
                  @RequestParam(required = false) Integer minLevel,
                  @RequestParam(required = false) Integer maxLevel) {
        return service.count();
    }
}
