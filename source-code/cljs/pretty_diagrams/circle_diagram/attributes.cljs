
(ns pretty-diagrams.circle-diagram.attributes
    (:require [pretty-attributes.api                 :as pretty-attributes]
              [pretty-diagrams.circle-diagram.config :as circle-diagram.config]
              [pretty-diagrams.circle-diagram.utils  :as circle-diagram.utils]
              [pretty-diagrams.engine.api            :as pretty-diagrams.engine]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn datum-attributes
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:strength (percentage)
  ;  ...}
  ; @param (integer) dex
  ; @param (*) datum
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [id {:keys [strength] :as props} dex datum]
  (let [datum-color     (pretty-diagrams.engine/get-diagram-datum-color id props dex datum)
        datum-pattern   (circle-diagram.utils/diagram-datum-pattern     id props dex datum)
        datum-transform (circle-diagram.utils/diagram-datum-transform   id props dex datum)]
       (-> {:class :pd-circle-diagram--datum}
           (pretty-attributes/svg-circle-attributes {:diameter circle-diagram.config/CIRCLE-DIAMETER         :stroke-width strength})
           (pretty-attributes/svg-stroke-attributes {:stroke-color datum-color :stroke-pattern datum-pattern :stroke-width strength})
           (pretty-attributes/transform-attributes  {:transform datum-transform}))))

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
  (-> {:class :pd-circle-diagram--inner}
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
  (-> {:class :pd-circle-diagram--outer}
      (pretty-attributes/class-attributes          props)
      (pretty-attributes/inner-position-attributes props)
      (pretty-attributes/outer-position-attributes props)
      (pretty-attributes/outer-size-attributes     props)
      (pretty-attributes/outer-space-attributes    props)
      (pretty-attributes/state-attributes          props)
      (pretty-attributes/theme-attributes          props)))
