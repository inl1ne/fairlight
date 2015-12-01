import hashlib
from google.appengine.api import users
from google.appengine.ext.ndb import Cursor, Model
import webapp2
from webapp2_extras import json


__author__ = 'ilewis'

DEFAULT_LIMIT = 100
QUERY_PARAM_LIMIT = 'maxResults'
QUERY_PARAM_PAGE_TOKEN = 'pageToken'
QUERY_PARAM_TERSE = 'deep'


class RestApp(webapp2.WSGIApplication):
    def __init__(self, api_path, *args, **kwargs):
        super(RestApp, self).__init__(*args, **kwargs)
        self.api_path = api_path

    def make_abs_path(self, rel_path):
        return "%s/%s" % (self.api_path, rel_path)

    def add_rest_route(self, path, http_method, handler_cls, handler_method):
        if handler_method and hasattr(handler_cls, handler_method):
            path = self.make_abs_path(path)
            self.router.add(webapp2.Route(path,
                                          handler=handler_cls,
                                          handler_method=handler_method,
                                          methods=[http_method],
                                          name=path))

    def entity(self,
               entity_name=None,
               get_method='list',
               get_id_method='get',
               post_method='add',
               put_id_method='update',
               patch_id_method='patch',
               delete_id_method='delete'):
        def entity_router(cls):
            name = entity_name
            if not entity_name:
                name = cls.__name__.lower()
            entity_path = name
            entity_id_path = entity_path + '/<:.*>'

            self.add_rest_route(entity_path, 'GET', cls, get_method)
            self.add_rest_route(entity_id_path, 'GET', cls, get_id_method)
            self.add_rest_route(entity_path, 'POST', cls, post_method)
            self.add_rest_route(entity_id_path, 'PUT', cls, put_id_method)
            self.add_rest_route(entity_id_path, 'PATCH', cls, patch_id_method)
            self.add_rest_route(entity_id_path, 'DELETE', cls, delete_id_method)
            cls.self_link = self.make_abs_path(entity_path)
            cls.entity_name = name
            return cls

        return entity_router

    def route(self, path, **kwargs):
        def simple_router(target):
            self.router.add(webapp2.Route(path, handler=target, name=target.__name__, **kwargs))

        return simple_router


class RestHandler(webapp2.RequestHandler):

    entity_name = "NO ENTITY NAME DEFINED"
    self_link = "NO SELF LINK DEFINED"
    auth_callback = None

    def __init__(self, entity_cls, *args, **kwargs):
        """
        :type self: RestHandler
        :type entity_cls: Model
        :return:
        """
        super(RestHandler, self).__init__(*args, **kwargs)
        self.entity_class = entity_cls

    def dispatch(self):
        user = users.get_current_user()
        if user is None:
            return self.redirect_to(self.auth_callback)
        else:
            return super(RestHandler, self).dispatch()

    def list(self):
        rq = self.request
        params = rq.params
        limit = params[QUERY_PARAM_LIMIT] if (QUERY_PARAM_LIMIT in params) else DEFAULT_LIMIT
        start = Cursor(urlsafe=params[QUERY_PARAM_PAGE_TOKEN]) if (QUERY_PARAM_PAGE_TOKEN in params) else None
        terse = True if (QUERY_PARAM_TERSE in params) else False

        q = self.entity_class.query()
        results, next_page, more = q.fetch_page(limit, start_cursor=start)
        response = {
            'kind': self.entity_name + "_list",
            'selfLink': self.self_link,
            'nextPageToken': next_page.urlsafe() if next_page else None,
            'items': []
        }
        etag_gen = hashlib.sha1()

        for result in results:
            if isinstance(result, Model):
                result_etag = result.__hash__()
                etag_gen.update(result_etag)
                response.items.append({
                    'kind': self.entity_name,
                    'id': result.key.urlsafe(),
                    'etag': result.__hash__(),
                    'contents': result.to_json(terse)
                })

        response['etag'] = etag_gen.hexdigest()
        self.response.body = json.encode(response)

    def add(self):
        values = json.decode(self.request.body)
        entity = self.entity_class(**values)
        key = entity.put()
        self.response.body = key.urlsafe()


