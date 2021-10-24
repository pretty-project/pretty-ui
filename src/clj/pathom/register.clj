
(ns pathom.register
    (:require [mid-fruits.vector :as vector]
              [x.server-core.api :as a]
              [x.server-db.api   :as db]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-mutations
  ; @return (vector)
  [db _]
  (get-in db (db/path ::mutations)))

(defn get-resolvers
  ; @return (vector)
  [db _]
  (get-in db (db/path ::resolvers)))

(defn get-handlers
  ; @return (vector)
  [db _]
  (let [resolvers (get-in db (db/path ::resolvers))
        mutations (get-in db (db/path ::mutations))]
       (vector/concat-items resolvers mutations)))

(defn get-registry
  ; @return (vector)
  [db _]
  (let [handlers (r get-handlers db)]
       [handlers]))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reg-mutation!
  ; @param (mutation function) mutation-f
  ;
  ; @return (map)
  [db [_ mutation-f]]
  (update-in db (db/path ::mutations)
             vector/conj-item mutation-f))

; @usage
;  (pco/defmutation do-something! [env] ...)
;  (a/dispatch [:pathom/reg-mutation! do-something!])
(a/reg-event-db :pathom/reg-mutation! reg-mutation!)

(defn reg-mutations!
  ; @param (mutation functions in vector) mutation-fs
  ;
  ; @return (map)
  [db [_ mutation-fs]]
  (update-in db (db/path ::mutations)
             vector/concat-items mutation-fs))

; @usage
;  (pco/defmutation do-something! [env] ...)
;  (pco/defmutation do-anything!  [env] ...)
;  (a/dispatch [:pathom/reg-mutations! [do-something! try-something!]])
(a/reg-event-db :pathom/reg-mutations! reg-mutations!)

(defn reg-resolver!
  ; @param (resolver function) resolver-f
  ;
  ; @return (map)
  [db [_ resolver-f]]
  (update-in db (db/path ::resolvers)
             vector/conj-item resolver-f))

; @usage
;  (pco/defresolver get-something [env] ...)
;  (a/dispatch [:pathom/reg-resolver! get-something])
(a/reg-event-db :pathom/reg-resolver! reg-resolver!)

(defn reg-resolvers!
  ; @param (resolver functions in vector) mutation-fs
  ;
  ; @return (map)
  [db [_ resolver-fs]]
  (update-in db (db/path ::resolvers)
             vector/conj-item resolver-fs))

; @usage
;  (pco/defresolver get-something [env] ...)
;  (pco/defresolver get-anything  [env] ...)
;  (a/dispatch [:pathom/reg-resolvers! [get-something get-anything]])
(a/reg-event-db :pathom/reg-resolvers! reg-resolvers!)



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------
