
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-lister.items.events
    (:require [engines.engine-handler.items.events :as items.events]
              [re-frame.api                        :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn disable-items!
  ; @param (keyword) lister-id
  ; @param (integers in vector) item-dexes
  ;
  ; @usage
  ;  (r disable-items! db :my-lister [0 1 4])
  ;
  ; @return (map)
  [db [_ lister-id item-dexes]]
  (r items.events/disable-items! db lister-id item-dexes))

(defn enable-items!
  ; @param (keyword) lister-id
  ; @param (integers in vector) item-dexes
  ;
  ; @usage
  ;  (r enable-items! db :my-lister [0 1 4])
  ;
  ; @return (map)
  [db [_ lister-id item-dexes]]
  (r items.events/enable-items! db lister-id item-dexes))

(defn enable-all-items!
  ; @param (keyword) lister-id
  ;
  ; @usage
  ;  (r enable-all-items! db :my-lister)
  ;
  ; @return (map)
  [db [_ lister-id]]
  (r items.events/enable-all-items! db lister-id))