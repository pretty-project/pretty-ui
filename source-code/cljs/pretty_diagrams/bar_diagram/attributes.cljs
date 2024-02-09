
(ns pretty-diagrams.bar-diagram.attributes
    (:require [pretty-attributes.api      :as pretty-attributes]
              [pretty-diagrams.engine.api :as pretty-diagrams.engine]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn diagram-datum-attributes
  ; @ignore
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ; @param (integer) datum-dex
  ; @param (*) datum
  ;
  ; @return (map)
  ; {}
  [diagram-id diagram-props datum-dex datum])

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
      (pretty-attributes/indent-attributes diagram-props)
      (pretty-attributes/size-attributes   diagram-props)
      (pretty-attributes/style-attributes  diagram-props)))

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
      (pretty-attributes/class-attributes        diagram-props)
      (pretty-attributes/outdent-attributes      diagram-props)
      (pretty-attributes/state-attributes        diagram-props)
      (pretty-attributes/theme-attributes        diagram-props)
      (pretty-attributes/wrapper-size-attributes diagram-props)))
