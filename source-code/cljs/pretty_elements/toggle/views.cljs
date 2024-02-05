
(ns pretty-elements.toggle.views
    (:require [fruits.random.api                 :as random]
              [metamorphic-content.api           :as metamorphic-content]
              [pretty-elements.toggle.attributes :as toggle.attributes]
              [pretty-elements.toggle.prototypes :as toggle.prototypes]
              [pretty-elements.engine.api                 :as pretty-elements.engine]
              [pretty-presets.engine.api :as pretty-presets.engine]
              [reagent.api                       :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- toggle
  ; @ignore
  ;
  ; @param (keyword) toggle-id
  ; @param (map) toggle-props
  ; {}
  [toggle-id {:keys [content placeholder] :as toggle-props}]
  [:div (toggle.attributes/toggle-attributes toggle-id toggle-props)
        [(pretty-elements.engine/clickable-auto-tag toggle-id toggle-props)
         (toggle.attributes/toggle-body-attributes  toggle-id toggle-props)
         [metamorphic-content/compose content placeholder]]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) toggle-id
  ; @param (map) toggle-props
  [toggle-id toggle-props]
  ; @note (tutorials#parameterizing)
  ; @note (pretty-elements.adornment.views#8097)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    toggle-id toggle-props))
                       :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount toggle-id toggle-props))
                       :component-did-update   (fn [%]   (pretty-elements.engine/element-did-update   toggle-id toggle-props %))
                       :reagent-render         (fn [_ toggle-props] [toggle toggle-id toggle-props])}))

(defn view
  ; @param (keyword)(opt) toggle-id
  ; @param (map) toggle-props
  ; {:border-color (keyword or string)(opt)
  ;  :border-position (keyword)(opt)
  ;  :border-radius (map)(opt)
  ;   {:all, :tl, :tr, :br, :bl (keyword, px or string)(opt)}
  ;  :border-width (keyword, px or string)(opt)
  ;  :class (keyword or keywords in vector)(opt)
  ;  :content (metamorphic-content)(opt)
  ;  :click-effect (keyword)(opt)
  ;  :cursor (keyword or string)(opt)
  ;   Default: :pointer
  ;  :disabled? (boolean)(opt)
  ;  :fill-color (keyword or string)(opt)
  ;  :fill-pattern (keyword)(opt)
  ;   Default: :cover
  ;  :height (keyword, px or string)(opt)
  ;  :highlighted? (boolean)(opt)
  ;  :highlight-color (keyword or string)(opt)
  ;  :highlight-pattern (keyword)(opt)
  ;  :hover-color (keyword or string)(opt)
  ;  :hover-effect (keyword)(opt)
  ;  :hover-pattern (keyword)(opt)
  ;  :href-target (keyword)(opt)
  ;  :href-uri (string)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :keypress (map)(opt)
  ;  :marker-color (keyword or string)(opt)
  ;  :marker-position (keyword)(opt)
  ;  :on-click-f (function)(opt)
  ;  :on-click-timeout (ms)(opt)
  ;  :on-mount-f (function)(opt)
  ;  :on-unmount-f (function)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :placeholder (metamorphic-content)(opt)
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :tab-disabled? (boolean)(opt)
  ;  :theme (keyword)(opt)
  ;  :width (keyword, px or string)(opt)}
  ;
  ; + keypress?
  ;
  ; @usage
  ; [toggle {...}]
  ;
  ; @usage
  ; [toggle :my-toggle {...}]
  ([toggle-props]
   [view (random/generate-keyword) toggle-props])

  ([toggle-id toggle-props]
   ; @note (tutorials#parameterizing)
   (fn [_ toggle-props]
       (let [toggle-props (pretty-presets.engine/apply-preset           toggle-id toggle-props)
             toggle-props (toggle.prototypes/toggle-props-prototype     toggle-id toggle-props)
             toggle-props (pretty-elements.engine/element-timeout-props toggle-id toggle-props :content)]
            [view-lifecycles toggle-id toggle-props]))))
