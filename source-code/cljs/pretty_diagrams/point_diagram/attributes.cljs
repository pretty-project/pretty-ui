
(ns pretty-diagrams.point-diagram.attributes
    (:require [pretty-css.api :as pretty-css]
              [pretty-css.layout.api :as pretty-css.layout]))

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
      (pretty-css/style-attributes  diagram-props)))

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
      (pretty-css/class-attributes   diagram-props)
      (pretty-css.layout/outdent-attributes diagram-props)
      (pretty-css/state-attributes   diagram-props)
      (pretty-css/theme-attributes   diagram-props)))
