
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-preview.download.validators
    (:require [mid-fruits.map                     :as map]
              [plugins.item-preview.download.subs :as download.subs]
              [re-frame.api                       :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request-item-response-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) preview-id
  ; @param (map) server-response
  ;
  ; @return (boolean)
  [db [_ preview-id server-response]]
  (let [resolver-id (r download.subs/get-resolver-id db preview-id :get-item)
        document    (get server-response resolver-id)]
       (map/namespaced? document)))
