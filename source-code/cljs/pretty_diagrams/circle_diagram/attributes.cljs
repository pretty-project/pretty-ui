
(ns pretty-diagrams.circle-diagram.attributes
    (:require [pretty-css.appearance.api             :as pretty-css.appearance]
              [pretty-css.basic.api                  :as pretty-css.basic]
              [pretty-css.live.api :as pretty-css.live]
              [pretty-css.layout.api                 :as pretty-css.layout]
              [pretty-css.svg.api                    :as pretty-css.svg]
              [pretty-diagrams.circle-diagram.config :as circle-diagram.config]
              [pretty-diagrams.circle-diagram.utils :as circle-diagram.utils]
              [pretty-diagrams.engine.api :as pretty-diagrams.engine]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn diagram-datum-attributes
  ; @ignore
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ; {:strength (percent)}
  ; @param (integer) datum-dex
  ; @param (*) datum
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [diagram-id {:keys [strength] :as diagram-props} datum-dex datum]
  (let [datum-color     (pretty-diagrams.engine/get-diagram-datum-color diagram-id diagram-props datum-dex datum)
        datum-pattern   (circle-diagram.utils/diagram-datum-pattern     diagram-id diagram-props datum-dex datum)
        datum-transform (circle-diagram.utils/diagram-datum-transform   diagram-id diagram-props datum-dex datum)]
       (-> {:class :pd-circle-diagram--datum}
           (pretty-css.live/transform-attributes {:transform datum-transform})
           (pretty-css.svg/circle-attributes     {:diameter circle-diagram.config/CIRCLE-DIAMETER         :stroke-width strength})
           (pretty-css.svg/stroke-attributes     {:stroke-color datum-color :stroke-pattern datum-pattern :stroke-width strength}))))

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
