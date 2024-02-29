
(ns pretty-elements.horizontal-separator.views
    (:require [fruits.random.api                               :as random]
              [pretty-elements.engine.api                      :as pretty-elements.engine]
              [pretty-elements.horizontal-separator.attributes :as horizontal-separator.attributes]
              [pretty-elements.horizontal-separator.prototypes :as horizontal-separator.prototypes]
              [pretty-presets.engine.api                       :as pretty-presets.engine]
              [pretty-accessories.api :as pretty-accessories]
              [reagent.core :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- horizontal-separator
  ; @ignore
  ;
  ; @param (keyword) separator-id
  ; @param (map) separator-props
  ; {:label (map)(opt)
  ;  ...}
  [separator-id {:keys [label] :as separator-props}]
  [:div (horizontal-separator.attributes/separator-attributes separator-id separator-props)
        [:div (horizontal-separator.attributes/separator-inner-attributes separator-id separator-props)
              (when :always [:hr (horizontal-separator.attributes/separator-line-attributes separator-id separator-props)])
              (when label   [:<> [pretty-accessories/label                                  separator-id label]])
              (when :always [:hr (horizontal-separator.attributes/separator-line-attributes separator-id separator-props)])]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) separator-id
  ; @param (map) separator-props
  [separator-id separator-props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    separator-id separator-props))
                         :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount separator-id separator-props))
                         :reagent-render         (fn [_ separator-props] [horizontal-separator separator-id separator-props])}))

(defn view
  ; @description
  ; Horizontal line element with optional label.
  ;
  ; @links Implemented accessories
  ; [Label](pretty-ui/cljs/pretty-accessories/api.html#label)
  ;
  ; @links Implemented properties
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Flex properties](pretty-core/cljs/pretty-properties/api.html#flex-properties)
  ; [Inner position properties](pretty-core/cljs/pretty-properties/api.html#inner-position-properties)
  ; [Inner size properties](pretty-core/cljs/pretty-properties/api.html#inner-size-properties)
  ; [Inner space properties](pretty-core/cljs/pretty-properties/api.html#inner-space-properties)
  ; [Lifecycle properties](pretty-core/cljs/pretty-properties/api.html#lifecycle-properties)
  ; [Line properties](pretty-core/cljs/pretty-properties/api.html#line-properties)
  ; [Outer position properties](pretty-core/cljs/pretty-properties/api.html#outer-position-properties)
  ; [Outer size properties](pretty-core/cljs/pretty-properties/api.html#outer-size-properties)
  ; [Outer space properties](pretty-core/cljs/pretty-properties/api.html#outer-space-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [State properties](pretty-core/cljs/pretty-properties/api.html#state-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ;
  ; @param (keyword)(opt) separator-id
  ; @param (map) separator-props
  ; Check out the implemented accessories.
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-elements/horizontal-separator.png)
  ; [horizontal-separator {:gap        :xs
  ;                        :label      {:content "My horizontal separator" :text-color :muted}
  ;                        :line-color :muted}]
  ([separator-props]
   [view (random/generate-keyword) separator-props])

  ([separator-id separator-props]
   ; @note (tutorials#parameterizing)
   (fn [_ separator-props]
       (let [separator-props (pretty-presets.engine/apply-preset                        separator-id separator-props)
             separator-props (horizontal-separator.prototypes/separator-props-prototype separator-id separator-props)]
            [view-lifecycles separator-id separator-props]))))
