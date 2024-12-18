package com.example.demo.repository;

import com.example.demo.models.entity.Stock;
import com.example.demo.models.entity.Watchlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface WatchlistRepository extends JpaRepository<Watchlist, Long> {

    @Query("SELECT w.stock FROM Watchlist w " +
            "WHERE w.user.userId = :userId AND w.group.groupId = " +
            "(SELECT g.groupId FROM WatchlistGroup g WHERE g.groupName = :groupName)")
    List<Stock> findStocksByUserIdAndGroupName(@Param("userId") Long userId, @Param("groupName") String groupName);

    @Query("SELECT w.stock FROM Watchlist w WHERE w.user.userId = :userId")
    List<Stock> findAllStocksByUserId(@Param("userId") Long userId);

    @Query("SELECT DISTINCT w.group.groupName FROM Watchlist w WHERE w.user.userId = :userId")
    List<String> findDistinctGroupNamesByUserId(@Param("userId") Long userId);

    List<Watchlist> findByGroupGroupName(String groupName);

    boolean existsByStockStockIdAndGroupGroupNameNot(Long stockId, String groupName);

    @Query("SELECT DISTINCT w.group.groupName FROM Watchlist w " +
            "WHERE w.user.userId = :userId AND w.stock.stockId = :stockId")
    List<String> findGroupNamesByUserIdAndStockId(@Param("userId") Long userId, @Param("stockId") Long stockId);
}
