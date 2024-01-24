
(ns pretty-elements.menu-bar.attributes
    (:require [pretty-css.api :as pretty-css]))

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
      (pretty-css/icon-attributes item-props)))

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
  [_ bar-props item-props]
  (-> {:class              :pe-menu-bar--menu-item--label
       :data-text-overflow :hidden}
      (pretty-css/font-attributes item-props)
      (pretty-css/unselectable-text-attributes bar-props))) ; <- not tested, just moved here (use bar-props! that contains the text related values)

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
      (pretty-css/badge-attributes       item-props)
      (pretty-css/border-attributes      item-props)
      (pretty-css/color-attributes       item-props)
      (pretty-css/effect-attributes      item-props)
      (pretty-css/indent-attributes      item-props)
      (pretty-css/mouse-event-attributes item-props)
      (pretty-css/href-attributes        item-props)
      (pretty-css/marker-attributes      item-props)))

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
      (pretty-css/outdent-attributes item-props)))

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
      (pretty-css/orientation-attributes bar-props)))

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
      (pretty-css/indent-attributes bar-props)
      (pretty-css/style-attributes  bar-props)))

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
      (pretty-css/class-attributes   bar-props)
      (pretty-css/outdent-attributes bar-props)
      (pretty-css/state-attributes   bar-props)))
