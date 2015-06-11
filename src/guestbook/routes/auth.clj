(ns guestbook.routes.auth
  (:require [compojure.core :refer [defroutes GET POST]]
             [guestbook.views.layout :as layout]
             [noir.response :refer [redirect]]
             [hiccup.form :refer
              [ form-to label text-field password-field submit-button]]))

(defn control [field name text]
  (list (label name text)
        (field name)
        [:br])
  )

(defn registration-page []
  (layout/common
    (form-to [:post "/register"]
      (control text-field "id" "Screen-name")
      (control password-field "pass" "Password")
      (control password-field "pass1" "Retype-password")
      (submit-button "Create Account"))
    )
  )

(defn check-same-pass [id pass pass1]
  (if (= pass pass1) (redirect "/")  (registration-page))
  )

(defroutes auth-routes
  (GET "/register" [_] (registration-page))
  (POST "/register" [id pass pass1] (check-same-pass id pass pass1))
  )
