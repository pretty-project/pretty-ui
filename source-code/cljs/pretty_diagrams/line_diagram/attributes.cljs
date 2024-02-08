
(ns pretty-diagrams.line-diagram.attributes
    (:require [fruits.css.api             :as css]
              [fruits.math.api            :as math]
              [pretty-attributes.api      :as pretty-attributes]
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
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [diagram-id {:keys [strength] :as diagram-props} datum-dex datum]
  (let [data-limit   (pretty-diagrams.engine/get-diagram-data-limit  diagram-id diagram-props)
        datum-color  (pretty-diagrams.engine/get-diagram-datum-color diagram-id diagram-props datum-dex datum)
        datum-value  (pretty-diagrams.engine/get-diagram-datum-value diagram-id diagram-props datum-dex datum)
        datum-ratio  (math/percent data-limit datum-value)
        datum-height (css/percent  strength)
        datum-width  (css/percent  datum-ratio)]
       (-> {:class :pd-line-diagram--datum}
           (pretty-attributes/background-color-attributes   {:fill-color datum-color})
           (pretty-attributes/quarter-block-size-attributes {:height     datum-height :width datum-width}))))

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
  ;  ...}
  [_ diagram-props]
  (-> {:class :pd-line-diagram--body}
      (pretty-attributes/full-block-size-attributes diagram-props)
      (pretty-attributes/flex-attributes            diagram-props)
      (pretty-attributes/indent-attributes          diagram-props)
      (pretty-attributes/style-attributes           diagram-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn diagram-attributes
  ; @ignore
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ diagram-props]
  (-> {:class :pd-line-diagram}
      (pretty-attributes/class-attributes        diagram-props)
      (pretty-attributes/outdent-attributes      diagram-props)
      (pretty-attributes/state-attributes        diagram-props)
      (pretty-attributes/theme-attributes        diagram-props)
      (pretty-attributes/wrapper-size-attributes diagram-props)))
