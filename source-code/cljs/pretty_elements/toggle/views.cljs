
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
  [toggle-id {:keys [content href on-click-f placeholder] :as toggle-props}]
  [:div (toggle.attributes/toggle-attributes toggle-id toggle-props)
        [(cond href :a on-click-f :button :else :div)
         (toggle.attributes/toggle-body-attributes toggle-id toggle-props)
         [metamorphic-content/compose content placeholder]]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- element-lifecycles
  ; @ignore
  ;
  ; @param (keyword) toggle-id
  ; @param (map) toggle-props
  [toggle-id toggle-props]
  ; @note (tutorials#parametering)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    toggle-id toggle-props))
                       :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount toggle-id toggle-props))
                       :reagent-render         (fn [_ toggle-props] [toggle toggle-id toggle-props])}))
                       ; @note (pretty-elements.adornment.views#8097)
                       ; + did-update, keypress?

(defn element
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
  ;   Default: :opacity (if 'href-uri' of 'on-click-f' is provided)
  ;  :cursor (keyword or string)(opt)
  ;   Default: :pointer
  ;  :disabled? (boolean)(opt)
  ;  :fill-color (keyword or string)(opt)
  ;  :fill-pattern (keyword)(opt)
  ;   Default: :cover
  ;  :height (keyword, px or string)(opt)
  ;  :hover-color (keyword or string)(opt)
  ;  :hover-effect (keyword)(opt)
  ;  :hover-pattern (keyword)(opt)
  ;  :href-target (keyword)(opt)
  ;  :href-uri (string)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :marker-color (keyword or string)(opt)
  ;  :marker-position (keyword)(opt)
  ;  :on-click-f (function)(opt)
  ;  :on-mouse-over-f (function)(opt)
  ;  :on-right-click-f (function)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :placeholder (metamorphic-content)(opt)
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :theme (keyword)(opt)
  ;  :width (keyword, px or string)(opt)}
  ;
  ; @usage
  ; [toggle {...}]
  ;
  ; @usage
  ; [toggle :my-toggle {...}]
  ([toggle-props]
   [element (random/generate-keyword) toggle-props])

  ([toggle-id toggle-props]
   ; @note (tutorials#parametering)
   (fn [_ toggle-props]
       (let [toggle-props (pretty-presets.engine/apply-preset                 toggle-props)
             toggle-props (toggle.prototypes/toggle-props-prototype toggle-id toggle-props)]
            [element-lifecycles toggle-id toggle-props]))))
