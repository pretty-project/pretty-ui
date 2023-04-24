
(ns elements.menu-bar.attributes
    (:require [dom.api        :as dom]
              [pretty-css.api :as pretty-css]
              [re-frame.api   :as r]))

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
  (-> {:class :e-menu-bar--menu-item--icon}
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
  [_ _ item-props]
  (-> {:class              :e-menu-bar--menu-item--label
       :data-text-overflow :no-wrap}
      (pretty-css/font-attributes item-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-item-body-attributes
  ; @ignore
  ;
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  ; @param (map) item-props
  ; {:active? (boolean)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :hover-effect (keyword)(opt)
  ;  :on-click (Re-Frame metamorphic-event)(opt)
  ;  :on-mouse-over (Re-Frame metamorphic-event)(opt)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :data-click-effect (keyword)
  ;  :data-disabled (boolean)
  ;  :on-click (function)
  ;  :on-mouse-over (function)
  ;  :on-mouse-up (function)}
  [_ _ {:keys [active? disabled? hover-effect on-click on-mouse-over] :as item-props}]
  (-> (if disabled? (cond-> {:class             :e-menu-bar--menu-item-body
                             :data-disabled     true
                             :on-mouse-up       #(dom/blur-active-element!)})
                    (cond-> {:class             :e-menu-bar--menu-item-body
                             :data-click-effect :opacity
                             :data-hover-effect hover-effect
                             :on-mouse-up       #(dom/blur-active-element!)}
                            (some? on-click)      (assoc :on-click      #(r/dispatch on-click))
                            (some? on-mouse-over) (assoc :on-mouse-over #(r/dispatch on-mouse-over))))
      (pretty-css/badge-attributes  item-props)
      (pretty-css/border-attributes item-props)
      (pretty-css/color-attributes  item-props)
      (pretty-css/indent-attributes item-props)
      (pretty-css/link-attributes   item-props)
      (pretty-css/marker-attributes item-props)))

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
  (-> {:class :e-menu-bar--menu-item}
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
  [_ {:keys [orientation]}]
  (merge {:class :e-menu-bar--menu-items}
         (case orientation :horizontal {:data-orientation :horizontal
                                        :data-scroll-axis :x}
                                       {:data-orientation :vertical})))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-bar-body-attributes
  ; @ignore
  ;
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  ; {:horizontal-align (keyword)
  ;  :style (map)(opt)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :data-horizontal-align (keyword)
  ;  :data-selectable (boolean)
  ;  :style (map)}
  [_ {:keys [horizontal-align style] :as bar-props}]
  (-> {:class                 :e-menu-bar--body
       :data-horizontal-align horizontal-align
       :data-selectable       false
       :style                 style}
      (pretty-css/indent-attributes bar-props)))

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
  (-> {:class :e-menu-bar}
      (pretty-css/default-attributes bar-props)
      (pretty-css/outdent-attributes bar-props)))
