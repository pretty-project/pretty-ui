
(ns pretty-layouts.plain-surface.attributes
    (:require [pretty-css.api :as pretty-css]))

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
      (pretty-css/color-attributes surface-props)
      (pretty-css/style-attributes surface-props)))
