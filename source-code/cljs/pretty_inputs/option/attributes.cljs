
(ns pretty-inputs.option.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn option-inner-attributes
  ; @ignore
  ;
  ; @param (keyword) option-id
  ; @param (map) option-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ option-props]
  (-> {:class :pi-option--inner}
      (pretty-attributes/background-color-attributes option-props)
      (pretty-attributes/border-attributes           option-props)
      (pretty-attributes/clickable-state-attributes  option-props)
      (pretty-attributes/cursor-attributes           option-props)
      (pretty-attributes/flex-attributes             option-props)
      (pretty-attributes/effect-attributes           option-props)
      (pretty-attributes/inner-size-attributes       option-props)
      (pretty-attributes/inner-space-attributes      option-props)
      (pretty-attributes/mouse-event-attributes      option-props)
      (pretty-attributes/style-attributes            option-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn option-attributes
  ; @ignore
  ;
  ; @param (keyword) option-id
  ; @param (map) option-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ option-props]
  (-> {:class :pi-option}
      (pretty-attributes/class-attributes          option-props)
      (pretty-attributes/inner-position-attributes option-props)
      (pretty-attributes/outer-position-attributes option-props)
      (pretty-attributes/outer-size-attributes     option-props)
      (pretty-attributes/outer-space-attributes    option-props)
      (pretty-attributes/state-attributes          option-props)
      (pretty-attributes/theme-attributes          option-props)))
