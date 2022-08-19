
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.popups.helpers
    (:require [x.app-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn popup-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ;
  ; @return (map)
  ;  {}
  [popup-id]
  (let [minimized? @(a/subscribe [:ui/get-popup-prop popup-id :minimized?])]
       (merge {:class          :x-app-popups--element
               :data-animation :reveal
               :data-minimized (boolean minimized?)
               :data-nosnippet true
               :id             (a/dom-value popup-id)
               :key            (a/dom-value popup-id)})))
