
(ns layouts.popup-b.helpers)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn layout-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) layout-props
  ; {:style (map)(opt)}
  ;
  ; @return (map)
  ; {:style (map)}
  [_ {:keys [style]}]
  {:style style})
