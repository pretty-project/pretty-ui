
(ns pretty-elements.text.views
    (:require [fruits.hiccup.api               :as hiccup]
              [fruits.random.api               :as random]
              [pretty-elements.engine.api      :as pretty-elements.engine]
              [pretty-elements.text.attributes :as text.attributes]
              [pretty-elements.text.prototypes :as text.prototypes]
              [pretty-presets.engine.api       :as pretty-presets.engine]
              [reagent.core :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- text
  ; @ignore
  ;
  ; @param (keyword) text-id
  ; @param (map) text-props
  ; {:content (metamorphic-content)(opt)
  ;  ...}
  [text-id {:keys [content] :as text-props}]
  [:div (text.attributes/text-attributes text-id text-props)
        [:div (text.attributes/text-inner-attributes text-id text-props)
              [:div (text.attributes/text-content-attributes text-id text-props)
                    (hiccup/parse-newlines [:<> content])]]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) text-id
  ; @param (map) text-props
  [text-id text-props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    text-id text-props))
                         :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount text-id text-props))
                         :reagent-render         (fn [_ text-props] [text text-id text-props])}))

(defn view
  ; @description
  ; Customizable text element.
  ;
  ; @links Implemented properties
  ; [Background color properties](pretty-core/cljs/pretty-properties/api.html#background-color-properties)
  ; [Border properties](pretty-core/cljs/pretty-properties/api.html#border-properties)
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Content properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Flex properties](pretty-core/cljs/pretty-properties/api.html#flex-properties)
  ; [Font properties](pretty-core/cljs/pretty-properties/api.html#font-properties)
  ; [Inner position properties](pretty-core/cljs/pretty-properties/api.html#inner-position-properties)
  ; [Inner size properties](pretty-core/cljs/pretty-properties/api.html#inner-size-properties)
  ; [Lifecycle properties](pretty-core/cljs/pretty-properties/api.html#lifecycle-properties)
  ; [Multiline properties](pretty-core/cljs/pretty-properties/api.html#multiline-properties)
  ; [Outer position properties](pretty-core/cljs/pretty-properties/api.html#outer-position-properties)
  ; [Outer size properties](pretty-core/cljs/pretty-properties/api.html#outer-size-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [Space properties](pretty-core/cljs/pretty-properties/api.html#space-properties)
  ; [State properties](pretty-core/cljs/pretty-properties/api.html#state-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Text properties](pretty-core/cljs/pretty-properties/api.html#text-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ;
  ; @param (keyword)(opt) text-id
  ; @param (map) text-props
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-elements/text.png)
  ; [text {:border-radius {:all :m}
  ;        :content       "My text line #1\nMy text line #2\nMy text line #3"
  ;        :fill-color    :highlight
  ;        :outer-height  :5xl
  ;        :outer-width   :5xl}]
  ([text-props]
   [view (random/generate-keyword) text-props])

  ([text-id text-props]
   ; @note (tutorials#parameterizing)
   (fn [_ text-props]
       (let [text-props (pretty-presets.engine/apply-preset   text-id text-props)
             text-props (text.prototypes/text-props-prototype text-id text-props)]
            [view-lifecycles text-id text-props]))))
