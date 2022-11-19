package com.base.service;

import static com.base.commons.utils.BaseConstants.MAX_PAGE_FETCH_LIMIT;

import com.base.commons.codes.BaseMessages;
import com.base.commons.entry.BaseEntry;
import com.base.commons.exception.BadRequestException;
import com.base.commons.response.PageDetail;
import com.base.commons.response.PageResponse;
import com.base.commons.utils.PageUtil;
import com.base.service.entity.BaseEntity;
import com.base.service.mapper.BaseMapper;
import com.base.service.repository.BaseRepository;
import com.base.utils.CommonsUtil;
import com.base.utils.SearchUtility;
import java.util.List;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/**
 * BaseService takes care of all the CRUD service execution and reduces the boiler plate code This takes care of basic
 * CREATE, UPDATE, GET, BULK GET, and SEARCH operations You just need to extend it and need to follow the proper
 * structure and pass the needed values
 *
 * @param <T>
 * @param <R>
 */
@Setter
@Getter
public abstract class BaseService<T extends BaseEntity, R extends BaseEntry> {

    /**
     * Initialising the repository associated with the service. You are suppose to set this while extending this service
     */
    protected BaseRepository<T> repository;

    /** Initialising the mapper associated with the service. You are suppose to set this while extending this service */
    protected BaseMapper<R, T> mapper;

    /**
     * Creates the entity provided in the request
     *
     * @param request
     * @return
     */
    public R create(@NonNull final R request) {
        T t = getMapper().entryToEntity(request);
        T e = getRepository().save(t);
        return getMapper().entityToEntry(e);
    }

    /**
     * Creates entities provided in the request
     *
     * @param request
     * @return
     */
    public List<R> createBulk(@NotNull final List<R> request) {
        if (CollectionUtils.isEmpty(request)) {
            throw new BadRequestException(BaseMessages.DATA_EMPTY.get());
        }
        List<T> t = getMapper().entryToEntity(request);
        List<T> e = getRepository().saveAll(t);
        return getMapper().entityToEntry(e);
    }

    /**
     * Update the entity provided in the request
     *
     * @param id
     * @param request
     * @return
     */
    public R update(@NonNull final long id, @NonNull final R request) {
        Optional<T> entityOptional = getRepository().findById(id);
        if (!entityOptional.isPresent()) {
            throw new BadRequestException(BaseMessages.DATA_NOT_FOUND.get(id));
        }
        T entity = entityOptional.get();
        T updateEntity = getMapper().entryToEntity(request);
        T updatedRequestEntity = getMapper().toUpdate(updateEntity, entity);
        T updatedEntity = getRepository().save(updatedRequestEntity);
        return getMapper().entityToEntry(updatedEntity);
    }

    /**
     * Retrieve data for a given ID
     *
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public R get(@NonNull final long id) {
        Optional<T> entityOptional = getRepository().findById(id);
        if (!entityOptional.isPresent()) {
            throw new BadRequestException(BaseMessages.DATA_NOT_FOUND.get(id));
        }
        return entityOptional.map(mapper::entityToEntry).get();
    }

    /**
     * Retrieve data for a given list of IDs Validate max page fetch limit as 100
     *
     * @param ids
     * @return
     */
    @Transactional(readOnly = true)
    public List<R> get(@NotNull List<Long> ids) {
        if (ids.size() > MAX_PAGE_FETCH_LIMIT) {
            throw new BadRequestException(BaseMessages.FETCH_LIMIT_EXCEEDED.get(MAX_PAGE_FETCH_LIMIT));
        }
        List<T> entityList = getRepository().findAllByIdIn(ids);
        return getMapper().entityToEntry(entityList);
    }

    /**
     * Search data based on provided parameters Default values will be initialised if not provided Validate max page
     * fetch limit as 100
     *
     * @param offset default 0
     * @param limit default 10
     * @param direction default DESC
     * @param sortBy default id
     * @return
     */
    @Transactional(readOnly = true)
    public PageResponse search(Integer offset, Integer limit, Sort.Direction direction, String sortBy) {

        SearchUtility searchData = new SearchUtility(offset, limit, direction, sortBy).initializeDefault();
        offset = searchData.getOffset();
        limit = searchData.getLimit();
        direction = searchData.getDirection();
        sortBy = searchData.getSortBy();

        searchData.validate();

        PageRequest pageRequest = CommonsUtil.getPageRequest(offset, limit, direction, sortBy);
        Page<T> page = getRepository().findAll(pageRequest);
        List<T> content = page.getContent();
        List<R> responseData = getMapper().entityToEntry(content);
        PageDetail pageDetail = PageUtil.getPage(page.getTotalPages(), offset, page.getSize());
        return new PageResponse(pageDetail, page.getTotalElements(), responseData);
    }
}
