
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.select.views
    (:require [layouts.popup-a.api                           :as popup-a]
              [mid-fruits.vector                             :as vector]
              [reagent.api                                   :as reagent]
              [x.app-components.api                          :as components]
              [x.app-core.api                                :as a]
              [x.app-elements.engine.api                     :as engine]
              [x.app-elements.element-components.button      :as button]
              [x.app-elements.element-components.icon-button :as icon-button]
              [x.app-elements.select.helpers                 :as select.helpers]
              [x.app-elements.select.prototypes              :as select.prototypes]))



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
  ;  {:options-path (vector)}
  [select-id {:keys [options-path] :as select-props}]
  (let [options @(a/subscribe [:db/get-item options-path])]
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
  ;  {:options-path (vector)}
  [select-id {:keys [options-path] :as select-props}]
  (let [options @(a/subscribe [:db/get-item options-path])]
       [:div.x-select--option-list {:class           :x-element
                                    :data-selectable false}
                                   (if (vector/nonempty? options)
                                       [select-option-list select-id select-props]
                                       [no-options-label   select-id select-props])]))

(defn- select-options-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ;  {}
  [select-id {:keys [options-label] :as select-props}]
  (if options-label [:div.x-select--options--header (components/content options-label)]
                    [:div.x-select--options--header {:data-placeholder true}]))

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
  [:div.x-select--options {:class           :x-element
                           :data-selectable false}
                          [select-options-structure select-id select-props]])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- select-button-icon
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  [_ _]
  [:i.x-select--button-icon :unfold_more])

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

(defn- select-layout-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  [select-id select-props]
  [:div.x-select (select.helpers/select-attributes select-id select-props)
                 [engine/element-header            select-id select-props]
                 [select-button                    select-id select-props]
                 [engine/element-helper            select-id select-props]])

(defn- select-layout
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  [select-id select-props]
  ; A {:layout :select} beállítással megjelenített select elem megjeleníti az aktuálisan kiválasztott
  ; értékét, ezért az elem React-fába csatolásakor szükséges meghívni az [:elements/init-select! ...]
  ; eseményt, hogy esetlegesen a Re-Frame adatbázisba írja az {:initial-value ...} kezdeti értéket!
  (reagent/lifecycles select-id
                      {:component-did-mount (fn [] (a/dispatch [:elements.select/init-element! select-id select-props]))
                       :reagent-render      (fn [] [select-layout-structure select-id select-props])}))



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
       [button/element select-id (assoc select-props :on-click on-click)]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- select
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ;  {:layout (keyword)}
  [select-id {:keys [layout] :as select-props}]
  (case layout :button      [button-layout      select-id select-props]
               :icon-button [icon-button-layout select-id select-props]
               :select      [select-layout      select-id select-props]))

(defn element
  ; @param (keyword)(opt) select-id
  ; @param (map) select-props
  ;  {:autoclear? (boolean)(opt)
  ;    Default: false
  ;   :class (keyword or keywords in vector)(opt)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :form-id (keyword)(opt)
  ;   :get-label-f (function)(constant)(opt)
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
  ;   :initial-options (vector)(constant)(opt)
  ;   :initial-value (*)(constant)(opt)
  ;   :label (metamorphic-content)(opt)
  ;   :layout (keyword)(opt)
  ;    :button, :icon-button, :select
  ;    Default: :select
  ;   :min-width (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl, :none
  ;    Default: :none
  ;   :no-options-label (metamorphic-content)(opt)
  ;    Default: :no-options
  ;   :on-popup-closed (metamorphic-event)(opt)
  ;   :on-select (metamorphic-event)(constant)(opt)
  ;   :options-label (metamorphic-content)(constant)(opt)
  ;   :options-path (vector)(constant)(opt)
  ;   :required? (boolean)(constant)(opt)
  ;    Default: false
  ;   :style (map)(opt)
  ;   :value-path (vector)(constant)(opt)}
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
