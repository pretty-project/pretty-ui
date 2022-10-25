
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-gestures.select-handler.subs
    (:require [x.app-core.api    :refer [r]]
              [mid-fruits.vector :as vector]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-selected-item-ids
  ; @param (keyword) handler-id
  ;
  ; @usage
  ;  (r get-selected-item-ids db :my-handler)
  ;
  ; @return (vector)
  [db [_ handler-id]]
  (get-in db [:gestures :select-handler/data-items handler-id :selected-items]))

(defn select-handler-nonempty?
  ; @param (keyword) handler-id
  ;
  ; @usage
  ;  (r select-handler-nonempty? db :my-handler)
  ;
  ; @return (vector)
  [db [_ handler-id]]
  (let [get-selected-item-ids (r get-selected-item-ids db handler-id)]
       (vector/nonempty? get-selected-item-ids)))

(defn select-handler-empty?
  ; @param (keyword) handler-id
  ;
  ; @usage
  ;  (r select-handler-empty? db :my-handler)
  ;
  ; @return (vector)
  [db [_ handler-id]]
  (let [get-selected-item-ids (r get-selected-item-ids db handler-id)]
       (-> get-selected-item-ids vector/nonempty? not)))

(defn select-handler-enabled?
  ; @param (keyword) handler-id
  ;
  ; @usage
  ;  (r select-handler-enabled? db :my-handler)
  ;
  ; @return (vector)
  [db [_ handler-id]]
  (let [handler-enabled? (get-in db [:gestures :select-handler/data-items handler-id :enabled?])]
       (boolean handler-enabled?)))

(defn select-handler-disabled?
  ; @param (keyword) handler-id
  ;
  ; @usage
  ;  (r select-handler-disabled? db :my-handler)
  ;
  ; @return (vector)
  [db [_ handler-id]]
  (let [handler-enabled? (get-in db [:gestures :select-handler/data-items handler-id :enabled?])]
       (not handler-enabled?)))

(defn item-selected?
  ; @param (keyword) handler-id
  ; @param (keyword) item-id
  ;
  ; @usage
  ;  (r item-selected? db :my-handler :my item)
  ;
  ; @return (boolean)
  [db [_ handler-id item-id]]
  (let [selected-item-ids (r get-selected-item-ids db handler-id)]
       (vector/contains-item? selected-item-ids item-id)))
