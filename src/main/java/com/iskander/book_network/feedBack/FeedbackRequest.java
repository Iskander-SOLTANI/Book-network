package com.iskander.book_network.feedBack;

import jakarta.validation.constraints.*;

public record FeedbackRequest(
        @Positive(message = "200")
        @Max(message = "201" , value = 5)
        @Min(message = "202", value = 0)
        Double note,

        @NotEmpty(message = "203")
        @NotEmpty(message = "203")
        @NotBlank(message = "203")
        String comment,

        @NotNull(message = "204")
        Long bookId
) {
}
