
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-viewer.update.validators
    (:require [mid-fruits.map                  :as map]
              [mid-fruits.string               :as string]
              [plugins.item-viewer.update.subs :as update.subs]
              [re-frame.api                    :refer [r]]))



;; -- Delete item validators --------------------------------------------------
;; ----------------------------------------------------------------------------

(defn delete-item-response-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ; @param (map) server-response
  ;
  ; @return (boolean)
  [db [_ viewer-id server-response]]
  (let [deleted-item-id (r update.subs/get-mutation-answer db viewer-id :delete-item! server-response)]
       (string/nonempty? deleted-item-id)))



;; -- Undo delete item validators ---------------------------------------------
;; ----------------------------------------------------------------------------

(defn undo-delete-item-response-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ; @param (map) server-response
  ;
  ; @return (boolean)
  [db [_ viewer-id server-response]]
  (let [recovered-item (r update.subs/get-mutation-answer db viewer-id :undo-delete-item! server-response)]
       (map/namespaced? recovered-item)))



;; -- Duplicate item validators -----------------------------------------------
;; ----------------------------------------------------------------------------

(defn duplicate-item-response-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ; @param (map) server-response
  ;
  ; @return (boolean)
  [db [_ viewer-id server-response]]
  (let [duplicated-item (r update.subs/get-mutation-answer db viewer-id :duplicate-item! server-response)]
       (map/namespaced? duplicated-item)))
