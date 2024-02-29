
(ns pretty-inputs.option-group.views
    (:require [fruits.hiccup.api                          :as hiccup]
              [fruits.random.api                          :as random]
              [pretty-inputs.option-group.attributes :as option-group.attributes]
              [pretty-inputs.option-group.prototypes :as option-group.prototypes]
              [pretty-inputs.option.views :as option.views]
              [pretty-inputs.engine.api :as pretty-inputs.engine]
              [pretty-presets.engine.api :as pretty-presets.engine]
              [reagent.core :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- option-group
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; {:options (maps in vector)(opt)
  ;  ...}
  [group-id {:keys [options] :as group-props}]
  [:div (option-group.attributes/group-attributes group-id group-props)
        [:div (option-group.attributes/group-inner-attributes group-id group-props)
              (letfn [(f0 [option-props] [option.views/view option-props])]
                     (hiccup/put-with [:<>] options f0))]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  [group-id group-props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-inputs.engine/input-did-mount    group-id group-props))
                         :component-will-unmount (fn [_ _] (pretty-inputs.engine/input-will-unmount group-id group-props))
                         :reagent-render         (fn [_ group-props] [option-group group-id group-props])}))

(defn view
  ; @description
  ; Group of customizable option elements.
  ;
  ; @links Implemented inputs
  ; [Option](pretty-ui/cljs/pretty-inputs/api.html#option)
  ;
  ; @links Implemented properties
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Flex properties](pretty-core/cljs/pretty-properties/api.html#flex-properties)
  ; [Inner position properties](pretty-core/cljs/pretty-properties/api.html#inner-position-properties)
  ; [Inner size properties](pretty-core/cljs/pretty-properties/api.html#inner-size-properties)
  ; [Inner space properties](pretty-core/cljs/pretty-properties/api.html#inner-space-properties)
  ; [Lifecycle properties](pretty-core/cljs/pretty-properties/api.html#lifecycle-properties)
  ; [Outer position properties](pretty-core/cljs/pretty-properties/api.html#outer-position-properties)
  ; [Outer size properties](pretty-core/cljs/pretty-properties/api.html#outer-size-properties)
  ; [Outer space properties](pretty-core/cljs/pretty-properties/api.html#outer-space-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [State properties](pretty-core/cljs/pretty-properties/api.html#State-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ;
  ; @param (keyword)(opt) group-id
  ; @param (map) group-props
  ; Check out the implemented inputs.
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-inputs/option-group.png)
  ; [option-group {:option-default {:icon {:border-color :muted :border-radius {:all :s} :border-width :xs}}
  ;                :options [{:label {:content "My option #1"} :icon {:icon-name :done}}
  ;                          {:label {:content "My option #2"}}
  ;                          {:label {:content "My option #2"}}]
  ;                :gap :xs}]
  ([group-props]
   [view (random/generate-keyword) group-props])

  ([group-id group-props]
   ; @note (tutorials#parameterizing)
   (fn [_ group-props]
       (let [group-props (pretty-presets.engine/apply-preset            group-id group-props)
             group-props (option-group.prototypes/group-props-prototype group-id group-props)]
            [view-lifecycles group-id group-props]))))
