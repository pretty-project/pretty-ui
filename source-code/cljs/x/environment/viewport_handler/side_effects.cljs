
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.environment.viewport-handler.side-effects
    (:require [dom.api                                    :as dom]
              [js-window.api                              :as js-window]
              [re-frame.api                               :as r]
              [x.environment.element-handler.side-effects :as element-handler.side-effects]
              [x.environment.viewport-handler.helpers     :as viewport-handler.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn listen-to-viewport-resize!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (dom/add-event-listener! "resize" viewport-handler.helpers/resize-listener))

(defn update-viewport-data!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (r/dispatch [:x.db/set-item! [:x.environment :viewport-handler/meta-items]
                               {:viewport-height      (js-window/get-viewport-height)
                                :viewport-orientation (js-window/get-viewport-orientation)
                                :viewport-profile     (viewport-handler.helpers/detect-viewport-profile)
                                :viewport-width       (js-window/get-viewport-width)}]))

(defn detect-viewport-profile!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (let [viewport-profile (viewport-handler.helpers/detect-viewport-profile)]
       (element-handler.side-effects/set-element-attribute! "x-body-container" "data-viewport-profile" (name viewport-profile))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-fx :x.environment/listen-to-viewport-resize! listen-to-viewport-resize!)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-fx :x.environment/update-viewport-data! update-viewport-data!)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-fx :x.environment/detect-viewport-profile! detect-viewport-profile!)
