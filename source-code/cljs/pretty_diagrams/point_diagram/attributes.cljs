
(ns pretty-diagrams.point-diagram.attributes
    (:require [pretty-build-kit.api :as pretty-build-kit]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn diagram-body-attributes
  ; @ignore
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ; {:style (map)(opt)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :style (map)}
  [_ {:keys [style] :as diagram-props}]
  (-> {:class :pd-point-diagram--body
       :style (merge style {:width "500px" :height "300px"})} ; TEMP
      (pretty-build-kit/indent-attributes diagram-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn diagram-attributes
  ; @ignore
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ;
  ; @return (map)
  ; {}
  [_ diagram-props]
  (-> {:class :pd-point-diagram}
      (pretty-build-kit/class-attributes   diagram-props)
      (pretty-build-kit/outdent-attributes diagram-props)
      (pretty-build-kit/state-attributes   diagram-props)))
