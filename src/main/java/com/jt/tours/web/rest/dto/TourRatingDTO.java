package com.jt.tours.web.rest.dto;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

/**
 * Tour Rating Data Transfer Object that is used to send tour rating data for REST call.
 * Extends RepresentationModel to add hypermedia resource link to the DTO for REST.
 *
 * Created by Jason Tao on 5/30/2020
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TourRatingDTO extends RepresentationModel<TourRatingDTO> {

    @NotNull
    private Long customerId;

    @Min(0)
    @Max(5)
    private Integer ratingScore;

    @Size(max = 500)
    private String comment;
}
