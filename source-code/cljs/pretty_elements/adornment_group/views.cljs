
(ns pretty-elements.adornment-group.views
    (:require [fruits.hiccup.api                          :as hiccup]
              [fruits.random.api                          :as random]
              [pretty-elements.adornment-group.attributes :as adornment-group.attributes]
              [pretty-elements.adornment-group.prototypes :as adornment-group.prototypes]
              [pretty-elements.adornment.views            :as adornment.views]
              [pretty-elements.engine.api                 :as pretty-elements.engine]
              [pretty-presets.engine.api                  :as pretty-presets.engine]
              [reagent.core :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- adornment-group
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; {:adornments (maps in vector)(opt)
  ;  ...}
  [group-id {:keys [adornments] :as group-props}]
  [:div (adornment-group.attributes/group-attributes group-id group-props)
        [:div (adornment-group.attributes/group-inner-attributes group-id group-props)
              (letfn [(f0 [adornment-props] [adornment.views/view adornment-props])]
                     (hiccup/put-with [:<>] adornments f0))]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  [group-id group-props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    group-id group-props))
                         :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount group-id group-props))
                         :reagent-render         (fn [_ group-props] [adornment-group group-id group-props])}))

(defn view
  ; @description
  ; Group of downsized button elements.
  ;
  ; @links Implemented elements
  ; [Adornment](pretty-ui/cljs/pretty-elements/api.html#adornment)
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
  ; Check out the implemented elements.
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-elements/adornment-group.png)
  ; [adornment-group {:adornment-default {:border-radius {:all :s} :fill-color :highlight}
  ;                   :adornments [{:icon {:icon-name :home}}
  ;                                {:icon {:icon-name :settings}}
  ;                                {:icon {:icon-name :star}}
  ;                                {:icon {:icon-name :delete}}
  ;                                {:icon {:icon-name :add}}
  ;                                {:icon {:icon-name :favorite :icon-color :warning :icon-family :material-symbols-filled}}]
  ;                   :gap :xs}]
  ([group-props]
   [view (random/generate-keyword) group-props])

  ([group-id group-props]
   ; @note (tutorials#parameterizing)
   (fn [_ group-props]
       (let [group-props (pretty-presets.engine/apply-preset               group-id group-props)
             group-props (adornment-group.prototypes/group-props-prototype group-id group-props)]
            [view-lifecycles group-id group-props]))))
