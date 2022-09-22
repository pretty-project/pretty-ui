
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.switch.views
    (:require [mid-fruits.random                :as random]
              [reagent.api                      :as reagent]
              [x.app-components.api             :as components]
              [x.app-elements.input.helpers     :as input.helpers]
              [x.app-elements.label.views       :as label.views]
              [x.app-elements.switch.helpers    :as switch.helpers]
              [x.app-elements.switch.prototypes :as switch.prototypes]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- switch-option-helper
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  ;  {:option-helper-f (function)}
  ; @param (*) option
  [_ {:keys [option-helper-f]} option]
  (if option-helper-f (let [option-helper (option-helper-f option)]
                           [:div.x-switch--option-helper (components/content option-helper)])))

(defn- switch-option-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  ;  {:option-label-f (function)}
  ; @param (*) option
  [_ {:keys [option-label-f]} option]
  (let [option-label (option-label-f option)]
       [:div.x-switch--option-label (components/content option-label)]))

(defn- switch-option-content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  ; @param (*) option
  [switch-id switch-props option]
  [:div.x-switch--option-content [switch-option-label  switch-id switch-props option]
                                 [switch-option-helper switch-id switch-props option]])

(defn- switch-option
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  ; @param (*) option
  [switch-id switch-props option]
  [:button.x-switch--option (switch.helpers/switch-option-attributes switch-id switch-props option)
                            [:div.x-switch--option-track]
                            [switch-option-content switch-id switch-props option]])

(defn- switch-options
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  [switch-id switch-props]
  (let [options (input.helpers/get-input-options switch-id switch-props)]
       (letfn [(f [option-list option] (conj option-list [switch-option switch-id switch-props option]))]
              (reduce f [:<>] options))))

(defn- switch-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  [switch-id switch-props]
  [:div.x-switch--body (switch.helpers/switch-body-attributes switch-id switch-props)
                       [switch-options                        switch-id switch-props]])

(defn- switch-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  ;  {}
  [_ {:keys [helper info-text label required?]}]
  (if label [label.views/element {:content   label
                                  :helper    helper
                                  :info-text info-text
                                  :required? required?}]))

(defn- switch-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  [switch-id switch-props]
  [:div.x-switch (switch.helpers/switch-attributes switch-id switch-props)
                 [switch-label                     switch-id switch-props]
                 [switch-body                      switch-id switch-props]])

(defn- switch
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  [switch-id switch-props]
  (reagent/lifecycles {:component-did-mount (switch.helpers/switch-did-mount-f switch-id switch-props)
                       :reagent-render      (fn [_ switch-props] [switch-structure switch-id switch-props])}))

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
  ;   :info-text (metamorphic-content)(opt)
  ;   :initial-options (vector)(opt)
  ;   :initial-value (boolean)(opt)
  ;   :label (metamorphic-content)(opt)
  ;   :on-check (metamorphic-event)(opt)
  ;   :on-uncheck (metamorphic-event)(opt)
  ;   :option-helper-f (function)(opt)
  ;   :option-label-f (function)(opt)
  ;    Default: return
  ;   :option-value-f (function)(opt)
  ;    Default: return
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
   [element (random/generate-keyword) switch-props])

  ([switch-id switch-props]
   (let [switch-props (switch.prototypes/switch-props-prototype switch-id switch-props)]
        [switch switch-id switch-props])))
