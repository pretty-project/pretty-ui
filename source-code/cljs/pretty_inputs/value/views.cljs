
(ns pretty-inputs.value.views
    (:require [fruits.random.api              :as random]
              [pretty-accessories.api         :as pretty-accessories]
              [pretty-elements.api            :as pretty-elements]
              [pretty-inputs.engine.api       :as pretty-inputs.engine]
              [pretty-inputs.methods.api      :as pretty-inputs.methods]
              [pretty-inputs.value.attributes :as value.attributes]
              [pretty-inputs.value.prototypes :as value.prototypes]
              [pretty-subitems.api            :as pretty-subitems]
              [reagent.core                   :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def SHORTHAND-MAP {:label                 label.views/SHORTHAND-MAP
                    :end-adornment-group   adornment-group.views/SHORTHAND-MAP
                    :start-adornment-group adornment-group.views/SHORTHAND-MAP})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- value
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:end-adornment-group (map)(opt)
  ;  :label (map)(opt)
  ;  :start-adornment-group (map)(opt)
  ;  ...}
  [id {:keys [end-adornment-group label start-adornment-group] :as props}]
  [:div (value.attributes/outer-attributes       id props)
        [:div (value.attributes/inner-attributes id props)
              [:<> (if start-adornment-group [pretty-elements/adornment-group (pretty-subitems/subitem-id id :start-adornment-group) start-adornment-group])
                   (if label                 [pretty-accessories/label        (pretty-subitems/subitem-id id :label)                 label])
                   (if end-adornment-group   [pretty-elements/adornment-group (pretty-subitems/subitem-id id :end-adornment-group)   end-adornment-group])]]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  [id props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-inputs.engine/pseudo-input-did-mount    id props))
                         :component-will-unmount (fn [_ _] (pretty-inputs.engine/pseudo-input-will-unmount id props))
                         :reagent-render         (fn [_ props] [value id props])}))

(defn view
  ; @description
  ; Customizable value element with optional adornments.
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
  ; [Input value properties](pretty-core/cljs/pretty-properties/api.html#input-value-properties)
  ; [Lifecycle properties](pretty-core/cljs/pretty-properties/api.html#lifecycle-properties)
  ; [Mouse event properties](pretty-core/cljs/pretty-properties/api.html#mouse-event-properties)
  ; [Outer position properties](pretty-core/cljs/pretty-properties/api.html#outer-position-properties)
  ; [Outer size properties](pretty-core/cljs/pretty-properties/api.html#outer-size-properties)
  ; [Outer space properties](pretty-core/cljs/pretty-properties/api.html#outer-space-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [State properties](pretty-core/cljs/pretty-properties/api.html#state-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ; [Visibility properties](pretty-core/cljs/pretty-properties/api.html#visibility-properties)
  ;
  ; @param (keyword)(opt) value-id
  ; @param (map) value-props
  ; Check out the implemented elements.
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-inputs/value.png)
  ; [value {:border-radius         {:all :l}
  ;         :fill-color            :highlight
  ;         :gap                   :auto
  ;         :get-value-f           #(-> "My value #1")
  ;         :indent                {:right :s}
  ;         :label                 {:content "My value (default label)"}
  ;         :outer-width           :xxl
  ;         :start-adornment-group {:adornment-default {:fill-color :default :border-color :muted :border-radius {:all :l}}
  ;                                 :adornments        [{:icon {:icon-name :close} :on-click-f (fn [_] ...)}]}}]
  ;
  ; [value {:border-radius       {:all :l}
  ;         :fill-color          :highlight
  ;         :gap                 :auto
  ;         :get-value-f         #(-> "My value #2")
  ;         :indent              {:left :s}
  ;         :label               {:content "My value (default label)"}
  ;         :outer-width         :xxl
  ;         :end-adornment-group {:adornment-default {:fill-color :default :border-color :muted :border-radius {:all :l}}
  ;                               :adornments        [{:icon {:icon-name :close} :on-click-f (fn [_] ...)}]}}]
  ([props]
   [view (random/generate-keyword) props])

  ([id props]
   ; @note (tutorials#parameterizing)
   (fn [_ props]
       (let [props (pretty-inputs.methods/apply-input-shorthand-map  id props SHORTHAND-MAP)
             props (pretty-inputs.methods/apply-input-preset         id props)
             props (pretty-inputs.methods/import-input-dynamic-props id props)
             props (pretty-inputs.methods/import-input-state-events  id props)
             props (pretty-inputs.methods/import-input-state         id props)
             props (value.prototypes/props-prototype                 id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
