package com.galibi.resellerledger.repositories;

import com.galibi.resellerledger.entities.Platform;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PlatformRepository extends JpaRepository<Platform, Long> {

  Optional<Platform> findByName(String name);
}

