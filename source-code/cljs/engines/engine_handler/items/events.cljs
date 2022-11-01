
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.engine-handler.items.events
    (:require [mid-fruits.map    :refer [dissoc-in]]
              [mid-fruits.vector :as vector]
              [re-frame.api      :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn disable-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (integers in vector) item-dexes
  ;
  ; @return (map)
  [db [_ engine-id item-dexes]]
  (update-in db [:engines :engine-handler/meta-items engine-id :disabled-items] vector/concat-items item-dexes))

(defn enable-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (integers in vector) item-dexes
  ;
  ; @return (map)
  [db [_ engine-id item-dexes]]
  (update-in db [:engines :engine-handler/meta-items engine-id :disabled-items] vector/remove-items item-dexes))

(defn enable-all-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ;
  ; @return (map)
  [db [_ engine-id]]
  (dissoc-in db [:engines :engine-handler/meta-items engine-id :disabled-items]))
