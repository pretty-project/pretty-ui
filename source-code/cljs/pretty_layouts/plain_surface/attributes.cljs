
(ns pretty-layouts.plain-surface.attributes
    (:require [pretty-css.api :as pretty-css]
              [pretty-css.appearance.api :as pretty-css.appearance]))

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
      (pretty-css.appearance/background-attributes surface-props)
      (pretty-css/style-attributes surface-props)
      (pretty-css/theme-attributes surface-props)))
