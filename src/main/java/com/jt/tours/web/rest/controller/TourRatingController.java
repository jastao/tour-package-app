package com.jt.tours.web.rest.controller;

import com.jt.tours.domain.TourRating;
import com.jt.tours.service.impl.TourRatingService;
import com.jt.tours.web.rest.dto.TourRatingDTO;
import com.jt.tours.web.rest.dto.mapper.TourRatingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.AbstractMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Tour Rating Controller
 *
 * Created by Jason Tao on 5/30/2020
 */
@RestController
@RequestMapping("/api/v1/tour/{tourId}/ratings")
public class TourRatingController {

    private TourRatingService tourRatingService;

    private TourRatingMapper tourRatingMapper;

    protected TourRatingController() {}

    @Autowired
    public TourRatingController(TourRatingService tourRatingService, TourRatingMapper tourRatingMapper) {
        this.tourRatingService = tourRatingService;
        this.tourRatingMapper = tourRatingMapper;
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
    public Page<TourRatingDTO> getAllRatingsFromTour(@PathVariable("tourId") long tourId, Pageable pageable) {

        Page<TourRating> pagedTourRating = tourRatingService.getTourRatings(tourId, pageable);
        List<TourRatingDTO> tourRatingDTOList = pagedTourRating.getContent()
                .stream().map(tourRatingMapper::tourRatingToTourRatingDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(tourRatingDTOList, pageable, pagedTourRating.getTotalPages());
    }

    /**
     * Calculate the average score of a Tour.
     *
     * @param tourId tour identifier
     * @return Tuple of "average" and the average value.
     */
    @GetMapping("/average")
    public AbstractMap.SimpleEntry<String, Double> getAverage(@PathVariable("tourId") long tourId) {
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
        return ex.getMessage();
    }
}

