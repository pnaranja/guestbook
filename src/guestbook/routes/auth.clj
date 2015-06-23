(ns guestbook.routes.auth
  (:require [compojure.core :refer [defroutes GET POST]]
             [guestbook.views.layout :as layout]
             [noir.response :refer [redirect]]
             [hiccup.form :refer [form-to label text-field password-field submit-button]]
             [noir.session :as session]
             [noir.validation :refer [rule errors? has-value? on-error]]
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
    ))


  ;; (defn login-page [& [error]]
  ;;   (layout/common
  ;;    (if error [:p 
  ;;               [:div.error "Login error: " error]])
  ;;    (form-to [:post "/login"]
  ;;              [:div.login
  ;;               (control text-field :id "screen-name")
  ;;               (control password-field :pass "password")
  ;;               [:input {:type "submit" :style "visibility:hidden"}]])
  ;;    )
  ;;   )


  (defn login-page [& [error]]
    (layout/common
     (if error [:p 
                [:div.error "Login error: " error]])
     [:div.container
      [:form.form-signin {:method "POST" :action "/login"}
       [:h2.form-signin-heading "Please sign in"]
       [:label {:for "inputText" :class "sr-only"}]
       [:input {:type "text" :class "form-control" :id "inputText" :placeholder "Username"}]
       [:label {:for "inputPassword" :class "sr-only"}]
       [:input {:type "password" :class "form-control" :id "inputPassword" :placeholder "Password"}]
       [:button {:class "btn btn-lg btn-primary btn-block" :type "submit"} "Login"]]]
     ))


(defn handle-login [id pass]
"Rule validator will execute code if conditional is false"
  (rule (has-value? id) [:id "screen name is required"])
  (rule (= id "foo") [:id "unknown user"])
  (rule (has-value? pass) [:id "password is required"])
  (rule (= pass "bar") [:pass "invalid password"])
  (if (errors? :id :pass) (login-page "Problem with Login or Password")
   (do
     (session/put! :user :id)
     (redirect "/")))
)



;(defn handle-login [id pass]
;  (cond
;   (empty? id)
;   (login-page "screen name is required")
;   (empty? pass)
;   (login-page "password is required")
;   (and (= "foo" id) (= "bar" pass))
;   (do
;     (session/put! :user :id)
;     (redirect "/"))
;   :else
;   (login-page "authentication failed")))


(defn check-same-pass [id pass pass1]
  (if (= pass pass1) (redirect "/")  (registration-page))
  )

(defroutes auth-routes
  (GET "/register" [] (registration-page))
  (POST "/register" [id pass pass1] (check-same-pass id pass pass1))
  (GET "/login" [] (login-page))
  (POST "/login" [id pass]
        (handle-login id pass))
  (GET "/logout" [] 
        ;; (layout/common (form-to [:post "/logout"] (submit-button "logout")))
        (session/clear!) (redirect "/login"))
  )
