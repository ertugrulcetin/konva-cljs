(ns samples.events
  (:require
   [re-frame.core :as re-frame]
   [samples.db :as db]
   ))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))
