
(ns pretty-elements.label.views
    (:require [fruits.random.api                 :as random]
              [pretty-elements.label.attributes :as label.attributes]
              [pretty-elements.label.prototypes :as label.prototypes]
              [pretty-elements.engine.api        :as pretty-elements.engine]
              [pretty-presets.engine.api         :as pretty-presets.engine]
              [reagent.core :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- label
  ; @ignore
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ; {:label (metamorphic-content)(opt)
  ;  :icon (keyword)(opt)
  ;  :icon-position (keyword)(opt)
  ;  ...}
  [label-id {:keys [content icon icon-position] :as label-props}]
  [:div (label.attributes/label-attributes label-id label-props)
        [:div (label.attributes/label-inner-attributes label-id label-props)
              (case icon-position :right [:<> (if content [:div (label.attributes/label-content-attributes label-id label-props) content])
                                              (if icon    [:i   (label.attributes/label-icon-attributes    label-id label-props) icon])]
                                         [:<> (if icon    [:i   (label.attributes/label-icon-attributes    label-id label-props) icon])
                                              (if content [:div (label.attributes/label-content-attributes label-id label-props) content])])]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  [label-id label-props]
  ; @note (tutorials#parameterizing)
  ; @note (pretty-elements.adornment.views#8097)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    label-id label-props))
                         :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount label-id label-props))
                         :reagent-render         (fn [_ label-props] [label label-id label-props])}))

(defn view
  ; @description
  ; Customizable label element.
  ;
  ; @links Implemented properties
  ; [Background color properties](pretty-core/cljs/pretty-properties/api.html#background-color-properties)
  ; [Border properties](pretty-core/cljs/pretty-properties/api.html#border-properties)
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Content properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Flex properties](pretty-core/cljs/pretty-properties/api.html#flex-properties)
  ; [Font properties](pretty-core/cljs/pretty-properties/api.html#font-properties)
  ; [Icon properties](pretty-core/cljs/pretty-properties/api.html#icon-properties)
  ; [Inner position properties](pretty-core/cljs/pretty-properties/api.html#inner-position-properties)
  ; [Inner size properties](pretty-core/cljs/pretty-properties/api.html#inner-size-properties)
  ; [Lifecycle properties](pretty-core/cljs/pretty-properties/api.html#lifecycle-properties)
  ; [Outer position properties](pretty-core/cljs/pretty-properties/api.html#outer-position-properties)
  ; [Outer size properties](pretty-core/cljs/pretty-properties/api.html#outer-size-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [Space properties](pretty-core/cljs/pretty-properties/api.html#space-properties)
  ; [State properties](pretty-core/cljs/pretty-properties/api.html#state-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Text properties](pretty-core/cljs/pretty-properties/api.html#text-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ;
  ; @param (keyword)(opt) label-id
  ; @param (map) label-props
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-elements/label.png)
  ; [label {:border-color     :secondary
  ;         :content          "My label"
  ;         :border-position  :bottom
  ;         :border-width     :xs
  ;         :gap              :auto
  ;         :horizontal-align :left
  ;         :icon             :star
  ;         :icon-position    :right
  ;         :outer-width      :xxl}]
  ([label-props]
   [view (random/generate-keyword) label-props])

  ([label-id label-props]
   ; @note (tutorials#parameterizing)
   (fn [_ label-props]
       (let [label-props (pretty-presets.engine/apply-preset     label-id label-props)
             label-props (label.prototypes/label-props-prototype label-id label-props)]
            [view-lifecycles label-id label-props]))))
