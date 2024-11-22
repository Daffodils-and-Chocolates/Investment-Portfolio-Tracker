package com.example.demo.repository;

import com.example.demo.models.entity.Watchlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface WatchlistRepository extends JpaRepository<Watchlist, Long> {

    @Query("SELECT w FROM Watchlist w WHERE w.group.id = :groupId")
    List<Watchlist> findWatchlistsByGroupId(@Param("groupId") Long groupId);

    @Query("SELECT w FROM Watchlist w WHERE w.user.id = :userId")
    List<Watchlist> findWatchlistsByUserId(@Param("userId") Long userId);

}