package com.iskander.book_network.feedBack;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FeedbackRepository extends JpaRepository<FeedBack, Long> {

    @Query("""
            SELECT f
            FROM FeedBack f
            WHERE f.book.id =: bookId
            """)
    Page<FeedBack> findAllByBookId(Long bookId, Pageable pageable);
}
