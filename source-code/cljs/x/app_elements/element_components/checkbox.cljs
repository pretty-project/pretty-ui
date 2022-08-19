

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.element-components.checkbox
    (:require [mid-fruits.candy          :refer [param]]
              [x.app-components.api      :as components]
              [x.app-core.api            :as a :refer [r]]
              [x.app-elements.engine.api :as engine]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- checkbox-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ;
  ; @return (map)
  ;  {:border-color (keyword or string)
  ;   :font-size (keyword)
  ;   :layout (keyword)}
  [checkbox-id checkbox-props]
  (merge {:border-color :primary
          :font-size    :s
          :layout       :row
          :value-path   (engine/default-value-path checkbox-id)}
         (param checkbox-props)))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-checkbox-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) checkbox-id
  [db [_ checkbox-id]]
  (merge (r engine/get-element-props   db checkbox-id)
         (r engine/get-checkable-props db checkbox-id)))

(a/reg-sub :elements/get-checkbox-props get-checkbox-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- checkbox-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ;  {:label (metamorphic-content)(opt)
  ;   :required? (boolean)(opt)}
  [_ {:keys [label required?]}]
  (if label [:div.x-checkbox--label [components/content label]
                                    (if required? [:span.x-input--label-asterisk "*"])]))

(defn- checkbox-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  [checkbox-id checkbox-props]
  [:button.x-checkbox--body (engine/checkable-body-attributes checkbox-id checkbox-props)
                            [:div.x-checkbox--button]
                            [checkbox-label checkbox-id checkbox-props]])

(defn- checkbox
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  [checkbox-id checkbox-props]
  [:div.x-checkbox (engine/checkable-attributes checkbox-id checkbox-props)
                   [checkbox-body               checkbox-id checkbox-props]
                   [engine/element-helper       checkbox-id checkbox-props]])

(defn element
  ; @param (keyword)(opt) checkbox-id
  ; @param (map) checkbox-props
  ;  {:border-color (keyword or string)(opt)
  ;    :default, :muted, :primary, :secondary, :success, :warning
  ;    Default: :primary
  ;   :default-value (boolean)(constant)(opt)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :font-size (keyword)(opt)
  ;    :xs, :s
  ;    Default: :s
  ;   :form-id (keyword)(opt)
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
  ;   :initial-value (boolean)(constant)(opt)
  ;   :on-check (metamorphic-event)(opt)
  ;   :on-uncheck (metamorphic-event)(opt)
  ;   :label (metamorphic-content)(opt)
  ;   :layout (keyword)(opt)
  ;    :fit, :row
  ;    Default: :row
  ;   :required? (boolean)(constant)(opt)
  ;    Default: false
  ;   :style (map)(opt)
  ;   :value-path (vector)(constant)(opt)}
  ;
  ; @usage
  ;  [elements/checkbox {...}]
  ;
  ; @usage
  ;  [elements/checkbox :my-checkbox {...}]
  ([checkbox-props]
   [element (a/id) checkbox-props])

  ([checkbox-id checkbox-props]
   (let [checkbox-props (checkbox-props-prototype checkbox-id checkbox-props)]
        [engine/stated-element checkbox-id
                               {:render-f      #'checkbox
                                :element-props checkbox-props
                                :initializer   [:elements/init-input!        checkbox-id]
                                :subscriber    [:elements/get-checkbox-props checkbox-id]}])))
