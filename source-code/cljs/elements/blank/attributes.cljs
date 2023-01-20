
(ns elements.blank.attributes
    (:require [pretty-css.api :as pretty-css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn blank-body-attributes
  ; @ignore
  ;
  ; @param (keyword) blank-id
  ; @param (map) blank-props
  ; {:style (map)(opt)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :style (map)}
  [_ {:keys [style] :as blank-props}]
  (-> {:class :e-blank--body
       :style style}
      (pretty-css/indent-attributes blank-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn blank-attributes
  ; @ignore
  ;
  ; @param (keyword) blank-id
  ; @param (map) blank-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ blank-props]
  (-> {:class :e-blank}
      (pretty-css/default-attributes blank-props)
      (pretty-css/outdent-attributes blank-props)))
