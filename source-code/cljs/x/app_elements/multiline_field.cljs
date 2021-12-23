
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.10.16
; Description:
; Version: v1.1.8
; Compatibility: x4.4.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.multiline-field
    (:require [mid-fruits.candy          :as candy :refer [param]]
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
  ;  {:layout (keyword)
  ;   :max-height (integer)
  ;   :min-height (integer)
  ;   :multiline? (boolean)
  ;   :status-animation? (boolean)}
  [field-id field-props]
  (merge {:layout     :row
          :max-height 32
          :min-height 1
          :value-path (engine/default-value-path field-id)}
         (param field-props)
         {:multiline? true
          ; XXX#6782
          :name (a/id)}))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- multiline-field-textarea
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (hiccup)
  [field-id field-props]
  [:textarea.x-text-field--textarea (engine/field-body-attributes field-id field-props)])

(defn- multiline-field-textarea-container
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (hiccup)
  [field-id field-props]
  [:div.x-text-field--input-container [multiline-field-textarea          field-id field-props]
                                      [text-field/text-field-placeholder field-id field-props]])

(defn- multiline-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (hiccup)
  [field-id field-props]
  [:label.x-text-field (engine/element-attributes             field-id field-props)
                       [text-field/text-field-label           field-id field-props]
                       [multiline-field-textarea-container    field-id field-props]
                       [text-field/text-field-invalid-message field-id field-props]
                       [engine/element-helper                 field-id field-props]
                       [engine/element-info-tooltip           field-id field-props]])

(defn element
  ; @param (keyword)(opt) field-id
  ; @param (map) initial-props
  ;  {:auto-focus? (boolean)(constant)(opt)
  ;   :border-color (keyword)(opt)
  ;    :default, :primary, :secondary,
  ;    Default: :default
  ;   :class (keyword or keywords in vector)(opt)
  ;   :default-value (string)(constant)(opt)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :disabler (subscription vector)(opt)
  ;   :form-id (keyword)(opt)
  ;   :helper (metamorphic-content)(opt)
  ;   :indent (keyword)(opt)
  ;    :left, :right, :both, :none
  ;    Default: :none
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
  ;    Default: :none
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
   [element (a/id) field-props])

  ([field-id field-props]
   (let [field-props (a/prot field-id field-props field-props-prototype)]
        [engine/stated-element field-id
                               {:component     #'multiline-field
                                :element-props field-props
                                :initializer   [:elements/init-field!          field-id]
                                :subscriber    [:elements/get-text-field-props field-id]}])))
