
(ns pretty-elements.menu-item.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-item-inner-attributes
  ; @ignore
  ;
  ; @param (keyword) item-id
  ; @param (map) item-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ item-props]
  (-> {:class :pe-menu-item--inner}
      (pretty-attributes/anchor-attributes           item-props)
      (pretty-attributes/background-color-attributes item-props)
      (pretty-attributes/border-attributes           item-props)
      (pretty-attributes/clickable-state-attributes  item-props)
      (pretty-attributes/cursor-attributes           item-props)
      (pretty-attributes/effect-attributes           item-props)
      (pretty-attributes/flex-attributes             item-props)
      (pretty-attributes/inner-size-attributes       item-props)
      (pretty-attributes/inner-space-attributes      item-props)
      (pretty-attributes/mouse-event-attributes      item-props)
      (pretty-attributes/style-attributes            item-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-item-attributes
  ; @ignore
  ;
  ; @param (keyword) item-id
  ; @param (map) item-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ item-props]
  (-> {:class :pe-menu-item}
      (pretty-attributes/class-attributes          item-props)
      (pretty-attributes/inner-position-attributes item-props)
      (pretty-attributes/outer-position-attributes item-props)
      (pretty-attributes/outer-size-attributes     item-props)
      (pretty-attributes/outer-space-attributes    item-props)
      (pretty-attributes/state-attributes          item-props)
      (pretty-attributes/theme-attributes          item-props)))
