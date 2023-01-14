
(ns elements.checkbox.views
    (:require [elements.checkbox.helpers    :as checkbox.helpers]
              [elements.checkbox.prototypes :as checkbox.prototypes]
              [elements.element.views       :as element.views]
              [elements.input.helpers       :as input.helpers]
              [random.api                   :as random]
              [re-frame.api                 :as r]
              [reagent.api                  :as reagent]
              [x.components.api             :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- checkbox-option-helper
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ; {:option-helper-f (function)}
  ; @param (*) option
  [_ {:keys [option-helper-f]} option]
  (if option-helper-f (let [option-helper (option-helper-f option)]
                           [:div.e-checkbox--option-helper {:data-font-size   :xs
                                                            :data-line-height :native}
                                                           (x.components/content option-helper)])))

(defn- checkbox-option-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ; {:option-label-f (function)}
  ; @param (*) option
  [_ {:keys [option-label-f]} option]
  (let [option-label (option-label-f option)]
       [:div.e-checkbox--option-label {:data-font-size   :s
                                       :data-font-weight :medium
                                       :data-line-height :text-block}
                                      (x.components/content option-label)]))

(defn- checkbox-option-content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ; @param (*) option
  [checkbox-id checkbox-props option]
  [:div.e-checkbox--option-content {:data-click-target :opacity}
                                   [checkbox-option-label  checkbox-id checkbox-props option]
                                   [checkbox-option-helper checkbox-id checkbox-props option]])

(defn- checkbox-option
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ; @param (*) option
  [checkbox-id checkbox-props option]
  [:button.e-checkbox--option (checkbox.helpers/checkbox-option-attributes checkbox-id checkbox-props option)
                              [:div.e-checkbox--option-button (checkbox.helpers/checkbox-option-button-attributes checkbox-id checkbox-props option)]
                              [checkbox-option-content checkbox-id checkbox-props option]])

(defn- checkbox-options
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  [checkbox-id checkbox-props]
  (letfn [(f [option-list option] (conj option-list [checkbox-option checkbox-id checkbox-props option]))]
         (let [options (input.helpers/get-input-options checkbox-id checkbox-props)]
              (reduce f [:<>] options))))

(defn- checkbox-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ; {}
  [checkbox-id {:keys [style] :as checkbox-props}]
  [:div.e-checkbox--body (checkbox.helpers/checkbox-body-attributes checkbox-id checkbox-props)
                         [checkbox-options                          checkbox-id checkbox-props]])

(defn- checkbox-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  [checkbox-id checkbox-props]
  [:div.e-checkbox (checkbox.helpers/checkbox-attributes checkbox-id checkbox-props)
                   [element.views/element-label          checkbox-id checkbox-props]
                   [checkbox-body                        checkbox-id checkbox-props]])

(defn- checkbox
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  [checkbox-id checkbox-props]
  (reagent/lifecycles {:component-did-mount (fn [_ _] (r/dispatch [:elements.checkbox/checkbox-did-mount checkbox-id checkbox-props]))
                       :reagent-render      (fn [_ checkbox-props] [checkbox-structure checkbox-id checkbox-props])}))

(defn element
  ; @param (keyword)(opt) checkbox-id
  ; @param (map) checkbox-props
  ; {:border-color (keyword or string)(opt)
  ;   :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;   Default: :default
  ;  :border-radius (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;   Default: :xs
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
  ;  :initial-options (vector)(opt)
  ;  :initial-value (boolean)(opt)
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
  ;  :label (metamorphic-content)(opt)
  ;  :outdent (map)(opt)
  ;   Same as the :indent property.
  ;  :style (map)(opt)
  ;  :value-path (vector)(opt)}
  ;
  ; @usage
  ; [checkbox {...}]
  ;
  ; @usage
  ; [checkbox :my-checkbox {...}]
  ([checkbox-props]
   [element (random/generate-keyword) checkbox-props])

  ([checkbox-id checkbox-props]
   (let [checkbox-props (checkbox.prototypes/checkbox-props-prototype checkbox-id checkbox-props)]
        [checkbox checkbox-id checkbox-props])))
