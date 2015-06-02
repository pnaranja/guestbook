(ns guestbook.views.layout
  (:require [hiccup.page :refer [html5 include-css]]))

(defn common [& body]
  "Called by the home route which supplies the body of the html"
  (html5
    [:head
     [:title "Welcome to guestbook"]
     (include-css "/css/screen.css")]
    [:body body]))
