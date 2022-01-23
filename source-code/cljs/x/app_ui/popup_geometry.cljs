
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.05.29
; Description:
; Version: v0.5.4
; Compatibility: x4.5.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.popup-geometry
    (:require [x.app-core.api        :as a :refer [r]]
              [x.app-environment.api :as environment]
              [x.app-ui.renderer     :as renderer]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn render-touch-anchor?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ;
  ; @return (boolean)
  [db [_ popup-id]]
  (let [layout (r renderer/get-element-prop db :popups popup-id :layout)]
       (and (= layout :boxed)
            (r environment/viewport-small?            db)
            (r environment/touch-events-api-detected? db))))
