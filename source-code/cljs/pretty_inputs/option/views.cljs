
(ns pretty-inputs.option.views
    (:require [fruits.random.api                 :as random]
              [pretty-inputs.option.attributes :as option.attributes]
              [pretty-inputs.option.prototypes :as option.prototypes]
              [pretty-inputs.engine.api          :as pretty-inputs.engine]
              [pretty-presets.engine.api         :as pretty-presets.engine]
              [reagent.core :as reagent]
              [pretty-guides.api :as pretty-guides]
              [pretty-accessories.api :as pretty-accessories]
              [pretty-models.api :as pretty-models]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- option
  ; @ignore
  ;
  ; @param (keyword) option-id
  ; @param (map) option-props
  ; {:helper-text (map)(opt)
  ;  :icon (map)(opt)
  ;  :label (map)(opt)
  ;  ...}
  [option-id {:keys [helper-text icon label] :as option-props}]
  [:div (option.attributes/option-attributes option-id option-props)
        [(pretty-models/clickable-auto-tag          option-id option-props)
         (option.attributes/option-inner-attributes option-id option-props)
         [:div {:class :pi-option--checkmark} (if icon        [pretty-accessories/icon   option-id icon])]
         [:div {:class :pi-option--content}   (if label       [pretty-accessories/label  option-id label])
                                              (if helper-text [pretty-guides/helper-text option-id helper-text])]]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) option-id
  ; @param (map) option-props
  [option-id option-props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-inputs.engine/input-did-mount    option-id option-props))
                         :component-will-unmount (fn [_ _] (pretty-inputs.engine/input-will-unmount option-id option-props))
                         :reagent-render         (fn [_ option-props] [option option-id option-props])}))

(defn view
  ; @description
  ; Customizable option element for optionable inputs.
  ;
  ; @links Implemented accessories
  ; [Icon](pretty-core/cljs/pretty-accessories/api.html#icon)
  ; [Label](pretty-core/cljs/pretty-accessories/api.html#label)
  ;
  ; @links Implemented guides
  ; [Helper-text](pretty-core/cljs/pretty-guides/api.html#helper-text)
  ;
  ; @links Implemented properties
  ; [Anchor properties](pretty-core/cljs/pretty-properties/api.html#anchor-properties)
  ; [Background color properties](pretty-core/cljs/pretty-properties/api.html#background-color-properties)
  ; [Border properties](pretty-core/cljs/pretty-properties/api.html#border-properties)
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Clickable state properties](pretty-core/cljs/pretty-properties/api.html#clickable-state-properties)
  ; [Cursor properties](pretty-core/cljs/pretty-properties/api.html#cursor-properties)
  ; [Effect properties](pretty-core/cljs/pretty-properties/api.html#effect-properties)
  ; [Flex properties](pretty-core/cljs/pretty-properties/api.html#flex-properties)
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
  ; @param (keyword)(opt) option-id
  ; @param (map) option-props
  ; Check out the implemented accessories.
  ; Check out the implemented guides.
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-inputs/option.png)
  ; [option {:helper-text {:content "My option helper"}
  ;          :icon        {:border-color  :muted
  ;                        :border-radius {:all :s}
  ;                        :border-width  :xs
  ;                        :icon-name     :done
  ;          :label       {:content "My option"}}]
  ([option-props]
   [view (random/generate-keyword) option-props])

  ([option-id option-props]
   ; @note (tutorials#parameterizing)
   (fn [_ option-props]
       (let [option-props (pretty-presets.engine/apply-preset       option-id option-props)
             option-props (option.prototypes/option-props-prototype option-id option-props)]
            [view-lifecycles option-id option-props]))))
