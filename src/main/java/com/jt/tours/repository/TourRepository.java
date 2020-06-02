package com.jt.tours.repository;

import com.jt.tours.domain.Tour;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * Tour Repository Interface
 *
 * Created by Jason Tao on 5/30/2020
 */
@RepositoryRestResource(collectionResourceRel = "tours", path = "tours")
public interface TourRepository extends PagingAndSortingRepository<Tour, Long> {

    /**
     * Returns a Page of Tours associated with a TourPackage.
     *
     * @param code the tour package code
     * @param pageable pageable detail for searching the correct page
     * @return page of tours if found; otherwise empty.
     */
    Page<Tour> findByTourPackageCode(@Param("code") String code, Pageable pageable);

    @Override
    @RestResource(exported = false)
    <S extends Tour> S save(S tour);

    @Override
    @RestResource(exported = false)
    <S extends Tour> Iterable<S> saveAll(Iterable<S> iterable);

    @Override
    @RestResource(exported = false)
    void deleteById(Long id);

    @Override
    @RestResource(exported = false)
    void delete(Tour var1);

    @Override
    @RestResource(exported = false)
    void deleteAll(Iterable<? extends Tour> iterable);

    @Override
    @RestResource(exported = false)
    void deleteAll();
}
