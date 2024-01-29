
(ns pretty-diagrams.circle-diagram.attributes
    (:require [pretty-css.appearance.api             :as pretty-css.appearance]
              [pretty-css.basic.api                  :as pretty-css.basic]
              [pretty-css.layout.api                 :as pretty-css.layout]
              [pretty-css.svg.api                    :as pretty-css.svg]
              [pretty-diagrams.circle-diagram.utils :as circle-diagram.utils]
              [pretty-diagrams.engine.api :as pretty-diagrams.engine]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn diagram-datum-attributes
  ; @ignore
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ; {:diameter (px)
  ;  :strength (px)}
  ; @param (integer) datum-dex
  ; @param (*) datum
  ;
  ; @return (map)
  ; {}
  [diagram-id {:keys [diameter strength] :as diagram-props} datum-dex datum]
  (let [datum-color     (pretty-diagrams.engine/get-diagram-datum-color diagram-id diagram-props datum-dex datum)
        datum-pattern   (circle-diagram.utils/diagram-datum-pattern     diagram-id diagram-props datum-dex datum)
        datum-transform (circle-diagram.utils/diagram-datum-transform   diagram-id diagram-props datum-dex datum)]
       (-> {:class :pd-circle-diagram--datum}
           (pretty-css.svg/circle-attributes       diagram-props)
           (pretty-css.layout/transform-attributes {:transform datum-transform})
           (pretty-css.svg/stroke-attributes       {:stroke-color datum-color :stroke-pattern datum-pattern :stroke-width strength}))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn diagram-svg-container-attributes
  ; @ignore
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ; {:diameter (px)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ {:keys [diameter]}]
  (-> {:class :pd-circle-diagram--svg-container}
      (pretty-css.layout/element-size-attributes {:height diameter :width diameter})))

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
  (-> {:class :pd-circle-diagram--body}
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
  (-> {:class :pd-circle-diagram}
      (pretty-css.appearance/theme-attributes    diagram-props)
      (pretty-css.basic/class-attributes         diagram-props)
      (pretty-css.basic/state-attributes         diagram-props)
      (pretty-css.layout/outdent-attributes      diagram-props)
      (pretty-css.layout/wrapper-size-attributes diagram-props)))
