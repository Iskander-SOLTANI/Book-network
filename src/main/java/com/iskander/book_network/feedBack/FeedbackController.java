package com.iskander.book_network.feedBack;

import com.iskander.book_network.common.PageResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("feedbacks")
@Tag(name = "feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<Long> saveFeedback (
            @Valid @RequestBody FeedbackRequest feedbackRequest,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(feedbackService.save(feedbackRequest,connectedUser));
    }

    @GetMapping("/book/{book-id}")
    public ResponseEntity<PageResponse<FeedbackResponse>> getAllFeedbackByBook(
            @PathVariable("book-id") Long bookId,
            @RequestParam(value = "page", defaultValue = "0" , required = false) int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
            ){
        return ResponseEntity.ok(feedbackService.findFeedbackByBook(bookId,page,size,connectedUser));
    }


}
