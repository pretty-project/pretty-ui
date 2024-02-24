
(ns pretty-inputs.chip-group.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn chip-group-chips-placeholder-attributes
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;
  ; @return (map)
  ; {}
  [_ _]
  {:class               :pi-chip-group--chips-placeholder
   :data-font-size      :s
   :data-letter-spacing :auto
   :data-line-height    :text-block
   :data-text-color     :highlight})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn chip-group-inner-attributes
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ group-props]
  (-> {:class :pi-chip-group--inner}
      (pretty-attributes/inner-space-attributes group-props)
      (pretty-attributes/style-attributes       group-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn chip-group-attributes
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;
  ; @return (map)
  ; {}
  [_ group-props]
  (-> {:class :pi-chip-group}
      ; + theme-attributes
      (pretty-attributes/class-attributes  group-props)
      (pretty-attributes/outer-space-attributes group-props)
      (pretty-attributes/state-attributes  group-props)))
