
(ns pretty-elements.horizontal-separator.views
    (:require [fruits.random.api                               :as random]
              [metamorphic-content.api                         :as metamorphic-content]
              [pretty-elements.engine.api                      :as pretty-elements.engine]
              [pretty-elements.horizontal-separator.attributes :as horizontal-separator.attributes]
              [pretty-elements.horizontal-separator.prototypes :as horizontal-separator.prototypes]
              [pretty-presets.engine.api                       :as pretty-presets.engine]
              [reagent.core :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- horizontal-separator
  ; @ignore
  ;
  ; @param (keyword) separator-id
  ; @param (map) separator-props
  ; {:label (metamorphic-content)(opt)
  ;  ...}
  [separator-id {:keys [label] :as separator-props}]
  [:div (horizontal-separator.attributes/separator-attributes separator-id separator-props)
        [:div (horizontal-separator.attributes/separator-body-attributes separator-id separator-props)
              (if label [:<> [:hr   (horizontal-separator.attributes/separator-line-attributes  separator-id separator-props)]
                             [:span (horizontal-separator.attributes/separator-label-attributes separator-id separator-props) label]
                             [:hr   (horizontal-separator.attributes/separator-line-attributes  separator-id separator-props)]]
                        [:<> [:hr   (horizontal-separator.attributes/separator-line-attributes  separator-id separator-props)]])]])

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
  ; @links Implemented properties
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Flex properties](pretty-core/cljs/pretty-properties/api.html#flex-properties)
  ; [Font properties](pretty-core/cljs/pretty-properties/api.html#font-properties)
  ; [Label properties](pretty-core/cljs/pretty-properties/api.html#label-properties)
  ; [Line properties](pretty-core/cljs/pretty-properties/api.html#line-properties)
  ; [Lifecycle properties](pretty-core/cljs/pretty-properties/api.html#lifecycle-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [Size properties](pretty-core/cljs/pretty-properties/api.html#size-properties)
  ; [Space properties](pretty-core/cljs/pretty-properties/api.html#space-properties)
  ; [State properties](pretty-core/cljs/pretty-properties/api.html#state-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Text properties](pretty-core/cljs/pretty-properties/api.html#text-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ;
  ; @param (keyword)(opt) separator-id
  ; @param (map) separator-props
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-elements/horizontal-separator.png)
  ; [horizontal-separator {:label      "My horizontal separator"
  ;                        :line-color :muted
  ;                        :text-color :muted}]
  ([separator-props]
   [view (random/generate-keyword) separator-props])

  ([separator-id separator-props]
   ; @note (tutorials#parameterizing)
   (fn [_ separator-props]
       (let [separator-props (pretty-presets.engine/apply-preset                        separator-id separator-props)
             separator-props (horizontal-separator.prototypes/separator-props-prototype separator-id separator-props)]
            [view-lifecycles separator-id separator-props]))))
