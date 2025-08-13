package com.iskander.book_network.history;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookTransactionHistoryRepository extends JpaRepository<BookTransactionHistory, Long> {

   @Query ("""
            SELECT history
            FROM BookTransactionHistory history
            WHERE history.user.id = :userId
            """)
   Page<BookTransactionHistory> findAllBorrowedBooks(Pageable pageable, Long userId) ;

    @Query ("""
            SELECT history
            FROM BookTransactionHistory history
            WHERE history.book.owner.id = :userId
            """)
    Page<BookTransactionHistory> findAllReturnedBooks(Pageable pageable, Long userId);

    @Query ("""
            SELECT (COUNT (*) > 0) AS isBorrowed
            FROM BookTransactionHistory history
            WHERE history.user.id = :userId
              and history.book.id = :bookId
              and history.returnedApproved = false
            """)
    boolean isAlreadyBorrowedByUser(long userId, long bookId);

    @Query ("""
            SELECT history
            FROM BookTransactionHistory history
            WHERE history.user.id = :userId
              and history.book.id = :bookId
              and history.returnedApproved = false
              and history.returned = false
            """)
    Optional<BookTransactionHistory> findByBookIdAndUserId(Long bookId, Long userId);

    @Query ("""
            SELECT history
            FROM BookTransactionHistory history
            WHERE history.book.owner.id = :userId
              and history.book.id = :bookId
              and history.returnedApproved = false
              and history.returned = true
            """)
    Optional<BookTransactionHistory> findByBookIdAndOwnerId(Long bookId, Long userId);
}

