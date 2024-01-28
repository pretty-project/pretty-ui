
(ns pretty-diagrams.point-diagram.attributes
    (:require [pretty-css.api :as pretty-css]
              [pretty-css.layout.api :as pretty-css.layout]
              [pretty-css.basic.api :as pretty-css.basic]
              [pretty-css.appearance.api :as pretty-css.appearance]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn diagram-body-attributes
  ; @ignore
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :style (map)}
  [_ diagram-props]
  (-> {:class :pd-point-diagram--body
       :style {:width "500px" :height "300px"}} ; TEMP
      (pretty-css.layout/indent-attributes diagram-props)
      (pretty-css.basic/style-attributes  diagram-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn diagram-attributes
  ; @ignore
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ;
  ; @return (map)
  ; {}
  [_ diagram-props]
  (-> {:class :pd-point-diagram}
      (pretty-css.basic/class-attributes   diagram-props)
      (pretty-css.layout/outdent-attributes diagram-props)
      (pretty-css.basic/state-attributes   diagram-props)
      (pretty-css.appearance/theme-attributes   diagram-props)))
