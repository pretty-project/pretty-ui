
(ns pretty-elements.header.views
    (:require [fruits.random.api                     :as random]
              [pretty-accessories.api                :as pretty-accessories]
              [pretty-elements.adornment-group.views :as adornment-group.views]
              [pretty-elements.engine.api            :as pretty-elements.engine]
              [pretty-elements.header.attributes     :as header.attributes]
              [pretty-elements.header.prototypes     :as header.prototypes]
              [pretty-elements.methods.api           :as pretty-elements.methods]
              [pretty-subitems.api                   :as pretty-subitems]
              [reagent.core                          :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:end-adornment-group (map)(opt)
  ;  :label (map)(opt)
  ;  :start-adornment-group (map)(opt)
  ;  ...}
  [id {:keys [end-adornment-group label start-adornment-group] :as props}]
  [:div (header.attributes/outer-attributes id props)
        [:div (header.attributes/inner-attributes id props)
              (if start-adornment-group [adornment-group.views/view (pretty-subitems/subitem-id id :start-adornment-group) start-adornment-group])
              (if label                 [pretty-accessories/label   (pretty-subitems/subitem-id id :label)                 label])
              (if end-adornment-group   [adornment-group.views/view (pretty-subitems/subitem-id id :end-adornment-group)   end-adornment-group])]])

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
                         :reagent-render         (fn [_ props] [header id props])}))

(defn view
  ; @description
  ; Header element with optional label and adornments.
  ;
  ; @links Implemented accessories
  ; [Label](pretty-ui/cljs/pretty-accessories/api.html#label)
  ;
  ; @links Implemented elements
  ; [Adornment-group](pretty-ui/cljs/pretty-elements/api.html#adornment-group)
  ;
  ; @links Implemented models
  ; [Container model](pretty-core/cljs/pretty-models/api.html#container-model)
  ; [Content model](pretty-core/cljs/pretty-models/api.html#content-model)
  ;
  ; @param (keyword)(opt) id
  ; @param (map) props
  ; Check out the implemented accessories.
  ; Check out the implemented elements.
  ; Check out the implemented models.
  ;
  ; @usage (pretty-elements/header.png)
  ; [header {:border-color          :primary
  ;          :label                 {:content "My header #1"}
  ;          :border-position       :bottom
  ;          :border-width          :xs
  ;          :gap                   :auto
  ;          :horizontal-align      :left
  ;          :outer-width           :xxl
  ;          :start-adornment-group {:adornment-default {:icon {:icon-color :default}}}
  ;                                  :adornments        [{:icon {:icon-name :star}}]}]
  ;
  ; [header {:border-color        :secondary
  ;          :label               {:content "My header #2"}
  ;          :border-position     :bottom
  ;          :border-width        :xs
  ;          :gap                 :auto
  ;          :horizontal-align    :left
  ;          :outer-width         :xxl
  ;          :end-adornment-group {:adornment-default {:icon {:icon-color :default}}}
  ;                                :adornments        [{:icon {:icon-name :star}}]}]
  ([props]
   [view (random/generate-keyword) props])

  ([id props]
   ; @note (tutorials#parameterizing)
   (fn [_ props]
       (let [props (pretty-elements.methods/apply-element-shorthand-map  id props {:label :content})
             props (pretty-elements.methods/apply-element-preset         id props)
             props (pretty-elements.methods/import-element-dynamic-props id props)
             props (pretty-elements.methods/import-element-state-events  id props)
             props (pretty-elements.methods/import-element-state         id props)
             props (header.prototypes/props-prototype                    id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
