
(ns pretty-elements.chip.views
    (:require [fruits.random.api                     :as random]
              [pretty-elements.chip.attributes       :as chip.attributes]
              [pretty-elements.chip.prototypes       :as chip.prototypes]
              [pretty-elements.engine.api            :as pretty-elements.engine]
              [pretty-presets.engine.api             :as pretty-presets.engine]
              [pretty-accessories.api             :as pretty-accessories]
              [pretty-models.api             :as pretty-models]
              [reagent.core :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- chip
  ; @ignore
  ;
  ; @param (keyword) chip-id
  ; @param (map) chip-props
  ; {:label (map)(opt)
  ;  ...}
  [chip-id {:keys [label] :as chip-props}]
  [:div (chip.attributes/chip-attributes chip-id chip-props)
        [(pretty-models/clickable-auto-tag      chip-id chip-props)
         (chip.attributes/chip-inner-attributes chip-id chip-props)
         (if label [pretty-accessories/label chip-id label])]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) chip-id
  ; @param (map) chip-props
  [chip-id chip-props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    chip-id chip-props))
                         :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount chip-id chip-props))
                         :reagent-render         (fn [_ chip-props] [chip chip-id chip-props])}))

(defn view
  ; @description
  ; Clickable chip style element.
  ;
  ; @links Implemented accessories
  ; [Label](pretty-ui/cljs/pretty-accessories/api.html#label)
  ; [Tooltip](pretty-ui/cljs/pretty-accessories/api.html#tooltip)
  ;
  ; @links Implemented properties
  ; [Anchor properties](pretty-core/cljs/pretty-properties/api.html#anchor-properties)
  ; [Background color properties](pretty-core/cljs/pretty-properties/api.html#background-color-properties)
  ; [Border properties](pretty-core/cljs/pretty-properties/api.html#border-properties)
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Clickable state properties](pretty-core/cljs/pretty-properties/api.html#clickable-state-properties)
  ; [Cursor properties](pretty-core/cljs/pretty-properties/api.html#cursor-properties)
  ; [Effect properties](pretty-core/cljs/pretty-properties/api.html#effect-properties)
  ; [Inner position properties](pretty-core/cljs/pretty-properties/api.html#inner-position-properties)
  ; [Inner size properties](pretty-core/cljs/pretty-properties/api.html#inner-size-properties)
  ; [Inner space properties](pretty-core/cljs/pretty-properties/api.html#inner-space-properties)
  ; [Lifecycle properties](pretty-core/cljs/pretty-properties/api.html#lifecycle-properties)
  ; [Mouse event properties](pretty-core/cljs/pretty-properties/api.html#mouse-event-properties)
  ; [Outer position properties](pretty-core/cljs/pretty-properties/api.html#outer-position-properties)
  ; [Outer size properties](pretty-core/cljs/pretty-properties/api.html#outer-size-properties)
  ; [Outer space properties](pretty-core/cljs/pretty-properties/api.html#outer-space-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ;
  ; @param (keyword)(opt) chip-id
  ; @param (map) chip-props
  ; Check out the implemented accessories.
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-elements/chip.png)
  ; [chip {:border-radius {:all :s}
  ;        :fill-color    :primary
  ;        :indent        {:horizontal :s}
  ;        :label         {:content "My chip #1" :font-weight :semi-bold}
  ;        :outer-width   :l}]
  ;
  ; [chip {:border-color  :highlight
  ;        :border-radius {:all :s}
  ;        :fill-color    :highlight
  ;        :indent        {:horizontal :s}
  ;        :label         {:content "My chip #2" :font-weight :semi-bold}
  ;        :outer-width   :l}]
  ([chip-props]
   [view (random/generate-keyword) chip-props])

  ([chip-id chip-props]
   ; @note (tutorials#parameterizing)
   (fn [_ chip-props]
       (let [chip-props (pretty-presets.engine/apply-preset   chip-id chip-props)
             chip-props (chip.prototypes/chip-props-prototype chip-id chip-props)]
            [view-lifecycles chip-id chip-props]))))
