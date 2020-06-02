package com.jt.tours.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * The TourPackage contains a classification of a tour.
 *
 * Created by Jason Tao on 5/29/2020
 */
@Data
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "tour_package")
public class TourPackage implements Serializable {

    @Id
    @ApiModelProperty(notes = "The tour code that identifies the tour.")
    private String code;

    @Column
    @ApiModelProperty(notes = "The name of the tour.")
    private String name;

    @Builder
    public TourPackage(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
