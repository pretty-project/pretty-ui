
(ns pretty-elements.adornment-group.views
    (:require [fruits.hiccup.api                          :as hiccup]
              [fruits.random.api                          :as random]
              [pretty-elements.adornment-group.attributes :as adornment-group.attributes]
              [pretty-elements.adornment-group.prototypes :as adornment-group.prototypes]
              [pretty-elements.adornment.views            :as adornment.views]
              [pretty-elements.engine.api                 :as pretty-elements.engine]
              [pretty-presets.engine.api                  :as pretty-presets.engine]
              [reagent.core                               :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------
 
(defn- adornment-group
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:adornments (maps in vector)(opt)
  ;  ...}
  [id {:keys [adornments] :as props}]
  [:div (adornment-group.attributes/outer-attributes id props)
        [:div (adornment-group.attributes/inner-attributes id props)
              (letfn [(f0 [adornment] [adornment.views/view adornment])]
                     (hiccup/put-with [:<>] adornments f0))]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  [id props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    id props))
                         :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount id props))
                         :reagent-render         (fn [_ props] [adornment-group id props])}))

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
  ; [Mouse event properties](pretty-core/cljs/pretty-properties/api.html#mouse-event-properties)
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
  ([props]
   [view (random/generate-keyword) props])

  ([id props]
   ; @note (tutorials#parameterizing)
   (fn [_ props]
       (let [props (pretty-presets.engine/apply-preset         id props)
             props (adornment-group.prototypes/props-prototype id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
