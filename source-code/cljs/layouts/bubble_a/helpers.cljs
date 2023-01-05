
(ns layouts.bubble-a.helpers)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn layout-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  ; @param (map) bubble-props
  ; {:style (map)(opt)}
  ;
  ; @return (map)
  ; {:style (map)}
  [_ {:keys [style]}]
  {:style style})
