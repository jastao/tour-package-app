package com.jt.tours.repository;

import com.jt.tours.domain.TourPackage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Optional;

/**
 * Tour Package Repository Interface
 *
 * Created by Jason Tao on 5/30/2020
 */
@RepositoryRestResource(collectionResourceRel = "packages", path = "packages")
public interface TourPackageRepository extends CrudRepository<TourPackage, String> {

    /**
     * Returns the tour package by name.
     *
     * @param name name of the tour
     * @return the tour package if found; otherwise empty.
     */
    Optional<TourPackage> findByName(String name);

    @Override
    @RestResource(exported = false)
    <S extends TourPackage> S save(S tour);

    @Override
    @RestResource(exported = false)
    <S extends TourPackage> Iterable<S> saveAll(Iterable<S> iterable);

    @Override
    @RestResource(exported = false)
    void deleteById(String code);

    @Override
    @RestResource(exported = false)
    void delete(TourPackage var1);

    @Override
    @RestResource(exported = false)
    void deleteAll(Iterable<? extends TourPackage> iterable);

    @Override
    @RestResource(exported = false)
    void deleteAll();
}
