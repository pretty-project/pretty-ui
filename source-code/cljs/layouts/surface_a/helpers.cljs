
(ns layouts.surface-a.helpers
    (:require [pretty-css.api :as pretty-css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn surface-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ; @param (map) surface-props
  ; {:content-orientation (keyword)
  ;  :style (map)(opt)}
  ;
  ; @return (map)
  ; {:data-content-orientation (keyword)
  ;  :style (map)}
  [_ {:keys [content-orientation style] :as surface-props}]
  (-> {:data-orientation content-orientation
       :style            style}
      (pretty-css/color-attributes surface-props)))
