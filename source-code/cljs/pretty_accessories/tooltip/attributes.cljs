
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

(defn tooltip-inner-attributes
  ; @ignore
  ;
  ; @param (keyword) tooltip-id
  ; @param (map) tooltip-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [tooltip-id tooltip-props]
  (-> {:class :pa-tooltip--inner}
      (pretty-attributes/animation-attributes        tooltip-props)
      (pretty-attributes/background-color-attributes tooltip-props)
      (pretty-attributes/border-attributes           tooltip-props)
      (pretty-attributes/inner-size-attributes       tooltip-props)
      (pretty-attributes/inner-space-attributes      tooltip-props)
      (pretty-attributes/style-attributes            tooltip-props)))

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
      (pretty-attributes/class-attributes          tooltip-props)
      (pretty-attributes/inner-position-attributes tooltip-props)
      (pretty-attributes/outer-position-attributes tooltip-props)
      (pretty-attributes/outer-size-attributes     tooltip-props)
      (pretty-attributes/outer-space-attributes    tooltip-props)
      (pretty-attributes/state-attributes          tooltip-props)
      (pretty-attributes/theme-attributes          tooltip-props)))
