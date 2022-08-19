
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.touch-handler.side-effects
    (:require [dom.api                                        :as dom]
              [x.app-core.api                                 :as a]
              [x.app-environment.element-handler.side-effects :as element-handler.side-effects]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn detect-touch-events-api!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (let [% (dom/touch-events-api-detected?)]
       (element-handler.side-effects/set-element-attribute! "x-body-container" "data-touch-detected" %)
       (a/dispatch [:db/set-item! [:environment :touch-handler/meta-items :touch-detected?] %])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-fx :environment/detect-touch-events-api! detect-touch-events-api!)
