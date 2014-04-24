<#ftl>




package ${packagename};

import com.github.inl1ne.arc.ListResult;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiReference;
import com.google.api.server.spi.config.DefaultValue;
import com.google.api.server.spi.config.Named;
import com.googlecode.objectify.cmd.Query;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Key;
import java.util.List;

import static com.googlecode.objectify.ObjectifyService.ofy;

@ApiReference(${baseclass}.class)
public class ${classname} {
    public class ${entityName}List {
        public List<${entityName}> entities;
    }

    @ApiMethod(httpMethod = ApiMethod.HttpMethod.GET,
            name = "${path}.arc.getAll",
            path = "${path}")
    public ListResult getAll${entityName}(@Named("page_size") @DefaultValue("-1") int pageSize,
            @Named("page_token") @DefaultValue("") String pageToken) {

        Query<${entityName}> query = ofy().load().type(${entityName}.class);
        return ListResult.fromQuery(query, pageSize, pageToken);
    }

    @ApiMethod(httpMethod = ApiMethod.HttpMethod.GET,
            name = "${path}.arc.getById",
            path = "${path}/{id}")
    public ${entityName} get${entityName}ById(@Named("id") ${idtype} id) {
        Key<${entityName}> entityKey = Key.create(${entityName}.class, id);
        return (${entityName})ofy().load().key(entityKey).now();
    }

    @ApiMethod(httpMethod = ApiMethod.HttpMethod.PUT,
            name = "${path}.arc.put",
            path = "${path}/{id}")
    public ${entityName} put${entityName}(${entityName} entity, @Named("id") ${idtype} id) {
        entity.${idfield} = id;
        ofy().save().entity(entity).now();
        return entity;
    }

    @ApiMethod(httpMethod = ApiMethod.HttpMethod.POST,
            name = "${path}.arc.post",
            path = "${path}")
    public ${entityName} add${entityName}(${entityName} entity) {
        ofy().save().entity(entity).now();
        return entity;
    }

    @ApiMethod(httpMethod = ApiMethod.HttpMethod.PUT,
            name = "${path}.arc.postMultiple",
            path = "${path}")
    public void add${entityName}List(${entityName}List entities) {
        for (${entityName} entity : entities.entities) {
            add${entityName}(entity);
        }
    }

}