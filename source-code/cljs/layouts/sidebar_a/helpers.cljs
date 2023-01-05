
(ns layouts.sidebar-a.helpers)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn layout-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sidebar-id
  ; @param (map) layout-props
  ; {:position (keyword)
  ;  :style (map)(opt)}
  ;
  ; @return (map)
  ; {:data-position (keyword)
  ;  :style (map)}
  [_ {:keys [position style]}]
  {:data-position position
   :style         style})
