
(ns pretty-guides.helper-text.views
    (:require [fruits.random.api :as random]
              [pretty-guides.helper-text.attributes :as helper-text.attributes]
              [pretty-guides.helper-text.prototypes :as helper-text.prototypes]
              [pretty-elements.engine.api :as pretty-elements.engine]
              [pretty-presets.engine.api :as pretty-presets.engine]
              [reagent.core :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- helper-text
  ; @ignore
  ;
  ; @param (keyword) text-id
  ; @param (map) text-props
  ; {:content (multitype-content)(opt)
  ;  ...}
  [text-id {:keys [content] :as text-props}]
  [:div (helper-text.attributes/text-attributes text-id text-props)
        [:div (helper-text.attributes/text-inner-attributes text-id text-props)
              [:div (helper-text.attributes/text-content-attributes text-id text-props) content]]])

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
                         :reagent-render         (fn [_ text-props] [helper-text text-id text-props])}))

(defn view
  ; @description
  ; Helper text guide for inputs.
  ;
  ; @links Implemented properties
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Content properties](pretty-core/cljs/pretty-properties/api.html#content-properties)
  ; [Font properties](pretty-core/cljs/pretty-properties/api.html#font-properties)
  ; [Inner position properties](pretty-core/cljs/pretty-properties/api.html#inner-position-properties)
  ; [Inner size properties](pretty-core/cljs/pretty-properties/api.html#inner-size-properties)
  ; [Inner space properties](pretty-core/cljs/pretty-properties/api.html#inner-space-properties)
  ; [Lifecycle properties](pretty-core/cljs/pretty-properties/api.html#lifecycle-properties)
  ; [Outer position properties](pretty-core/cljs/pretty-properties/api.html#outer-position-properties)
  ; [Outer size properties](pretty-core/cljs/pretty-properties/api.html#outer-size-properties)
  ; [Outer space properties](pretty-core/cljs/pretty-properties/api.html#outer-space-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [State properties](pretty-core/cljs/pretty-properties/api.html#state-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Text properties](pretty-core/cljs/pretty-properties/api.html#text-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ;
  ; @param (keyword)(opt) text-id
  ; @param (map) text-props
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-guides/helper-text.png)
  ; [helper-text {:content "My helper text"}]
  ([text-props]
   [view (random/generate-keyword) text-props])

  ([text-id text-props]
   ; @note (tutorials#parameterizing)
   (fn [_ text-props]
       (let [text-props (pretty-presets.engine/apply-preset          text-id text-props)
             text-props (helper-text.prototypes/text-props-prototype text-id text-props)]
            [view-lifecycles text-id text-props]))))
