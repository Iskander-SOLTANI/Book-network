package com.iskander.book_network.feedBack;

import com.iskander.book_network.book.Book;
import org.springframework.stereotype.Service;

import java.util.Objects;
@Service
public class FeedbackMapper {
    public FeedBack toFeedback(FeedbackRequest feedbackRequest) {
        return  FeedBack.builder()
                .note(feedbackRequest.note())
                .comment(feedbackRequest.comment())
                .book(Book.builder()
                        .id(feedbackRequest.bookId())
                        .archived(false)
                        .shareable(false)
                        .build())
                .build();
    }

    public FeedbackResponse toFeedbackResponse(FeedBack feedBack, Long id) {
        return FeedbackResponse.builder()
                .note(feedBack.getNote())
                .comment(feedBack.getComment())
                .ownFeedback(Objects.equals(id, feedBack.getCreatedBy()))
                .build();
    }
}
