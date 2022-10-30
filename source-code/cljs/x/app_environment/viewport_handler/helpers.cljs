
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.viewport-handler.helpers
    (:require [mid-fruits.map                            :as map]
              [re-frame.api                              :as r]
              [window.api                                :as window]
              [x.app-environment.viewport-handler.config :as viewport-handler.config]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn detect-viewport-profile
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (keyword)
  ;  :xxs, :xs, :s, :m, :l, :xl, :xxl
  []
  (let [viewport-width (window/get-viewport-width)]
       (letfn [(f [{:keys [min max]}]
                  (and (>= viewport-width min)
                       (<= viewport-width max)))]
              (map/get-first-match-key viewport-handler.config/VIEWPORT-PROFILES f))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn resize-listener
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (function)
  []
  (r/dispatch-once 250 [:environment/viewport-resized]))
