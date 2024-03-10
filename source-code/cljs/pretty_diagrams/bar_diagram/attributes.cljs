
(ns pretty-diagrams.bar-diagram.attributes
    (:require [fruits.css.api             :as css]
              [fruits.math.api            :as math]
              [pretty-attributes.api      :as pretty-attributes]
              [pretty-diagrams.engine.api :as pretty-diagrams.engine]
              [pretty-models.api          :as pretty-models]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn datum-attributes
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:datum-count (integer)
  ;  :datum-max (number)
  ;  :max-value (number)
  ;  :strength (percent)
  ;  ...}
  ; @param (integer) dex
  ; @param (*) datum
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [id {:keys [datum-count datum-max max-value strength] :as props} dex datum]
  (let [datum-color  (pretty-diagrams.engine/diagram-datum-color id props dex datum)
        datum-value  (pretty-diagrams.engine/diagram-datum-value id props dex datum)
        data-limit   (max max-value datum-max)
        datum-ratio  (math/percent data-limit datum-value)
        datum-height (css/percent  (/ strength datum-count))
        datum-width  (css/percent  datum-ratio)]
       (-> {:class :pd-line-diagram--datum}
           (pretty-attributes/background-color-attributes {:fill-color     datum-color})
           (pretty-attributes/content-size-attributes     {:content-height datum-height :content-width datum-width}))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn inner-attributes
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ props]
  (-> {:class :pd-bar-diagram--inner}
      (pretty-models/plain-container-inner-attributes props)
      (pretty-models/shape-canvas-inner-attributes    props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn outer-attributes
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ props]
  (-> {:class :pd-bar-diagram--outer}
      (pretty-models/plain-container-outer-attributes props)
      (pretty-models/shape-canvas-outer-attributes    props)))
