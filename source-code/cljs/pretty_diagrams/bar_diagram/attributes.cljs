
(ns pretty-diagrams.bar-diagram.attributes
    (:require [pretty-css.appearance.api :as pretty-css.appearance]
              [pretty-css.basic.api      :as pretty-css.basic]
              [pretty-css.layout.api     :as pretty-css.layout]
              [pretty-diagrams.engine.api :as pretty-diagrams.engine]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn diagram-datum-attributes
  ; @ignore
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ; @param (integer) datum-dex
  ; @param (*) datum
  ;
  ; @return (map)
  ; {}
  [diagram-id diagram-props datum-dex datum])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn diagram-body-attributes
  ; @ignore
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ diagram-props]
  (-> {:class :pd-bar-diagram--body}
      (pretty-css.basic/style-attributes         diagram-props)
      (pretty-css.layout/element-size-attributes diagram-props)
      (pretty-css.layout/indent-attributes       diagram-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn diagram-attributes
  ; @ignore
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ diagram-props]
  (-> {:class :pd-bar-diagram}
      (pretty-css.appearance/theme-attributes    diagram-props)
      (pretty-css.basic/class-attributes         diagram-props)
      (pretty-css.basic/state-attributes         diagram-props)
      (pretty-css.layout/outdent-attributes      diagram-props)
      (pretty-css.layout/wrapper-size-attributes diagram-props)))