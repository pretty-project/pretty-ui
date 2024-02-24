
(ns pretty-inputs.select.views
    (:require [fruits.hiccup.api               :as hiccup]
              [fruits.random.api               :as random]
              [fruits.vector.api               :as vector]
              [metamorphic-content.api         :as metamorphic-content]
              [pretty-elements.api             :as pretty-elements]
              [pretty-inputs.engine.api        :as pretty-inputs.engine]
              [pretty-inputs.header.views      :as header.views]
              [pretty-inputs.select.attributes :as select.attributes]
              [pretty-inputs.select.env        :as select.env]
              [pretty-inputs.select.prototypes :as select.prototypes]
              [pretty-inputs.text-field.views  :as text-field.views]
              [pretty-layouts.api              :as pretty-layouts]
              [pretty-presets.engine.api       :as pretty-presets.engine]
              [reagent.core :as reagent]
              [pretty-subitems.api :as pretty-subitems]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- option-field
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  [select-id select-props]
  ; The option field  ...
  ; ... filters the selectable options.
  ; ... optionally provide an "Add" button as end adornment to add new options.
  (let [field-id    (pretty-subitems/subitem-id select-id :text-field)
        field-props (select.prototypes/field-props-prototype select-id select-props)]
       [text-field.views/view field-id field-props]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- select-option
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ; @param (integer) option-dex
  ; @param (*) option
  [select-id select-props option-dex option]
  (if (pretty-inputs.engine/render-input-option? select-id select-props option-dex option)
      [:button (select.attributes/select-option-attributes select-id select-props option-dex option)
               (if (pretty-inputs.engine/input-option-selected? select-id select-props option-dex option)
                   [:div (select.attributes/select-option-checkmark-attributes select-id select-props option-dex option) :done])
               [:div {:class :pi-select--option-content}
                     (if-some [option-label (pretty-inputs.engine/get-input-option-label select-id select-props option-dex option)]
                              [:div (select.attributes/select-option-label-attributes select-id select-props option-dex option)
                                    (-> option-label)])
                     (if-some [option-helper (pretty-inputs.engine/get-input-option-helper select-id select-props option-dex option)]
                              [:div (select.attributes/select-option-helper-attributes select-id select-props option-dex option)
                                    (-> option-helper)])]]))

(defn- select-option-list
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ; {}
  [select-id {:keys [placeholder] :as select-props}]
  (letfn [(f0 [option-dex option] [select-option select-id select-props option-dex option])]
         (let [options (pretty-inputs.engine/get-input-options select-id select-props)]
              (cond (-> options vector/not-empty?) (hiccup/put-with-indexed [:<>] options f0)
                    (-> placeholder) [:div (select.attributes/select-placeholder-attributes select-id select-props)
                                           (metamorphic-content/compose placeholder)]))))

(defn- select-popup-body
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  [select-id select-props]
  [:div {:class :pi-select--popup-body}
        [select-option-list select-id select-props]])

(defn- select-popup-header
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ; {:popup (map)(opt)
  ;   {:label (metamorphic-content)(opt)}}
  [select-id {:keys [options-label] :as select-props}]
  [:div {:class :pi-select--popup-header}
        [:div (select.attributes/select-options-label-attributes select-id select-props)
              (metamorphic-content/compose options-label)]
        [option-field select-id select-props]])

(defn- select-popup
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ; {:popup (map)(opt)}
  [select-id {:keys [popup] :as select-props}]
  (let [popup-id    (pretty-subitems/subitem-id select-id :popup)
        popup-props (select.prototypes/popup-props-prototype select-id select-props)
        popup-props (assoc popup-props :body   [select-popup-body   select-id select-props]
                                       :header [select-popup-header select-id select-props])]
       (if (pretty-inputs.engine/input-popup-visible? select-id select-props)
           [:div {:class :pi-select--popup}
                 [pretty-layouts/popup popup-id popup-props]])))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- select-button-layout
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  [select-id select-props]
  (let [button-id    (pretty-subitems/subitem-id select-id :button)
        button-props (select.prototypes/select-button-props-prototype select-id select-props)]
       [pretty-elements/button button-id button-props]))

(defn- icon-button-layout
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  [select-id select-props]
  (let [button-id    (pretty-subitems/subitem-id select-id :button)
        button-props (select.prototypes/icon-button-props-prototype select-id select-props)]
       [pretty-elements/icon-button button-id button-props]))

(defn- button-layout
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  [select-id select-props]
  (let [button-id    (pretty-subitems/subitem-id select-id :button)
        button-props (select.prototypes/button-props-prototype  select-id select-props)]
       [pretty-elements/button button-id button-props]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- select
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ; {:layout (keyword)}
  [select-id {:keys [layout] :as select-props}]
  [:div (select.attributes/select-attributes     select-id select-props)
        [pretty-inputs.header.views/view         select-id select-props]
        [pretty-inputs.engine/input-synchronizer select-id select-props]
        [:div (select.attributes/select-inner-attributes select-id select-props)
              (case layout :button        [button-layout        select-id select-props]
                           :icon-button   [icon-button-layout   select-id select-props]
                           :select-button [select-button-layout select-id select-props]
                                          [select-button-layout select-id select-props])
              [select-popup select-id select-props]]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  [select-id select-props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-inputs.engine/input-did-mount    select-id select-props))
                         :component-will-unmount (fn [_ _] (pretty-inputs.engine/input-will-unmount select-id select-props))
                         :reagent-render         (fn [_ select-props] [select select-id select-props])}))

(defn view
  ; @param (keyword)(opt) select-id
  ; @param (map) select-props
  ; {:add-option-f (function)(opt)
  ;  :add-options? (boolean)(opt)
  ;  :button (map)(opt)
  ;  :class (keyword or keywords in vector)(opt)
  ;  :click-effect (keyword)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :font-size (keyword, px or string)(opt)
  ;  :get-options-f (function)(opt)
  ;  :get-value-f (function)(opt)
  ;  :helper (metamorphic-content)(opt)
  ;  :hover-effect (keyword)(opt)
  ;  :info (metamorphic-content)(opt)
  ;  :initial-value (*)(opt)
  ;  :label (metamorphic-content)(opt)
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
  ;  :options-label (metamorphic-content)(opt)
  ;  :orientation (keyword)(opt)
  ;  :placeholder (metamorphic-content)(opt)
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
  ;     :invalid-message (metamorphic-content)(opt)}]}
  ;
  ; @usage
  ; [select {...}]
  ;
  ; @usage
  ; [select :my-select {...}]
  ([select-props]
   [view (random/generate-keyword) select-props])

  ([select-id select-props]
   ; @note (tutorials#parameterizing)
   (fn [_ select-props]
       (let [select-props (pretty-presets.engine/apply-preset       select-id select-props)
             select-props (select.prototypes/select-props-prototype select-id select-props)]
            [view-lifecycles select-id select-props]))))
