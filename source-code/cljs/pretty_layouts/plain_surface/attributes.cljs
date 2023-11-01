
(ns pretty-layouts.plain-surface.attributes
    (:require [pretty-css.api :as pretty-css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn surface-body-attributes
  ; @ignore
  ;
  ; @param (keyword) surface-id
  ; @param (map) surface-props
  ; {:content-orientation (keyword)
  ;  :style (map)(opt)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :data-content-orientation (keyword)
  ;  :style (map)}
  [_ {:keys [content-orientation style] :as surface-props}]
  (-> {:class            :pl-plain-surface--body
       :data-orientation content-orientation
       :style            style}
      (pretty-css/color-attributes surface-props)))
