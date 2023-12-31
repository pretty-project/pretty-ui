
(ns pretty-elements.menu-bar.attributes
    (:require [dom.api        :as dom]
              [pretty-build-kit.api :as pretty-build-kit]))

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
  ; {:active? (boolean)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :hover-effect (keyword)(opt)
  ;  :on-click (function or Re-Frame metamorphic-event)(opt)
  ;  :on-mouse-over (function or Re-Frame metamorphic-event)(opt)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :data-click-effect (keyword)
  ;  :data-disabled (boolean)
  ;  :on-click (function)
  ;  :on-mouse-over (function)
  ;  :on-mouse-up (function)}
  [_ _ {:keys [active? disabled? hover-effect on-click on-mouse-over] :as item-props}]
  (-> (if disabled? (cond-> {:class             :pe-menu-bar--menu-item-body
                             :data-disabled     true
                             :on-mouse-up       #(dom/blur-active-element!)})
                    (cond-> {:class             :pe-menu-bar--menu-item-body
                             :data-click-effect :opacity
                             :data-hover-effect hover-effect
                             :on-mouse-up       #(dom/blur-active-element!)}
                            (some? on-click)      (assoc :on-click      #(pretty-build-kit/dispatch-event-handler! on-click))
                            (some? on-mouse-over) (assoc :on-mouse-over #(pretty-build-kit/dispatch-event-handler! on-mouse-over))))
      (pretty-build-kit/badge-attributes  item-props)
      (pretty-build-kit/border-attributes item-props)
      (pretty-build-kit/color-attributes  item-props)
      (pretty-build-kit/indent-attributes item-props)
      (pretty-build-kit/link-attributes   item-props)
      (pretty-build-kit/marker-attributes item-props)))

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
  [_ {:keys [orientation]}]
  (merge {:class :pe-menu-bar--menu-items}
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
  ; {:style (map)(opt)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :data-selectable (boolean)
  ;  :style (map)}
  [_ {:keys [style] :as bar-props}]
  (-> {:class           :pe-menu-bar--body
       :data-selectable false
       :style           style}
      (pretty-build-kit/indent-attributes bar-props)))

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
