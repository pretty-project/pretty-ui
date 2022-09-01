
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.text-field.views
    (:require [mid-fruits.candy                      :as candy :refer [param return]]
              [mid-fruits.string                     :as string]
              [mid-fruits.vector                     :as vector]
              [reagent.api :as reagent]
              [x.app-components.api                  :as components]
              [x.app-core.api                        :as a :refer [r]]
              [x.app-elements.surface-handler.subs   :as surface-handler.subs]
              [x.app-elements.engine.api             :as engine]
              [x.app-elements.input.views            :as input.views]
              [x.app-elements.target-handler.helpers :as target-handler.helpers]
              [x.app-elements.text-field.helpers     :as text-field.helpers]
              [x.app-elements.text-field.prototypes  :as text-field.prototypes]

              ; TEMP
              [x.app-elements.text-field.state :as text-field.state]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-adornment
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; @param (map) adornment-props
  ;  {:disabled? (boolean)(opt)
  ;    Default: false
  ;   :icon (keyword)(opt)
  ;   :icon-family (keyword)(opt)
  ;    :material-icons-filled, :material-icons-outlined
  ;    Default: :material-icons-filled
  ;   :label (string)(opt)
  ;   :on-click (metamorphic-event)
  ;   :tab-indexed? (boolean)(opt)
  ;    False érték esetén az adornment gomb nem indexelődik tabolható elemként.
  ;    Default: true
  ;   :tooltip (metamorphic-content)(opt)}
  [field-id field-props {:keys [icon icon-family label] :as adornment-props}]
  (let [adornment-attributes (text-field.helpers/button-adornment-attributes field-id field-props adornment-props)]
       (cond icon  [:button.x-field-adornments--button-adornment adornment-attributes icon]
             label [:button.x-field-adornments--button-adornment adornment-attributes label])))

(defn static-adornment
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; @param (map) adornment-props
  ;  {:icon (keyword)(opt)
  ;   :icon-family (keyword)(opt)
  ;    :material-icons-filled, :material-icons-outlined
  ;    Default: :material-icons-filled
  ;   :label (string)(opt)}
  [_ _ {:keys [icon icon-family label]}]
  (cond icon  [:div.x-field-adornments--static-adornment {:data-icon-family icon-family} icon]
        label [:div.x-field-adornments--static-adornment label]))

(defn field-adornment
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; @param (map) adornment-props
  ;  {:on-click (metamorphic-event)(opt)}
  [field-id field-props {:keys [on-click] :as adornment-props}]
  (if on-click [button-adornment field-id field-props adornment-props]
               [static-adornment field-id field-props adornment-props]))

(defn field-end-adornments
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {:end-adornments (maps in vector)(opt)}
  [field-id {:keys [end-adornments] :as field-props}]
  (if (vector/nonempty? end-adornments)
      (letfn [(f [adornments adornment-props]
                 (let [adornment-props (text-field.prototypes/adornment-props-prototype adornment-props)]
                      (conj adornments [field-adornment field-id field-props adornment-props])))]
             (reduce f [:div.x-field-adornments] end-adornments))
      (let [placeholder-attributes (text-field.helpers/adornment-placeholder-attributes field-id field-props)]
           [:div.x-field-adornments--placeholder placeholder-attributes])))

(defn field-start-adornments
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {:start-adornments (maps in vector)(opt)}
  [field-id {:keys [start-adornments] :as field-props}]
  (if (vector/nonempty? start-adornments)
      (letfn [(f [adornments adornment-props]
                 (let [adornment-props (text-field.prototypes/adornment-props-prototype adornment-props)]
                      (conj adornments [field-adornment field-id field-props adornment-props])))]
             (reduce f [:div.x-field-adornments] start-adornments))
      (let [placeholder-attributes (text-field.helpers/adornment-placeholder-attributes field-id field-props)]
           [:div.x-field-adornments--placeholder placeholder-attributes])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- text-field-surface
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {:surface (metamorphic-content)(opt)}
  [field-id {:keys [surface]}]
  (if surface (if-let [surface-visible? @(a/subscribe [:elements.text-field/surface-visible? field-id])]
                      [:div.x-text-field--surface {:on-mouse-down #(.preventDefault %)}
                                                  [components/content field-id surface]])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- text-field-placeholder
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {:placeholder (metamorphic-content)}
  [field-id {:keys [placeholder] :as field-props}]
  ; - A placeholder elem {:on-mouse-down #(focus-element! ...)} eseménye nem adta át a fókuszt
  ;   az input elem számára, ezért a placeholder elem az input elem alatt kell, hogy megjelenjen
  ;   Google Chrome 98.0.4758.80
  ;
  ; - Az input elemek az on-mouse-down esemény hatására kapnak fókuszt
  (if placeholder (let [field-value (text-field.helpers/get-field-value field-id)]
                       (if (empty? field-value)
                           [:div.x-text-field--placeholder (components/content placeholder)]))))

(defn- text-field-input
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  [:input.x-text-field--input (text-field.helpers/field-body-attributes field-id field-props)])

(defn- text-field-input-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  ; - XXX#3415
  ;   Az .x-text-field--input-structure elem tartalmazza az input elemet és az abszolút pozícionálású
  ;   placeholder elemet, amely elhelyezéséhez szükséges, hogy közös elemben legyen az input elemmel.
  ;
  ; - BUG#3418
  ;   A DOM-fában az .x-text-field--input (input) elem ELŐTT elhelyezett .x-text-field--placeholder (div)
  ;   elem valamiért az .x-text-field--input elem FELETT jelent meg ezért az .x-text-field--input elem
  ;   az .x-text-field--input-emphasize (div) elembe került, így a placeholder elem az input elem alatt jelenik meg.
  ;   Google Chrome 98.0.4758.80
  [:div.x-text-field--input-structure                                     [text-field-placeholder field-id field-props]
                                      [:div.x-text-field--input-emphasize [text-field-input       field-id field-props]]])

(defn- text-field-input-container
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  [:div.x-text-field--input-container [field-start-adornments field-id field-props]
                                      [text-field-input-structure                     field-id field-props]
                                      [field-end-adornments   field-id field-props]
                                      [text-field-surface                             field-id field-props]])




;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- text-field-required-warning
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  (if-let [required-warning? @(a/subscribe [:elements.input/required-warning? field-id field-props])]
          [:div.x-text-field--warning {:data-selectable false}
                                      (components/content :please-fill-out-this-field)]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- text-field-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  [:div.x-text-field (text-field.helpers/field-attributes field-id field-props)
                     [engine/element-header               field-id field-props]
                     [text-field-input-container          field-id field-props]
                     [text-field-required-warning         field-id field-props]

                     ; TODO
                     [input.views/invalid-message        field-id field-props]])

(defn- text-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {}
  [field-id {:keys [auto-focus? autoclear? initial-value] :as field-props}]
  (reagent/lifecycles {:component-did-mount    (fn [] (if (or auto-focus? initial-value)
                                                          (a/dispatch [:elements.text-field/init-field! field-id field-props])))
                       :component-will-unmount (fn [] (if autoclear? (a/dispatch [:elements.text-field/clear-field! field-id field-props])))
                       :reagent-render         (fn [_ field-props] [text-field-structure field-id field-props])}))

(defn element
  ; @param (keyword)(opt) field-id
  ; @param (map) field-props
  ;  {:autoclear? (boolean)(opt)
  ;    Default: false
  ;   :autofill? (boolean)(opt)
  ;    Default: false
  ;   :auto-focus? (boolean)(constant)(opt)
  ;   :border-color (keyword or string)(opt)
  ;    :default, :primary, :secondary
  ;    Default: :default
  ;   :class (keyword or keywords in vector)(opt)
  ;   :default-value (string)(constant)(opt)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :end-adornments (maps in vector)(opt)
  ;    [{:disabled? (boolean)(opt)
  ;       Default: false
  ;      :icon (keyword)
  ;      :icon-family (keyword)(opt)
  ;       :material-icons-filled, :material-icons-outlined
  ;       Default: :material-icons-filled
  ;      :label (string)(opt)
  ;      :on-click (metamorphic-event)(opt)
  ;      :tab-indexed? (boolean)(opt)
  ;       Default: true
  ;      :tooltip (metamorphic-content)(opt)}]
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
  ;   :info-text (metamorphic-content)(opt)
  ;   :initial-value (string)(constant)(opt)
  ;   :label (metamorphic-content)(opt)
  ;    Only w/o {:placeholder ...}
  ;   :max-length (integer)(opt)
  ;   :min-width (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl, :none
  ;    Default: :none
  ;   :modifier (function)(opt)
  ;   :name (keyword)(opt)
  ;   :on-blur (metamorphic-event)(constant)(opt)
  ;   :on-change (metamorphic-event)(constant)(opt)
  ;    Az esemény-vektor utolsó paraméterként megkapja a mező aktuális értékét.
  ;   :on-empty (metamorphic-event)(constant)(opt)
  ;   :on-focus (metamorphic-event)(constant)(opt)
  ;   :placeholder (metamorphic-content)(opt)
  ;    Only w/o {:label ...}
  ;   :required? (boolean or keyword)(constant)(opt)
  ;    true, false, :unmarked
  ;    Default: false
  ;   :start-adornments (maps in vector)(opt)
  ;    [{:disabled? (boolean)(opt)
  ;       Default: false
  ;      :icon (keyword)(opt)
  ;      :icon-family (keyword)(opt)
  ;       :material-icons-filled, :material-icons-outlined
  ;       Default: :material-icons-filled
  ;      :label (string)(opt)
  ;      :on-click (metamorphic-event)
  ;      :tab-indexed? (boolean)(opt)
  ;       Default: true
  ;      :tooltip (metamorphic-content)}]
  ;   :stretch-orientation (keyword)(opt)
  ;    :horizontal, :none
  ;    Default: :none
  ;   :style (map)(opt)
  ;   :surface (metamorphic-content)(opt)
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
  ;   :value-path (vector)(opt)}
  ;
  ; @usage
  ;  [elements/text-field {...}]
  ;
  ; @usage
  ;  [elements/text-field :my-text-field {...}]
  ;
  ; @usage
  ;  [elements/text-field {:validator {:f               #(not (empty? %))
  ;                                    :invalid-message "Invalid value"}}]
  ;
  ; @usage
  ;  (defn get-invalid-message [value] "Invalid value")
  ;  [elements/text-field {:validator {:f                 #(not (empty? %))
  ;                                    :invalid-message-f get-invalid-message}}]
  ;
  ; @usage
  ;  (defn my-surface [field-id])
  ;  [elements/text-field {:surface {:content #'my-surface}}]
  ;
  ; @usage
  ;  (defn my-surface [field-id surface-props])
  ;  [elements/text-field {:surface #'my-surface}]
  ;
  ; @usage
  ; [elements/text-field {:modifier #(string/starts-with! % "/")}]
  ([field-props]
   [element (a/id) field-props])

  ([field-id field-props]
   (let [field-props (text-field.prototypes/field-props-prototype field-id field-props)]
        [text-field field-id field-props])))
