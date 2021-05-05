package com.game.repository;

import com.game.entity.Player;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

public class CustomizedPlayersImpl implements CustomizedPlayers{

    @PersistenceContext
    private EntityManager em;

    @Override
    public List getAllPlayersQuery(String name) {
        TypedQuery query = em.createQuery(
                "select a from Player a where a.name = ?1", Player.class);
        query.setParameter(1, name);

        return query.getResultList();
    }
}
