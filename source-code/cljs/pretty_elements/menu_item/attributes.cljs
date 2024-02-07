
(ns pretty-elements.menu-item.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-item-icon-attributes
  ; @ignore
  ;
  ; @param (keyword) item-id
  ; @param (map) item-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ item-props]
  (-> {:class :pe-menu-item--icon}
      (pretty-attributes/icon-attributes item-props)))

(defn menu-item-label-attributes
  ; @ignore
  ;
  ; @param (keyword) item-id
  ; @param (map) item-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ item-props]
  (-> {:class :pe-menu-item--label}
      (pretty-attributes/font-attributes item-props)
      (pretty-attributes/text-attributes item-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-item-body-attributes
  ; @ignore
  ;
  ; @param (keyword) item-id
  ; @param (map) item-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ item-props]
  (-> {:class :pe-menu-item--body}
      (pretty-attributes/anchor-attributes          item-props)
      (pretty-attributes/background-attributes      item-props)
      (pretty-attributes/badge-attributes           item-props)
      (pretty-attributes/border-attributes          item-props)
      (pretty-attributes/clickable-state-attributes item-props)
      (pretty-attributes/cursor-attributes          item-props)
      (pretty-attributes/effect-attributes          item-props)
      (pretty-attributes/flex-attributes            item-props)
      (pretty-attributes/focus-attributes           item-props)
      (pretty-attributes/full-block-size-attributes item-props)
      (pretty-attributes/indent-attributes          item-props)
      (pretty-attributes/marker-attributes          item-props)
      (pretty-attributes/mouse-event-attributes     item-props)
      (pretty-attributes/style-attributes           item-props)))

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
      (pretty-attributes/class-attributes        item-props)
      (pretty-attributes/outdent-attributes      item-props)
      (pretty-attributes/state-attributes        item-props)
      (pretty-attributes/theme-attributes        item-props)
      (pretty-attributes/wrapper-size-attributes item-props)))
