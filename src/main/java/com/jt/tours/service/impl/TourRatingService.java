package com.jt.tours.service.impl;

import com.jt.tours.domain.Tour;
import com.jt.tours.domain.TourRating;
import com.jt.tours.repository.TourRatingRepository;
import com.jt.tours.repository.TourRepository;
import com.jt.tours.service.ITourRatingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Tour Rating Service
 *
 * Created by Jason Tao on 5/30/2020
 */
@Slf4j
@Service
@Transactional
public class TourRatingService implements ITourRatingService {

    private TourRatingRepository tourRatingRepository;

    private TourRepository tourRepository;

    @Autowired
    public TourRatingService(TourRatingRepository tourRatingRepository, TourRepository tourRepository) {
        this.tourRatingRepository = tourRatingRepository;
        this.tourRepository = tourRepository;
    }

    /**
     * Create a new Tour Rating in the database.
     *
     * @param tourId the tour identifier
     * @param customerId the customer identifier
     * @param ratingScore score of the tour rating
     * @param comment comment given about the tour
     * @throws NoSuchElementException if no Tour found
     */
    @Override
    public void createNewRating(Long tourId, Long customerId, Integer ratingScore, String comment) throws NoSuchElementException {

        log.info("Create tour rating for tour {} of customer {}", tourId, customerId);
        Tour targetTour = findTour(tourId);
        tourRatingRepository.save(TourRating.builder()
                                    .tour(targetTour)
                                    .customerId(customerId)
                                    .ratingScore(ratingScore)
                                    .comment(comment).build());
    }

    /**
     * Give the same score from multiple customers on a tour.
     *
     * @param tourId tour identifier
     * @param ratingScore score of tour rating
     * @param customerIds the id of multiple customers.
     */
    public void createNewRatings(Long tourId, Long[] customerIds, Integer ratingScore) {

        log.info("Create tour rating for tour {} of customers {}", tourId, Arrays.asList(customerIds).toString());
        tourRepository.findById(tourId).ifPresent(tour -> {
            for(Long id : customerIds) {
                tourRatingRepository.save(TourRating.builder()
                        .tour(tour)
                        .ratingScore(ratingScore)
                        .build());
            }
        });
    }

    /**
     * Find the tour rating by tour rating id.
     *
     * @param id the tour rating identifier
     * @return the tour rating if found
     */
    @Override
    public Optional<TourRating> searchRatingById(Long id) {

        log.info("Find tour rating by rating {}", id);
        return tourRatingRepository.findById(id);
    }

    /**
     * Find all tour ratings.
     *
     * @return list of tour ratings
     */
    @Override
    public List<TourRating> searchAllRatings() {

        log.info("Find all tour ratings available");
        return tourRatingRepository.findAll();
    }

    /**
     * Find a page of tour ratings for a tour
     *
     * @param tourId tour identifier
     * @param pageable page parameters to determine which item to fetch
     * @return page of the any found TourRatings
     * @throws NoSuchElementException if not tour rating found
     */
    @Override
    public Page<TourRating> getTourRatings(Long tourId, Pageable pageable) throws NoSuchElementException {

        log.info("Retrieve tour rating for tour {}", tourId);
        Tour targetTour = findTour(tourId);
        return tourRatingRepository.findByTourId(targetTour.getId(), pageable);
    }

    /**
     * Updates a tour rating.
     *
     * @param tourId tour identifier
     * @param customerId customer identifier
     * @param ratingScore score of tour rating
     * @param comment comment given about the tour
     * @return Tour Rating Domain Object
     */
    @Override
    public TourRating updateRating(Long tourId, Long customerId, Integer ratingScore, String comment) {

        TourRating targetRating = findTourRating(tourId, customerId);

        //update the rating
        targetRating.setRatingScore(ratingScore);
        targetRating.setComment(comment);

        return tourRatingRepository.save(targetRating);
    }

    /**
     * Updates some of the rating.
     *
     * @param tourId tour identifier
     * @param customerId customer identifier
     * @param ratingScore score of tour rating
     * @param comment comment given about the tour
     * @return Tour Rating Domain Object
     */
    @Override
    public TourRating updateSomeRating(Long tourId, Long customerId, Integer ratingScore, String comment) {

        TourRating targetRating = findTourRating(tourId, customerId);

        if(ratingScore != null && ratingScore > 0) {
            targetRating.setRatingScore(ratingScore);
        }
        if(comment != null) {
            targetRating.setComment(comment);
        }

        return tourRatingRepository.save(targetRating);
    }

    /**
     * Delete a tour rating
     *
     * @param tourId tour identifier
     * @param customerId customer identifier
     * @throws NoSuchElementException if not tour rating found
     */
    @Override
    public void delete(Long tourId, Long customerId) throws NoSuchElementException {
        TourRating rating = findTourRating(tourId, customerId);
        tourRatingRepository.delete(rating);
    }

    /**
     * Calculate the average rating score of a tour
     *
     * @param tourId tour identifier
     * @return the average score as a Double
     * @throws NoSuchElementException if not tour rating found
     */
    public Double getAverageRatingScore(Long tourId) throws NoSuchElementException {

        List<TourRating> ratings = tourRatingRepository.findByTourId(tourId);
        OptionalDouble average = ratings.stream().mapToInt( (rating) -> rating.getRatingScore()).average();
        return average.isPresent() ? average.getAsDouble() : null;
    }

    /**
     * Check and return the existed tour.
     *
     * @param tourId the tour identifier
     * @return the found tour
     * @throws NoSuchElementException if no tour is found
     */
    private Tour findTour(Long tourId) throws NoSuchElementException {
        return tourRepository.findById(tourId).orElseThrow( () ->
                new NoSuchElementException("Tour does not exist " + tourId));
    }

    /**
     * Check and return the existed tour rating.
     *
     * @param tourId tour identifier
     * @param customerId customer identifier
     * @return tour rating if found
     * @throws NoSuchElementException if no tour is found
     */
    private TourRating findTourRating(Long tourId, Long customerId) throws NoSuchElementException {
        return tourRatingRepository.findByTourIdAndCustomerId(tourId, customerId).orElseThrow( () ->
                new NoSuchElementException("Tour rating does not exist for tourId(" + tourId + ") for customer( " + customerId + ")"));
    }
}
