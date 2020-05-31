package com.jt.tours.repository;

import com.jt.tours.domain.TourRating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;
import java.util.Optional;

/**
 * Created by Jason Tao on 5/30/2020
 */
@RepositoryRestResource(exported = false)
public interface TourRatingRepository extends JpaRepository<TourRating, Integer> {

    /**
     * Find a list of TourRating by tour id.
     *
     * @param tourId is the tour identifier
     * @return list of any found TourRatings
     */
    List<TourRating> findByTourId(Long tourId);

    /**
     * Return a page of TourRatings for a tour
     *
     * @param tourId is the tour identifier
     * @param pageable details for the desired page
     * @return page of the any found TourRatings
     */
    Page<TourRating> findByTourId(Long tourId, Pageable pageable);

    /**
     * Return a TourRating by tour id and customer id.
     *
     * @param tourId is the tour identifier
     * @param customerId is the customer identifier
     * @return TourRating if found, null otherwise.
     */
    Optional<TourRating> findByTourIdAndCustomerId(Long tourId, Long customerId);

}
