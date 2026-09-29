package lk.ijse.backend.service.impl;

import lk.ijse.backend.dto.ReviewDTO;
import lk.ijse.backend.entity.Movie;
import lk.ijse.backend.entity.Review;
import lk.ijse.backend.entity.User;
import lk.ijse.backend.exception.ResourceNotFoundException;
import lk.ijse.backend.exception.UnauthorizedException;
import lk.ijse.backend.repository.MovieRepository;
import lk.ijse.backend.repository.ReviewRepository;
import lk.ijse.backend.repository.UserRepository;
import lk.ijse.backend.service.ReviewService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository,
                             UserRepository userRepository,
                             MovieRepository movieRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
    }

    @Override
    @Transactional
    public ReviewDTO addReview(Long userId, ReviewDTO reviewDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Movie movie = movieRepository.findById(reviewDTO.getMovieId())
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + reviewDTO.getMovieId()));

        Review review = reviewRepository.findByUserIdAndMovieId(userId, movie.getId())
                .orElseGet(() -> Review.builder().user(user).movie(movie).build());

        review.setRating(reviewDTO.getRating());
        review.setComment(reviewDTO.getComment().trim());
        review.setCreatedAt(LocalDateTime.now());

        Review saved = reviewRepository.save(review);

        Double avgRating = reviewRepository.getAverageRatingByMovieId(movie.getId());
        if (avgRating != null) {
            movie.setRating(Math.round(avgRating * 10.0) / 10.0);
            movieRepository.save(movie);
        }

        return mapToDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewDTO> getReviewsByMovieId(Long movieId) {
        return reviewRepository.findByMovieIdOrderByCreatedAtDesc(movieId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewDTO> getReviewsByUserId(Long userId) {
        return reviewRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteReview(Long id, Long userId) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + id));

        if (!review.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to delete this review");
        }

        Movie movie = review.getMovie();
        reviewRepository.deleteById(id);

        Double avgRating = reviewRepository.getAverageRatingByMovieId(movie.getId());
        movie.setRating(avgRating != null ? Math.round(avgRating * 10.0) / 10.0 : 0.0);
        movieRepository.save(movie);
    }

    private ReviewDTO mapToDTO(Review review) {
        return ReviewDTO.builder()
                .id(review.getId())
                .userId(review.getUser().getId())
                .userName(review.getUser().getName())
                .movieId(review.getMovie().getId())
                .movieTitle(review.getMovie().getTitle())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .build();
    }
}
