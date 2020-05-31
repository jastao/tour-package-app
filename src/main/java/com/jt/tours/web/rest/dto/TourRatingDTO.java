package com.jt.tours.web.rest.dto;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

/**
 * Tour Rating Data Transfer Object that is used to send tour rating data for REST call.
 *
 * Created by Jason Tao on 5/30/2020
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TourRatingDTO {

    @NotNull
    private Long customerId;

    @Min(0)
    @Max(5)
    private Integer ratingScore;

    @Size(max = 500)
    private String comment;
}
