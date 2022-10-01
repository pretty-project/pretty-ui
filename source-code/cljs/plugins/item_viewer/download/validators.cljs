
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-viewer.download.validators
    (:require [mid-fruits.map                    :as map]
              [plugins.item-viewer.download.subs :as download.subs]
              [re-frame.api                      :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request-item-response-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ; @param (map) server-response
  ;
  ; @return (boolean)
  [db [_ viewer-id server-response]]
  (let [resolver-id (r download.subs/get-resolver-id db viewer-id :get-item)
        document    (get server-response resolver-id)]
       (map/namespaced? document)))
