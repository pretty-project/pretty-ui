
(ns pretty-elements.card.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn card-content-attributes
  ; @ignore
  ;
  ; @param (keyword) card-id
  ; @param (map) card-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [card-id card-props]
  (-> {:class :pe-card--content}
      (pretty-attributes/font-attributes card-props)
      (pretty-attributes/text-attributes card-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn card-inner-attributes
  ; @ignore
  ;
  ; @param (keyword) card-id
  ; @param (map) card-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [card-id card-props]
  (-> {:class :pe-card--inner}
      (pretty-attributes/anchor-attributes           card-props)
      (pretty-attributes/background-color-attributes card-props)
      (pretty-attributes/border-attributes           card-props)
      (pretty-attributes/clickable-state-attributes  card-props)
      (pretty-attributes/cursor-attributes           card-props)
      (pretty-attributes/effect-attributes           card-props)
      (pretty-attributes/flex-attributes             card-props)
      (pretty-attributes/inner-size-attributes       card-props)
      (pretty-attributes/inner-space-attributes      card-props)
      (pretty-attributes/mouse-event-attributes      card-props)
      (pretty-attributes/react-attributes            card-props)
      (pretty-attributes/style-attributes            card-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn card-attributes
  ; @ignore
  ;
  ; @param (keyword) card-id
  ; @param (map) card-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ card-props]
  (-> {:class :pe-card}
      (pretty-attributes/class-attributes          card-props)
      (pretty-attributes/inner-position-attributes card-props)
      (pretty-attributes/outer-position-attributes card-props)
      (pretty-attributes/outer-size-attributes     card-props)
      (pretty-attributes/outer-space-attributes    card-props)
      (pretty-attributes/state-attributes          card-props)
      (pretty-attributes/theme-attributes          card-props)))
