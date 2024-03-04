
(ns pretty-diagrams.bar-diagram.attributes
    (:require [pretty-attributes.api      :as pretty-attributes]
              [pretty-diagrams.engine.api :as pretty-diagrams.engine]
              [fruits.css.api :as css]
              [fruits.math.api :as math]))

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
  ;  :strength (percentage)
  ;  ...}
  ; @param (integer) dex
  ; @param (*) datum
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [id {:keys [datum-count datum-max max-value strength] :as props} dex datum]
  (let [datum-color  (pretty-diagrams.engine/get-diagram-datum-color id props dex datum)
        datum-value  (pretty-diagrams.engine/get-diagram-datum-value id props dex datum)
        data-limit   (max max-value datum-max)
        datum-ratio  (math/percent data-limit datum-value)
        datum-height (css/percent  (/ strength datum-count))
        datum-width  (css/percent  datum-ratio)]
       (-> {:class :pd-line-diagram--datum}
           (pretty-attributes/background-color-attributes {:fill-color     datum-color})
           (pretty-attributes/content-size-attributes     {:content-height datum-height :conte-width datum-width}))))

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
      (pretty-attributes/inner-size-attributes  props)
      (pretty-attributes/inner-space-attributes props)
      (pretty-attributes/style-attributes       props)))

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
      (pretty-attributes/class-attributes          props)
      (pretty-attributes/inner-position-attributes props)
      (pretty-attributes/outer-position-attributes props)
      (pretty-attributes/outer-size-attributes     props)
      (pretty-attributes/outer-space-attributes    props)
      (pretty-attributes/state-attributes          props)
      (pretty-attributes/theme-attributes          props)))
