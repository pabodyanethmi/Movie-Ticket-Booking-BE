package lk.ijse.backend.controller;

import jakarta.validation.Valid;
import lk.ijse.backend.dto.ReviewDTO;
import lk.ijse.backend.dto.UserDTO;
import lk.ijse.backend.service.ReviewService;
import lk.ijse.backend.service.UserService;
import lk.ijse.backend.util.StandardResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final UserService userService;

    public ReviewController(ReviewService reviewService, UserService userService) {
        this.reviewService = reviewService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<StandardResponse<ReviewDTO>> addReview(
            Authentication authentication,
            @Valid @RequestBody ReviewDTO reviewDTO) {
        UserDTO user = userService.getUserByEmail(authentication.getName());
        ReviewDTO created = reviewService.addReview(user.getId(), reviewDTO);
        return new ResponseEntity<>(
                new StandardResponse<>(HttpStatus.CREATED.value(), "Review posted successfully", created),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<StandardResponse<List<ReviewDTO>>> getReviewsByMovie(@PathVariable Long movieId) {
        List<ReviewDTO> reviews = reviewService.getReviewsByMovieId(movieId);
        return ResponseEntity.ok(
                new StandardResponse<>(HttpStatus.OK.value(), "Movie reviews fetched successfully", reviews)
        );
    }

    @GetMapping("/user/my-reviews")
    public ResponseEntity<StandardResponse<List<ReviewDTO>>> getMyReviews(Authentication authentication) {
        UserDTO user = userService.getUserByEmail(authentication.getName());
        List<ReviewDTO> reviews = reviewService.getReviewsByUserId(user.getId());
        return ResponseEntity.ok(
                new StandardResponse<>(HttpStatus.OK.value(), "User reviews fetched successfully", reviews)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<StandardResponse<Void>> deleteReview(
            Authentication authentication,
            @PathVariable Long id) {
        UserDTO user = userService.getUserByEmail(authentication.getName());
        reviewService.deleteReview(id, user.getId());
        return ResponseEntity.ok(
                new StandardResponse<>(HttpStatus.OK.value(), "Review deleted successfully", null)
        );
    }
}
