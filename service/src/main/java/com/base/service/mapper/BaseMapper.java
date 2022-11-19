package com.base.service.mapper;

import com.base.commons.entry.BaseEntry;
import com.base.service.entity.BaseEntity;
import java.util.List;
import org.mapstruct.MapperConfig;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

/**
 * BaseMapper is the parent class for all the mappers and provides the basic mappings if you wish to have your
 * personalised mapping then override the methods which you wants to personalise
 *
 * <p>IMO::: Please do add @Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE) in your
 * actual mapper which is extending this mapper to ignore null values mapping during the update mapping
 *
 * @param <T>
 * @param <U>
 */
@MapperConfig(
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BaseMapper<T extends BaseEntry, U extends BaseEntity> {

    @Mapping(ignore = true, source = "id", target = "id")
    U entryToEntity(T entry);

    List<U> entryToEntity(List<T> entries);

    T entityToEntry(U entity);

    List<T> entityToEntry(List<U> entities);

    @Mapping(ignore = true, source = "id", target = "id")
    @Mapping(ignore = true, source = "createdBy", target = "createdBy")
    @Mapping(ignore = true, source = "createdAt", target = "createdAt")
    @Mapping(ignore = true, source = "version", target = "version")
    U toUpdate(U request, @MappingTarget U target);
}
