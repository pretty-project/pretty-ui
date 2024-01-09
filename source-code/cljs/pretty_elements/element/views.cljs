
(ns pretty-elements.element.views
    (:require [pretty-elements.label.views :as label.views]))

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
  ;  :marker-color (keyword or string)(opt)}
  [element-id {:keys [helper info-text label marker-color]}]
  (if label [label.views/element {:content      label
                                  :helper       helper
                                  :info-text    info-text
                                  :marker-color marker-color
                                  :focus-id     element-id}]))
