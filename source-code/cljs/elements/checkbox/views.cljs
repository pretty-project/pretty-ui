
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.checkbox.views
    (:require [elements.checkbox.helpers    :as checkbox.helpers]
              [elements.checkbox.prototypes :as checkbox.prototypes]
              [elements.input.helpers       :as input.helpers]
              [elements.label.views         :as label.views]
              [plugins.reagent.api          :as reagent]
              [random.api                   :as random]
              [x.components.api             :as x.components]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- checkbox-option-helper
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ;  {:option-helper-f (function)}
  ; @param (*) option
  [_ {:keys [option-helper-f]} option]
  (if option-helper-f (let [option-helper (option-helper-f option)]
                           [:div.e-checkbox--option-helper (x.components/content option-helper)])))

(defn- checkbox-option-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ;  {:option-label-f (function)}
  ; @param (*) option
  [_ {:keys [option-label-f]} option]
  (let [option-label (option-label-f option)]
       [:div.e-checkbox--option-label (x.components/content option-label)]))

(defn- checkbox-option-content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ; @param (*) option
  [checkbox-id checkbox-props option]
  [:div.e-checkbox--option-content [checkbox-option-label  checkbox-id checkbox-props option]
                                   [checkbox-option-helper checkbox-id checkbox-props option]])

(defn- checkbox-option
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ; @param (*) option
  [checkbox-id checkbox-props option]
  [:button.e-checkbox--option (checkbox.helpers/checkbox-option-attributes checkbox-id checkbox-props option)
                              [:div.e-checkbox--option-button {:data-icon-family :material-icons-filled}]
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
  ;  {}
  [checkbox-id {:keys [style] :as checkbox-props}]
  [:div.e-checkbox--body (checkbox.helpers/checkbox-body-attributes checkbox-id checkbox-props)
                         [checkbox-options                          checkbox-id checkbox-props]])

(defn- checkbox-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ;  {}
  [_ {:keys [helper info-text label required?]}]
  (if label [label.views/element {:content     label
                                  :helper      helper
                                  :info-text   info-text
                                  :line-height :block
                                  :required?   required?}]))

(defn- checkbox-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  [checkbox-id checkbox-props]
  [:div.e-checkbox (checkbox.helpers/checkbox-attributes checkbox-id checkbox-props)
                   [checkbox-label                       checkbox-id checkbox-props]
                   [checkbox-body                        checkbox-id checkbox-props]])

(defn- checkbox
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  [checkbox-id checkbox-props]
  (reagent/lifecycles {:component-did-mount (checkbox.helpers/checkbox-did-mount-f checkbox-id checkbox-props)
                       :reagent-render      (fn [_ checkbox-props] [checkbox-structure checkbox-id checkbox-props])}))

(defn element
  ; @param (keyword)(opt) checkbox-id
  ; @param (map) checkbox-props
  ;  {:border-color (keyword or string)(opt)
  ;    :default, :muted, :primary, :secondary, :success, :warning
  ;    Default: :primary
  ;   :class (keyword or keywords in vector)(opt)
  ;   :default-value (boolean)(opt)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :font-size (keyword)(opt)
  ;    :xs, :s, :inherit
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
  ;   :initial-options (vector)(opt)
  ;   :initial-value (boolean)(opt)
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
  ;   :label (metamorphic-content)(opt)
  ;   :required? (boolean or keyword)(opt)
  ;    true, false, :unmarked
  ;    Default: false
  ;   :style (map)(opt)
  ;   :value-path (vector)(opt)}
  ;
  ; @usage
  ;  [checkbox {...}]
  ;
  ; @usage
  ;  [checkbox :my-checkbox {...}]
  ([checkbox-props]
   [element (random/generate-keyword) checkbox-props])

  ([checkbox-id checkbox-props]
   (let [checkbox-props (checkbox.prototypes/checkbox-props-prototype checkbox-id checkbox-props)]
        [checkbox checkbox-id checkbox-props])))
