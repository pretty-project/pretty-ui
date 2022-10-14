
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.popups.helpers
    (:require [mid-fruits.hiccup :as hiccup]
              [re-frame.api      :as r]))



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
  (let [minimized?    @(r/subscribe [:ui/get-popup-prop popup-id :minimized?])
        stick-to-top? @(r/subscribe [:ui/get-popup-prop popup-id :stick-to-top?])]
       (merge {:class             :x-app-popups--element
               :data-animation    :reveal
               :data-minimized    (boolean minimized?)
               :data-stick-to-top (boolean stick-to-top?)
               :data-nosnippet    true
               :id                (hiccup/value popup-id)
               :key               (hiccup/value popup-id)})))
