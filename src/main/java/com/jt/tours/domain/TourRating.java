package com.jt.tours.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

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
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tour_id")
    private Tour tour;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "rating_score", nullable = false)
    private Integer ratingScore;

    @Column(length = 500)
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
