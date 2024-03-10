
(ns pretty-diagrams.circle-diagram.attributes
    (:require [pretty-attributes.api                 :as pretty-attributes]
              [pretty-diagrams.circle-diagram.config :as circle-diagram.config]
              [pretty-diagrams.circle-diagram.utils  :as circle-diagram.utils]
              [pretty-diagrams.engine.api            :as pretty-diagrams.engine]
              [pretty-models.api                     :as pretty-models]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn datum-attributes
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:strength (percent)
  ;  ...}
  ; @param (integer) dex
  ; @param (*) datum
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [id {:keys [strength] :as props} dex datum]
  (let [datum-color     (pretty-diagrams.engine/diagram-datum-color   id props dex datum)
        datum-pattern   (circle-diagram.utils/diagram-datum-pattern   id props dex datum)
        datum-transform (circle-diagram.utils/diagram-datum-transform id props dex datum)]
       (-> {:class :pd-circle-diagram--datum}
           (pretty-attributes/svg-circle-attributes     {:diameter circle-diagram.config/CIRCLE-DIAMETER         :stroke-width strength})
           (pretty-attributes/svg-stroke-attributes     {:stroke-color datum-color :stroke-pattern datum-pattern :stroke-width strength})
           (pretty-attributes/transformation-attributes {:transform datum-transform}))))

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
  (-> {:class :pd-circle-diagram--outer}
      (pretty-models/plain-container-outer-attributes props)
      (pretty-models/shape-canvas-outer-attributes    props)))
