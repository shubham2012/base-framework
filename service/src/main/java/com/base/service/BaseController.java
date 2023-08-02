package com.base.service;

import com.base.commons.entry.BaseEntry;
import com.base.commons.response.BaseResponse;
import com.base.commons.response.PageResponse;
import com.base.service.entity.BaseEntity;
import java.util.List;
import javax.validation.Valid;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * BaseController takes care of all the CRUD operations and reduces the boiler plate code This gives you basic CREATE,
 * UPDATE, GET, BULK GET, and SEARCH operations You just need to extend it and need to follow the proper structure and
 * pass the needed values
 *
 * @param <T>
 * @param <R>
 * @param <S>
 */
public abstract class BaseController<T extends BaseEntity, R extends BaseEntry, S extends BaseResponse> {

    /**
     * Initialising the service associated with the controller You are suppose to set this while extending this
     * controller
     */
    @Setter @Getter protected BaseService<T, R> service;

    /**
     * Creates an entity based on its input (request).
     *
     * @param request
     * @return
     */
    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity create(@Valid @RequestBody R request) {
        R r1 = getService().create(request);
        S responseData = createResponse(r1);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseData);
    }

    /**
     * Creates entities based on input (request).
     *
     * @param request
     * @return
     */
    @PostMapping(
            value = "/bulk",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createBulk(@Valid @RequestBody List<R> request) {
        List<R> r1 = getService().createBulk(request);
        S responseData = createResponse(r1);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseData);
    }

    /**
     * Updates an entity by its id, with provided input (request).
     *
     * @param id
     * @param request
     * @return
     */
    @PutMapping(
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity update(@PathVariable("id") Long id, @Valid @RequestBody R request) {
        R r1 = getService().update(id, request);
        S responseData = createResponse(r1);
        return ResponseEntity.ok(responseData);
    }

    /**
     * Retrieves the entity by its id.
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity get(@PathVariable("id") Long id) {
        R r1 = getService().get(id);
        S response = createResponse(r1);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves asked entities with max limit 100.
     *
     * @param ids
     * @return
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity get(@RequestParam("ids") List<Long> ids) {
        List<R> r1 = getService().get(ids);
        S response = createResponse(r1);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves all entities with max limit 100. Considers parameter and puts the default values if not provided
     *
     * @param offset default 0
     * @param limit default 10
     * @param direction default DESC
     * @param sortBy default id
     * @return
     */
    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity search(
            @RequestParam(value = "by", defaultValue = "") String searchBy,
            @RequestParam(value = "offset", defaultValue = "0") Integer offset,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit,
            @RequestParam(value = "sortDir", defaultValue = "DESC") Sort.Direction direction,
            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy) {
        PageResponse<R> pageResponse = getService().search(searchBy, offset, limit, direction, sortBy);
        List<R> data = pageResponse.getData();
        S responseData = createResponse(data);
        responseData.setPageDetail(pageResponse.getPageDetail());
        responseData.setTotalCount(pageResponse.getTotalCount());
        return ResponseEntity.ok(responseData);
    }

    /**
     * Need to initialise this by child classes to set the response
     *
     * @param entries
     * @return
     */
    protected abstract S createResponse(List<R> entries);

    /**
     * Need to initialise this by child classes to set the response
     *
     * @param entry
     * @return
     */
    protected abstract S createResponse(R entry);
}
