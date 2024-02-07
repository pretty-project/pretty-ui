
(ns pretty-elements.ghost.views
    (:require [fruits.random.api                :as random]
              [pretty-elements.engine.api       :as pretty-elements.engine]
              [pretty-elements.ghost.attributes :as ghost.attributes]
              [pretty-elements.ghost.prototypes :as ghost.prototypes]
              [pretty-presets.engine.api        :as pretty-presets.engine]
              [reagent.api                      :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn ghost
  ; @ignore
  ;
  ; @param (keyword) ghost-id
  ; @param (map) ghost-props
  [ghost-id ghost-props]
  [:div (ghost.attributes/ghost-attributes ghost-id ghost-props)
        [:div (ghost.attributes/ghost-body-attributes ghost-id ghost-props)]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) ghost-id
  ; @param (map) ghost-props
  [ghost-id ghost-props]
  ; @note (tutorials#parameterizing)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    ghost-id ghost-props))
                       :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount ghost-id ghost-props))
                       :reagent-render         (fn [_ ghost-props] [ghost ghost-id ghost-props])}))

(defn view
  ; @description
  ; Placeholder element for loading contents.
  ;
  ; @param (keyword)(opt) ghost-id
  ; @param (map) ghost-props
  ; {:animation-duration (ms)(opt)
  ;  :animation-mode (keyword)(opt)
  ;  :animation-name (keyword or string)(opt)
  ;  :border-color (keyword or string)(opt)
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
  ;  :max-height (keyword, px or string)(opt)
  ;  :max-width (keyword, px or string)(opt)
  ;  :min-height (keyword, px or string)(opt)
  ;  :min-width (keyword, px or string)(opt)
  ;  :on-mount-f (function)(opt)
  ;  :on-unmount-f (function)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :theme (keyword)(opt)
  ;  :width (keyword, px or string)(opt)}
  ;
  ; @usage
  ; [ghost {...}]
  ;
  ; @usage
  ; [ghost :my-ghost {...}]
  ([ghost-props]
   [view (random/generate-keyword) ghost-props])

  ([ghost-id ghost-props]
   ; @note (tutorials#parameterizing)
   (fn [_ ghost-props]
       (let [ghost-props (pretty-presets.engine/apply-preset     ghost-id ghost-props)
             ghost-props (ghost.prototypes/ghost-props-prototype ghost-id ghost-props)]
            [view-lifecycles ghost-id ghost-props]))))
