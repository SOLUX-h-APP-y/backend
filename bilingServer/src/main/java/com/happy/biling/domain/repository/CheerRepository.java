package com.happy.biling.domain.repository;

import com.happy.biling.domain.entity.Cheer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CheerRepository extends JpaRepository<Cheer, Long> {
    // 특정 사용자가 받은 총 응원 횟수 조회
    @Query("SELECT COUNT(c) FROM Cheer c WHERE c.receiverId = :receiverId")
    long countByReceiverId(@Param("receiverId") Long receiverId);

    boolean existsBySenderIdAndReceiverId(Long senderId, Long receiverId);
}


