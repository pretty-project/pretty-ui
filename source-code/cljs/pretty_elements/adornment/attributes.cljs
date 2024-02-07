
(ns pretty-elements.adornment.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn adornment-icon-attributes
  ; @ignore
  ;
  ; @param (keyword) adornment-id
  ; @param (map) adornment-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [adornment-id adornment-props]
  (-> {:class :pe-adornment--icon}
      (pretty-attributes/icon-attributes adornment-props)))

(defn adornment-label-attributes
  ; @ignore
  ;
  ; @param (keyword) adornment-id
  ; @param (map) adornment-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ adornment-props]
  (-> {:class :pe-adornment--label}
      (pretty-attributes/font-attributes adornment-props)
      (pretty-attributes/text-attributes adornment-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn adornment-body-attributes
  ; @ignore
  ;
  ; @param (keyword) adornment-id
  ; @param (map) adornment-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [adornment-id adornment-props]
  (-> {:class :pe-adornment--body}
      (pretty-attributes/anchor-attributes          adornment-props)
      (pretty-attributes/background-attributes      adornment-props)
      (pretty-attributes/border-attributes          adornment-props)
      (pretty-attributes/clickable-state-attributes adornment-props)
      (pretty-attributes/cursor-attributes          adornment-props)
      (pretty-attributes/effect-attributes          adornment-props)
      (pretty-attributes/flex-attributes            adornment-props)
      (pretty-attributes/focus-attributes           adornment-props)
      (pretty-attributes/half-block-size-attributes adornment-props)
      (pretty-attributes/indent-attributes          adornment-props)
      (pretty-attributes/mouse-event-attributes     adornment-props)
      (pretty-attributes/progress-attributes        adornment-props)
      (pretty-attributes/style-attributes           adornment-props)
      (pretty-attributes/tooltip-attributes         adornment-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn adornment-attributes
  ; @ignore
  ;
  ; @param (keyword) adornment-id
  ; @param (map) adornment-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ adornment-props]
  (-> {:class :pe-adornment}
      (pretty-attributes/class-attributes        adornment-props)
      (pretty-attributes/outdent-attributes      adornment-props)
      (pretty-attributes/state-attributes        adornment-props)
      (pretty-attributes/theme-attributes        adornment-props)
      (pretty-attributes/wrapper-size-attributes adornment-props)))
