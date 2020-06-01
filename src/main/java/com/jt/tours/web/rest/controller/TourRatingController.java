package com.jt.tours.web.rest.controller;

import com.jt.tours.domain.TourRating;
import com.jt.tours.service.impl.TourRatingService;
import com.jt.tours.web.rest.assembler.RatingAssembler;
import com.jt.tours.web.rest.dto.TourRatingDTO;
import com.jt.tours.web.rest.dto.mapper.TourRatingMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.AbstractMap;
import java.util.NoSuchElementException;

/**
 * Tour Rating Controller
 *
 * Created by Jason Tao on 5/30/2020
 */
@RequestMapping("/api/v1/tours/{tourId}/ratings")
@RestController
@Slf4j
public class TourRatingController {

    private static final String ROOT_API_PATH_PREFIX = "/api/v1";

    private TourRatingService tourRatingService;

    private TourRatingMapper tourRatingMapper;

    private RatingAssembler ratingAssembler;

    protected TourRatingController() {}

    @Autowired
    public TourRatingController(TourRatingService tourRatingService, TourRatingMapper tourRatingMapper, RatingAssembler ratingAssembler) {
        this.tourRatingService = tourRatingService;
        this.tourRatingMapper = tourRatingMapper;
        this.ratingAssembler = ratingAssembler;
    }

    /**
     * Create a Tour Rating.
     *
     * @param tourId tour identifier
     * @param tourRatingDTO the tour rating data transfer object
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createTourRating(@PathVariable("tourId") long tourId, @Valid @RequestBody TourRatingDTO tourRatingDTO) {
        log.info("POST {}/tours/{}/ratings", ROOT_API_PATH_PREFIX, tourId);
        tourRatingService.createNewRating(tourId, tourRatingDTO.getCustomerId(), tourRatingDTO.getRatingScore(), tourRatingDTO.getComment());
    }

    /**
     * Create a Tour Rating with the same rating score for multiple customers.
     *
     * @param tourId tour identifier
     * @param ratingScore rating score of the tour
     * @param customers array of customer id
     */
    @PostMapping("/{score}")
    @ResponseStatus(HttpStatus.CREATED)
    public void createTourRatings(@PathVariable("tourId") long tourId,
                                  @PathVariable("score") int ratingScore,
                                  @RequestParam("customers") Long[] customers) {
        log.info("POST {}/tours/{}/ratings/{}", ROOT_API_PATH_PREFIX, tourId, ratingScore);
        tourRatingService.createNewRatings(tourId, customers, ratingScore);
    }

    /**
     * Find all the rating associated with a given tour.
     *
     * @param tourId tour identifier
     * @param pageable page parameters to determine which item to fetch
     * @return page of Tour Rating DTO
     */
    @GetMapping
    public PagedModel<TourRatingDTO> getAllRatingsFromTour(@PathVariable("tourId") long tourId, Pageable pageable,
                                                           PagedResourcesAssembler pagedAssembler) {
        log.info("GET {}/tours/{}/ratings", ROOT_API_PATH_PREFIX, tourId);
        Page<TourRating> pagedTourRating = tourRatingService.getTourRatings(tourId, pageable);
        return pagedAssembler.toModel(pagedTourRating, ratingAssembler);
    }

    /**
     * Calculate the average score of a Tour.
     *
     * @param tourId tour identifier
     * @return Tuple of "average" and the average value.
     */
    @GetMapping("/average")
    public AbstractMap.SimpleEntry<String, Double> getAverage(@PathVariable("tourId") long tourId) {
        log.info("GET {}/tours/{}/ratings/average", ROOT_API_PATH_PREFIX, tourId);
        return new AbstractMap.SimpleEntry<String, Double>("average", tourRatingService.getAverageRatingScore(tourId));
    }

    /**
     * Update the score and the comment for a tour.
     *
     * @param tourId tour identifier
     * @param tourRatingDTO the tour rating data transfer object
     * @return the modified tour rating object
     */
    @PutMapping
    public TourRatingDTO updateTourRating(@PathVariable("tourId") long tourId, @Valid @RequestBody TourRatingDTO tourRatingDTO) {
        log.info("PUT {}/tours/{}/ratings", ROOT_API_PATH_PREFIX, tourId);
        return tourRatingMapper
                .tourRatingToTourRatingDTO(tourRatingService.updateRating(tourId, tourRatingDTO.getCustomerId(), tourRatingDTO.getRatingScore(), tourRatingDTO.getComment()));
    }

    /**
     * Update either the score or the comment for a tour.
     *
     * @param tourId tour identifier
     * @param tourRatingDTO the tour rating data transfer object
     * @return the modified tour rating object
     */
    @PatchMapping
    public TourRatingDTO updatePartialTourRating(@PathVariable("tourId") long tourId, @Valid @RequestBody TourRatingDTO tourRatingDTO) {
        log.info("PATCH {}/tours/{}/ratings", ROOT_API_PATH_PREFIX, tourId);
        return tourRatingMapper
                .tourRatingToTourRatingDTO(tourRatingService.updateSomeRating(tourId, tourRatingDTO.getCustomerId(), tourRatingDTO.getRatingScore(), tourRatingDTO.getComment()));
    }

    /**
     * Deletes a tour rating.
     *
     * @param tourId tour identifier
     * @param customerId customer identifier
     */
    @DeleteMapping("/{customerId}")
    public void removeTourRating(@PathVariable("tourId") long tourId, @PathVariable("customerId") long customerId) {
        log.info("PATCH {}/tours/{}/ratings/{}", ROOT_API_PATH_PREFIX, tourId, customerId);
        tourRatingService.delete(tourId, customerId);
    }

    /**
     * Process error handling if NoSuchElementException is thrown.
     *
     * @param ex the exception
     * @return error message
     */
    @ExceptionHandler({NoSuchElementException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleTourRatingNotFoundException(NoSuchElementException ex) {
        log.info("Error finding tour rating: ", ex.getMessage());
        return ex.getMessage();
    }
}

