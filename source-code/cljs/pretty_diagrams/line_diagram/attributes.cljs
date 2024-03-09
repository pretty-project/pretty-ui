
(ns pretty-diagrams.line-diagram.attributes
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
  ; {:max-value (number)
  ;  :strength (percent)
  ;  :total-value (number)
  ;  ...}
  ; @param (integer) dex
  ; @param (*) datum
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [id {:keys [max-value strength total-value] :as props} dex datum]
  (let [datum-color  (pretty-diagrams.engine/diagram-datum-color id props dex datum)
        datum-value  (pretty-diagrams.engine/diagram-datum-value id props dex datum)
        data-limit   (max max-value total-value)
        datum-ratio  (math/percent data-limit datum-value)
        datum-height (css/percent  strength)
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
  (-> {:class :pd-line-diagram--inner}
      (pretty-models/container-model-inner-attributes props)
      (pretty-models/shape-model-inner-attributes     props)))

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
  (-> {:class :pd-line-diagram--outer}
      (pretty-models/container-model-outer-attributes props)
      (pretty-models/shape-model-outer-attributes     props)))
