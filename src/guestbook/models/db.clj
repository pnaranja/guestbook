(ns guestbook.models.db
  (:require [clojure.java.jdbc :as sql])
  (:import java.sql.DriverManager))

;;DB Connection
(def db {:classname "org.sqlite.JDBC",
         :subprotocol "sqlite"
         :subname "db.sq3"})

(defn create-guestbook-table []
  "Creates Table to store guest messages.  Also creates an index"
  (sql/db-do-commands
    db
    (sql/create-table-ddl
     :guestbook
     [:id "INTEGER PRIMARY KEY AUTOINCREMENT"]
     [:timestamp "TIMESTAMP DEFAULT CURRENT_TIMESTAMP"]
     [:name "TEXT"]
     [:message "TEXT"])
    (sql/execute! db ["CREATE INDEX timestamp_index ON guestbook (timestamp)"]) 
    ))

(defn read-guests []
  "Reads the guestbook table in descending order"
  (sql/query db
    "Select * from guestbook order by timestamp desc"))

(defn save-message [name message]
  "Inserts name and message into guestbook table. Notice, id and timestamp is auto created"
  (sql/insert! db
               :guestbook
               {:name name, :message message}))
