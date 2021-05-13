package com.game.controller;


import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayersDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.xml.bind.ValidationException;
import java.util.*;

@RestController
@RequestMapping("/rest/players")
@Validated
public class PlayersController {

    Logger logger = LoggerFactory.getLogger(PlayersController.class);

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

        List<Player> result = service.findAllPlayers(
                name,
                title,
                race,
                profession,
                after,
                before,
                minExperience,
                maxExperience,
                minLevel,
                maxLevel,
                banned,
                sorted);
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

        return service.count(name,
                title,
                race,
                profession,
                after,
                before,
                minExperience,
                maxExperience,
                minLevel,
                maxLevel,
                banned);
    }

    @PostMapping()
    public Player newPlayer(@RequestBody Player newPlayer) {
        System.out.println("PlayersController.newPlayer");
        System.out.println("newPlayer = " + newPlayer);
        System.out.println("newPLayer is null: " + newPlayer == null );
        Long longId = null;
        try {
            if (newPlayer.getName() == null ||
                newPlayer.getTitle() == null ||
                newPlayer.getRace() == null ||
                newPlayer.getProfession() == null ||
                newPlayer.getBirthday() == null ||
                newPlayer.getExperience() == null ||
                newPlayer.getBanned() == null) throw new ValidationException("Content is empty");
            if (newPlayer.getExperience() < 0) throw new ValidationException("Experience less then 0");
            if (newPlayer.getExperience() > 10_000_000) throw new ValidationException("Experience greater then 10_000_0000");
            if (newPlayer.getBirthday().getTime() < 0) throw new ValidationException("Invalid birthday");
            if (newPlayer.getTitle().length() > 30) throw new ValidationException("Title length is too big");
        } catch (Exception e) {
            System.out.println("e = " + e);
            if (e instanceof ValidationException)
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Parameters is not valid", e);
        }
        System.out.println("newPlayer = " + newPlayer);

        return service.createPLayer(newPlayer);
    }

    @GetMapping("/{id}")
    Player one(@PathVariable String id) {
        /*System.out.println("method one:");
        System.out.println(id);*/
        Player player = null;
        Long longId = null;
        try {
            longId = Long.parseLong(id);
            if (longId <= 0) throw new ValidationException("Id is zero or less");
            player = service.findById(longId).get();
        } catch (Exception e) {
//            System.out.println(e);
            if (e instanceof ValidationException || e instanceof NumberFormatException)
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Id is not valid", e);
            if (e instanceof NoSuchElementException)
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Player not found", e);
        }

        return player;
        /*return service.findById(id)
                .orElseThrow(
                        () -> new PlayerNotFoundException(id)
                );*/
    }

    @PostMapping("/{id}")
    Player replacePlayer(@RequestBody Player newPlayer, @PathVariable String id) {
        /*System.out.println("method replace:");
        System.out.println(id);*/
        Player player = null;
        Long longId = null;
//        System.out.println("newPlayer = " + newPlayer + ", id = " + id);
        try {
            longId = Long.parseLong(id);
            if (longId <= 0) throw new ValidationException("Id is zero");
            if (newPlayer.getExperience() < 0) throw new ValidationException("Experience less then 0");
            if (newPlayer.getExperience() > 10_000_000) throw new ValidationException("Experience greater then 10_000_0000");
            if (newPlayer.getBirthday().getTime() < 0) throw new ValidationException("Invalid birthday");
            player = service.findById(longId).get();
        } catch (Exception e) {
//            System.out.println(e);
            if (e instanceof ValidationException
                || e instanceof NumberFormatException)
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Parameters is not valid", e);
            if (e instanceof NoSuchElementException)
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Player not found", e);
        }


        /*System.out.println("longId = " + longId);
        System.out.println("newPlayer = " + newPlayer);*/
        return service.replacePlayer(newPlayer, longId);
    }


    @DeleteMapping("/{id}")
    void deletePlayer(@PathVariable String id) {


        Player player = null;
        Long longId = null;
        try {
            longId = Long.parseLong(id);
            if (longId <= 0) throw new ValidationException("Id is zero or less");
            player = service.findById(longId).get();
            service.deleteById(longId);
        } catch (Exception e) {
//            System.out.println(e);
            if (e instanceof ValidationException || e instanceof NumberFormatException)
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Id is not valid", e);
            if (e instanceof NoSuchElementException)
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Player not found", e);
        }

    }


}
