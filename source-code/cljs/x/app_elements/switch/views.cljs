
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.switch.views
    (:require [reagent.api                      :as reagent]
              [x.app-components.api             :as components]
              [x.app-core.api                   :as a :refer [r]]
              [x.app-elements.engine.api        :as engine]
              [x.app-elements.switch.helpers    :as switch.helpers]
              [x.app-elements.switch.prototypes :as switch.prototypes]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-switch-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) switch-id
  ;
  ; @return (map)
  [db [_ switch-id]]
  (merge (r engine/get-element-props   db switch-id)
         (r engine/get-checkable-props db switch-id)))

(a/reg-sub :elements/get-switch-props get-switch-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- switch-secondary-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  ;  {:secondary-label (metamorphic-content)}
  [_ {:keys [secondary-label]}]
  [:div.x-switch--secondary-label [components/content secondary-label]])

(defn- switch-secondary-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  [switch-id switch-props]
  [:button.x-switch--secondary-body (engine/checkable-secondary-body-attributes switch-id switch-props)
                                    [switch-secondary-label                     switch-id switch-props]])

(defn- switch-primary-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  ;  {:label (metamorphic-content)
  ;   :required? (boolean or keyword)(opt)}
  [_ {:keys [label required?]}]
  [:div.x-switch--primary-label [components/content label]
                                (if (true? required?)
                                    [:span.x-input--label-asterisk "*"])])

(defn- switch-primary-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  [switch-id switch-props]
  [:button.x-switch--primary-body (engine/checkable-primary-body-attributes switch-id switch-props)
                                  [:div.x-switch--track [:div.x-switch--thumb]]
                                  [switch-primary-label switch-id switch-props]])

(defn- switch-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  ;  {:label (metamorphic-content)
  ;   :required? (boolean)(opt)}
  [_ {:keys [label required?]}]
  [:div.x-switch--label [components/content label]
                        (if required? [:span.x-input--label-asterisk "*"])])

(defn- switch-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; (keyword) switch-id
  ; @param (map) switch-props
  [switch-id switch-props]
  [:button.x-switch--body (engine/checkable-body-attributes switch-id switch-props)
                          [:div.x-switch--track [:div.x-switch--thumb]]
                          [switch-label switch-id switch-props]])

(defn- switch-option
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  ;  {:get-label-f (function)}
  ; @param (*) option
  [switch-id {:keys [get-label-f] :as switch-props} option]
  (let [option-label (get-label-f option)]
       [:button.x-switch--option (switch.helpers/switch-option-attributes switch-id switch-props option)
                                 [:div.x-switch--option-track]
                                 [:div.x-switch--option-label (components/content option-label)]]))

(defn- switch-options
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  [switch-id switch-props]
  (let [options (engine/input-options switch-id switch-props)]
       (letfn [(f [option-list option] (conj option-list [switch-option switch-id switch-props option]))]
              (reduce f [:div.x-switch--options] options))))

(defn- switch
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  [switch-id switch-props]
  [:div.x-switch (switch.helpers/switch-attributes switch-id switch-props)
                 [engine/element-header switch-id switch-props]
                 [switch-options        switch-id switch-props]])

(defn- stated-switch
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  [switch-id switch-props]
  (reagent/lifecycles {:component-did-mount (fn [] (a/dispatch [:elements.switch/init-switch! switch-id switch-props]))
                       :reagent-render      (fn [_ switch-props] [switch switch-id switch-props])}))

(defn element
  ; @param (keyword)(opt) switch-id
  ; @param (map) switch-props
  ;  {:border-color (keyword or string)(opt)
  ;    :default, :muted, :primary, :secondary, :success, :warning
  ;    Default: :primary
  ;   :class (keyword or keywords in vector)(opt)
  ;   :default-value (boolean)(opt)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :font-size (keyword)(opt)
  ;    :xs, :s
  ;    Default: :s
  ;   :get-label-f (function)(opt)
  ;    Default: return
  ;   :get-value-f (function)(opt)
  ;    Default: return
  ;   :helper (metamorphic-content)(opt)
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
  ;   :initial-value (boolean)(opt)
  ;   :label (metamorphic-content)(opt)
  ;   :on-check (metamorphic-event)(opt)
  ;   :on-uncheck (metamorphic-event)(opt)
  ;   :options (vector)(opt)
  ;   :options-orientation (keyword)(opt)
  ;    :horizontal, :vertical
  ;    Default: :vertical
  ;   :options-path (vector)(opt)
  ;   :required? (boolean or keyword)(opt)
  ;    true, false, :unmarked
  ;    Default: false
  ;   :style (map)(opt)
  ;   :value-path (vector)(opt)}
  ;
  ; @usage
  ;  [elements/switch {...}]
  ;
  ; @usage
  ;  [elements/switch :my-switch {...}]
  ([switch-props]
   [element (a/id) switch-props])

  ([switch-id {:keys [initial-options initial-value] :as switch-props}]
   (let [switch-props (switch.prototypes/switch-props-prototype switch-id switch-props)]
        (if (or initial-options initial-value)
            [stated-switch switch-id switch-props]
            [switch        switch-id switch-props]))))
