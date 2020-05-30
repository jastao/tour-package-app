package com.jt.tours.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

/**
 * Rating of a tour by a customer
 *
 * Created by Jason Tao on 5/29/2020
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
public class TourRating {

    @EmbeddedId
    private TourRatingPK tourRatingPK;

    @Column(nullable = false)
    private Integer ratingScore;

    @Column(length = 500)
    private String comment;
}
