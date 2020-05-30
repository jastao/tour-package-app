package com.jt.tours.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

/**
 * Base entity class contains the creation and last modified timestamp.
 *
 * Created by Jason Tao on 5/29/2020
 */
@Data
@NoArgsConstructor
@MappedSuperclass
public class BaseEntity {

    public BaseEntity(long id, Date createdDate, Date lastModifiedDate) {
        this.id = id;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date createdDate;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date lastModifiedDate;
}

