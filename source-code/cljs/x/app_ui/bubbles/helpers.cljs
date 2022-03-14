
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.bubbles.helpers
    (:require [x.app-ui.element :as element]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn primary-button-on-click
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  ; @param (map) bubble-props
  ;  {:primary-button (map)
  ;    {:on-click (metamorphic-event)(opt)}
  ;
  ; @return (map)
  ;  {:dispatch-n (vector)}
  [bubble-id {:keys [primary-button]}]
  {:dispatch-n [(:on-click primary-button)
                [:ui/pop-bubble! bubble-id]]})

(defn bubble-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  ; @param (map) bubble-props
  ;
  ; @return (map)
  [bubble-id bubble-props]
  (element/element-attributes :bubbles bubble-id bubble-props
                              {:data-nosnippet true}))
