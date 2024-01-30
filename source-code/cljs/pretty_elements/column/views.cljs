
(ns pretty-elements.column.views
    (:require [fruits.random.api                 :as random]
              [metamorphic-content.api           :as metamorphic-content]
              [pretty-elements.column.attributes :as column.attributes]
              [pretty-elements.column.prototypes :as column.prototypes]
              [pretty-elements.engine.api                 :as pretty-elements.engine]
              [pretty-presets.engine.api :as pretty-presets.engine]
              [reagent.api                       :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- column
  ; @ignore
  ;
  ; @param (keyword) column-id
  ; @param (map) column-props
  ; {:content (metamorphic-content)(opt)
  ;  :placeholder (metamorphic-content)(opt)}
  [column-id {:keys [content placeholder] :as column-props}]
  [:div (column.attributes/column-attributes column-id column-props)
        [:div (column.attributes/column-body-attributes column-id column-props)
              [metamorphic-content/compose content placeholder]]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- element-lifecycles
  ; @ignore
  ;
  ; @param (keyword) column-id
  ; @param (map) column-props
  [column-id column-props]
  ; @note (tutorials#parametering)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    column-id column-props))
                       :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount column-id column-props))
                       :reagent-render         (fn [_ column-props] [column column-id column-props])}))

(defn element
  ; @param (keyword)(opt) column-id
  ; @param (map) column-props
  ; {:border-color (keyword or string)(opt)
  ;  :border-position (keyword)(opt)
  ;  :border-radius (map)(opt)
  ;   {:all, :tl, :tr, :br, :bl (keyword, px or string)(opt)}
  ;  :border-width (keyword, px or string)(opt)
  ;  :class (keyword or keywords in vector)(opt)
  ;  :content (metamorphic-content)(opt)
  ;  :fill-color (keyword or string)(opt)
  ;  :fill-pattern (keyword)(opt)
  ;   Default: :cover
  ;  :gap (keyword, px or string)(opt)
  ;  :height (keyword, px or string)(opt)
  ;  :horizontal-align (keyword)(opt)
  ;   Default: :center
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
  ;  :overflow (keyword)(opt)
  ;  :placeholder (metamorphic-content)(opt)
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :theme (keyword)(opt)
  ;  :vertical-align (keyword)(opt)
  ;   Default: :top
  ;  :width (keyword, px or string)(opt)}
  ;
  ; @usage
  ; [elements/column {...}]
  ;
  ; @usage
  ; [elements/column :my-column {...}]
  ([column-props]
   [element (random/generate-keyword) column-props])

  ([column-id column-props]
   ; @note (tutorials#parametering)
   (fn [_ column-props]
       (let [column-props (pretty-presets.engine/apply-preset       column-id column-props)
             column-props (column.prototypes/column-props-prototype column-id column-props)]
            [element-lifecycles column-id column-props]))))
