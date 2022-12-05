
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-browser.update.validators
    (:require [engines.item-browser.update.subs :as update.subs]
              [map.api                          :as map]
              [re-frame.api                     :refer [r]]
              [string.api                       :as string]))



;; -- Delete item validators --------------------------------------------------
;; ----------------------------------------------------------------------------

(defn delete-item-response-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (map) server-response
  ;
  ; @return (boolean)
  [db [_ browser-id server-response]]
  (let [deleted-item-id (r update.subs/get-mutation-answer db browser-id :delete-item! server-response)]
       (string/nonblank? deleted-item-id)))



;; -- Undo delete item validators ---------------------------------------------
;; ----------------------------------------------------------------------------

(defn undo-delete-item-response-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (map) server-response
  ;
  ; @return (boolean)
  [db [_ browser-id server-response]]
  (let [recovered-item (r update.subs/get-mutation-answer db browser-id :undo-delete-item! server-response)]
       (map/namespaced? recovered-item)))



;; -- Duplicate item validators -----------------------------------------------
;; ----------------------------------------------------------------------------

(defn duplicate-item-response-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (map) server-response
  ;
  ; @return (boolean)
  [db [_ browser-id server-response]]
  (let [duplicated-item (r update.subs/get-mutation-answer db browser-id :duplicate-item! server-response)]
       (map/namespaced? duplicated-item)))



;; -- Update item validators --------------------------------------------------
;; ----------------------------------------------------------------------------

(defn update-item-response-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (map) server-response
  ;
  ; @return (boolean)
  [db [_ browser-id server-response]]
  (let [updated-item (r update.subs/get-mutation-answer db browser-id :update-item! server-response)]
       (map/namespaced? updated-item)))
