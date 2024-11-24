package com.example.demo.repository;

import com.example.demo.models.entity.WatchlistGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WatchlistGroupRepository extends JpaRepository<WatchlistGroup, String> {
    boolean existsByGroupName(String groupName);
    Optional<WatchlistGroup> findByGroupName(String groupName);
    void deleteByGroupName(String groupName);
}
