package com.jt.tours.service;

import com.jt.tours.domain.Tour;
import com.jt.tours.domain.TourRating;
import com.jt.tours.repository.TourRatingRepository;
import com.jt.tours.repository.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Tour Rating Service Interface
 *
 * Created by Jason Tao on 5/30/2020
 */
public interface ITourRatingService {

    void createNewRating(Long tourId, Long customerId, Integer ratingScore, String comment);

    void createNewRatings(Long tourId, Long[] customerId, Integer ratingScore);

    Page<TourRating> getTourRatings(Long tourId, Pageable pageable);

    TourRating updateRating(Long tourId, Long customerId, Integer ratingScore, String comment);

    TourRating updateSomeRating(Long tourId, Long customerId, Integer ratingScore, String comment);

    void delete(Long tourId, Long customerId);
}
