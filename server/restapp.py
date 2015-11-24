from functools import wraps
import webapp2

__author__ = 'ilewis'

API_PATH = '/api/v1'

class RestApp(webapp2.WSGIApplication):
    def __init__(self, *args, **kwargs):
        super(RestApp, self).__init__(*args, **kwargs)

    @classmethod
    def makeAbsPath(cls, rel_path):
        return "%s/%s" % (API_PATH, rel_path)

    def add_rest_route(self, path, http_method, handler_cls, handler_method):
        if handler_method and hasattr(handler_cls, handler_method):
            path = RestApp.makeAbsPath(path)
            self.router.add(webapp2.Route(path,
                                          handler=handler_cls,
                                          handler_method=handler_method,
                                          methods=[http_method]))

    def route(self,
              entity_name = None,
              get_method='list',
              get_id_method='get',
              post_method='add',
              put_id_method='update',
              patch_id_method='patch',
              delete_id_method='delete'):
        def class_router(cls):
            entity_path = entity_name
            if entity_path is None:
                entity_path = cls.__name__.lower()
            entity_id_path = entity_path + '/<:.*>'
            self.add_rest_route(entity_path,    'GET', cls, get_method)
            self.add_rest_route(entity_id_path, 'GET', cls, get_id_method)
            self.add_rest_route(entity_path,    'POST', cls, post_method)
            self.add_rest_route(entity_id_path, 'PUT', cls, put_id_method)
            self.add_rest_route(entity_id_path, 'PATCH', cls, patch_id_method)
            self.add_rest_route(entity_id_path, 'DELETE', cls, delete_id_method)
            cls.selfLink = RestApp.makeAbsPath(entity_path)
            return cls
        return class_router

