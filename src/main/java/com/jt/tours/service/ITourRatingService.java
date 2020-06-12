package com.jt.tours.service;

import com.jt.tours.domain.TourRating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Tour Rating Service Interface
 *
 * Created by Jason Tao on 5/30/2020
 */
public interface ITourRatingService {

    TourRating createNewRating(Long tourId, Long customerId, Integer ratingScore, String comment);

    List<TourRating> createNewRatings(Long tourId, Long[] customerId, Integer ratingScore);

    Optional<TourRating> searchRatingById(Long id);

    List<TourRating> searchAllRatings();

    Page<TourRating> getTourRatings(Long tourId, Pageable pageable);

    TourRating updateRating(Long tourId, Long customerId, Integer ratingScore, String comment);

    TourRating updateSomeRating(Long tourId, Long customerId, Integer ratingScore, String comment);

    void delete(Long tourId, Long customerId);
}
