package com.example.fairlight;

import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;

import java.util.List;

/**
 * Created by ilewis on 3/24/14.
 */
public class ListResult {
    @ApiResourceProperty
    private String next_token;
    private List results;

    public ListResult(List results, String next_token) {
        this.next_token = next_token;
        this.results = results;
    }

    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
    public String getNextToken() {
        return next_token;
    }

    public List getResults() {
        return results;
    }
}
