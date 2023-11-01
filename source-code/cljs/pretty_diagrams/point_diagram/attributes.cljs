
(ns pretty-diagrams.point-diagram.attributes
    (:require [pretty-css.api :as pretty-css]))

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
      (pretty-css/indent-attributes diagram-props)))

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
      (pretty-css/default-attributes diagram-props)
      (pretty-css/outdent-attributes diagram-props)))
