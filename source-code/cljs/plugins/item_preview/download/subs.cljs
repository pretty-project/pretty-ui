
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-preview.download.subs
    (:require [engines.engine-handler.download.subs :as download.subs]
              [plugins.item-preview.core.subs       :as core.subs]
              [re-frame.api                         :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.engine-handler.download.subs
(def get-resolver-id download.subs/get-resolver-id)
(def data-received?  download.subs/data-received?)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request-item?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) preview-id
  ;
  ; @return (boolean)
  [db [_ preview-id]]
  ; A body komponens a React-fába csatolódásakor vagy a paramétereinek
  ; megváltozásakor item-id tulajdonságként kaphat NIL értéket is!
  (let [current-item-id (r core.subs/get-current-item-id db preview-id)]
       (some? current-item-id)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) preview-id
;
; @usage
;  [:item-preview/data-received? :my-preview]
(r/reg-sub :item-preview/data-received? data-received?)
