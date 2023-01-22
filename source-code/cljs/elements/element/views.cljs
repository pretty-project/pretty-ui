
(ns elements.element.views
    (:require [elements.label.views :as label.views]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element-label
  ; @ignore
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  ; {:helper (metamorphic-content)(opt)
  ;  :info-text (metamorphic-content)(opt)
  ;  :label (metamorphic-content)(opt)
  ;  :marker-color (keyword)(opt)}
  [element-id {:keys [helper info-text label marker-color]}]
  ; This component is the default label of elements.
  (if label [label.views/element {:content      label
                                  :helper       helper
                                  :info-text    info-text
                                  :marker-color marker-color
                                  :target-id    element-id}]))
