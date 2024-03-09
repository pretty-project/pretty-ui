
(ns pretty-elements.adornment-group.views
    (:require [fruits.hiccup.api                          :as hiccup]
              [fruits.random.api                          :as random]
              [pretty-elements.adornment-group.attributes :as adornment-group.attributes]
              [pretty-elements.adornment-group.prototypes :as adornment-group.prototypes]
              [pretty-elements.adornment.views            :as adornment.views]
              [pretty-elements.engine.api                 :as pretty-elements.engine]
              [pretty-elements.methods.api :as pretty-elements.methods]
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
  ; @links Implemented models
  ; [Container model](pretty-core/cljs/pretty-models/api.html#container-model)
  ;
  ; @param (keyword)(opt) id
  ; @param (map) props
  ; Check out the implemented elements.
  ; Check out the implemented models.
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
       (let [props (pretty-elements.methods/apply-element-preset         id props)
             props (pretty-elements.methods/import-element-dynamic-props id props)
             props (pretty-elements.methods/import-element-state-events  id props)
             props (pretty-elements.methods/import-element-state         id props)
             props (adornment-group.prototypes/props-prototype           id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
