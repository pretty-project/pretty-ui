
(ns pretty-elements.row.attributes
    (:require [pretty-build-kit.api :as pretty-build-kit]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn row-body-attributes
  ; @ignore
  ;
  ; @param (keyword) row-id
  ; @param (map) row-props
  ; {:style (map)(opt)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :style (map)}
  [_ {:keys [style] :as row-props}]
  (-> {:class :pe-row--body
       :style style}
      (pretty-build-kit/border-attributes           row-props)
      (pretty-build-kit/color-attributes            row-props)
      (pretty-build-kit/indent-attributes           row-props)
      (pretty-build-kit/element-max-size-attributes row-props)
      (pretty-build-kit/element-min-size-attributes row-props)
      (pretty-build-kit/element-size-attributes     row-props)
      (pretty-build-kit/row-attributes              row-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn row-attributes
  ; @ignore
  ;
  ; @param (keyword) row-id
  ; @param (map) row-props
  ;
  ; @return (map)
  ; {}
  [_ row-props]
  (-> {:class :pe-row}
      (pretty-build-kit/class-attributes        row-props)
      (pretty-build-kit/outdent-attributes      row-props)
      (pretty-build-kit/state-attributes        row-props)
      (pretty-build-kit/wrapper-size-attributes row-props)))
