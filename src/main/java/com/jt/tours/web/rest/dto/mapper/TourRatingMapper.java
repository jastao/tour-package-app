package com.jt.tours.web.rest.dto.mapper;

import com.jt.tours.domain.TourRating;
import com.jt.tours.web.rest.dto.TourRatingDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * Simple mapper that converts TourRating to TourRatingDTO.
 *
 * Created by Jason Tao on 5/30/2020
 */
@Mapper(componentModel = "spring")
public interface TourRatingMapper {

    /**
     * Mapping function that converts the tour rating to tour rating DTO.
     *
     * @param tourRating tour rating entity
     * @return tour rating DTO
     */
    @Mappings({
            @Mapping(target = "customerId", source = "customerId"),
            @Mapping(target = "ratingScore", source = "ratingScore"),
            @Mapping(target = "comment", source = "comment")
    })
    TourRatingDTO tourRatingToTourRatingDTO(TourRating tourRating);

}
