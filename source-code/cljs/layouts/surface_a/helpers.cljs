
(ns layouts.surface-a.helpers)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn layout-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ; @param (map) layout-props
  ; {:content-orientation (keyword)
  ;  :style (map)(opt)}
  ;
  ; @return (map)
  ; {:data-content-orientation (keyword)
  ;  :style (map)}
  [_ {:keys [content-orientation style]}]
  {:data-content-orientation content-orientation
   :style                    style})

(defn layout-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ; @param (map) layout-props
  ;
  ; @return (map)
  [_ _]
  {})
