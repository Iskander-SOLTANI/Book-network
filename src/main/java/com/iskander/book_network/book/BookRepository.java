package com.iskander.book_network.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> , JpaSpecificationExecutor<Book> {

   @Query("""
        select b
        from Book b
        where b.shareable = true
        and b.archived = false
        and b.createdBy != :userId
        """)
    Page<Book> findAllDisplayableBooks(Pageable pageable, Long userId);
}
