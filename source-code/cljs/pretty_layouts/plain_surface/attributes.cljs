
(ns pretty-layouts.plain-surface.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

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
      (pretty-attributes/background-attributes surface-props)
      (pretty-attributes/style-attributes surface-props)
      (pretty-attributes/theme-attributes surface-props)))
      ; + class-attributes, state-attributes, style-attributes, theme-attributes
