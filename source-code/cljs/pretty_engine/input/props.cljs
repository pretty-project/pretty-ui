
(ns pretty-engine.input.props)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn input-label-props
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {:helper (metamorphic-content)(opt)
  ;  :info-text (metamorphic-content)(opt)
  ;  :label (metamorphic-content)(opt)
  ;  :marker-color (keyword or string)(opt)}
  ;
  ; @return (map)
  ; {}
  [input-id {:keys [helper info-text label marker-color]}]
  (if label {:content      label
             :helper       helper
             :info-text    info-text
             :marker-color marker-color
             :focus-id     input-id}))
