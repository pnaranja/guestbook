(defproject guestbook "0.1.0-SNAPSHOT"
  :description "My first guestbook app!"
  :url "http://localhost:3000"
  :dependencies [[org.clojure/clojure "1.7.0-RC1"]
                 [compojure "1.3.4"]
                 [hiccup "1.0.5"]
                 [ring-server "0.4.0"]
                 ;;Utilies for building Ring apps
                 [lib-noir "0.9.9"]
                 ;;JDBC Dependencies
                 [org.clojure/java.jdbc "0.3.6"]
                 [org.xerial/sqlite-jdbc "3.8.7"]
                 ]
  :plugins [[lein-ring "0.9.4"]]
  :ring {:handler guestbook.handler/app
         :init guestbook.handler/init
         :destroy guestbook.handler/destroy}
  :profiles
  {:uberjar {:aot :all}
   :production
   {:ring
    {:open-browser? false, :stacktraces? false, :auto-reload? false}}
   :dev
   {:dependencies [[ring-mock "0.1.5"] [ring/ring-devel "1.4.0-RC1"]]}})
