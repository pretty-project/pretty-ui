
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.10.16
; Description:
; Version: v1.0.8
; Compatibility: x4.4.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.text-field
    (:require [mid-fruits.candy          :as candy :refer [param return]]
              [mid-fruits.string         :as string]
              [mid-fruits.vector         :as vector]
              [x.app-components.api      :as components]
              [x.app-core.api            :as a :refer [r]]
              [x.app-elements.engine.api :as engine]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- end-adornments-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {:emptiable? (boolean)(opt)
  ;   :end-adornments (maps in vector)(opt)
  ;   :resetable? (boolean)(opt)}
  ;
  ; @return (map)
  [field-id {:keys [emptiable? end-adornments resetable?]}]
  (cond-> end-adornments (boolean resetable?) (vector/conj-item (engine/reset-field-adornment-preset field-id))
                         (boolean emptiable?) (vector/conj-item (engine/empty-field-adornment-preset field-id))))

(defn- field-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (map)
  ;  {:autocomplete? (boolean)
  ;   :end-adornments (maps in vector)
  ;   :layout (keyword)
  ;   :name (keyword)
  ;   :type (keyword)}
  [field-id field-props]
  (merge {:layout     :row
          :type       :text
          :value-path (engine/default-value-path field-id)}
         (param field-props)
         {:end-adornments (end-adornments-prototype field-id field-props)
          ; BUG#6782
          ; A field mezők számára szükséges {:name ...} tulajdonságot a komponens React-fába
          ; csatolásakor szükséges generálni, mert az attributes térképben a felhasználás helyén
          ; generált érték az attributes térkép minden megváltozásakor feleslegesen újragenerálódna.
          :name (a/id)}))



;; -- Modifiers ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- field-props-modifier
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {:field-empty? (boolean)}
  ;
  ; @return (map)
  [_ {:keys [end-adornments field-empty?] :as field-props}]
                      ; XXX#8073
  (cond-> field-props (boolean field-empty?)
                      (update :end-adornments vector/remove-items-kv :preset :empty-field-adornment)
                      ; XXX#8073
                      (boolean field-empty?)
                      (update :end-adornments vector/remove-items-kv :preset :reset-field-adornment)))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-text-field-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (map)
  [db [_ field-id]]
  (merge (r engine/get-element-props db field-id)
         (r engine/get-field-props   db field-id)
         (r engine/get-surface-props db field-id)))

(a/reg-sub :elements/get-text-field-props get-text-field-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- text-field-surface
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {:surface (map)(opt)
  ;   :surface-visible? (boolean)(opt)}
  ;
  ; @return (hiccup)
  [field-id {:keys [surface surface-visible?]}]
  (if (and (some?   surface)
           (boolean surface-visible?))
      [:div.x-text-field--surface [components/content field-id surface]]))

(defn- text-field-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {:label (metamorphic-content)
  ;   :required? (boolean)(opt)}
  ;
  ; @return (hiccup)
  [_ {:keys [label required?]}]
  (if (some? label)
      [:div.x-text-field--label [components/content {:content label}]
                                (if required? [:span.x-input--label-asterisk "*"])]))

(defn- text-field-placeholder
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {:placeholder (metamorphic-content)}
  ;
  ; @return (hiccup)
  [field-id {:keys [placeholder] :as field-props}]
  (if (engine/field-props->render-field-placeholder? field-props)
      [:div.x-text-field--placeholder (engine/field-placeholder-attributes field-id field-props)
                                      [components/content {:content placeholder}]]))

(defn- text-field-input
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {:value (string)}
  ;
  ; @return (hiccup)
  [field-id {:keys [value] :as field-props}]
  [:input.x-text-field--input (engine/field-body-attributes field-id field-props)])

(defn- text-field-invalid-message
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {:invalid-message (metamorphic-content)(opt)}
  ;
  ; @return (hiccup)
  [_ {:keys [invalid-message]}]
  (if (some? invalid-message)
      [:div.x-text-field--invalid-message [components/content {:content invalid-message}]]))

(defn- text-field-input-container
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (hiccup)
  [field-id field-props]
  [:div.x-text-field--input-container
    [text-field-input                field-id field-props]
    [text-field-placeholder          field-id field-props]
    [text-field-surface              field-id field-props]
    [engine/element-start-adornments field-id field-props]
    [engine/element-end-adornments   field-id field-props]])

(defn- text-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (hiccup)
  [field-id field-props]
  ; XXX#8094
  ; A text-field komponensbe helyezett placeholder komponensre kattintva az input elem fókuszt kap,
  ; mert egy közös [:label] HTML elemben vannak. Másképpen a placeholder komponens {:on-mouse-up ...}
  ; eseményével kell fókuszt adni az inputnak.
  [:label.x-text-field (engine/element-attributes   field-id field-props)
                       [text-field-label            field-id field-props]
                       [text-field-input-container  field-id field-props]
                       [text-field-invalid-message  field-id field-props]
                       [engine/element-helper       field-id field-props]
                       [engine/element-info-tooltip field-id field-props]])

(defn element
  ; @param (keyword)(opt) field-id
  ; @param (map) field-props
  ;  {:auto-focus? (boolean)(constant)(opt)
  ;   :autocomplete? (boolean)(opt)
  ;    Default: false
  ;   :boder-color (keyword)(opt)
  ;    :default, :primary, :secondary
  ;    Default: :default
  ;   :class (keyword or keywords in vector)(opt)
  ;   :default-value (string)(constant)(opt)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :disabler (subscription vector)(opt)
  ;   :end-adornments (maps in vector)(opt)
  ;    [{:icon (keyword)
  ;      :icon-family (keyword)(opt)
  ;       :material-icons-filled, :material-icons-outlined
  ;       Default: :material-icons-filled
  ;      :on-click (metamorphic-event)(opt)
  ;      :tab-indexed? (boolean)(opt)
  ;       Default: true
  ;      :tooltip (metamorphic-content)(opt)}]
  ;   :emptiable? (boolean)(constant)(opt)
  ;    Default: false
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
  ;   :max-length (integer)(opt)
  ;   :min-width (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl, :none
  ;    Default: :none
  ;   :modifier (function)(opt)
  ;   :on-blur (metamorphic-event)(constant)(opt)
  ;   :on-change (metamorphic-event)(constant)(opt)
  ;   :on-empty (metamorphic-event)(constant)(opt)
  ;    Only w/ {:emptiable? true}
  ;   :on-focus (metamorphic-event)(constant)(opt)
  ;   :on-reset (metamorphic-event)(constant)(opt)
  ;    Only w/ {:resetable? true}
  ;   :on-type-ended (event-vector)(opt)
  ;    Az esemény-vektor utolsó paraméterként megkapja a mező aktuális értékét.
  ;   :placeholder (metamorphic-content)(opt)
  ;    Only w/o {:label ...}
  ;   :required? (boolean)(constant)(opt)
  ;    Default: false
  ;   :resetable? (boolean)(opt)
  ;    Default: false
  ;   :start-adornments (maps in vector)(opt)
  ;    [{:icon (keyword)
  ;      :icon-family (keyword)(opt)
  ;       :material-icons-filled, :material-icons-outlined
  ;       Default: :material-icons-filled
  ;      :on-click (metamorphic-event)
  ;      :tab-indexed? (boolean)(opt)
  ;       Default: true
  ;      :tooltip (metamorphic-content)}]
  ;   :stretch-orientation (keyword)(opt)
  ;    :horizontal, :none
  ;    Default: :none
  ;   :style (map)(opt)
  ;   :surface (map)(opt)
  ;    {:content (metamorphic-content)
  ;     :content-props (map)(opt)
  ;     :subscriber (subscription vector)(opt)}
  ;   :unemptiable? (boolean)(opt)
  ;    Default: false
  ;    TODO ...
  ;    A field on-blur esemény pillanatában, ha üres a value-path, akkor
  ;    az eltárolt backup-value értéket beállítja a value-path -re.
  ;   :validator (map)(constant)(opt)
  ;    {:f (function)
  ;     :invalid-message (metamorphic-content)(opt)
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
  ;  [elements/text-field {...}]
  ;
  ; @usage
  ;  [elements/text-field :my-text-field {...}]
  ;
  ; @usage
  ;  (defn valid? [value] (value-valid? value))
  ;  [elements/text-field {:validator {:f               valid?
  ;                                    :invalid-message "Invalid value"}}]
  ;
  ; @usage
  ;  (defn valid?              [value] (value-valid? value))
  ;  (defn get-invalid-message [value] "Invalid value")
  ;  [elements/text-field {:validator {:f                 valid?
  ;                                    :invalid-message-f get-invalid-message}}]
  ;
  ; @usage
  ;  (defn my-surface [field-id])
  ;  [elements/text-field {:surface {:content #'my-surface}}]
  ;
  ; @usage
  ;  (defn my-surface [field-id surface-props])
  ;  [elements/text-field {:surface {:content #'my-surface
  ;                                  :content-props {...}}}]
  ;
  ; @usage
  ; [elements/text-field {:modifier #(string/starts-with! % "/")}]
  ;
  ; @return (component)
  ([field-props]
   [element (a/id) field-props])

  ([field-id field-props]
   (let [field-props (field-props-prototype field-id field-props)]
        [engine/stated-element field-id
                               {:component     text-field
                                :element-props field-props
                                :modifier      field-props-modifier
                                :initializer   [:elements/init-field!          field-id]
                                :subscriber    [:elements/get-text-field-props field-id]}])))
