
(ns pretty-layouts.plain-surface.attributes
    (:require [pretty-build-kit.api :as pretty-build-kit]))

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
      (pretty-build-kit/color-attributes surface-props)))
