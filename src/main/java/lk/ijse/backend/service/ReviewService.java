package lk.ijse.backend.service;

import lk.ijse.backend.dto.ReviewDTO;

import java.util.List;

public interface ReviewService {
    ReviewDTO addReview(Long userId, ReviewDTO reviewDTO);
    List<ReviewDTO> getReviewsByMovieId(Long movieId);
    List<ReviewDTO> getReviewsByUserId(Long userId);
    void deleteReview(Long id, Long userId);
}
