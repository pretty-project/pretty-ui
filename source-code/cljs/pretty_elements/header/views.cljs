
(ns pretty-elements.header.views
    (:require [fruits.random.api                 :as random]
              [pretty-elements.header.attributes :as header.attributes]
              [pretty-elements.header.prototypes :as header.prototypes]
              [pretty-elements.engine.api        :as pretty-elements.engine]
              [pretty-presets.engine.api         :as pretty-presets.engine]
              [pretty-accessories.api         :as pretty-accessories]
              [reagent.core :as reagent]
              [pretty-elements.adornment-group.views :as adornment-group.views]
              [pretty-subitems.api :as pretty-subitems]))

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
  ; @links Implemented properties
  ; [Background color properties](pretty-core/cljs/pretty-properties/api.html#background-color-properties)
  ; [Border properties](pretty-core/cljs/pretty-properties/api.html#border-properties)
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
  ; [State properties](pretty-core/cljs/pretty-properties/api.html#state-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ;
  ; @param (keyword)(opt) id
  ; @param (map) props
  ; Check out the implemented accessories.
  ; Check out the implemented elements.
  ; Check out the implemented properties.
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
       (let [props (pretty-presets.engine/apply-preset id props)
             props (header.prototypes/props-prototype  id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
