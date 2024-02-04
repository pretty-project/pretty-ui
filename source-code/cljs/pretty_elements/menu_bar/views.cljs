
(ns pretty-elements.menu-bar.views
    (:require [fruits.hiccup.api                   :as hiccup]
              [fruits.random.api                   :as random]
              [pretty-elements.button.views :as button.views]
              [pretty-elements.menu-bar.attributes :as menu-bar.attributes]
              [pretty-elements.menu-bar.prototypes :as menu-bar.prototypes]
              [pretty-elements.engine.api :as pretty-elements.engine]
              [pretty-presets.engine.api :as pretty-presets.engine]
              [reagent.api :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-bar-button
  ; @ignore
  ;
  ; @param (integer) button-dex
  ; @param (map) button-props
  [button-dex button-props]
  (let [button-props (menu-bar.prototypes/button-props-prototype button-dex button-props)]
       [button.views/view button-props]))

(defn- menu-bar
  ; @ignore
  ;
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  ; {:buttons (maps in vector)(opt)}
  [bar-id {:keys [buttons] :as bar-props}]
  [:div (menu-bar.attributes/menu-bar-attributes bar-id bar-props)
        [:div (menu-bar.attributes/menu-bar-body-attributes bar-id bar-props)
              (letfn [(f0 [button-dex button-props] [menu-bar-button button-dex button-props])]
                     (hiccup/put-with-indexed [:<>] buttons f0))]])

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
  ; {:button-default (map)(opt)
  ;  :buttons (maps in vector)(opt)
  ;  :class (keyword or keywords in vector)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :gap (keyword, px or string)(opt)
  ;  :height (keyword, px or string)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :max-height (keyword, px or string)(opt)
  ;  :max-width (keyword, px or string)(opt)
  ;  :min-height (keyword, px or string)(opt)
  ;  :min-width (keyword, px or string)(opt)
  ;  :on-mount-f (function)(opt)
  ;  :on-unmount-f (function)(opt)
  ;  :orientation (keyword)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :overflow (keyword)(opt)
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :theme (keyword)(opt)
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
       (let [bar-props (pretty-presets.engine/apply-preset                    bar-id bar-props)
             bar-props (menu-bar.prototypes/bar-props-prototype               bar-id bar-props)
             bar-props (pretty-elements.engine/apply-element-item-default     bar-id bar-props :buttons :button-default)
             bar-props (pretty-elements.engine/inherit-element-disabled-state bar-id bar-props :buttons :button-default)]
            [view-lifecycles bar-id bar-props]))))
