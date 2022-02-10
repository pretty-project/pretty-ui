
(ns x.app-developer.engine
    (:require [mid-fruits.candy :refer [param return]]
              [x.app-core.api   :as a]))



;; -- State -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @atom (integer)
(def DB-WRITE-COUNT (atom 0))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-db-write-count
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (swap!   DB-WRITE-COUNT inc)
  (return @DB-WRITE-COUNT))

(a/reg-sub :developer/get-db-write-count get-db-write-count)



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :developer/test!
  [:ui/blow-bubble! {:content "It works!"}])
