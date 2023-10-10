
(ns elements.element.views
    (:require [elements.label.views :as label.views]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element-label
  ; @ignore
  ;
  ; @description
  ; This component is the default label component of the elements.
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  ; {:helper (metamorphic-content)(opt)
  ;  :info-text (metamorphic-content)(opt)
  ;  :label (metamorphic-content)(opt)
  ;  :marker-color (keyword)(opt)}
  [element-id {:keys [helper info-text label marker-color]}]
  ; TODO
  ; Using the target-id property when no input element connected to the label
  ; causes a console warning:
  ; "The label's for attribute doesn't match any element id. This might prevent
  ; the browser from correctly autofilling the form and accessibility tools from
  ; working correctly."
  (if label [label.views/element {:content      label
                                  :helper       helper
                                  :info-text    info-text
                                  :marker-color marker-color
                                  :target-id    element-id}]))
