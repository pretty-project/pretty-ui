
(ns pretty-elements.vertical-spacer.views
    (:require [fruits.random.api                          :as random]
              [pretty-elements.engine.api                 :as pretty-elements.engine]
              [pretty-elements.vertical-spacer.attributes :as vertical-spacer.attributes]
              [pretty-elements.vertical-spacer.prototypes :as vertical-spacer.prototypes]
              [pretty-presets.engine.api                  :as pretty-presets.engine]
              [reagent.core :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- vertical-spacer
  ; @ignore
  ;
  ; @param (keyword) spacer-id
  ; @param (map) spacer-props
  [spacer-id spacer-props]
  [:div (vertical-spacer.attributes/spacer-attributes spacer-id spacer-props)
        [:div (vertical-spacer.attributes/spacer-inner-attributes spacer-id spacer-props)]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) spacer-id
  ; @param (map) spacer-props
  [spacer-id spacer-props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    spacer-id spacer-props))
                         :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount spacer-id spacer-props))
                         :reagent-render         (fn [_ spacer-props] [vertical-spacer spacer-id spacer-props])}))

(defn view
  ; @description
  ; Empty vertical spacer element.
  ;
  ; @links Implemented properties
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
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
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ;
  ; @param (keyword)(opt) spacer-id
  ; @param (map) spacer-props
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-elements/vertical-spacer.png)
  ; [vertical-spacer {:outer-width :s}]
  ([spacer-props]
   [view (random/generate-keyword) spacer-props])

  ([spacer-id spacer-props]
   ; @note (tutorials#parameterizing)
   (fn [_ spacer-props]
       (let [spacer-props (pretty-presets.engine/apply-preset                spacer-id spacer-props)
             spacer-props (vertical-spacer.prototypes/spacer-props-prototype spacer-id spacer-props)]
            [view-lifecycles spacer-id spacer-props]))))
