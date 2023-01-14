
(ns elements.switch.views
    (:require [elements.element.views     :as element.views]
              [elements.input.helpers     :as input.helpers]
              [elements.switch.helpers    :as switch.helpers]
              [elements.switch.prototypes :as switch.prototypes]
              [pretty-css.api             :as pretty-css]
              [random.api                 :as random]
              [re-frame.api               :as r]
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
                                                          :data-line-height :native}
                                                         (x.components/content option-helper)])))

(defn- switch-option-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  ; {:option-label-f (function)}
  ; @param (*) option
  [_ {:keys [option-label-f]} option]
  (let [option-label (option-label-f option)]
       [:div.e-switch--option-label {:data-font-size   :s
                                     :data-font-weight :medium
                                     :data-line-height :text-block}
                                    (x.components/content option-label)]))

(defn- switch-option-content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  ; @param (*) option
  [switch-id switch-props option]
  [:div.e-switch--option-content {:data-click-target :opacity}
                                 [switch-option-label  switch-id switch-props option]
                                 [switch-option-helper switch-id switch-props option]])

(defn- switch-option
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  ; @param (*) option
  [switch-id switch-props option]
  [:button.e-switch--option (switch.helpers/switch-option-attributes switch-id switch-props option)
                            [:div.e-switch--option-track (switch.helpers/switch-option-track-attributes switch-id switch-props)]
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

(defn- switch-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  [switch-id switch-props]
  [:div.e-switch (switch.helpers/switch-attributes switch-id switch-props)
                 [element.views/element-label      switch-id switch-props]
                 [switch-body                      switch-id switch-props]])

(defn- switch
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  [switch-id switch-props]
  (reagent/lifecycles {:component-did-mount (fn [_ _] (r/dispatch [:elements.switch/switch-did-mount switch-id switch-props]))
                       :reagent-render      (fn [_ switch-props] [switch-structure switch-id switch-props])}))

(defn element
  ; @param (keyword)(opt) switch-id
  ; @param (map) switch-props
  ; {:border-color (keyword or string)(opt)
  ;   :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;   Default: :default
  ;  :border-radius (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;   Default: :m
  ;  :border-width (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;   Default: :xs
  ;  :class (keyword or keywords in vector)(opt)
  ;  :default-value (boolean)(opt)
  ;  :disabled? (boolean)(opt)
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
  ;  :marker-color (keyword)(opt)
  ;   :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
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
  ;   Same as the :indent property.
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
