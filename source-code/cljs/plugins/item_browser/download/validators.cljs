
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.download.validators
    (:require [mid-fruits.map                     :as map]
              [plugins.item-browser.download.subs :as download.subs]
              [re-frame.api                       :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request-item-response-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (map) server-response
  ;
  ; @return (boolean)
  [db [_ browser-id server-response]]
  (let [received-item (r download.subs/get-resolver-answer db browser-id :get-item server-response)]
       (map/namespaced? received-item)))
