package com.github.inl1ne.fairlight;

import com.github.inl1ne.fairlight.entities.UserList;
import com.github.inl1ne.fairlight.entities.Task;
import com.github.inl1ne.fairlight.entities.TaskList;
import com.github.inl1ne.fairlight.entities.User;
import com.github.inl1ne.arc.ListResult;
import com.github.inl1ne.fairlight.results.StringResult;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.DefaultValue;
import com.google.api.server.spi.config.Named;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;

import static com.googlecode.objectify.ObjectifyService.ofy;

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

    @ApiMethod(httpMethod = ApiMethod.HttpMethod.GET,
                name = "users.getAll",
                path = "users")
    public ListResult users(@Named("page_size") @DefaultValue("-1") int pageSize,
                            @Named("page_token") @DefaultValue("") String pageToken) {

        Query<User> query = ofy().load().type(User.class);
        return ListResult.fromQuery(query, pageSize, pageToken);
    }

    @ApiMethod(httpMethod = ApiMethod.HttpMethod.GET,
                name = "users.getSingle",
                path = "users/{username}")
    public User getUser(@Named("username") String username) {
        Key<User> userKey = Key.create(User.class, username);
        return (User)ofy().load().key(userKey).now();
    }

    @ApiMethod(httpMethod = ApiMethod.HttpMethod.PUT,
                name = "users.put",
                path = "users/{username}")
    public User putUser(User user) {
        ofy().save().entity(user).now();
        return user;
    }

    @ApiMethod(httpMethod = ApiMethod.HttpMethod.POST,
            name = "users.post",
            path = "users")
    public void addUsers(UserList users) {
        for (User user : users.users) {
            putUser(user);
        }
    }
}
