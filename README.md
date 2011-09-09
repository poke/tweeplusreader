# twee+ reader

*Twee+ reader* is an Android application that locally decodes [twee+ links](http://tweeplus.com)] and shows the decoded text as an overlay. The application listens for `ACTION_VIEW` intents with `tweeplus.com` as its host, so it is able to intercept any intents to show the website and process the URL itself.

The program source is currently available under the GNU Public License (see the LICENSE file for the full text). The original web application [tweeplus.com](http://tweeplus.com) and its encoding/decoding rules, that are adopted in *twee+ reader*, are made by [Lea Verou](http://leaverou.me).