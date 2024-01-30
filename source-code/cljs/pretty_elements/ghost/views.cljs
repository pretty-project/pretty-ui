
(ns pretty-elements.ghost.views
    (:require [fruits.random.api                :as random]
              [pretty-elements.ghost.attributes :as ghost.attributes]
              [pretty-elements.ghost.prototypes :as ghost.prototypes]
              [pretty-elements.engine.api                :as pretty-elements.engine]
              [pretty-presets.engine.api :as pretty-presets.engine]
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

(defn- element-lifecycles
  ; @ignore
  ;
  ; @param (keyword) ghost-id
  ; @param (map) ghost-props
  [ghost-id ghost-props]
  ; @note (tutorials#parametering)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    ghost-id ghost-props))
                       :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount ghost-id ghost-props))
                       :reagent-render         (fn [_ ghost-props] [ghost ghost-id ghost-props])}))

(defn element
  ; @param (keyword)(opt) ghost-id
  ; @param (map) ghost-props
  ; {:border-radius (map)(opt)
  ;   {:all, :tl, :tr, :br, :bl (keyword, px or string)(opt)}
  ;  :class (keyword or keywords in vector)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :height (keyword, px or string)(opt)
  ;   Default: :s
  ;  :on-mount-f (function)(opt)
  ;  :on-unmount-f (function)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :theme (keyword)(opt)
  ;  :width (keyword, px or string)(opt)
  ;   Default: :s}
  ;
  ; + fill-color
  ; + animation-attributes
  ;
  ; @usage
  ; [ghost {...}]
  ;
  ; @usage
  ; [ghost :my-ghost {...}]
  ([ghost-props]
   [element (random/generate-keyword) ghost-props])

  ([ghost-id ghost-props]
   ; @note (tutorials#parametering)
   (fn [_ ghost-props]
       (let [ghost-props (pretty-presets.engine/apply-preset     ghost-id ghost-props)
             ghost-props (ghost.prototypes/ghost-props-prototype ghost-id ghost-props)]
            [element-lifecycles ghost-id ghost-props]))))
