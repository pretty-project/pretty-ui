
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
           (pretty-attributes/background-color-attributes {:fill-color datum-color})
           (pretty-attributes/size-attributes             {:height     datum-height :width datum-width}))))

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
  (-> {:class :pd-bar-diagram--body}
      (pretty-attributes/body-size-attributes diagram-props)
      (pretty-attributes/indent-attributes    diagram-props)
      (pretty-attributes/style-attributes     diagram-props)))

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
      (pretty-attributes/class-attributes   diagram-props)
      (pretty-attributes/outdent-attributes diagram-props)
      (pretty-attributes/size-attributes    diagram-props)
      (pretty-attributes/state-attributes   diagram-props)
      (pretty-attributes/theme-attributes   diagram-props)))
