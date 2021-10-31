
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.10.16
; Description:
; Version: v1.1.4
; Compatibility: x4.4.3



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.multiline-field
    (:require [mid-fruits.candy          :as candy :refer [param]]
              [mid-fruits.string         :as string]
              [x.app-core.api            :as a :refer [r]]
              [x.app-elements.engine.api :as engine]
              [x.app-elements.text-field :as text-field]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- field-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (map)
  ;  {:color (keyword)
  ;   :layout (keyword)
  ;   :max-height (integer)
  ;   :min-height (integer)
  ;   :min-width (keyword)
  ;   :multiline? (boolean)
  ;   :status-animation? (boolean)}
  [field-id field-props]
  (merge {:color      :default
          :layout     :row
          :max-height 32
          :min-height 1
          :min-width  :s
          :value-path (engine/default-value-path field-id)}
         (param field-props)
         {:multiline? true}))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-sub ::get-view-props text-field/get-view-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- multiline-field-textarea
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) view-props
  ;
  ; @return (hiccup)
  [field-id view-props]
  [:textarea.x-text-field--textarea
    (engine/field-body-attributes field-id view-props)])

(defn- multiline-field-textarea-container
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) view-props
  ;
  ; @return (hiccup)
  [field-id view-props]
  [:div.x-text-field--input-container
    [multiline-field-textarea          field-id view-props]
    [text-field/text-field-placeholder field-id view-props]])

(defn- multiline-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) view-props
  ;
  ; @return (hiccup)
  [field-id view-props]
  [:label.x-text-field
     (engine/element-attributes field-id view-props)
     [text-field/text-field-label           field-id view-props]
     [multiline-field-textarea-container    field-id view-props]
     [text-field/text-field-invalid-message field-id view-props]
     [engine/element-helper                 field-id view-props]
     [engine/element-info-tooltip           field-id view-props]])

(defn view
  ; @param (keyword)(opt) field-id
  ; @param (map) initial-props
  ;  {:auto-focus? (boolean)(constant)(opt)
  ;   :color (keyword)(opt)
  ;    :primary, :secondary, :default
  ;    Default: :default
  ;   :class (string or vector)(opt)
  ;   :default-value (string)(constant)(opt)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :disabler (subscription vector)(opt)
  ;   :form-id (keyword)(opt)
  ;   :helper (metamorphic-content)(opt)
  ;   :highlighted? (boolean)(opt)
  ;    Default: false
  ;   :info-tooltip (metamorphic-content)(opt)
  ;   :initial-value (string)(constant)(opt)
  ;   :label (metamorphic-content)(opt)
  ;    Only w/o {:placeholder ...}
  ;   :layout (keyword)(opt)
  ;    :fit, :row
  ;    Default: :row
  ;   :listen-to-change? (boolean)(constant)(opt)
  ;    XXX#4880
  ;    Default: false
  ;   :max-height (integer)(opt)
  ;    TODO ...
  ;    Max lines count
  ;    Default: 32
  ;   :max-length (integer)(opt)
  ;   :min-height (integer)(opt)
  ;    TODO ...
  ;    Min lines count
  ;    Default: 1
  ;   :min-width (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl, :none
  ;    Default: :s
  ;   :on-blur (metamorphic-event)(constant)(opt)
  ;   :on-change (metamorphic-event)(constant)(opt)
  ;   :on-enter (metamorphic-event)(constant)(opt)
  ;   :on-focus (metamorphic-event)(constant)(opt)
  ;   :on-type-ended (event-vector)(opt)
  ;    Az esemény-vektor utolsó paraméterként megkapja a mező aktuális értékét.
  ;   :placeholder (metamorphic-content)(opt)
  ;    Only w/o {:label ...}
  ;   :required? (boolean)(constant)(opt)
  ;    Default: false
  ;   :style (map)(opt)
  ;   :unemptiable? (boolean)(opt)
  ;    Default: false
  ;   :validator (map)(constant)(opt)
  ;    {:f (function)
  ;     :invalid-message (metamorphic-content)
  ;      Only w/o {:validator {:invalid-message-f ...}}
  ;     :invalid-message-f (function)(opt)
  ;      Only w/o {:validator {:invalid-message ...}}
  ;     :pre-validate? (boolean)(opt)
  ;      A mező kitöltése közben validálja annak értékét, még mielőtt a mező
  ;      {:visited? true} állapotba lépne.
  ;      Default: false}
  ;   :value-path (item-path vector)(constant)(opt)}
  ;
  ; @usage
  ;  [elements/multiline-field {...}]
  ;
  ; @usage
  ;  [elements/multiline-field :my-multiline-field {...}]
  ;
  ; @usage
  ;  (defn valid? [value] (value-valid? value))
  ;  [elements/multiline-field {:validator {:f               valid?
  ;                                         :inavlid-message "Invalid value"}}]
  ;
  ; @usage
  ;  (defn valid?              [value] (value-valid? value))
  ;  (defn get-invalid-message [value] "Invalid value")
  ;  [elements/multiline-field {:validator {:f                 valid?
  ;                                         :inavlid-message-f get-invalid-message}}]
  ;
  ; @return (component)
  ([field-props]
   [view nil field-props])

  ([field-id field-props]
   (let [field-id    (a/id   field-id)
         field-props (a/prot field-id field-props field-props-prototype)]
        [engine/stated-element field-id
          {:component     #'multiline-field
           :element-props field-props
           :initializer   [:x.app-elements/init-input! field-id]
           :subscriber    [::get-view-props            field-id]}])))
