package com.jt.tours.web.rest.controller;

import com.jt.tours.domain.TourRating;
import com.jt.tours.service.impl.TourRatingService;
import com.jt.tours.web.rest.assembler.RatingAssembler;
import com.jt.tours.web.rest.dto.TourRatingDTO;
import com.jt.tours.web.rest.dto.mapper.TourRatingMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.AbstractMap;

/**
 * Tour Rating Controller
 *
 * Created by Jason Tao on 5/30/2020
 */
@Api(description = "API for accessing tour ratings")
@RequestMapping("/api/v1/tours/{tourId}/ratings")
@RestController
@Slf4j
public class TourRatingController {

    private final String ROOT_API_PATH_PREFIX = "/api/v1";

    private TourRatingService tourRatingService;

    private TourRatingMapper tourRatingMapper;

    private RatingAssembler ratingAssembler;

    protected TourRatingController() {}

    @Autowired
    protected TourRatingController(TourRatingService tourRatingService,
                                   TourRatingMapper tourRatingMapper, RatingAssembler ratingAssembler) {
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
    @ApiOperation(value = "Create a tour rating")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Tour rating created."),
            @ApiResponse(code = 400, message = "The tour id is invalid."),
            @ApiResponse(code = 404, message = "Tour id does not exist.")
    })
    @PostMapping
    @PreAuthorize("hasRole('CSR_USER') or hasRole('CSR_ADMIN')")
    public ResponseEntity<?> createTourRating(@PathVariable("tourId") long tourId, @Valid @RequestBody TourRatingDTO tourRatingDTO) {

        if(tourId < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The tour id is invalid.");
        }

        log.info("POST {}/tours/{}/ratings", ROOT_API_PATH_PREFIX, tourId);

        TourRating createdRating = tourRatingService.createNewRating(
                tourId, tourRatingDTO.getCustomerId(), tourRatingDTO.getRatingScore(), tourRatingDTO.getComment());

        return ResponseEntity.created(UriComponentsBuilder
                                                .fromPath(ROOT_API_PATH_PREFIX + "/tours/" + tourId + "/ratings/" + createdRating.getId())
                                                .build().toUri())
                            .body(tourRatingMapper.tourRatingToTourRatingDTO(createdRating));
    }

    /**
     * Create a Tour Rating with the same rating score for multiple customers.
     *
     * @param tourId tour identifier
     * @param ratingScore rating score of the tour
     * @param customers array of customer id
     */
    @ApiOperation(value = "Create a tour rating with same rating score for multiple customers.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request POST completed."),
            @ApiResponse(code = 400, message = "The tour id is invalid."),
            @ApiResponse(code = 404, message = "Tour id does not exist.")
    })
    @PostMapping("/{score}")
    @PreAuthorize("hasRole('CSR_USER') or hasRole('CSR_ADMIN')")
    public ResponseEntity<String> createTourRatings(@PathVariable("tourId") long tourId,
                                  @PathVariable("score") int ratingScore,
                                  @RequestParam("customers") Long[] customers) {

        if(tourId < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The tour id is invalid.");
        }

        log.info("POST {}/tours/{}/ratings/{}", ROOT_API_PATH_PREFIX, tourId, ratingScore);
        tourRatingService.createNewRatings(tourId, customers, ratingScore);
        return ResponseEntity.ok("Created rating for multiple customers");
    }

    /**
     * Find all the rating associated with a given tour.
     *
     * @param tourId tour identifier
     * @param pageable page parameters to determine which item to fetch
     * @return page of Tour Rating DTO
     */
    @ApiOperation(value = "Find all the rating associated with a given tour.", response = TourRatingDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request GET completed."),
            @ApiResponse(code = 400, message = "The tour id is invalid."),
            @ApiResponse(code = 404, message = "Tour id does not exist.")
    })
    @PreAuthorize("permitAll()")
    @GetMapping
    public ResponseEntity<?> getAllRatingsFromTour(@PathVariable("tourId") long tourId, Pageable pageable,
                                                   PagedResourcesAssembler pagedAssembler) {
        log.info("GET {}/tours/{}/ratings", ROOT_API_PATH_PREFIX, tourId);

        if(tourId < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The tour id is invalid.");
        }

        Page<TourRating> pagedTourRating = tourRatingService.getTourRatings(tourId, pageable);

        return ResponseEntity.ok(pagedAssembler.toModel(pagedTourRating, ratingAssembler));
    }

    /**
     * Calculate the average score of a tour.
     *
     * @param tourId tour identifier
     * @return Tuple of "average" and the average value.
     */
    @ApiOperation(value = "Calculate the average score of a tour.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request GET completed."),
            @ApiResponse(code = 400, message = "The tour id is invalid."),
            @ApiResponse(code = 404, message = "Tour id does not exist.")
    })
    @PreAuthorize("hasRole('CSR_USER') or hasRole('CSR_ADMIN')")
    @GetMapping("/average")
    public ResponseEntity<?> getAverage(@PathVariable("tourId") long tourId) {

        if(tourId < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The tour id is invalid.");
        }

        log.info("GET {}/tours/{}/ratings/average", ROOT_API_PATH_PREFIX, tourId);
        return ResponseEntity.ok(new AbstractMap.SimpleEntry<String, Double>("average", tourRatingService.getAverageRatingScore(tourId)));
    }

    /**
     * Update the score and the comment for a tour.
     *
     * @param tourId tour identifier
     * @param tourRatingDTO the tour rating data transfer object
     * @return the modified tour rating object
     */
    @ApiOperation(value = "Update the score and the comment for a tour.", response = TourRatingDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request PUT completed."),
            @ApiResponse(code = 400, message = "The tour id is invalid."),
            @ApiResponse(code = 404, message = "Tour id does not exist.")
    })
    @PreAuthorize("hasRole('CSR_USER') or hasRole('CSR_ADMIN')")
    @PutMapping
    public ResponseEntity<?> updateTourRating(@PathVariable("tourId") long tourId, @Valid @RequestBody TourRatingDTO tourRatingDTO) {

        if(tourId < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The tour id is invalid.");
        }

        log.info("PUT {}/tours/{}/ratings", ROOT_API_PATH_PREFIX, tourId);
        return ResponseEntity.ok(tourRatingMapper
                .tourRatingToTourRatingDTO(tourRatingService.updateRating(tourId, tourRatingDTO.getCustomerId(), tourRatingDTO.getRatingScore(), tourRatingDTO.getComment())));
    }

    /**
     * Update either the score or the comment for a tour.
     *
     * @param tourId tour identifier
     * @param tourRatingDTO the tour rating data transfer object
     * @return the modified tour rating object
     */
    @ApiOperation(value = "Update either the score or the comment for a tour.", response = TourRatingDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request PATCH completed."),
            @ApiResponse(code = 400, message = "The tour id is invalid."),
            @ApiResponse(code = 404, message = "Tour id does not exist.")
    })
    @PreAuthorize("hasRole('CSR_USER') or hasRole('CSR_ADMIN')")
    @PatchMapping
    public ResponseEntity<?> updatePartialTourRating(@PathVariable("tourId") long tourId, @Valid @RequestBody TourRatingDTO tourRatingDTO) {

        if(tourId < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The tour id is invalid.");
        }

        log.info("PATCH {}/tours/{}/ratings", ROOT_API_PATH_PREFIX, tourId);
        return ResponseEntity.ok(tourRatingMapper
                .tourRatingToTourRatingDTO(tourRatingService.updateSomeRating(tourId, tourRatingDTO.getCustomerId(), tourRatingDTO.getRatingScore(), tourRatingDTO.getComment())));
    }

    /**
     * Deletes a tour rating.
     *
     * @param tourId tour identifier
     * @param customerId customer identifier
     */
    @ApiOperation(value = "Deletes a tour rating.")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "The tour rating is removed."),
            @ApiResponse(code = 400, message = "The tour id is invalid."),
            @ApiResponse(code = 404, message = "Tour id does not exist.")
    })
    @PreAuthorize("hasRole('CSR_USER') or hasRole('CSR_ADMIN')")
    @DeleteMapping("/{customerId}")
    public ResponseEntity<?> removeTourRating(@PathVariable("tourId") long tourId, @PathVariable("customerId") long customerId) {

        if(tourId < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The tour id is invalid.");
        }

        log.info("PATCH {}/tours/{}/ratings/{}", ROOT_API_PATH_PREFIX, tourId, customerId);
        tourRatingService.delete(tourId, customerId);

        return ResponseEntity.noContent().build();
    }

}

