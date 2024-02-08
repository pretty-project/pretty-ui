
(ns pretty-accessories.bullet.views
    (:require [fruits.random.api :as random]
              [pretty-accessories.bullet.attributes :as bullet.attributes]
              [pretty-accessories.bullet.prototypes :as bullet.prototypes]
              [pretty-elements.engine.api :as pretty-elements.engine]
              [pretty-presets.engine.api :as pretty-presets.engine]
              [reagent.api :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- bullet
  ; @ignore
  ;
  ; @param (keyword) bullet-id
  ; @param (map) bullet-props
  [bullet-id bullet-props]
  [:div (bullet.attributes/bullet-attributes bullet-id bullet-props)
        [:div (bullet.attributes/bullet-body-attributes bullet-id bullet-props)]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) bullet-id
  ; @param (map) bullet-props
  [bullet-id bullet-props]
  ; @note (tutorials#parameterizing)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    bullet-id bullet-props))
                       :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount bullet-id bullet-props))
                       :reagent-render         (fn [_ bullet-props] [bullet bullet-id bullet-props])}))

(defn view
  ; @param (keyword)(opt) bullet-id
  ; @param (map) bullet-props
  ; {:border-color (keyword or string)(opt)
  ;  :border-position (keyword)(opt)
  ;  :border-radius (map)(opt)
  ;   {:all, :tl, :tr, :br, :bl (keyword, px or string)(opt)}
  ;  :border-width (keyword, px or string)(opt)
  ;  :class (keyword or keywords in vector)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :fill-color (keyword or string)(opt)
  ;  :fill-pattern (keyword)(opt)
  ;  :height (keyword, px or string)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :on-mount-f (function)(opt)
  ;  :on-unmount-f (function)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :position (keyword)(opt)
  ;  :position-base (keyword)(opt)
  ;  :position-method (keyword)(opt)
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :theme (keyword)(opt)
  ;  :width (keyword, px or string)(opt)}
  ;
  ; @usage
  ; [bullet {...}]
  ;
  ; @usage
  ; [bullet :my-bullet {...}]
  ([bullet-props]
   [view (random/generate-keyword) bullet-props])

  ([bullet-id bullet-props]
   ; @note (tutorials#parameterizing)
   (fn [_ bullet-props]
       (let [bullet-props (pretty-presets.engine/apply-preset       bullet-id bullet-props)
             bullet-props (bullet.prototypes/bullet-props-prototype bullet-id bullet-props)]
            [view-lifecycles bullet-id bullet-props]))))
