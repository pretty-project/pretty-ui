
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.environment.touch-handler.side-effects
    (:require [js-window.api                              :as js-window]
              [re-frame.api                               :as r]
              [x.environment.element-handler.side-effects :as element-handler.side-effects]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn detect-touch-events-api!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (let [% (js-window/touch-events-api-detected?)]
       (element-handler.side-effects/set-element-attribute! "x-body-container" "data-touch-detected" %)
       (r/dispatch [:x.db/set-item! [:x.environment :touch-handler/meta-items :touch-detected?] %])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-fx :x.environment/detect-touch-events-api! detect-touch-events-api!)
