
(ns pretty-diagrams.bar-diagram.attributes
    (:require [pretty-attributes.api      :as pretty-attributes]
              [pretty-diagrams.engine.api :as pretty-diagrams.engine]
              [fruits.css.api :as css]
              [fruits.math.api :as math]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn diagram-datum-attributes
  ; @ignore
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ; {:datum-count (integer)
  ;  :datum-max (number)
  ;  :max-value (number)
  ;  :strength (percentage)
  ;  ...}
  ; @param (integer) datum-dex
  ; @param (*) datum
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [diagram-id {:keys [datum-count datum-max max-value strength] :as diagram-props} datum-dex datum]
  (let [datum-color  (pretty-diagrams.engine/get-diagram-datum-color diagram-id diagram-props datum-dex datum)
        datum-value  (pretty-diagrams.engine/get-diagram-datum-value diagram-id diagram-props datum-dex datum)
        data-limit   (max max-value datum-max)
        datum-ratio  (math/percent data-limit datum-value)
        datum-height (css/percent  (/ strength datum-count))
        datum-width  (css/percent  datum-ratio)]
       (-> {:class :pd-line-diagram--datum}
           (pretty-attributes/background-color-attributes {:fill-color     datum-color})
           (pretty-attributes/content-size-attributes     {:content-height datum-height :conte-width datum-width}))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn diagram-inner-attributes
  ; @ignore
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ diagram-props]
  (-> {:class :pd-bar-diagram--inner}
      (pretty-attributes/inner-size-attributes  diagram-props)
      (pretty-attributes/inner-space-attributes diagram-props)
      (pretty-attributes/style-attributes       diagram-props)))

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
  (-> {:class :pd-bar-diagram}
      (pretty-attributes/class-attributes          diagram-props)
      (pretty-attributes/inner-position-attributes diagram-props)
      (pretty-attributes/outer-position-attributes diagram-props)
      (pretty-attributes/outer-size-attributes     diagram-props)
      (pretty-attributes/outer-space-attributes    diagram-props)
      (pretty-attributes/state-attributes          diagram-props)
      (pretty-attributes/theme-attributes          diagram-props)))
