
(ns components.error-label.prototypes)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn label-props-prototype
  ; @param (map) label-props
  ;
  ; @return (map)
  ; {}
  [label-props]
  (merge label-props {:color     :warning
                      :font-size :xs}))
