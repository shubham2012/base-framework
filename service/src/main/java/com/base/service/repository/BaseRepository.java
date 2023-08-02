package com.base.service.repository;

import com.base.service.entity.BaseEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * This is Base Abstract repository. This is supposed to be extended by all the repositories in your services This
 * repository supports QueryDSL, PagingAndSortingRepository features so you don't need to worry about writing the basic
 * methods to fetch the data
 *
 * @param <T>
 */
@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity>
        extends JpaRepository<T, Long>, QuerydslPredicateExecutor<T>, JpaSpecificationExecutor<T> {

    List<T> findAllByIdIn(List<Long> ids);
}
