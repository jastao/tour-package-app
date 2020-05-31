package com.jt.tours.domain;

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
    private String code;

    @Column
    private String name;

    @Builder
    public TourPackage(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
