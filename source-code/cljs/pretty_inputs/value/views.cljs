
(ns pretty-inputs.value.views
    (:require [fruits.random.api                 :as random]
              [pretty-inputs.value.attributes :as value.attributes]
              [pretty-inputs.value.prototypes :as value.prototypes]
              [pretty-inputs.engine.api        :as pretty-inputs.engine]
              [pretty-presets.engine.api         :as pretty-presets.engine]
              [reagent.core :as reagent]
              [pretty-elements.api :as pretty-elements]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- value
  ; @ignore
  ;
  ; @param (keyword) value-id
  ; @param (map) value-props
  ; {:end-adornments (maps in vector)(opt)
  ;  :start-adornments (maps in vector)(opt)
  ;  ...}
  [value-id {:keys [end-adornments start-adornments] :as value-props}]
  [:div (value.attributes/value-attributes       value-id value-props)
        [pretty-inputs.engine/input-synchronizer value-id value-props]
        [:div (value.attributes/value-inner-attributes value-id value-props)
              (let [value (pretty-inputs.engine/get-input-internal-value value-id value-props)]
                   [:<> (when start-adornments [pretty-elements/adornment-group value-id {:adornments start-adornments}])
                        (when :always          [:div (value.attributes/value-content-attributes value-id value-props) value])
                        (when end-adornments   [pretty-elements/adornment-group value-id {:adornments end-adornments}])])]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) value-id
  ; @param (map) value-props
  [value-id value-props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-inputs.engine/input-did-mount    value-id value-props))
                         :component-will-unmount (fn [_ _] (pretty-inputs.engine/input-will-unmount value-id value-props))
                         :reagent-render         (fn [_ value-props] [value value-id value-props])}))

(defn view
  ; @description
  ; Customizable value element with optional adornments.
  ;
  ; @links Implemented elements
  ; [Adornment-group](pretty-ui/cljs/pretty-elements/api.html#adornment-group)
  ;
  ; @links Implemented properties
  ; [Background color properties](pretty-core/cljs/pretty-properties/api.html#background-color-properties)
  ; [Border properties](pretty-core/cljs/pretty-properties/api.html#border-properties)
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Flex properties](pretty-core/cljs/pretty-properties/api.html#flex-properties)
  ; [Font properties](pretty-core/cljs/pretty-properties/api.html#font-properties)
  ; [Inner position properties](pretty-core/cljs/pretty-properties/api.html#inner-position-properties)
  ; [Inner size properties](pretty-core/cljs/pretty-properties/api.html#inner-size-properties)
  ; [Inner space properties](pretty-core/cljs/pretty-properties/api.html#inner-space-properties)
  ; [Input value properties](pretty-core/cljs/pretty-properties/api.html#input-value-properties)
  ; [Lifecycle properties](pretty-core/cljs/pretty-properties/api.html#lifecycle-properties)
  ; [Outer position properties](pretty-core/cljs/pretty-properties/api.html#outer-position-properties)
  ; [Outer size properties](pretty-core/cljs/pretty-properties/api.html#outer-size-properties)
  ; [Outer space properties](pretty-core/cljs/pretty-properties/api.html#outer-space-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [State properties](pretty-core/cljs/pretty-properties/api.html#state-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Text properties](pretty-core/cljs/pretty-properties/api.html#text-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ;
  ; @param (keyword)(opt) value-id
  ; @param (map) value-props
  ; Check out the implemented elements.
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-inputs/value.png)
  ; [value {:border-radius           {:all :l}
  ;         :fill-color              :highlight
  ;         :gap                     :auto
  ;         :get-value-f             #(-> "My value #1")
  ;         :indent                  {:right :s}
  ;         :outer-width             :xxl
  ;         :start-adornment-default {:fill-color :default :border-color :muted :border-radius {:all :l}}
  ;         :start-adornments        [{:icon :close :on-click-f (fn [_] ...)}]}]
  ;
  ; [value {:border-radius         {:all :l}
  ;         :fill-color            :highlight
  ;         :gap                   :auto
  ;         :get-value-f           #(-> "My value #2")
  ;         :indent                {:left :s}
  ;         :outer-width           :xxl
  ;         :end-adornment-default {:fill-color :default :border-color :muted :border-radius {:all :l}}
  ;         :end-adornments        [{:icon :close :on-click-f (fn [_] ...)}]}]
  ([value-props]
   [view (random/generate-keyword) value-props])

  ([value-id value-props]
   ; @note (tutorials#parameterizing)
   (fn [_ value-props]
       (let [value-props (pretty-presets.engine/apply-preset     value-id value-props)
             value-props (value.prototypes/value-props-prototype value-id value-props)]
            [view-lifecycles value-id value-props]))))
