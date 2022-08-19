
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.content-handler.subs
    (:require [x.app-core.api :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-selected-content-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ;
  ; @return (keyword)
  [db [_ surface-id]]
  (get-in db [:elements :content-handler/data-items surface-id :selected-content-id]))

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
