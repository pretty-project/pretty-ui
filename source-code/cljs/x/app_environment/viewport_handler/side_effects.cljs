
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.viewport-handler.side-effects
    (:require [dom.api                                        :as dom]
              [re-frame.api                                   :as r]
              [x.app-environment.element-handler.side-effects :as element-handler.side-effects]
              [x.app-environment.viewport-handler.helpers     :as viewport-handler.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn listen-to-viewport-resize!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (dom/add-event-listener! "resize" viewport-handler.helpers/resize-listener))

(defn update-viewport-data!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (r/dispatch [:db/set-item! [:environment :viewport-handler/meta-items]
                             {:viewport-height      (dom/get-viewport-height)
                              :viewport-orientation (dom/get-viewport-orientation)
                              :viewport-profile     (dom/get-viewport-profile)
                              :viewport-width       (dom/get-viewport-width)}]))

(defn detect-viewport-profile!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (let [viewport-profile (dom/get-viewport-profile)]
       (element-handler.side-effects/set-element-attribute! "x-body-container" "data-viewport-profile" (name viewport-profile))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-fx :environment/listen-to-viewport-resize! listen-to-viewport-resize!)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-fx :environment/update-viewport-data! update-viewport-data!)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-fx :environment/detect-viewport-profile! detect-viewport-profile!)
