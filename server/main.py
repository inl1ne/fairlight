#!/usr/bin/env python
#
# Copyright 2007 Google Inc.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
from google.appengine.api import users
import webapp2
from models.submission import Submission
import restapp

app = restapp.RestApp('/api/v1', debug=True)


@app.route('/auth')
def auth_callback(request, *args, **kwargs):
    return webapp2.Response('AUUUUUUTH!!!!')


@app.entity()
class Submissions(restapp.RestHandler):
    def __init__(self, *args, **kwargs):
        super(Submissions, self).__init__("submissions", Submission, app.make_abs_path("submissions"), *args, **kwargs)




    def add(self):
        submission = Submission(
            contents=self.request.body,
            mime_type=self.request.content_type,
            submitted_by=users.get_current_user()
        )
        key = submission.put()
        return webapp2.Response(key.urlsafe())


