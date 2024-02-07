
(ns pretty-elements.menu-bar.views
    (:require [fruits.hiccup.api                   :as hiccup]
              [fruits.random.api                   :as random]
              [pretty-elements.engine.api          :as pretty-elements.engine]
              [pretty-elements.menu-bar.attributes :as menu-bar.attributes]
              [pretty-elements.menu-bar.prototypes :as menu-bar.prototypes]
              [pretty-elements.menu-item.views     :as menu-item.views]
              [pretty-presets.engine.api           :as pretty-presets.engine]
              [reagent.api                         :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-bar-menu-item
  ; @ignore
  ;
  ; @param (integer) item-dex
  ; @param (map) item-props
  [item-dex item-props]
  (let [item-props (menu-bar.prototypes/item-props-prototype item-dex item-props)]
       [menu-item.views/view item-props]))

(defn- menu-bar
  ; @ignore
  ;
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  ; {:menu-items (maps in vector)(opt)}
  [bar-id {:keys [menu-items] :as bar-props}]
  [:div (menu-bar.attributes/menu-bar-attributes bar-id bar-props)
        [:div (menu-bar.attributes/menu-bar-body-attributes bar-id bar-props)
              (letfn [(f0 [item-dex item-props] [menu-bar-menu-item item-dex item-props])]
                     (hiccup/put-with-indexed [:<>] menu-items f0))]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  [bar-id bar-props]
  ; @note (tutorials#parameterizing)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    bar-id bar-props))
                       :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount bar-id bar-props))
                       :reagent-render         (fn [_ bar-props] [menu-bar bar-id bar-props])}))

(defn view
  ; @param (keyword)(opt) bar-id
  ; @param (map) bar-props
  ; {:border-color (keyword or string)(opt)
  ;  :border-position (keyword)(opt)
  ;  :border-radius (map)(opt)
  ;   {:all, :tl, :tr, :br, :bl (keyword, px or string)(opt)}
  ;  :border-width (keyword, px or string)(opt)
  ;  :class (keyword or keywords in vector)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :fill-color (keyword or string)(opt)
  ;  :fill-pattern (keyword)(opt)
  ;  :gap (keyword, px or string)(opt)
  ;  :height (keyword, px or string)(opt)
  ;  :horizontal-align (keyword)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :max-height (keyword, px or string)(opt)
  ;  :max-width (keyword, px or string)(opt)
  ;  :menu-item-default (map)(opt)
  ;  :menu-items (maps in vector)(opt)
  ;  :min-height (keyword, px or string)(opt)
  ;  :min-width (keyword, px or string)(opt)
  ;  :on-mount-f (function)(opt)
  ;  :on-unmount-f (function)(opt)
  ;  :orientation (keyword)(opt)
  ;  :overflow (keyword)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :overflow (keyword)(opt)
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :theme (keyword)(opt)
  ;  :vertical-align (keyword)(opt)
  ;  :width (keyword, px or string)(opt)}
  ;
  ; @usage
  ; [menu-bar {...}]
  ;
  ; @usage
  ; [menu-bar :my-menu-bar {...}]
  ([bar-props]
   [view (random/generate-keyword) bar-props])

  ([bar-id bar-props]
   ; @note (tutorials#parameterizing)
   (fn [_ bar-props]
       (let [bar-props (pretty-presets.engine/apply-preset                            bar-id bar-props)
             bar-props (menu-bar.prototypes/bar-props-prototype                       bar-id bar-props)
             bar-props (pretty-elements.engine/element-subitem-group<-subitem-default bar-id bar-props :menu-items :menu-item-default)
             bar-props (pretty-elements.engine/element-subitem-group<-disabled-state  bar-id bar-props :menu-items :menu-item-default)
             bar-props (pretty-elements.engine/dissoc-element-disabled-state          bar-id bar-props)]
            [view-lifecycles bar-id bar-props]))))
