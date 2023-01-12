
(ns elements.select.views
    (:require [candy.api                  :refer [return]]
              [elements.button.views      :as button.views]
              [elements.element.views     :as element.views]
              [elements.icon-button.views :as icon-button.views]
              [elements.input.helpers     :as input.helpers]
              [elements.select.helpers    :as select.helpers]
              [elements.select.prototypes :as select.prototypes]
              [elements.text-field.views  :as text-field.views]
              [layouts.popup-a.api        :as popup-a]
              [random.api                 :as random]
              [re-frame.api               :as r]
              [reagent.api                :as reagent]
              [vector.api                 :as vector]
              [x.components.api           :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- option-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ; {:extendable? (boolean)(opt)
  ;  :option-field-placeholder (metamorphic-content)}
  [select-id {:keys [extendable? option-field-placeholder] :as select-props}]
  (if extendable? (let [adornment-on-click [:elements.select/add-option! select-id select-props]
                        adornment-props    {:icon :add :on-click adornment-on-click :title :add!}]
                       [text-field.views/element :elements.select/option-field
                                                 {:end-adornments [adornment-props]
                                                  :indent         {:bottom :xs :vertical :xs}
                                                  :placeholder    option-field-placeholder}])))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- select-option
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ; {:option-label-f (function)}
  ; @param (*) option
  [select-id {:keys [option-label-f] :as select-props} option]
  [:button.e-select--option (select.helpers/select-option-attributes select-id select-props option)
                            (-> option option-label-f x.components/content)])

(defn- select-option-list-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  [select-id select-props]
  (let [options (input.helpers/get-input-options select-id select-props)]
       (letfn [(f [options option]
                  (if (select.helpers/render-option? select-id select-props option)
                      (conj   options [select-option select-id select-props option])
                      (return options)))]
              (reduce f [:<>] options))))

(defn- options-placeholder
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ; {:options-placeholder (metamorphic-content)}
  [_ {:keys [options-placeholder]}]
  [:div.e-select--options-placeholder {:data-font-size   :s
                                       :data-font-weight :medium
                                       :data-line-height :text-block}
                                      (x.components/content options-placeholder)])

(defn- select-option-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  [select-id select-props]
  (let [options (input.helpers/get-input-options select-id select-props)]
       [:div.e-select--option-list {:data-selectable false}
                                   (if (vector/nonempty? options)
                                       [select-option-list-items select-id select-props]
                                       [options-placeholder      select-id select-props])]))

(defn- select-options-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ; {}
  [_ {:keys [options-label]}]
  (if options-label [:div.e-select--options--label {:data-font-size   :s
                                                    :data-font-weight :medium
                                                    :data-line-height :text-block}
                                                   (x.components/content options-label)]
                    [:div.e-select--options--label {:data-placeholder true}]))

(defn- select-options-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ; {}
  [select-id select-props]
  [:div.e-select--options--header {:data-selectable false}
                                  [select-options-label select-id select-props]
                                  [option-field         select-id select-props]])

(defn- select-options-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  [select-id select-props]
  (reagent/lifecycles {:reagent-render         (fn [_ _] [select-option-list select-id select-props])
                       :component-did-mount    (fn [_ _] (r/dispatch [:elements.select/select-options-did-mount    select-id select-props]))
                       :component-will-unmount (fn [_ _] (r/dispatch [:elements.select/select-options-will-unmount select-id select-props]))}))

(defn- select-options
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  [select-id select-props]
  [popup-a/layout :elements.select/options
                  {:body      [select-options-body   select-id select-props]
                   :header    [select-options-header select-id select-props]
                   :min-width :xxs}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- select-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  [select-id select-props]
  (let [on-click [:elements.select/render-options!   select-id select-props]
        label    (select.helpers/select-button-label select-id select-props)]
       [button.views/element select-id (assoc select-props :class         :e-select-button
                                                           :icon          :unfold_more
                                                           :icon-position :right
                                                           :label         label
                                                           :on-click      on-click)]))

(defn- select-layout
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  [select-id select-props]
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (r/dispatch [:elements.select/active-button-did-mount    select-id select-props]))
                       :component-will-unmount (fn [_ _] (r/dispatch [:elements.select/active-button-will-unmount select-id select-props]))
                       :reagent-render         (fn [_ select-props] [select-button select-id select-props])}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- icon-button-layout
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  [select-id select-props]
  (let [on-click [:elements.select/render-options! select-id select-props]]
       [icon-button.views/element select-id (assoc select-props :on-click on-click)]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- button-layout
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  [select-id select-props]
  (let [on-click [:elements.select/render-options! select-id select-props]]
       [button.views/element select-id (assoc select-props :on-click on-click)]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- select
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ; {:layout (keyword)}
  [select-id {:keys [layout] :as select-props}]
  (case layout :button      [button-layout      select-id select-props]
               :icon-button [icon-button-layout select-id select-props]
               :select      [select-layout      select-id select-props]))

(defn element
  ; @param (keyword)(opt) select-id
  ; @param (map) select-props
  ; {:add-option-f (function)(opt)
  ;   Default: return
  ;  :autoclear? (boolean)(opt)
  ;   Default: false
  ;  :border-color (keyword)(opt)
  ;   :default, :highlight, :invert, :primary, :secondary, :success, :transparent, :warning
  ;  :border-radius (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;  :border-width (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;   Default: :xxs
  ;  :class (keyword or keywords in vector)(opt)
  ;  :disabled? (boolean)(opt)
  ;   Default: false
  ;  :extendable? (boolean)(opt)
  ;   Default: false
  ;  :helper (metamorphic-content)(opt)
  ;  :indent (map)(opt)
  ;   {:bottom (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    :left (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    :right (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    :top (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl}
  ;  :info-text (metamorphic-content)(opt)
  ;  :initial-options (vector)(opt)
  ;  :initial-value (*)(opt)
  ;  :label (metamorphic-content)(opt)
  ;  :layout (keyword)(opt)
  ;   :button, :icon-button, :select
  ;   Default: :select
  ;  :marker-color (keyword)(opt)
  ;   :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  ;  :min-width (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;  :on-select (metamorphic-event)(opt)
  ;  :option-field-placeholder (metamorphic-content)(opt)
  ;   Default: :add!
  ;  :option-label-f (function)(opt)
  ;   Default: return
  ;  :option-value-f (function)(opt)
  ;   Default: return
  ;  :options (vector)(opt)
  ;  :options-label (metamorphic-content)(opt)
  ;  :options-path (vector)(opt)
  ;  :options-placeholder (metamorphic-content)(opt)
  ;   Default: :no-options
  ;  :outdent (map)(opt)
  ;   Same as the :indent property.
  ;  :style (map)(opt)
  ;  :value-path (vector)(opt)}
  ;
  ; @usage
  ; [select {...}]
  ;
  ; @usage
  ; [select :my-select {...}]
  ;
  ; @usage
  ; [select {:icon         :sort
  ;          :layout       :icon-button
  ;          :options-path [:my-options]
  ;          :value-path   [:my-selected-option]}]
  ([select-props]
   [element (random/generate-keyword) select-props])

  ([select-id select-props]
   (let [select-props (select.prototypes/select-props-prototype select-id select-props)]
        [select select-id select-props])))
