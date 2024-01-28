
(ns pretty-elements.menu-bar.attributes
    (:require [pretty-css.api :as pretty-css]
              [pretty-css.appearance.api :as pretty-css.appearance]
              [pretty-css.accessories.api :as pretty-css.accessories]
              [pretty-css.basic.api :as pretty-css.basic]
              [pretty-css.control.api :as pretty-css.control]
              [pretty-css.content.api :as pretty-css.content]
              [pretty-css.layout.api :as pretty-css.layout]
              [pretty-css.live.api :as pretty-css.live]))

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
      (pretty-css.content/icon-attributes item-props)))

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
      (pretty-css.content/font-attributes item-props)
      (pretty-css.content/unselectable-text-attributes bar-props))) ; <- not tested, just moved here (use bar-props! that contains the text related values)

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
      (pretty-css.accessories/badge-attributes       item-props)
      (pretty-css.appearance/background-attributes       item-props)
      (pretty-css.appearance/border-attributes      item-props)
      (pretty-css.live/effect-attributes      item-props)
      (pretty-css.layout/indent-attributes      item-props)
      (pretty-css.control/mouse-event-attributes item-props)
      (pretty-css.control/anchor-attributes        item-props)
      (pretty-css.accessories/marker-attributes      item-props)))

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
      (pretty-css.layout/outdent-attributes item-props)))

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
       :data-scroll-axis (case orientation :horizontal :x nil)} ; <- támogatja már a flex-attributes
      (pretty-css.layout/flex-attributes bar-props)))

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
      (pretty-css.layout/indent-attributes bar-props)
      (pretty-css.basic/style-attributes  bar-props)))

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
      (pretty-css.basic/class-attributes   bar-props)
      (pretty-css.layout/outdent-attributes bar-props)
      (pretty-css.basic/state-attributes   bar-props)
      (pretty-css.appearance/theme-attributes   bar-props)))
