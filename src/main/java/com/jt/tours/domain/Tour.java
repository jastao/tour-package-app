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
@EqualsAndHashCode(callSuper = true)
@Entity
public class Tour extends BaseEntity implements Serializable {

    @Builder
    public Tour(long id, Date createdDate, Date lastModifiedDate, String tourTitle, String description,
                String blurb, Integer price, String duration, TourPackage tourPackage,
                DifficultyEnum difficulty, RegionEnum region) {
        super(id, createdDate, lastModifiedDate);
        this.tourTitle = tourTitle;
        this.description = description;
        this.blurb = blurb;
        this.price = price;
        this.duration = duration;
        this.tourPackage = tourPackage;
        this.difficulty = difficulty;
        this.region = region;
    }

    @NotNull
    @Column
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

    @Column(length = 1000)
    private String keyBullets;

    @Column
    private String keywords;

    @ManyToOne
    private TourPackage tourPackage;

    @NotNull
    @Column
    private DifficultyEnum difficulty;

    @NotNull
    @Column
    private RegionEnum region;

}
