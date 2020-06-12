package com.jt.tours.domain;

import com.sun.istack.NotNull;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 * The Tour contains all the attributes for a tour.
 *
 * Created by Jason Tao on 5/29/2020
 */
@Data
@NoArgsConstructor
@Entity
public class Tour implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "The database generated tour id.")
    private Long id;

    @NotNull
    @Column(name = "tour_title", length = 100)
    @ApiModelProperty(notes = "The tour title.")
    private String tourTitle;

    @Column(length = 2000)
    @ApiModelProperty(notes = "The description about the tour.")
    private String description;

    @Column(length = 2000)
    @ApiModelProperty(notes = "The short description about the tour.")
    private String blurb;

    @NotNull
    @Column
    @ApiModelProperty(notes = "The price of the tour.")
    private Integer price;

    @NotNull
    @Column
    @ApiModelProperty(notes = "The time duration of the tour.")
    private String duration;

    @Column(name = "bullets", length = 1000)
    @ApiModelProperty(notes = "Key bullet points for the tour.")
    private String keyBullets;

    @Column
    @ApiModelProperty(notes = "Search keywords associated with the tour.")
    private String keywords;

    @ManyToOne
    private TourPackage tourPackage;

    @NotNull
    @Column
    @Enumerated(EnumType.STRING)
    @ApiModelProperty(notes = "")
    private DifficultyEnum difficulty;

    @NotNull
    @Column
    @ApiModelProperty(notes = "")
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
