package com.jt.tours.domain;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * The Tour contains all the attributes for a tour.
 *
 * Created by Jason Tao on 5/29/2020
 */
@Data
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false)
@Entity
public class Tour implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "tour_title", length = 100)
    private String tourTitle;

    @Column(length = 2000)
    private String description;

    @Column(length = 2000)
    private String blurb;

    @NotNull
    @Column
    private Integer price;

    @NotNull
    @Column
    private String duration;

    @Column(name = "bullets", length = 1000)
    private String keyBullets;

    @Column
    private String keywords;

    @ManyToOne
    private TourPackage tourPackage;

    @NotNull
    @Column
    @Enumerated(EnumType.STRING)
    private DifficultyEnum difficulty;

    @NotNull
    @Column
    private RegionEnum region;

    @Builder
    public Tour(String tourTitle, String description, String blurb, String keyBullets, Integer price, String duration,
                TourPackage tourPackage, DifficultyEnum difficulty, RegionEnum region, String keywords) {
        this.tourTitle = tourTitle;
        this.description = description;
        this.blurb = blurb;
        this.keyBullets = keyBullets;
        this.price = price;
        this.duration = duration;
        this.tourPackage = tourPackage;
        this.difficulty = difficulty;
        this.region = region;
        this.keywords = keywords;
    }

}
