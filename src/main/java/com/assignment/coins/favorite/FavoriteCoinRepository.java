package com.assignment.coins.favorite;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface FavoriteCoinRepository extends JpaRepository<FavoriteCoin, FavoriteCoinPkId> {

  @Query(value = """
      SELECT fp FROM FavoriteCoin fp WHERE fp.user.id = :userId AND fp.coin.coinId = :coinId
      """)
  Optional<FavoriteCoin> findByUserIdAndCoinId(String userId, String coinId);

  @Query(value = """
            SELECT fp FROM FavoriteCoin fp
            LEFT JOIN FETCH fp.user u
            WHERE fp.user.id = :userId
      """)
  List<FavoriteCoin> findAllByUserId(String userId);
}
