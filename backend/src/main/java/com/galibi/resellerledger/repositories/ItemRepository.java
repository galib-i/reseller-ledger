package com.galibi.resellerledger.repositories;

import com.galibi.resellerledger.entities.Item;
import com.galibi.resellerledger.entities.User;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

  Optional<Item> findByName(String name);

  List<Item> findByUserAndDeletedAtIsNull(User user);

  Optional<Item> findByIdAndUserAndDeletedAtIsNull(Long id, User user);
}
