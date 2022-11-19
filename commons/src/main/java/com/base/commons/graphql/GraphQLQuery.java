package com.base.commons.graphql;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GraphQLQuery {
    @JsonProperty("variables")
    private Object variables;

    @JsonProperty("query")
    private String query;
}
