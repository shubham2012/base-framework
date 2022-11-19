package com.base.utils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.util.CollectionUtils;

@Slf4j
public class ESQueryUtil {

    /**
     * Add must-term query on the passed main query
     *
     * @param field
     * @param value
     * @param mainQuery
     * @return
     */
    public static void appendMustTermQuery(String field, String value, BoolQueryBuilder mainQuery) {
        if (StringUtils.isNotEmpty(field) && Objects.nonNull(value)) {
            mainQuery.must(QueryBuilders.termQuery(field, value));
        }
    }

    /**
     * Add must-match query on the passed main query
     *
     * @param field
     * @param value
     * @param mainQuery
     * @return
     */
    public static void appendMustMatchQuery(String field, String value, BoolQueryBuilder mainQuery) {
        if (StringUtils.isNotEmpty(field) && Objects.nonNull(value)) {
            mainQuery.must(QueryBuilders.matchQuery(field, value));
        }
    }

    /**
     * Add filter-term query on the passed main query
     *
     * @param field
     * @param value
     * @param mainQuery
     * @return
     */
    public static void appendFilterTermQuery(String field, String value, BoolQueryBuilder mainQuery) {
        if (StringUtils.isNotEmpty(field) && Objects.nonNull(value)) {
            mainQuery.filter(QueryBuilders.matchQuery(field, value));
        }
    }

    /**
     * Add should-match query on the passed main query
     *
     * @param field
     * @param value
     * @param mainQuery
     * @return
     */
    public static void appendShouldMatchQuery(String field, String value, BoolQueryBuilder mainQuery) {
        if (StringUtils.isNotEmpty(field) && StringUtils.isNotEmpty(value)) {
            mainQuery.should(QueryBuilders.matchQuery(field, value));
        }
    }

    /**
     * Add filter-terms query for multiple values on the passed main query
     *
     * @param field
     * @param values
     * @param mainQuery
     * @return
     */
    public static void appendFilterTermsQuery(String field, List<String> values, BoolQueryBuilder mainQuery) {
        if (StringUtils.isNotEmpty(field) && !CollectionUtils.isEmpty(values)) {
            mainQuery.filter(QueryBuilders.termsQuery(field, values));
        }
    }

    /**
     * Add nestedQuery as a filter query on the passed main query
     *
     * @param nestedQuery
     * @param mainQuery
     * @return
     */
    public static void appendFilterNestedQuery(NestedQueryBuilder nestedQuery, BoolQueryBuilder mainQuery) {
        if (Objects.nonNull(nestedQuery)) {
            mainQuery.filter(nestedQuery);
        }
    }

    /**
     * Add boolQuery as a filter query on the passed main query
     *
     * @param boolQuery
     * @param mainQuery
     * @return
     */
    public static void appendFilterBoolQuery(BoolQueryBuilder boolQuery, BoolQueryBuilder mainQuery) {
        if (Objects.nonNull(boolQuery)) {
            mainQuery.filter(boolQuery);
        }
    }

    /**
     * Add boolQuery as a must_not query on the passed main query
     *
     * @param boolQuery
     * @param mainQuery
     * @return
     */
    public static void appendMustNotBoolQuery(BoolQueryBuilder boolQuery, BoolQueryBuilder mainQuery) {
        if (Objects.nonNull(boolQuery)) {
            mainQuery.mustNot(boolQuery);
        }
    }

    /**
     * Add boolQuery as must query on the passed main query
     *
     * @param boolQuery
     * @param mainQuery
     * @return
     */
    public static void appendMustBoolQuery(BoolQueryBuilder boolQuery, BoolQueryBuilder mainQuery) {
        if (Objects.nonNull(boolQuery)) {
            mainQuery.must(boolQuery);
        }
    }

    /**
     * Add boolQueries as should queries on the passed main query
     *
     * @param boolQueries
     * @param mainQuery
     * @return
     */
    public static void appendShouldBoolQueries(List<BoolQueryBuilder> boolQueries, BoolQueryBuilder mainQuery) {
        if (!CollectionUtils.isEmpty(boolQueries)) {
            for (BoolQueryBuilder boolQuery : boolQueries) {
                mainQuery.should(boolQuery);
            }
        }
    }

    public static void appendGeoPointQuery(
            String fieldName,
            Double distance,
            String distanceUnit,
            Double currentLatitude,
            Double currentLongitude,
            BoolQueryBuilder mainQuery) {
        if (StringUtils.isNotEmpty(fieldName)
                && StringUtils.isNotEmpty(distanceUnit)
                && !Double.isNaN(currentLatitude)
                && !Double.isNaN(currentLongitude)
                && Objects.nonNull(mainQuery)
                && !Double.isNaN(distance)) {
            DistanceUnit unit = DistanceUnit.fromString(distanceUnit);
            GeoDistanceQueryBuilder geoQuery = QueryBuilders.geoDistanceQuery(fieldName);
            geoQuery.distance(distance, unit);
            geoQuery.point(currentLatitude, currentLongitude);
            mainQuery.filter(geoQuery);
        }
    }

    public static void appendWeightedMultiMatchQuery(
            BoolQueryBuilder mainQuery, String queryString, Map<String, Float> fieldWeightMap) {
        if (StringUtils.isNotBlank(queryString) && !CollectionUtils.isEmpty(fieldWeightMap)) {
            MultiMatchQueryBuilder textMatchQuery = QueryBuilders.multiMatchQuery(queryString).fields(fieldWeightMap);
            mainQuery.must(textMatchQuery);
        }
    }

    public static SearchSourceBuilder buildSearchRequest(
            BoolQueryBuilder mainQuery, String sortField, SortOrder sortOrder, Integer limit, Integer offset) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(mainQuery);

        if (Objects.nonNull(limit)) searchSourceBuilder.size(limit);
        if (Objects.nonNull(offset)) searchSourceBuilder.from(offset);
        if (!StringUtils.isEmpty(sortField) && Objects.nonNull(sortOrder))
            searchSourceBuilder.sort(sortField, sortOrder);

        searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        return searchSourceBuilder;
    }
}
