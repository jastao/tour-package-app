package com.jt.tours.web.rest.controller;

import com.jt.tours.service.impl.TourRatingService;
import com.jt.tours.web.rest.assembler.RatingAssembler;
import com.jt.tours.web.rest.dto.TourRatingDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

/**
 * Rating controller
 *
 * Created by Jason Tao on 6/1/2020
 */
@RequestMapping("${spring.data.rest.base-path}/ratings")
@RestController
@Slf4j
public class RatingController {

    private TourRatingService tourRatingService;

    private RatingAssembler ratingAssembler;

    @Autowired
    public RatingController(TourRatingService tourRatingService, RatingAssembler ratingAssembler) {
        this.tourRatingService = tourRatingService;
        this.ratingAssembler = ratingAssembler;
    }

    /**
     * Retrieve the tour rating by rating id.
     *
     * @param id tour rating identifier
     * @return tour rating DTO
     */
    @GetMapping("/{ratingId}")
    public TourRatingDTO getRating(@PathVariable("ratingId") Long id) {
        log.info("GET /ratings/{}", id);
        return ratingAssembler.toModel(tourRatingService.searchRatingById(id)
                .orElseThrow( () -> new NoSuchElementException("Rating " + id + " not found.")));
    }

    /**
     * Retrieve all the tour ratings.
     *
     * @return list of available tour ratings
     */
    @GetMapping
    public CollectionModel<TourRatingDTO> getRatings() {
        log.info("GET /ratings");
        return ratingAssembler.toCollectionModel(tourRatingService.searchAllRatings());
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
