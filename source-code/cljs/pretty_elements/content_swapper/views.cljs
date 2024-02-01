
(ns pretty-elements.content-swapper.views
    (:require [fruits.random.api                          :as random]
              [pretty-elements.content-swapper.prototypes :as content-swapper.prototypes]
              [pretty-elements.content-swapper.attributes :as content-swapper.attributes]
              [pretty-elements.content-swapper.config      :as content-swapper.config]
              [pretty-elements.engine.api :as pretty-elements.engine]
              [pretty-presets.engine.api :as pretty-presets.engine]
              [reagent.api :as reagent]
              [transition-controller.api :as transition-controller]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- content-swapper
  ; @ignore
  ;
  ; @param (keyword) swapper-id
  ; @param (map) swapper-props
  ; {:content (metamorphic-content)(opt)}
  [swapper-id {:keys [content] :as swapper-props}]
  [:div (content-swapper.attributes/swapper-attributes swapper-id swapper-props)
        [:div (content-swapper.attributes/swapper-body-attributes swapper-id swapper-props)
              [transition-controller/view swapper-id [:div {:class :pe-content-swapper--content} content]
                                                     {:transition-duration content-swapper.config/TRANSITION-DURATION}]]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) swapper-id
  ; @param (map) swapper-props
  [swapper-id swapper-props]
  ; @note (tutorials#parameterizing)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    swapper-id swapper-props))
                       :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount swapper-id swapper-props))
                       :reagent-render         (fn [_ swapper-props] [content-swapper swapper-id swapper-props])}))

(defn view
  ; @param (keyword)(opt) swapper-id
  ; @param (map) swapper-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :content (metamorphic-content)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :height (keyword, px or string)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :max-height (keyword, px or string)(opt)
  ;  :max-width (keyword, px or string)(opt)
  ;  :min-height (keyword, px or string)(opt)
  ;  :min-width (keyword, px or string)(opt)
  ;  :on-mount-f (function)(opt)
  ;  :on-unmount-f (function)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :placeholder (metamorphic-content)(opt)
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :theme (keyword)(opt)
  ;  :width (keyword, px or string)(opt)}
  ;
  ; @usage
  ; [content-swapper {...}]
  ;
  ; @usage
  ; [content-swapper :my-content-swapper {...}]
  ([swapper-props]
   [view (random/generate-keyword) swapper-props])

  ([swapper-id swapper-props]
   ; @note (tutorials#parameterizing)
   (fn [_ swapper-props]
       (let [swapper-props (pretty-presets.engine/apply-preset                  swapper-id swapper-props)
             swapper-props (content-swapper.prototypes/swapper-props-prototype  swapper-id swapper-props)
             swapper-props (pretty-elements.engine/import-element-dynamic-props swapper-id swapper-props)]
            [view-lifecycles swapper-id swapper-props]))))
