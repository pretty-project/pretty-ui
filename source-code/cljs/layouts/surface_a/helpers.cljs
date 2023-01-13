
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
  ;  :fill-color (keyword or string)(opt)
  ;  :style (map)(opt)}
  ;
  ; @return (map)
  ; {:data-content-orientation (keyword)
  ;  :style (map)}
  [_ {:keys [content-orientation fill-color style]}]
  (-> {:data-orientation content-orientation
       :style            style}
      (pretty-css/apply-color :fill-color :data-fill-color fill-color)))
