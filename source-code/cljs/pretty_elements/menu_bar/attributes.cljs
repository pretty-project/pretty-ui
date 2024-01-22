
(ns pretty-elements.menu-bar.attributes
    (:require [pretty-build-kit.api :as pretty-build-kit]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-item-icon-attributes
  ; @ignore
  ;
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  ; @param (map) item-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ _ item-props]
  (-> {:class :pe-menu-bar--menu-item--icon}
      (pretty-build-kit/icon-attributes item-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-item-label-attributes
  ; @ignore
  ;
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  ; @param (map) item-props
  ;
  ; @return (map)
  ; {}
  [_ _ item-props]
  (-> {:class              :pe-menu-bar--menu-item--label
       :data-text-overflow :hidden}
      (pretty-build-kit/font-attributes item-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-item-body-attributes
  ; @ignore
  ;
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  ; @param (map) item-props
  ; {:disabled? (boolean)(opt)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :data-disabled (boolean)}
  [_ _ {:keys [disabled?] :as item-props}]
  (-> (if disabled? (cond-> {:class         :pe-menu-bar--menu-item-body
                             :data-disabled true})
                    (cond-> {:class         :pe-menu-bar--menu-item-body}))
      (pretty-build-kit/badge-attributes       item-props)
      (pretty-build-kit/border-attributes      item-props)
      (pretty-build-kit/color-attributes       item-props)
      (pretty-build-kit/effect-attributes      item-props)
      (pretty-build-kit/indent-attributes      item-props)
      (pretty-build-kit/mouse-event-attributes item-props)
      (pretty-build-kit/href-attributes        item-props)
      (pretty-build-kit/marker-attributes      item-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-item-attributes
  ; @ignore
  ;
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  ; @param (map) item-props
  ;
  ; @return (map)
  ; {}
  [_ _ item-props]
  (-> {:class :pe-menu-bar--menu-item}
      (pretty-build-kit/outdent-attributes item-props)))

(defn menu-bar-items-attributes
  ; @ignore
  ;
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [_ {:keys [orientation] :as bar-props}]
  (-> {:class :pe-menu-bar--menu-items
       :data-scroll-axis (case orientation :horizontal :x nil)}
      (pretty-build-kit/orientation-attributes bar-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-bar-body-attributes
  ; @ignore
  ;
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ bar-props]
  (-> {:class :pe-menu-bar--body}
      (pretty-build-kit/indent-attributes       bar-props)
      (pretty-build-kit/style-attributes        bar-props)
      (pretty-build-kit/unselectable-attributes bar-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-bar-attributes
  ; @ignore
  ;
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  ;
  ; @return (map)
  ; {}
  [_ bar-props]
  (-> {:class :pe-menu-bar}
      (pretty-build-kit/class-attributes   bar-props)
      (pretty-build-kit/outdent-attributes bar-props)
      (pretty-build-kit/state-attributes   bar-props)))
