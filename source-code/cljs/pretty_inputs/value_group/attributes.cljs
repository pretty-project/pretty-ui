
(ns pretty-inputs.value-group.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn value-group-values-placeholder-attributes
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;
  ; @return (map)
  ; {}
  [_ _]
  {:class               :pi-value-group--values-placeholder
   :data-font-size      :s
   :data-letter-spacing :auto
   :data-line-height    :text-block
   :data-text-color     :highlight})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn value-group-inner-attributes
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ group-props]
  (-> {:class :pi-value-group--inner}
      (pretty-attributes/inner-space-attributes group-props)
      (pretty-attributes/style-attributes       group-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn value-group-attributes
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;
  ; @return (map)
  ; {}
  [_ group-props]
  (-> {:class :pi-value-group}
      ; + theme-attributes
      (pretty-attributes/class-attributes  group-props)
      (pretty-attributes/outer-space-attributes group-props)
      (pretty-attributes/state-attributes  group-props)))
