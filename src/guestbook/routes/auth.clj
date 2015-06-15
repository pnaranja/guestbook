(ns guestbook.routes.auth
  (:require [compojure.core :refer [defroutes GET POST]]
             [guestbook.views.layout :as layout]
             [noir.response :refer [redirect]]
             [hiccup.form :refer [form-to label text-field password-field submit-button]]
             [noir.session :as session]
             ))

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

(defn login-page []
  (layout/common
    (form-to [:post "/login"]
      (control text-field :id "screen-name")
      (control password-field :pass "password")
      (submit-button "login"))
    )
  )

(defn check-same-pass [id pass pass1]
  (if (= pass pass1) (redirect "/")  (registration-page))
  )

(defroutes auth-routes
  (GET "/register" [] (registration-page))
  (POST "/register" [id pass pass1] (check-same-pass id pass pass1))
  (GET "/login" [] (login-page))
  (POST "/login" [id pass]
        (session/put! :user id)
        (redirect "/"))
  (GET "/logout" [] 
       (layout/common 
         (form-to [:post "/logout"] (submit-button "logout"))))
  (POST "/logout" []
        (session/clear!) (redirect "/"))
  )
