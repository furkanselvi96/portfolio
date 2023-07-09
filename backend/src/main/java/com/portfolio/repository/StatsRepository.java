package com.portfolio.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.portfolio.entity.Stats;

@Repository
public interface StatsRepository extends CrudRepository<Stats, Integer> {
    
}
