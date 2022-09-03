
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.select.views
    (:require [layouts.popup-a.api                           :as popup-a]
              [mid-fruits.vector                             :as vector]
              [reagent.api                                   :as reagent]
              [x.app-components.api                          :as components]
              [x.app-core.api                                :as a]
              [x.app-elements.button.views                   :as button.views]
              [x.app-elements.element-components.icon-button :as icon-button]
              [x.app-elements.engine.api                     :as engine]
              [x.app-elements.input.helpers                  :as input.helpers]
              [x.app-elements.text-field.views               :as text-field.views]
              [x.app-elements.select.helpers                 :as select.helpers]
              [x.app-elements.select.prototypes              :as select.prototypes]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- new-option-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ;  {:extendable? (boolean)(opt)
  ;   :new-option-placeholder (metamorphic-content)}
  [select-id {:keys [extendable? new-option-placeholder] :as select-props}]
  (if extendable? (let [field-empty?      @(a/subscribe [:elements/field-empty? :elements.select/new-option-field])
                        adornment-on-click [:elements.select/enter-pressed select-id select-props]
                        adornment-props    {:disabled? field-empty? :icon :add :on-click adornment-on-click :title :add!}]
                       [text-field.views/element :elements.select/new-option-field
                                                 {:end-adornments [adornment-props]
                                                  :indent         {:bottom :xs :vertical :xs}
                                                  :placeholder    new-option-placeholder}])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- select-option
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ;  {:get-label-f (function)}
  ; @param (*) option
  [select-id {:keys [get-label-f] :as select-props} option]
  [:button.x-select--option (select.helpers/select-option-attributes select-id select-props option)
                            (-> option get-label-f components/content)])

(defn- select-option-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  [select-id select-props]
  (let [options (input.helpers/get-input-options select-id select-props)]
       (letfn [(f [options option] (conj options [select-option select-id select-props option]))]
              (reduce f [:<>] options))))

(defn- no-options-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ;  {}
  [_ {:keys [no-options-label]}]
  [:div.x-select--no-options-label (components/content no-options-label)])

(defn- select-options-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  [select-id select-props]
  (let [options (input.helpers/get-input-options select-id select-props)]
       [:div.x-select--option-list (if (vector/nonempty? options)
                                       [select-option-list select-id select-props]
                                       [no-options-label   select-id select-props])]))

(defn- select-options-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ;  {}
  [_ {:keys [options-label]}]
  (if options-label [:div.x-select--options--label (components/content options-label)]
                    [:div.x-select--options--label {:data-placeholder true}]))

(defn- select-options-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ;  {}
  [select-id select-props]
  [:div.x-select--options--header [select-options-label select-id select-props]
                                  [new-option-field     select-id select-props]])

(defn- select-options-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ;  {}
  [select-id {:keys [options-label] :as select-props}]
  [popup-a/layout :elements.select/options
                  {:body      [select-options-body   select-id select-props]
                   :header    [select-options-header select-id select-props]
                   :min-width :xxs}])

(defn- select-options
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  [select-id select-props]
  [:div.x-select--options (select.helpers/select-options-attributes select-id select-props)
                          [select-options-structure                 select-id select-props]])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- select-button-icon
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  [_ _]
  [:i.x-select--button-icon {:data-icon-family :material-icons-filled} :unfold_more])

(defn- select-button-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ;  {}
  [_ {:keys [get-label-f value-path]}]
  (if-let [selected-option @(a/subscribe [:db/get-item value-path])]
          [:div.x-select--button-label (-> selected-option get-label-f components/content)]
          [:div.x-select--button-label (-> :select!                    components/content)]))

(defn- select-button-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ;  {}
  [select-id select-props]
  [:button.x-select--button-body (select.helpers/select-button-body-attributes select-id select-props)
                                 [select-button-label                          select-id select-props]
                                 [select-button-icon                           select-id select-props]])

(defn- select-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  [select-id select-props]
  [:div.x-select--button [select-button-body select-id select-props]])

(defn- active-button-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  [select-id select-props]
  [:div.x-select (select.helpers/select-attributes select-id select-props)
                 [engine/element-header            select-id select-props]
                 [engine/element-helper            select-id select-props]
                 [select-button                    select-id select-props]])

(defn- active-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  [select-id select-props]
  (reagent/lifecycles {:component-did-mount (select.helpers/active-button-did-mount-f select-id select-props)
                       :reagent-render      (fn [_ select-props] [active-button-structure select-id select-props])}))

(defn- active-button-layout
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  [select-id select-props]
  [active-button select-id select-props])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- icon-button-layout
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  [select-id select-props]
  (let [on-click [:elements.select/render-options! select-id select-props]]
       [icon-button/element select-id (assoc select-props :on-click on-click)]))



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
  ;  {:layout (keyword)}
  [select-id {:keys [layout] :as select-props}]
  (case layout :button      [button-layout        select-id select-props]
               :icon-button [icon-button-layout   select-id select-props]
               :select      [active-button-layout select-id select-props]))

(defn element
  ; @param (keyword)(opt) select-id
  ; @param (map) select-props
  ;  {:autoclear? (boolean)(opt)
  ;    Default: false
  ;   :border-radius (keyword)(opt)
  ;    :none, :xxs, :xs, :s, :m, :l
  ;    Default: :s
  ;   :class (keyword or keywords in vector)(opt)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :extendable? (boolean)(opt)
  ;    Default: false
  ;   :get-label-f (function)(opt)
  ;    Default: return
  ;   :get-value-f (function)(opt)
  ;    Default: return
  ;   :indent (map)(opt)
  ;    {:bottom (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :left (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :right (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :top (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl}
  ;   :initial-options (vector)(opt)
  ;   :initial-value (*)(opt)
  ;   :label (metamorphic-content)(opt)
  ;   :layout (keyword)(opt)
  ;    :button, :icon-button, :select
  ;    Default: :select
  ;   :min-width (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl, :none
  ;    Default: :none
  ;   :new-option-f (function)(opt)
  ;    Default: return
  ;   :new-option-placeholder (metamorphic-content)(opt)
  ;    Default: :new-option
  ;   :no-options-label (metamorphic-content)(opt)
  ;    Default: :no-options
  ;   :on-popup-closed (metamorphic-event)(opt)
  ;   :on-select (metamorphic-event)(opt)
  ;   :options (vector)(opt)
  ;   :options-label (metamorphic-content)(opt)
  ;   :options-path (vector)(opt)
  ;   :required? (boolean or keyword)(opt)
  ;    true, false, :unmarked
  ;    Default: false
  ;   :style (map)(opt)
  ;   :value-path (vector)(opt)}
  ;
  ; @usage
  ;  [elements/select {...}]
  ;
  ; @usage
  ;  [elements/select :my-select {...}]
  ;
  ; @usage
  ;  [elements/select {:icon         :sort
  ;                    :layout       :icon-button
  ;                    :options-path [:my-options]
  ;                    :value-path   [:my-selected-option]}]
  ([select-props]
   [element (a/id) select-props])

  ([select-id select-props]
   (let [select-props (select.prototypes/select-props-prototype select-id select-props)]
        [select select-id select-props])))
