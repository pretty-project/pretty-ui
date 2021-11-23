
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.08
; Description:
; Version: v0.2.0
; Compatibility: x4.4.5



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-db.data-history-handler
    (:require [mid-fruits.candy           :refer [param return]]
              [mid-fruits.map             :refer [dissoc-in]]
              [mid-fruits.vector          :as vector]
              [x.mid-core.api             :as a :refer [r]]
              [x.mid-db.partition-handler :as partition-handler]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-db.partition-handler
(def data-item-path    partition-handler/data-item-path)
(def data-history-path partition-handler/data-history-path)
(def get-data-item     partition-handler/get-data-item)



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (integer)
; Szükséges limitálni az egy adatbázis elemek történetének elemszámát.
; Pl.: Egy infinite-loader végtelenítve elküld egy lekérést ...
(def DEFAULT-MAX-HISTORY-COUNT 256)



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-partition-history
  ; @param (namespaced keyword) partition-id
  ;
  ; @usage
  ;  (r db/get-partition-history db ::my-partition)
  ;
  ; @return (map)
  [db [_ partition-id]]
  (get-in db [partition-id :data-history]))

(defn get-data-history
  ; @param (namespaced keyword) partition-id
  ; @param (keyword) data-item-id
  ;
  ; @usage
  ;  (r db/get-data-history db ::my-partition :my-item-id)
  ;
  ; @return (vector)
  [db [_ partition-id data-item-id]]
  (get-in db (data-history-path partition-id data-item-id)))

(defn get-last-data-history-item
  ; @param (namespaced keyword) partition-id
  ; @param (keyword) data-item-id
  ;
  ; @usage
  ;  (r db/get-last-data-history-item db ::my-partition :my-item-id)
  ;
  ; @return (vector)
  [db [_ partition-id data-item-id]]
  (let [data-history (get-in db (data-history-path partition-id data-item-id))]
       (if (vector? data-history)
           (last    data-history))))

(defn get-data-history-result
  ; @param (namespaced keyword) partition-id
  ; @param (keyword) data-item-id
  ;
  ; @usage
  ;  (r db/get-data-history-result db ::my-partition :my-item-id)
  ;
  ; @return (map)
  [db [_ partition-id data-item-id]]
  (if-let [data-history (r get-data-history db partition-id data-item-id)]
          (reduce (fn [result x])
                  (param {})
                  (param data-history))))

(defn- max-history-count-reached?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (namespaced keyword) partition-id
  ; @param (keyword) data-item-id
  ; @param (map)(opt) options
  ;  {:max-history-count (integer)(opt)
  ;    Default: DEFAULT-MAX-HISTORY-COUNT}
  ;
  ; @return (boolean)
  [db [_ partition-id data-item-id {:keys [max-history-count]}]]
  (let [data-history       (r get-data-history db partition-id data-item-id)
        data-history-count (count data-history)
        max-history-count  (or max-history-count DEFAULT-MAX-HISTORY-COUNT)]
       ; = vizsgálat helyett szükséges >= vizsgálatot alkalmazni, hogy ha hibásan
       ; nagyobb a data-history-count értéke, mint a max-history-count értéke,
       ; akkor ne tároljon több elemet a data-history, mert az az adatbázis folyamatos
       ; méretnövekedéséhez vezethet.
       (>= data-history-count max-history-count)))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn clear-data-history!
  ; @param (namespaced keyword) partition-id
  ; @param (keyword) data-item-id
  ;
  ; @usage
  ;  (r db/clear-data-history! db ::my-partition :my-item-id)
  ;
  ; @return (map)
  [db [_ partition-id data-item-id]]
  (dissoc-in db (data-history-path partition-id data-item-id)))

(defn update-data-history!
  ; @param (namespaced keyword) partition-id
  ; @param (keyword) data-item-id
  ; @param (map)(opt) options
  ;  {:max-history-count (integer)(opt)
  ;    Default: DEFAULT-MAX-HISTORY-COUNT}
  ;
  ; @usage
  ;  (r db/update-data-history! db ::my-partition :my-item-id)
  ;
  ; @return (map)
  [db [_ partition-id data-item-id options]]
  (let [current-item (r get-data-item db partition-id data-item-id)]
       (cond-> db :update-data-history!
                  (update-in (data-history-path partition-id data-item-id) vector/conj-item current-item)
                  (r max-history-count-reached? db partition-id data-item-id options)
                  (update-in (data-history-path partition-id data-item-id) vector/remove-first-item))))
