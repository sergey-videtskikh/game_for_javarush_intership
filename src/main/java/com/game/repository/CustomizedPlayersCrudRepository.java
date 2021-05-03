package com.game.repository;

import com.game.entity.Player;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomizedPlayersCrudRepository extends PagingAndSortingRepository<Player, Long> {

}
