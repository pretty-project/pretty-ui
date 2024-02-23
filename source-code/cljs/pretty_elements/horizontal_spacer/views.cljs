
(ns pretty-elements.horizontal-spacer.views
    (:require [fruits.random.api                            :as random]
              [pretty-elements.engine.api                   :as pretty-elements.engine]
              [pretty-elements.horizontal-spacer.attributes :as horizontal-spacer.attributes]
              [pretty-elements.horizontal-spacer.prototypes :as horizontal-spacer.prototypes]
              [pretty-presets.engine.api                    :as pretty-presets.engine]
              [reagent.core :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- horizontal-spacer
  ; @ignore
  ;
  ; @param (keyword) spacer-id
  ; @param (map) spacer-props
  [spacer-id spacer-props]
  [:div (horizontal-spacer.attributes/spacer-attributes spacer-id spacer-props)
        [:div (horizontal-spacer.attributes/spacer-body-attributes spacer-id spacer-props)]])

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
                         :reagent-render         (fn [_ spacer-props] [horizontal-spacer spacer-id spacer-props])}))

(defn view
  ; @description
  ; Empty horizontal spacer element.
  ;
  ; @links Implemented properties
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Inner size properties](pretty-core/cljs/pretty-properties/api.html#inner-size-properties)
  ; [Lifecycle properties](pretty-core/cljs/pretty-properties/api.html#lifecycle-properties)
  ; [Outer size properties](pretty-core/cljs/pretty-properties/api.html#outer-size-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [Space properties](pretty-core/cljs/pretty-properties/api.html#space-properties)
  ; [State properties](pretty-core/cljs/pretty-properties/api.html#state-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ;
  ; @param (keyword)(opt) spacer-id
  ; @param (map) spacer-props
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-elements/horizontal-spacer.png)
  ; [horizontal-spacer {:outer-height :s}]
  ([spacer-props]
   [view (random/generate-keyword) spacer-props])

  ([spacer-id spacer-props]
   ; @note (tutorials#parameterizing)
   (fn [_ spacer-props]
       (let [spacer-props (pretty-presets.engine/apply-preset                  spacer-id spacer-props)
             spacer-props (horizontal-spacer.prototypes/spacer-props-prototype spacer-id spacer-props)]
            [view-lifecycles spacer-id spacer-props]))))
