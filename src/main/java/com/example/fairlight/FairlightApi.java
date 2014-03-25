package com.example.fairlight;

import com.example.fairlight.entities.Task;
import com.example.fairlight.entities.TaskList;
import com.example.fairlight.entities.User;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.DefaultValue;
import com.google.api.server.spi.config.Named;
import com.google.appengine.api.datastore.Cursor;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;

/**
 * Created by ilewis on 2/11/14.
 */
@Api(name="fairlight")
public class FairlightApi {

    static {
        ObjectifyService.register(User.class);
        ObjectifyService.register(Task.class);
        ObjectifyService.register(TaskList.class);
    }

    public StringResult echo(@Named("input") String input) {
        return new StringResult(String.format("Input String: %s", input));
    }

    @ApiMethod(httpMethod = ApiMethod.HttpMethod.GET)
    public List<User> users(@Named("page_size") @DefaultValue("-1") int pageSize,
                            @Named("page_token") @DefaultValue("") String pageToken) {

        Query<User> query = ofy().load().type(User.class);
        if (pageSize > 0) {
            query = query.limit(pageSize);
        }
        if (!pageToken.isEmpty()) {
            query = query.startAt(Cursor.fromWebSafeString(pageToken));
        }

        return query.list();
    }

    @ApiMethod(httpMethod = ApiMethod.HttpMethod.POST)
    public User putUser(@Named("username") String username) {
        User newUser = new User(username);
        ofy().save().entity(newUser).now();
        return newUser;
    }
}
