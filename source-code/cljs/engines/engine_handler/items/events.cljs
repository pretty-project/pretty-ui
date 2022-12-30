
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.engine-handler.items.events
    (:require [engines.engine-handler.body.subs :as body.subs]
              [map.api                          :refer [dissoc-in]]
              [re-frame.api                     :refer [r]]
              [vector.api                       :as vector]))



;; -- Single item events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn disable-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (string) item-id
  ;
  ; @return (map)
  [db [_ engine-id item-id]]
  (update-in db [:engines :engine-handler/meta-items engine-id :disabled-items] vector/conj-item-once item-id))

(defn enable-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (string) item-id
  ;
  ; @return (map)
  [db [_ engine-id item-id]]
  (update-in db [:engines :engine-handler/meta-items engine-id :disabled-items] vector/remove-item item-id))

(defn mark-item-as-changed!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (string) item-id
  ;
  ; @return (map)
  [db [_ engine-id item-id]]
  (let [items-path (r body.subs/get-body-prop db engine-id :items-path)]
       (assoc-in db (conj items-path item-id :meta-items :changed?) true)))

(defn unmark-item-as-changed!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (string) item-id
  ;
  ; @return (map)
  [db [_ engine-id item-id]]
  (let [items-path (r body.subs/get-body-prop db engine-id :items-path)]
       (dissoc-in db (conj items-path item-id :meta-items :changed?))))



;; -- Multiple item events ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn disable-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (strings in vector) item-ids
  ;
  ; @return (map)
  [db [_ engine-id item-ids]]
  (update-in db [:engines :engine-handler/meta-items engine-id :disabled-items] vector/concat-items-once item-ids))

(defn enable-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (strings in vector) item-ids
  ;
  ; @return (map)
  [db [_ engine-id item-ids]]
  (update-in db [:engines :engine-handler/meta-items engine-id :disabled-items] vector/remove-items item-ids))

(defn enable-all-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ;
  ; @return (map)
  [db [_ engine-id]]
  (dissoc-in db [:engines :engine-handler/meta-items engine-id :disabled-items]))
