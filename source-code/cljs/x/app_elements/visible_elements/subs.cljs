
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.visible-elements.subs
    (:require [x.app-core.api :refer [r]]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-selected-content-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ;
  ; @return (keyword)
  [db [_ surface-id]]
  (get-in db [:elements :visible-elements/data-items surface-id :selected-content-id]))

(defn content-visible?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ; @param (keyword) content-id
  ;
  ; @return (boolean)
  [db [_ surface-id content-id]]
  (let [selected-content-id (r get-selected-content-id db surface-id)]
       (= selected-content-id content-id)))

(defn surface-empty?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ;
  ; @return (keyword)
  [db [_ surface-id]]
  (nil? (r get-selected-content-id db surface-id)))

(defn get-visible-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ;
  ; @return (map)
  ;  {:selected-content-id (keyword)}
  [db [_ surface-id]]
  ; XXX#4339
  ; Az elem {:selected-content-id ...} tulajdonságát base-props paraméterként
  ; is át lehet adni (ameddig a tulajdonság az adatbázisban nincs jelen)
  (when-let [selected-content-id (r get-selected-content-id db surface-id)]
            {:selected-content-id selected-content-id}))
