
(ns elements.element.views
    (:require [elements.label.views :as label.views]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  ; {:helper (metamorphic-content)(opt)
  ;  :info-text (metamorphic-content)(opt)
  ;  :label (metamorphic-content)(opt)
  ;  :marker-color (keyword)(opt)}
  [element-id {:keys [helper info-text label marker-color]}]
  ; The default label of the elements is implemented by the 'label' element.
  (if label [label.views/element {:content      label
                                  :helper       helper
                                  :info-text    info-text
                                  :line-height  :block
                                  :marker-color marker-color
                                  :target-id    element-id}]))
