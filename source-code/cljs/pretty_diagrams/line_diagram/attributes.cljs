
(ns pretty-diagrams.line-diagram.attributes
    (:require [fruits.css.api                     :as css]
              [pretty-css.accessories.api         :as pretty-css.accessories]
              [pretty-css.appearance.api          :as pretty-css.appearance]
              [pretty-css.basic.api               :as pretty-css.basic]
              [pretty-css.layout.api              :as pretty-css.layout]
              [pretty-diagrams.engine.api :as pretty-diagrams.engine]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn diagram-datum-attributes
  ; @ignore
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ; {:strength (px)}
  ; @param (integer) datum-dex
  ; @param (*) datum
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [diagram-id {:keys [strength] :as diagram-props} _ datum]
  (let [datum-color  (pretty-diagrams.engine/get-diagram-datum-color diagram-id diagram-props datum)
        datum-label  (pretty-diagrams.engine/get-diagram-datum-label diagram-id diagram-props datum)
        datum-ratio  (pretty-diagrams.engine/get-diagram-datum-ratio diagram-id diagram-props datum)
        datum-value  (pretty-diagrams.engine/get-diagram-datum-value diagram-id diagram-props datum)
        datum-height (css/px      strength)
        datum-width  (css/percent datum-ratio)]
       (-> {:class :pd-line-diagram--datum}
           (pretty-css.accessories/badge-attributes     {:badge-content datum-label :badge-position :left :badge-color :highlight})
           (pretty-css.appearance/background-attributes {:fill-color    datum-color})
           (pretty-css.layout/block-size-attributes     {:height        datum-height :width datum-width}))))

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
  (-> {:class :pd-line-diagram--body}
      (pretty-css.basic/style-attributes         diagram-props)
      (pretty-css.layout/element-size-attributes diagram-props)
      (pretty-css.layout/flex-attributes         diagram-props)
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
  (-> {:class :pd-line-diagram}
      (pretty-css.appearance/theme-attributes    diagram-props)
      (pretty-css.basic/class-attributes         diagram-props)
      (pretty-css.basic/state-attributes         diagram-props)
      (pretty-css.layout/outdent-attributes      diagram-props)
      (pretty-css.layout/wrapper-size-attributes diagram-props)))
