package com.example.fairlight.results;

import com.example.fairlight.entities.User;
import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.cmd.Query;

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

    /**
     * Make a list result from an Objectify query
     *
     * @param query     The Objectify query object to load the list from
     * @param pageSize  Max # of records to load, or -1 for no limit
     * @param pageToken Next page token supplied by the previous cursor, or empty for none
     * @return A {@link ListResult} containing the results of the query
     */
    public static ListResult fromQuery(Query query, int pageSize, String pageToken) {
        if (pageSize > 0) {
            query = query.limit(pageSize);
        }
        if (!pageToken.isEmpty()) {
            query = query.startAt(Cursor.fromWebSafeString(pageToken));
        }

        QueryResultIterator<User> iter = query.iterator();

        // Find the token for the next page of results. This unfortunately
        // seems to require iterating through the entire list to move the
        // cursor to the last record fetched.
        //
        boolean more = false;
        String nextToken = "";

        // move iterator to end
        while (iter.hasNext()) {
            more = true;
            iter.next();
        }
        // get the string representation of the iterator's current position
        if (more) {
            Cursor cursor = iter.getCursor();
            nextToken = cursor.toWebSafeString();
        }


        return new ListResult(query.list(), nextToken);
    }

    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
    public String getNextToken() {
        return next_token;
    }

    public List getResults() {
        return results;
    }
}
