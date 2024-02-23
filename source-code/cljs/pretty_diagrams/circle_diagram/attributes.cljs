
(ns pretty-diagrams.circle-diagram.attributes
    (:require [pretty-attributes.api                 :as pretty-attributes]
              [pretty-diagrams.circle-diagram.config :as circle-diagram.config]
              [pretty-diagrams.circle-diagram.utils  :as circle-diagram.utils]
              [pretty-diagrams.engine.api            :as pretty-diagrams.engine]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn diagram-datum-attributes
  ; @ignore
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ; {:strength (percentage)
  ;  ...}
  ; @param (integer) datum-dex
  ; @param (*) datum
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [diagram-id {:keys [strength] :as diagram-props} datum-dex datum]
  (let [datum-color     (pretty-diagrams.engine/get-diagram-datum-color diagram-id diagram-props datum-dex datum)
        datum-pattern   (circle-diagram.utils/diagram-datum-pattern     diagram-id diagram-props datum-dex datum)
        datum-transform (circle-diagram.utils/diagram-datum-transform   diagram-id diagram-props datum-dex datum)]
       (-> {:class :pd-circle-diagram--datum}
           (pretty-attributes/svg-circle-attributes {:diameter circle-diagram.config/CIRCLE-DIAMETER         :stroke-width strength})
           (pretty-attributes/svg-stroke-attributes {:stroke-color datum-color :stroke-pattern datum-pattern :stroke-width strength})
           (pretty-attributes/transform-attributes  {:transform datum-transform}))))

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
  (-> {:class :pd-circle-diagram--body}
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
  (-> {:class :pd-circle-diagram}
      (pretty-attributes/class-attributes   diagram-props)
      (pretty-attributes/outdent-attributes diagram-props)
      (pretty-attributes/size-attributes    diagram-props)
      (pretty-attributes/state-attributes   diagram-props)
      (pretty-attributes/theme-attributes   diagram-props)))
