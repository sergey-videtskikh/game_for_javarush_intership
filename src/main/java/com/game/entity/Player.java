package com.game.entity;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "player")
public class Player {
    @Id
    @GeneratedValue
    private Long id; //ID игрока
    private String name; //Имя персонажа (до 12 знаков включительно)
    private String title; //Титул персонажа (до 30 знаков включительно)
    @Enumerated(EnumType.STRING)
    private Race race; //Расса персонажа
    @Enumerated(EnumType.STRING)
    private Profession profession; //Профессия персонажа
    private Integer experience; //Опыт персонажа. Диапазон значений 0..10,000,000
    private Integer level; //Уровень персонажа
    private Integer untilNextLevel; //Остаток опыта до следующего уровня
    private Date birthday; //Дата регистрации    Диапазон значений года 2000..3000 включительно
    private Boolean banned; //Забанен / не забанен

    public Player() {
    }
}
