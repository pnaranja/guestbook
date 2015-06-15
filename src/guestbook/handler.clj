(ns guestbook.handler
  (:require [compojure.core :refer [defroutes routes]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.file-info :refer [wrap-file-info]]
            [hiccup.middleware :refer [wrap-base-url]]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [guestbook.routes.home :refer [home-routes]]
            [guestbook.routes.auth :refer [auth-routes]]
            [guestbook.models.db :refer [create-guestbook-table]]
            [noir.session :as session]
            [ring.middleware.session.memory :refer [memory-store]]))

(defn init []
  "Referenced with the ring key init in the project.clj"
  (println "guestbook is starting")
  (if-not (.exists (java.io.File. "./db.sq3"))
    (create-guestbook-table)))

(defn destroy []
  "Referenced with the ring key destroy in the project.clj"
  (println "guestbook is shutting down"))

(defroutes app-routes
  "Route for static resources and catch-all for routes not found"
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  "Referenced with the ring key handler in the project.clj.  Put app-routes last because of 
  the not-found route, which can prevent the home-routes from being processed.
  Also store user sessions in memory."
  (-> (handler/site (routes auth-routes home-routes app-routes))
      (session/wrap-noir-session {:store (memory-store)})))
