
(ns pretty-layouts.plain-surface.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn surface-inner-attributes
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
  (-> {:class            :pl-plain-surface--inner
       :data-orientation content-orientation}
      (pretty-attributes/background-color-attributes surface-props)
      (pretty-attributes/style-attributes surface-props)
      (pretty-attributes/theme-attributes surface-props)))
      ; + class-attributes, state-attributes, style-attributes, theme-attributes
