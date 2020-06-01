package com.jt.tours.web.rest.assembler;

import com.jt.tours.domain.TourRating;
import com.jt.tours.repository.TourRepository;
import com.jt.tours.web.rest.controller.RatingController;
import com.jt.tours.web.rest.dto.TourRatingDTO;
import com.jt.tours.web.rest.dto.mapper.TourRatingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Rating assembler that converts TourRating to a HATEOAS supported TourRating class.
 *
 * Created by Jason Tao on 6/1/2020
 */
@Component
public class RatingAssembler extends RepresentationModelAssemblerSupport<TourRating, TourRatingDTO> {

    // Helper to fetch Spring Data Rest Repository links
    private RepositoryEntityLinks tourRepositoryLinks;

    // Mapper to convert tour rating to tour rating DTO
    private TourRatingMapper tourRatingMapper;

    @Autowired
    public RatingAssembler(RepositoryEntityLinks tourRepositoryLinks, TourRatingMapper tourRatingMapper) {
        super(RatingController.class, TourRatingDTO.class);
        this.tourRepositoryLinks = tourRepositoryLinks;
        this.tourRatingMapper = tourRatingMapper;
    }

    /**
     * Converts the tour rating to the tour rating DTO with "self", "rating" and tour links.
     *
     * @param tourRating tour rating object
     * @return tour rating DTO
     */
    @Override
    public TourRatingDTO toModel(TourRating tourRating) {
        TourRatingDTO tourRatingDTO = tourRatingMapper.tourRatingToTourRatingDTO(tourRating);

        // "self" : ".../ratings/{ratingId}"
        WebMvcLinkBuilder ratingLink = linkTo(methodOn(RatingController.class).getRating(tourRating.getId()));
        tourRatingDTO.add(ratingLink.withSelfRel());

        // "tour" : ".../tours/{tourId}"
        Link link = tourRepositoryLinks.linkToItemResource(TourRepository.class, tourRating.getTour().getId());
        tourRatingDTO.add(link.withRel("tour"));

        return tourRatingDTO;
    }
}
