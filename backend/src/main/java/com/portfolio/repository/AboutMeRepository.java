package com.portfolio.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.portfolio.entity.AboutMe;

@Repository
public interface AboutMeRepository extends CrudRepository<AboutMe, Integer> {
    
}
