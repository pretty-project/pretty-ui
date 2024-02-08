
(ns pretty-accessories.tooltip.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn tooltip-icon-attributes
  ; @ignore
  ;
  ; @param (keyword) tooltip-id
  ; @param (map) tooltip-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [tooltip-id tooltip-props]
  (-> {:class :pa-tooltip--icon}
      (pretty-attributes/icon-attributes tooltip-props)))

(defn tooltip-label-attributes
  ; @ignore
  ;
  ; @param (keyword) tooltip-id
  ; @param (map) tooltip-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ tooltip-props]
  (-> {:class :pa-tooltip--label}
      (pretty-attributes/font-attributes tooltip-props)
      (pretty-attributes/text-attributes tooltip-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn tooltip-body-attributes
  ; @ignore
  ;
  ; @param (keyword) tooltip-id
  ; @param (map) tooltip-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [tooltip-id tooltip-props]
  (-> {:class :pa-tooltip--body}
      (pretty-attributes/background-color-attributes   tooltip-props)
      (pretty-attributes/border-attributes             tooltip-props)
      (pretty-attributes/quarter-block-size-attributes tooltip-props)
      (pretty-attributes/indent-attributes             tooltip-props)
      (pretty-attributes/style-attributes              tooltip-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn tooltip-attributes
  ; @ignore
  ;
  ; @param (keyword) tooltip-id
  ; @param (map) tooltip-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ tooltip-props]
  (-> {:class :pa-tooltip}
      (pretty-attributes/class-attributes        tooltip-props)
      (pretty-attributes/outdent-attributes      tooltip-props)
      (pretty-attributes/position-attributes     tooltip-props)
      (pretty-attributes/state-attributes        tooltip-props)
      (pretty-attributes/theme-attributes        tooltip-props)
      (pretty-attributes/wrapper-size-attributes tooltip-props)))
