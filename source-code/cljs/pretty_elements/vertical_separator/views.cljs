
(ns pretty-elements.vertical-separator.views
    (:require [fruits.random.api                             :as random]
              [metamorphic-content.api                       :as metamorphic-content]
              [pretty-elements.engine.api                    :as pretty-elements.engine]
              [pretty-elements.vertical-separator.attributes :as vertical-separator.attributes]
              [pretty-elements.vertical-separator.prototypes :as vertical-separator.prototypes]
              [pretty-presets.engine.api                     :as pretty-presets.engine]
              [reagent.core :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- vertical-separator
  ; @ignore
  ;
  ; @param (keyword) separator-id
  ; @param (map) separator-props
  ; {:label (metamorphic-content)(opt)
  ;  ...}
  [separator-id {:keys [label] :as separator-props}]
  [:div (vertical-separator.attributes/separator-attributes separator-id separator-props)
        [:div (vertical-separator.attributes/separator-body-attributes separator-id separator-props)
              (if label [:<> [:hr   (vertical-separator.attributes/separator-line-attributes  separator-id separator-props)]
                             [:span (vertical-separator.attributes/separator-label-attributes separator-id separator-props) label]
                             [:hr   (vertical-separator.attributes/separator-line-attributes  separator-id separator-props)]]
                        [:<> [:hr   (vertical-separator.attributes/separator-line-attributes  separator-id separator-props)]])]])

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
                         :reagent-render         (fn [_ separator-props] [vertical-separator separator-id separator-props])}))

(defn view
  ; @description
  ; Vertical line element with optional label.
  ;
  ; @links Implemented properties
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Flex properties](pretty-core/cljs/pretty-properties/api.html#flex-properties)
  ; [Font properties](pretty-core/cljs/pretty-properties/api.html#font-properties)
  ; [Inner size properties](pretty-core/cljs/pretty-properties/api.html#inner-size-properties)
  ; [Label properties](pretty-core/cljs/pretty-properties/api.html#label-properties)
  ; [Line properties](pretty-core/cljs/pretty-properties/api.html#line-properties)
  ; [Lifecycle properties](pretty-core/cljs/pretty-properties/api.html#lifecycle-properties)
  ; [Outer size properties](pretty-core/cljs/pretty-properties/api.html#outer-size-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
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
  ; @usage (pretty-elements/vertical-separator.png)
  ; [vertical-separator {:label        "My vertical separator"
  ;                      :line-color   :muted
  ;                      :outer-height :5xl
  ;                      :text-color   :muted}]
  ([separator-props]
   [view (random/generate-keyword) separator-props])

  ([separator-id separator-props]
   ; @note (tutorials#parameterizing)
   (fn [_ separator-props]
       (let [separator-props (pretty-presets.engine/apply-preset                        separator-id separator-props)
             separator-props (vertical-separator.prototypes/separator-props-prototype separator-id separator-props)]
            [view-lifecycles separator-id separator-props]))))
