
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.19
; Description:
; Version: v0.2.6
; Compatibility: x4.4.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.engine.element-badge
    (:require [x.app-components.api :as components]
              [x.app-core.api       :as a]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element-badge
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;  {:badge-color (keyword)(opt)
  ;   :badge-content (metamorphic-content)(opt)}
  ;
  ; @return (hiccup)
  [_ {:keys [badge-color badge-content]}]
  (if (some? badge-color)
      [:div.x-element-badge {:data-color badge-color}
                            (if (some? badge-content)
                                [:div.x-element-badge--content (components/content {:content badge-content})])]))
