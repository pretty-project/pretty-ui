
(ns elements.switch.views
    (:require [elements.input.helpers     :as input.helpers]
              [elements.label.views       :as label.views]
              [elements.switch.helpers    :as switch.helpers]
              [elements.switch.prototypes :as switch.prototypes]
              [random.api                 :as random]
              [reagent.api                :as reagent]
              [x.components.api           :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- switch-option-helper
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  ; {:option-helper-f (function)}
  ; @param (*) option
  [_ {:keys [option-helper-f]} option]
  (if option-helper-f (let [option-helper (option-helper-f option)]
                           [:div.e-switch--option-helper {:data-font-size   :xs
                                                          :data-line-height :normal}
                                                         (x.components/content option-helper)])))

(defn- switch-option-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  ; {:font-size (keyword)
  ;  :option-label-f (function)}
  ; @param (*) option
  [_ {:keys [font-size option-label-f]} option]
  (let [option-label (option-label-f option)]
       [:div.e-switch--option-label {:data-font-size   font-size
                                     :data-font-weight :bold
                                     :data-line-height :block}
                                    (x.components/content option-label)]))

(defn- switch-option-content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  ; @param (*) option
  [switch-id switch-props option]
  [:div.e-switch--option-content [switch-option-label  switch-id switch-props option]
                                 [switch-option-helper switch-id switch-props option]])

(defn- switch-option
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  ; @param (*) option
  [switch-id switch-props option]
  [:button.e-switch--option (switch.helpers/switch-option-attributes switch-id switch-props option)
                            [:div.e-switch--option-track]
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
  [:div.e-switch--body (switch.helpers/switch-body-attributes switch-id switch-props)
                       [switch-options                        switch-id switch-props]])

(defn- switch-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  ; {}
  [_ {:keys [helper info-text label marked? required?]}]
  (if label [label.views/element {:content     label
                                  :helper      helper
                                  :info-text   info-text
                                  :line-height :block
                                  :marked?     marked?
                                  :required?   required?}]))

(defn- switch-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  [switch-id switch-props]
  [:div.e-switch (switch.helpers/switch-attributes switch-id switch-props)
                 [switch-label                     switch-id switch-props]
                 [switch-body                      switch-id switch-props]])

(defn- switch
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  [switch-id switch-props]
  (reagent/lifecycles {:component-did-mount (fn [_ _] (switch.helpers/switch-did-mount switch-id switch-props))
                       :reagent-render      (fn [_ switch-props] [switch-structure switch-id switch-props])}))

(defn element
  ; @param (keyword)(opt) switch-id
  ; @param (map) switch-props
  ; {:border-color (keyword or string)(opt)
  ;   :default, :muted, :primary, :secondary, :success, :warning
  ;   Default: :primary
  ;  :class (keyword or keywords in vector)(opt)
  ;  :default-value (boolean)(opt)
  ;  :disabled? (boolean)(opt)
  ;   Default: false
  ;  :font-size (keyword)(opt)
  ;   :xs, :s, :inherit
  ;   Default: :s
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
  ;  :initial-value (boolean)(opt)
  ;  :label (metamorphic-content)(opt)
  ;  :marked? (boolean)(opt)
  ;   Default: false
  ;  :on-check (metamorphic-event)(opt)
  ;  :on-uncheck (metamorphic-event)(opt)
  ;  :option-helper-f (function)(opt)
  ;  :option-label-f (function)(opt)
  ;   Default: return
  ;  :option-value-f (function)(opt)
  ;   Default: return
  ;  :options (vector)(opt)
  ;  :options-orientation (keyword)(opt)
  ;   :horizontal, :vertical
  ;   Default: :vertical
  ;  :options-path (vector)(opt)
  ;  :outdent (map)(opt)
  ;  :required? (boolean or keyword)(opt)
  ;   true, false, :unmarked
  ;   Default: false
  ;  :style (map)(opt)
  ;  :value-path (vector)(opt)}
  ;
  ; @usage
  ; [switch {...}]
  ;
  ; @usage
  ; [switch :my-switch {...}]
  ([switch-props]
   [element (random/generate-keyword) switch-props])

  ([switch-id switch-props]
   (let [switch-props (switch.prototypes/switch-props-prototype switch-id switch-props)]
        [switch switch-id switch-props])))
