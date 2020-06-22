package com.jt.tours.web.rest.controller;

import com.jt.tours.service.impl.TourRatingService;
import com.jt.tours.web.rest.assembler.RatingAssembler;
import com.jt.tours.web.rest.dto.TourRatingDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

/**
 * Rating controller
 *
 * Created by Jason Tao on 6/1/2020
 */
@RequestMapping("/api/v1/ratings")
@RestController
@PreAuthorize("hasRole('CSR_USER') or hasRole('CSR_ADMIN')")
@Slf4j
public class RatingController {

    private TourRatingService tourRatingService;

    private RatingAssembler ratingAssembler;

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
}
