
(ns pretty-inputs.option-group.views
    (:require [fruits.hiccup.api                          :as hiccup]
              [fruits.random.api                          :as random]
              [fruits.vector.api                          :as vector]
              [pretty-inputs.option-group.attributes :as option-group.attributes]
              [pretty-inputs.option-group.prototypes :as option-group.prototypes]
              [pretty-inputs.option.views :as option.views]
              [pretty-inputs.engine.api :as pretty-inputs.engine]
              [pretty-guides.api :as pretty-guides]
              [pretty-presets.engine.api :as pretty-presets.engine]
              [reagent.core :as reagent]
              [pretty-subitems.api :as pretty-subitems]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- option-group
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:options (maps in vector)(opt)
  ;  :placeholder-text (map)(opt)
  ;  ...}
  [id {:keys [options placeholder-text] :as props}]
  [:div (option-group.attributes/outer-attributes id props)
        [pretty-inputs.engine/input-synchronizer  id props]
        [:div (option-group.attributes/inner-attributes id props)
              (letfn [(f0 [option] [option.views/view option])]
                     (if (-> options vector/not-empty?) (hiccup/put-with [:<>] options f0)
                         (if placeholder-text [pretty-guides/placeholder-text (pretty-subitems/subitem-id id :placeholder-text) placeholder-text])))]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  [id props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-inputs.engine/input-did-mount    id props))
                         :component-will-unmount (fn [_ _] (pretty-inputs.engine/input-will-unmount id props))
                         :reagent-render         (fn [_ props] [option-group id props])}))

(defn view
  ; @description
  ; Group of customizable option elements.
  ;
  ; @links Implemented guides
  ; [Placeholder-text](pretty-ui/cljs/pretty-guides/api.html#placeholder-text)
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
  ; [Input option properties](pretty-core/cljs/pretty-properties/api.html#input-option-properties)
  ; [Input validation properties](pretty-core/cljs/pretty-properties/api.html#input-validation-properties)
  ; [Input value properties](pretty-core/cljs/pretty-properties/api.html#input-value-properties)
  ; [Lifecycle properties](pretty-core/cljs/pretty-properties/api.html#lifecycle-properties)
  ; [Outer position properties](pretty-core/cljs/pretty-properties/api.html#outer-position-properties)
  ; [Outer size properties](pretty-core/cljs/pretty-properties/api.html#outer-size-properties)
  ; [Outer space properties](pretty-core/cljs/pretty-properties/api.html#outer-space-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [State properties](pretty-core/cljs/pretty-properties/api.html#State-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ;
  ; @param (keyword)(opt) id
  ; @param (map) props
  ; Check out the implemented inputs.
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-inputs/option-group.png)
  ; [option-group {:option-default  {:icon {:border-color :muted :border-radius {:all :s} :border-width :xs}}
  ;                :option-selected {:icon {:icon-name :done}}
  ;                :options [{:label {:content "My option #1"}}
  ;                          {:label {:content "My option #2"}}
  ;                          {:label {:content "My option #2"}}]
  ;                :gap :xs}]
  ([props]
   [view (random/generate-keyword) props])

  ([id props]
   ; @note (tutorials#parameterizing)
   (fn [_ props]
       (let [props (pretty-presets.engine/apply-preset                 id props)
             props (option-group.prototypes/props-prototype            id props)
             props (pretty-inputs.engine/import-input-option-events    id props)
             props (pretty-inputs.engine/import-input-option-selection id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
