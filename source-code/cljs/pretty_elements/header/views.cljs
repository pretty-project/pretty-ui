
(ns pretty-elements.header.views
    (:require [fruits.random.api                 :as random]
              [pretty-elements.header.attributes :as header.attributes]
              [pretty-elements.header.prototypes :as header.prototypes]
              [pretty-elements.engine.api        :as pretty-elements.engine]
              [pretty-presets.engine.api         :as pretty-presets.engine]
              [pretty-accessories.api         :as pretty-accessories]
              [reagent.core :as reagent]
              [pretty-elements.adornment-group.views :as adornment-group.views]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header
  ; @ignore
  ;
  ; @param (keyword) header-id
  ; @param (map) header-props
  ; {:end-adornment-group (map)(opt)
  ;  :label (map)(opt)
  ;  :start-adornment-group (map)(opt)
  ;  ...}
  [header-id {:keys [end-adornment-group label start-adornment-group] :as header-props}]
  [:div (header.attributes/header-attributes header-id header-props)
        [:div (header.attributes/header-inner-attributes header-id header-props)
              (if start-adornment-group [adornment-group.views/view header-id start-adornment-group])
              (if label                 [pretty-accessories/label   header-id label])
              (if end-adornment-group   [adornment-group.views/view header-id end-adornment-group])]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) header-id
  ; @param (map) header-props
  [header-id header-props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    header-id header-props))
                         :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount header-id header-props))
                         :reagent-render         (fn [_ header-props] [header header-id header-props])}))

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
  ; @param (keyword)(opt) header-id
  ; @param (map) header-props
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
  ([header-props]
   [view (random/generate-keyword) header-props])

  ([header-id header-props]
   ; @note (tutorials#parameterizing)
   (fn [_ header-props]
       (let [header-props (pretty-presets.engine/apply-preset       header-id header-props)
             header-props (header.prototypes/header-props-prototype header-id header-props)]
            [view-lifecycles header-id header-props]))))
