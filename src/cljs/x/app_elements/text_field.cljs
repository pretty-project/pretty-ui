
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.10.16
; Description:
; Version: v0.9.4
; Compatibility: x4.1.7



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.text-field
    (:require [app-fruits.dom            :as dom]
              [mid-fruits.candy          :as candy :refer [param return]]
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
  (cond-> (param end-adornments)
          (boolean resetable?)
          (vector/conj-item (engine/reset-field-adornment-preset field-id))
          (boolean emptiable?)
          (vector/conj-item (engine/empty-field-adornment-preset field-id))))

(defn- field-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {:stretch-orientation (keyword)(opt)}
  ;
  ; @return (map)
  ;  {:color (keyword)
  ;   :end-adornments (maps in vector)
  ;   :layout (keyword)
  ;   :min-width (keyword)
  ;   :type (keyword)}
  [field-id {:keys [stretch-orientation] :as field-props}]
  (merge {:color      :default
          :layout     :row
          :min-width  :s
          :type       :text
          :value-path (engine/default-value-path field-id)

          ; A stretch-orientation tulajdonságot szükséges az element-container komponens
          ; számára is átadni, hogy alkalmazkodni tudjon a környezethez az elem.
          :container-stretch-orientation stretch-orientation}

         (param field-props)
         {:end-adornments (end-adornments-prototype field-id field-props)}))



;; -- Modifiers ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-props-modifier
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) view-props
  ;  {:field-empty? (boolean)}
  ;
  ; @return (map)
  [_ {:keys [end-adornments field-empty?] :as view-props}]
  (cond-> (param view-props)
          ; XXX#8073
          (boolean field-empty?)
          (update :end-adornments vector/remove-items-kv :preset :empty-field-adornment)
          ; XXX#8073
          (boolean field-empty?)
          (update :end-adornments vector/remove-items-kv :preset :reset-field-adornment)))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (map)
  [db [_ field-id]]
  (merge (r engine/get-element-view-props db field-id)
         (r engine/get-field-view-props   db field-id)
         (r engine/get-surface-view-props db field-id)))

(a/reg-sub ::get-view-props get-view-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- text-field-surface
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) view-props
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
  ; @param (map) view-props
  ;  {:label (metamorphic-content)
  ;   :required? (boolean)(opt)}
  ;
  ; @return (hiccup)
  [field-id {:keys [label required?]}]
  (if (some? label)
      [:div.x-text-field--label [components/content {:content label}]
                                (if required? "*")]))

(defn- text-field-placeholder
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) view-props
  ;  {:placeholder (metamorphic-content)}
  ;
  ; @return (hiccup)
  [field-id {:keys [placeholder] :as view-props}]
  (if (engine/view-props->render-field-placeholder? view-props)
      [:div.x-text-field--placeholder
        (engine/field-placeholder-attributes field-id view-props)
        [components/content {:content placeholder}]]))

(defn- text-field-input
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) view-props
  ;  {:value (string)}
  ;
  ; @return (hiccup)
  [field-id {:keys [value] :as view-props}]
  [:input.x-text-field--input
    (engine/field-body-attributes field-id view-props)])

(defn- text-field-invalid-message
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) view-props
  ;  {:invalid-message (metamorphic-content)(opt)}
  ;
  ; @return (hiccup)
  [field-id {:keys [invalid-message]}]
  (if (some? invalid-message)
      [:div.x-text-field--invalid-message
        [components/content {:content invalid-message}]]))

(defn- text-field-input-container
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) view-props
  ;
  ; @return (hiccup)
  [field-id view-props]
  [:div.x-text-field--input-container
    [text-field-input                field-id view-props]
    [text-field-placeholder          field-id view-props]
    [text-field-surface              field-id view-props]
    [engine/element-start-adornments field-id view-props]
    [engine/element-end-adornments   field-id view-props]])

(defn- text-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) view-props
  ;
  ; @return (hiccup)
  [field-id view-props]
  [:label.x-text-field
    (engine/element-attributes  field-id view-props)
    [text-field-label           field-id view-props]
    [text-field-input-container field-id view-props]
    [text-field-invalid-message field-id view-props]])

(defn view
  ; @param (keyword)(opt) field-id
  ; @param (map) field-props
  ;  {:auto-focus? (boolean)(constant)(opt)
  ;   :class (string or vector)(opt)
  ;   :color (keyword)(opt)
  ;    :primary, :secondary, :default
  ;    Default: :default
  ;   :default-value (string)(constant)(opt)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :end-adornments (maps in vector)(opt)
  ;    [{:icon (keyword) Material icon class
  ;      :on-click (metamorphic-event)(opt)
  ;      :tooltip (metamorphic-content)(opt)}]
  ;   :emptiable? (boolean)(constant)(opt)
  ;    Default: false
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
  ;   :max-length (integer)(opt)
  ;   :min-width (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl, :none
  ;    Default: :s
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
  ;    [{:icon (keyword) Material icon class
  ;      :on-click (metamorphic-event)
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
   [view nil field-props])

  ([field-id field-props]
   (let [field-id    (a/id   field-id)
         field-props (a/prot field-id field-props field-props-prototype)]
        [engine/container field-id
          {:base-props  field-props
           :component   text-field
           :modifier    view-props-modifier
           :initializer [:x.app-elements/init-input! field-id]
           :subscriber  [::get-view-props            field-id]}])))
