
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.19
; Description:
; Version: v0.2.0
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.engine.element-badge
    (:require [mid-fruits.keyword   :as keyword]
              [x.app-components.api :as components]))



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
      [:div.x-element-badge {:data-color (keyword/to-dom-value badge-color)}
                            (if (some? badge-content)
                                [:div.x-element-badge--content (components/content {:content badge-content})])]))
