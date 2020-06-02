package com.jt.tours.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Rating of a tour by a customer
 *
 * Created by Jason Tao on 5/29/2020
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "tour_rating")
public class TourRating implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "The database generated tour rating id.")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tour_id")
    @ApiModelProperty(notes = "The tour id corresponds to the tour rating.")
    private Tour tour;

    @Column(name = "customer_id")
    @ApiModelProperty(notes = "The customer id that provides the tour rating.")
    private Long customerId;

    @Column(name = "rating_score", nullable = false)
    @ApiModelProperty(notes = "The actual rating score for the tour.")
    private Integer ratingScore;

    @Column(length = 500)
    @ApiModelProperty(notes = "The comment given to the tour.")
    private String comment;

    @Builder
    public TourRating(Tour tour, Long customerId, Integer ratingScore, String comment) {
        this.tour = tour;
        this.customerId = customerId;
        this.ratingScore = ratingScore;
        this.comment = comment == null ? toComment(ratingScore) : comment;
    }

    /**
     * Auto Generate a message for a score.
     *
     * @param score
     * @return
     */
    private String toComment(Integer score) {
        switch (score) {
            case 1:return "Terrible";
            case 2:return "Poor";
            case 3:return "Fair";
            case 4:return "Good";
            case 5:return "Great";
            default: return score.toString();
        }
    }
}
