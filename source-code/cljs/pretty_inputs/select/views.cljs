
(ns pretty-inputs.select.views
    (:require [fruits.hiccup.api               :as hiccup]
              [fruits.random.api               :as random]
              [pretty-elements.api             :as pretty-elements]
              [pretty-inputs.engine.api        :as pretty-inputs.engine]
              [pretty-inputs.select.attributes :as select.attributes]
              [pretty-inputs.select.prototypes :as select.prototypes]
              [pretty-layouts.api              :as pretty-layouts]
              [pretty-presets.engine.api       :as pretty-presets.engine]
              [reagent.core :as reagent]
              [dynamic-props.api :as dynamic-props]
              [pretty-inputs.option-group.views :as option-group.views]
              [pretty-inputs.header.views :as header.views]
              [pretty-inputs.select-button.views :as select-button.views]
              [pretty-subitems.api :as pretty-subitems]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- select
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:button (map)(opt)
  ;  :header (map)(opt)
  ;  :icon-button (map)(opt)
  ;  :option-group (map)(opt)
  ;  :popup (map)(opt)
  ;  :popup-visible? (boolean)(opt)
  ;  :select-button (map)(opt)
  ;  ...}
  [id {:keys [button header icon-button option-group popup popup-visible? select-button] :as props}]
  (let [popup (assoc popup :body {:content [option-group.views/view (pretty-subitems/subitem-id id :option-group) option-group]})]
       [:div (select.attributes/outer-attributes id props)
             [:div (select.attributes/inner-attributes id props)
                   (when header         [header.views/view           (pretty-subitems/subitem-id id :header)        header])
                   (cond button         [pretty-elements/button      (pretty-subitems/subitem-id id :button)        button]
                         icon-button    [pretty-elements/icon-button (pretty-subitems/subitem-id id :icon-button)   icon-button]
                         select-button  [select-button.views/view    (pretty-subitems/subitem-id id :select-button) select-button])
                   (when popup-visible? [pretty-layouts/popup        (pretty-subitems/subitem-id id :popup)         popup])]]))

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
                         :reagent-render         (fn [_ props] [select id props])}))

(defn view
  ; @description
  ; Select style input.
  ;
  ; @links Implemented elements
  ; [Button](pretty-core/cljs/pretty-elements/api.html#button)
  ; [Icon-button](pretty-core/cljs/pretty-elements/api.html#icon-button)
  ;
  ; @links Implemented inputs
  ; [Header](pretty-core/cljs/pretty-inputs/api.html#header)
  ; [Option-group](pretty-core/cljs/pretty-inputs/api.html#option-group)
  ; [Select-button](pretty-core/cljs/pretty-inputs/api.html#select-button)
  ;
  ; @links Implemented layouts
  ; [Popup](pretty-core/cljs/pretty-layouts/api.html#popup)
  ;
  ; @links Implemented properties
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
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
  ; Check out the implemented elements.
  ; Check out the implemented inputs.
  ; Check out the implemented layouts.
  ; Check out the implemented properties.


  ; @param (keyword)(opt) select-id
  ; @param (map) select-props
  ;  :font-size (keyword, px or string)(opt)
  ;  :get-options-f (function)(opt)
  ;  :get-value-f (function)(opt)
  ;  :helper (multitype-content)(opt)
  ;  :hover-effect (keyword)(opt)
  ;  :info (multitype-content)(opt)
  ;  :initial-value (*)(opt)
  ;  :label (multitype-content)(opt)
  ;  :layout (keyword)(opt)
  ;   :button, :icon-button, :select-button
  ;   Default: :select-button
  ;  :max-selection (integer)(opt)
  ;  :on-empty-f (function)(opt)
  ;  :on-invalid-f (function)(opt)
  ;  :on-mount-f (function)(opt)
  ;  :on-selected-f (function)(opt)
  ;  :on-unmount-f (function)(opt)
  ;  :on-unselected-f (function)(opt)
  ;  :on-valid-f (function)(opt)
  ;  :option-field (map)(opt)
  ;  :option-color-f (function)(opt)
  ;  :option-helper-f (function)(opt)
  ;  :option-label-f (function)(opt)
  ;  :option-value-f (function)(opt)
  ;  :options-label (multitype-content)(opt)
  ;  :orientation (keyword)(opt)
  ;  :placeholder (multitype-content)(opt)
  ;  :preset (keyword)(opt)
  ;  :popup (map)(opt)
  ;  :projected-value (*)(opt)
  ;  :set-value-f (function)(opt)
  ;  :style (map)(opt)
  ;  :theme (keyword)(opt)
  ;  :validate-when-change? (boolean)(opt)
  ;  :validate-when-leave? (boolean)(opt)
  ;  :validators (maps in vector)(opt)
  ;   [{:f (function)
  ;     :invalid-message (multitype-content)(opt)}]}
  ;
  ; @usage
  ; [select {...}]
  ;
  ; @usage
  ; [select :my-select {...}]
  ([props]
   [view (random/generate-keyword) props])

  ([id props]
   ; @note (tutorials#parameterizing)
   (fn [_ props]
       (let [props (pretty-presets.engine/apply-preset id props)
             props (select.prototypes/props-prototype  id props)
             props (dynamic-props/import-props         id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
