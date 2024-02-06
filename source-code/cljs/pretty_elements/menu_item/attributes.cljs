
(ns pretty-elements.menu-item.attributes
    (:require [pretty-css.accessories.api :as pretty-css.accessories]
              [pretty-css.appearance.api  :as pretty-css.appearance]
              [pretty-css.basic.api       :as pretty-css.basic]
              [pretty-css.content.api     :as pretty-css.content]
              [pretty-css.control.api     :as pretty-css.control]
              [pretty-css.layout.api      :as pretty-css.layout]
              [pretty-css.live.api        :as pretty-css.live]))

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
      (pretty-css.content/icon-attributes item-props)))

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
      (pretty-css.content/font-attributes              item-props)
      (pretty-css.content/unselectable-text-attributes item-props)))

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
      (pretty-css.accessories/badge-attributes      item-props)
      (pretty-css.accessories/marker-attributes     item-props)
      (pretty-css.appearance/background-attributes  item-props)
      (pretty-css.appearance/border-attributes      item-props)
      (pretty-css.basic/style-attributes            item-props)
      (pretty-css.control/anchor-attributes         item-props)
      (pretty-css.control/focus-attributes          item-props)
      (pretty-css.control/mouse-event-attributes    item-props)
      (pretty-css.control/state-attributes          item-props)
      (pretty-css.control/tab-attributes            item-props)
      (pretty-css.content/cursor-attributes         item-props)
      (pretty-css.layout/full-block-size-attributes item-props)
      (pretty-css.layout/flex-attributes            item-props)
      (pretty-css.layout/indent-attributes          item-props)
      (pretty-css.live/effect-attributes            item-props)))

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
      (pretty-css.appearance/theme-attributes    item-props)
      (pretty-css.basic/class-attributes         item-props)
      (pretty-css.basic/state-attributes         item-props)
      (pretty-css.layout/outdent-attributes      item-props)
      (pretty-css.layout/wrapper-size-attributes item-props)))
