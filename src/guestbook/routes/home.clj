(ns guestbook.routes.home
  (:require [compojure.core :refer :all]
            [guestbook.views.layout :as layout]
            [hiccup.form :refer :all]
            [guestbook.models.db :as db]))


(defn show-guests []
  "Reads db and then creates a list of the message, name and timestamp.
  The ':keys' directive allows you to bind same named symbols to map keys"
  [:ul.guests
   (for [{:keys [message name timestamp]} (db/read-guests)]
         [:li
          [:blockquote message]
          [:p "-" [:cite name]]
         [:time timestamp]])])

(defn home [& [name message error]]
  "Called on the GET to create the guestbook page"
  (layout/common [:h1 "Guestbook"]
                 [:p "Welcome to my guestbook"]
                 [:p error]

                 ;Generate list of existing comments
                 (show-guests)
                 [:hr]

                 ;Create form with text fields "name" and "message"
                 ;These will be sent when the form posts to the server
                 ;as keywords of the same name
                 (form-to [:post "/"]
                          [:p "Name:"]
                          (text-field "name" name)
                          [:p "Message:"]
                          (text-area {:rows 40, :cols 40} "message" message)
                          [:br]
                          (submit-button "comment"))))

(defn save-message [name message]
  "Will be called on a POST.  If name or message empty, call home function with error, else save message
   and then call home function"
  (cond
   (empty? name)
   (home name message "Forgot to leave a name")
   (empty? message)
   (home name message "Don't you have something to say?")
   :else
   (do
     (db/save-message name message)
     (home))))


(defroutes home-routes
  "If GET, call home function.  If POST, call save-message"
  (GET "/" [] (home))
  (POST "/" [name message] (save-message name message)))
