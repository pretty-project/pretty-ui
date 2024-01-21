
(ns pretty-layouts.plain-surface.attributes
    (:require [pretty-build-kit.api :as pretty-build-kit]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn surface-body-attributes
  ; @ignore
  ;
  ; @param (keyword) surface-id
  ; @param (map) surface-props
  ; {:content-orientation (keyword)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :data-content-orientation (keyword)}
  [_ {:keys [content-orientation] :as surface-props}]
  (-> {:class            :pl-plain-surface--body
       :data-orientation content-orientation}
      (pretty-build-kit/color-attributes surface-props)
      (pretty-build-kit/style-attributes surface-props)))
