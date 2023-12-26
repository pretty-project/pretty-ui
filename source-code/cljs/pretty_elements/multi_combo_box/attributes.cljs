
(ns pretty-elements.multi-combo-box.attributes
    (:require [pretty-css.api :as pretty-css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn box-body-attributes
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ; {:style (map)(opt)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :style (map)}
  [_ {:keys [style] :as box-props}]
  (-> {:class :pe-multi-combo-box--body
       :style style}
      (pretty-css/indent-attributes box-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn box-attributes
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ;
  ; @return (map)
  ; {}
  [_ box-props]
  (-> {:class :pe-multi-combo-box}
      (pretty-css/class-attributes   box-props)
      (pretty-css/state-attributes   box-props)
      (pretty-css/outdent-attributes box-props)))
