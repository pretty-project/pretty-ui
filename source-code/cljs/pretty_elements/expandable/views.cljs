
(ns pretty-elements.expandable.views
    (:require [fruits.random.api :as random]
              [pretty-elements.expandable.attributes :as expandable.attributes]
              [pretty-elements.button.views :as button.views]
              [pretty-elements.surface.views :as surface.views]
              [pretty-elements.expandable.prototypes :as expandable.prototypes]
              [pretty-elements.engine.api :as pretty-elements.engine]
              [pretty-presets.engine.api :as pretty-presets.engine]
              [reagent.api :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn expandable
  ; @ignore
  ;
  ; @param (keyword) expandable-id
  ; @param (map) expandable-props
  [expandable-id expandable-props]
  [:div (expandable.attributes/expandable-attributes expandable-id expandable-props)
        [:div (expandable.attributes/expandable-body-attributes expandable-id expandable-props)
              (let [button-props (expandable.prototypes/button-props-prototype expandable-id expandable-props)]
                   [button.views/view expandable-id button-props])
              (let [surface-props (expandable.prototypes/surface-props-prototype expandable-id expandable-props)]
                   [surface.views/view expandable-id surface-props])]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) expandable-id
  ; @param (map) expandable-props
  [expandable-id expandable-props]
  ; @note (tutorials#parameterizing)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    expandable-id expandable-props))
                       :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount expandable-id expandable-props))
                       :reagent-render         (fn [_ expandable-props] [expandable expandable-id expandable-props])}))

(defn view
  ; @param (keyword)(opt) expandable-id
  ; @param (map) expandable-props
  ; {:button (map)(opt)
  ;  :class (keywords or keywords in vector)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :on-mount-f (function)(opt)
  ;  :on-unmount-f (function)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :surface (map)(opt)
  ;  :theme (keyword)(opt)}
  ;
  ; @usage
  ; [expandable {...}]
  ;
  ; @usage
  ; [expandable :my-expandable {...}]
  ([expandable-props]
   [view (random/generate-keyword) expandable-props])

  ([expandable-id expandable-props]
   ; @note (tutorials#parameterizing)
   (fn [_ expandable-props]
       (let [expandable-props (pretty-presets.engine/apply-preset               expandable-id expandable-props)
             expandable-props (expandable.prototypes/expandable-props-prototype expandable-id expandable-props)]
            [view-lifecycles expandable-id expandable-props]))))
