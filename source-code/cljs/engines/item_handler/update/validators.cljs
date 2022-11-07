
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-handler.update.validators
    (:require [engines.item-handler.core.subs   :as core.subs]
              [engines.item-handler.update.subs :as update.subs]
              [mid-fruits.map                   :as map]
              [mid-fruits.string                :as string]
              [re-frame.api                     :refer [r]]))



;; -- Save item validators ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn save-item-response-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ; @param (map) server-response
  ;
  ; @return (boolean)
  [db [_ handler-id server-response]]
  (let [new-item?  (r core.subs/new-item?             db handler-id)
        saved-item (r update.subs/get-mutation-answer db handler-id (if new-item? :add-item! :save-item!) server-response)]
       (map/namespaced? saved-item)))



;; -- Delete item validators --------------------------------------------------
;; ----------------------------------------------------------------------------

(defn delete-item-response-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ; @param (map) server-response
  ;
  ; @return (boolean)
  [db [_ handler-id server-response]]
  (let [deleted-item-id (r update.subs/get-mutation-answer db handler-id :delete-item! server-response)]
       (string/nonempty? deleted-item-id)))



;; -- Undo delete item validators ---------------------------------------------
;; ----------------------------------------------------------------------------

(defn undo-delete-item-response-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ; @param (map) server-response
  ;
  ; @return (boolean)
  [db [_ handler-id server-response]]
  (let [recovered-item (r update.subs/get-mutation-answer db handler-id :undo-delete-item! server-response)]
       (map/namespaced? recovered-item)))



;; -- Duplicate item validators -----------------------------------------------
;; ----------------------------------------------------------------------------

(defn duplicate-item-response-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ; @param (map) server-response
  ;
  ; @return (boolean)
  [db [_ handler-id server-response]]
  (let [duplicated-item (r update.subs/get-mutation-answer db handler-id :duplicate-item! server-response)]
       (map/namespaced? duplicated-item)))
