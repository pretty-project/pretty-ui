
(ns pretty-inputs.multi-combo-box.attributes
    (:require [pretty-build-kit.api :as pretty-build-kit]))

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
  (-> {:class :pi-multi-combo-box--body
       :style style}
      (pretty-build-kit/indent-attributes box-props)))

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
  (-> {:class :pi-multi-combo-box}
      (pretty-build-kit/class-attributes   box-props)
      (pretty-build-kit/outdent-attributes box-props)
      (pretty-build-kit/state-attributes   box-props)))
