
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.checkbox.views
    (:require [reagent.api                        :as reagent]
              [x.app-components.api               :as components]
              [x.app-core.api                     :as a]
              [x.app-elements.checkbox.helpers    :as checkbox.helpers]
              [x.app-elements.checkbox.prototypes :as checkbox.prototypes]
              [x.app-elements.engine.api          :as engine]
              [x.app-elements.input.helpers       :as input.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- checkbox-option
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ;  {:option-label-f (function)}
  ; @param (*) option
  [checkbox-id {:keys [option-label-f] :as checkbox-props} option]
  (let [option-label (option-label-f option)]
       [:button.x-checkbox--option (checkbox.helpers/checkbox-option-attributes checkbox-id checkbox-props option)
                                   [:div.x-checkbox--option-button {:data-icon-family :material-icons-filled}]
                                   [:div.x-checkbox--option-label (components/content option-label)]]))

(defn- checkbox-options
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  [checkbox-id checkbox-props]
  (letfn [(f [option-list option] (conj option-list [checkbox-option checkbox-id checkbox-props option]))]
         (let [options (input.helpers/get-input-options checkbox-id checkbox-props)]
              (reduce f [:div.x-checkbox--options] options))))

(defn- checkbox-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  [checkbox-id checkbox-props]
  [:div.x-checkbox (checkbox.helpers/checkbox-attributes checkbox-id checkbox-props)
                   [engine/element-header                checkbox-id checkbox-props]
                   [checkbox-options                     checkbox-id checkbox-props]])

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
  ;    :xs, :s
  ;    Default: :s
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
  ;   :initial-options (vector)(opt)
  ;   :initial-value (boolean)(opt)
  ;   :on-check (metamorphic-event)(opt)
  ;   :on-uncheck (metamorphic-event)(opt)
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
  ;  [elements/checkbox {...}]
  ;
  ; @usage
  ;  [elements/checkbox :my-checkbox {...}]
  ([checkbox-props]
   [element (a/id) checkbox-props])

  ([checkbox-id checkbox-props]
   (let [checkbox-props (checkbox.prototypes/checkbox-props-prototype checkbox-id checkbox-props)]
        [checkbox checkbox-id checkbox-props])))