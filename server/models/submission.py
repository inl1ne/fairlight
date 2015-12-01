__author__ = 'Ian'

from google.appengine.ext import ndb

class Submission(ndb.Model):
    """Models a file submission"""
    date_submitted = ndb.DateTimeProperty(auto_now_add=True)
    contents = ndb.TextProperty()
    submitted_by = ndb.UserProperty()
    mime_type = ndb.StringProperty()

