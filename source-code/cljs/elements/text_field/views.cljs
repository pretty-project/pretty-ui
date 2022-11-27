
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.text-field.views
    (:require [elements.label.views           :as label.views]
              [elements.text-field.helpers    :as text-field.helpers]
              [elements.text-field.prototypes :as text-field.prototypes]
              [hiccup.api                     :as hiccup]
              [plugins.reagent.api            :as reagent]
              [random.api                     :as random]
              [re-frame.api                   :as r]
              [string.api                     :as string]
              [vector.api                     :as vector]
              [x.components.api               :as x.components]))



;; -- Field adornments components ---------------------------------------------
;; ----------------------------------------------------------------------------

(defn- field-adornments-placeholder
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  (let [placeholder-attributes (text-field.helpers/adornment-placeholder-attributes field-id field-props)]
       [:div.e-text-field--adornments-placeholder placeholder-attributes]))

(defn button-adornment
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; @param (map) adornment-props
  ;  {:icon (keyword)(opt)
  ;   :label (string)(opt)}
  [field-id field-props {:keys [icon label] :as adornment-props}]
  (let [adornment-attributes (text-field.helpers/button-adornment-attributes field-id field-props adornment-props)]
       (cond icon  [:button.e-text-field--button-adornment adornment-attributes icon]
             label [:button.e-text-field--button-adornment adornment-attributes label])))

(defn static-adornment
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; @param (map) adornment-props
  ;  {:icon (keyword)(opt)
  ;   :label (string)(opt)}
  [field-id field-props {:keys [icon label] :as adornment-props}]
  (let [adornment-attributes (text-field.helpers/static-adornment-attributes field-id field-props adornment-props)]
       (cond icon  [:div.e-text-field--static-adornment adornment-attributes icon]
             label [:div.e-text-field--static-adornment adornment-attributes (x.components/content label)])))

(defn field-adornment
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; @param (map) adornment-props
  ;  {:on-click (metamorphic-event)(opt)}
  [field-id field-props {:keys [on-click] :as adornment-props}]
  (let [adornment-props (text-field.prototypes/adornment-props-prototype adornment-props)]
       (if on-click [button-adornment field-id field-props adornment-props]
                    [static-adornment field-id field-props adornment-props])))

(defn field-end-adornments
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {:end-adornments (maps in vector)(opt)}
  [field-id {:keys [end-adornments] :as field-props}]
  (let [end-adornments (text-field.prototypes/end-adornments-prototype field-id field-props)]
       (if (vector/nonempty? end-adornments)
           (letfn [(f [% adornment-props] (conj % [field-adornment field-id field-props adornment-props]))]
                  (reduce f [:div.e-text-field--adornments] end-adornments))
           [field-adornments-placeholder field-id field-props])))

(defn field-start-adornments
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {:start-adornments (maps in vector)(opt)}
  [field-id {:keys [start-adornments] :as field-props}]
  (if (vector/nonempty? start-adornments)
      (letfn [(f [% adornment-props] (conj % [field-adornment field-id field-props adornment-props]))]
             (reduce f [:div.e-text-field--adornments] start-adornments))
      [field-adornments-placeholder field-id field-props]))



;; -- Field surface components ------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- text-field-surface
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {:surface (metamorphic-content)(opt)}
  [field-id {:keys [surface] :as field-props}]
  (if surface (if-let [surface-visible? @(r/subscribe [:elements.text-field/surface-visible? field-id])]
                      [:div.e-text-field--surface (text-field.helpers/surface-attributes field-id field-props)
                                                  [x.components/content field-id surface]])))



;; -- Field warning components ------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- text-field-required-warning
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  (if-let [required-warning? @(r/subscribe [:elements.text-field/required-warning? field-id field-props])]
          [:div.e-text-field--warning {:data-selectable false}
                                      (x.components/content :please-fill-out-this-field)]))

(defn- text-field-invalid-warning
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {}
  [field-id {:keys [validator] :as field-props}]
  (if-let [required-warning? @(r/subscribe [:elements.text-field/required-warning? field-id field-props])]
          [:<>] ; Ha a mező {:required-warning? true} állapotban van, akkor nem szükséges validálni a mező tartalmát ...
          (if-let [invalid-warning? @(r/subscribe [:elements.text-field/invalid-warning? field-id field-props])]
                  [:div.e-text-field--warning {:data-selectable false}
                                              (-> validator :invalid-message x.components/content)])))



;; -- Field structure components ----------------------------------------------
;; ----------------------------------------------------------------------------

(defn- text-field-placeholder
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {:placeholder (metamorphic-content)}
  [field-id {:keys [placeholder] :as field-props}]
  ; A placeholder elem {:on-mouse-down #(focus-element! ...)} eseménye nem adta át a fókuszt
  ; az input elem számára, ezért a placeholder elem az input elem alatt kell, hogy megjelenjen
  ; Google Chrome 98.0.4758.80
  ;
  ; Az input elemek az on-mouse-down esemény hatására kapnak fókuszt
  (if placeholder (let [field-content (text-field.helpers/get-field-content field-id)]
                       ; BUG#3401 (source-code/cljs/elements/text_field/helpers.cljs)
                       (if (empty? field-content)
                           [:div.e-text-field--placeholder {:data-selectable false}
                                                           (x.components/content placeholder)]))))

(defn- text-field-input
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {:multiline? (boolean)(opt)}
  [field-id {:keys [multiline?] :as field-props}]
  (if multiline? [:textarea.e-text-field--input (text-field.helpers/field-body-attributes field-id field-props)]
                 [:input.e-text-field--input    (text-field.helpers/field-body-attributes field-id field-props)]))

(defn- text-field-input-emphasize
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  ; BUG#3445
  ; A {:multiline? true} beállítással használt mező textarea komponensénél valamiért
  ; 6.5px-el magasabb az .e-text-field--input-emphasize elem, ezért szükséges az .e-...-emphasize
  ; elemnek is beállítani a textarea elem magasságát!
  ; Google Chrome 101.0.4951.64
  [:div.e-text-field--input-emphasize {:style (text-field.helpers/field-body-style field-id field-props)}
                                      [text-field-input field-id field-props]])

(defn- text-field-input-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  ; XXX#3415
  ; Az .e-text-field--input-structure elem tartalmazza az input elemet és az abszolút pozícionálású
  ; placeholder elemet, amely elhelyezéséhez szükséges, hogy közös elemben legyen az input elemmel.
  ;
  ; BUG#3418
  ; A DOM-fában az .e-text-field--input (input) elem ELŐTT elhelyezett .e-text-field--placeholder (div)
  ; elem valamiért az .e-text-field--input elem FELETT jelent meg ezért az .e-text-field--input elem
  ; az .e-text-field--input-emphasize (div) elembe került, így a placeholder elem az input elem alatt jelenik meg.
  ; Google Chrome 98.0.4758.80
  [:div.e-text-field--input-structure [text-field-placeholder     field-id field-props]
                                      [text-field-input-emphasize field-id field-props]])

(defn- text-field-input-container
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  [:div.e-text-field--input-container [field-start-adornments     field-id field-props]
                                      [text-field-input-structure field-id field-props]
                                      [field-end-adornments       field-id field-props]
                                      [text-field-surface         field-id field-props]])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.text-field/hack5041
  (fn [{:keys [db]} [_ field-id {:keys [field-content-f value-path]}]]
      (let [field-content  (text-field.helpers/get-field-content field-id)
            stored-value   (get-in db value-path)
            stored-content (field-content-f stored-value)]
           (if (not= field-content stored-content)
               (text-field.helpers/set-field-content! field-id stored-content))
           {})))

(defn- hack5041
  [field-id {:keys [value-path] :as field-props}]
  ; HACK#5041
  ; Yes! This is what it seems like! Sorry for that :(
  (let [stored-value @(r/subscribe [:x.db/get-item value-path])]
       (r/dispatch [:elements.text-field/hack5041 field-id field-props])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- text-field-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {:helper (metamorphic-content)(opt)
  ;   :info-text (metamorphic-content)(opt)
  ;   :label (metamorphic-content)(opt)
  ;   :required? (boolean)(opt)}
  [field-id {:keys [helper info-text label marked? required?]}]
  (if label (let [input-id (hiccup/value field-id "input")]
                 [label.views/element {:content     label
                                       :helper      helper
                                       :info-text   info-text
                                       :line-height :block
                                       :marked?     marked?
                                       :required?   required?
                                       :target-id   input-id}])))

(defn- text-field-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  [:div.e-text-field (text-field.helpers/field-attributes field-id field-props)
                     [text-field-label                    field-id field-props]
                     [text-field-input-container          field-id field-props]
                     [text-field-required-warning         field-id field-props]
                     [text-field-invalid-warning          field-id field-props]
                     ; HACK#5041
                     [hack5041 field-id field-props]])

(defn- text-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  (reagent/lifecycles {:component-did-mount    (text-field.helpers/field-did-mount-f    field-id field-props)
                       :component-will-unmount (text-field.helpers/field-will-unmount-f field-id field-props)
                       :reagent-render         (fn [_ field-props] [text-field-structure field-id field-props])}))

(defn element
  ; XXX#0711
  ; @param (keyword)(opt) field-id
  ; @param (map) field-props
  ;  {:autoclear? (boolean)(opt)
  ;    Default: false
  ;   :autofill-name (keyword)(opt)
  ;   :autofocus? (boolean)(opt)
  ;   :border-color (keyword or string)(opt)
  ;    :default, :primary, :secondary, :success, :warning
  ;    Default: :default
  ;   :border-radius (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :none
  ;    Default: :s
  ;   :class (keyword or keywords in vector)(opt)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :emptiable? (boolean)(opt)
  ;    Default: false
  ;   :end-adornments (maps in vector)(opt)
  ;    [{:color (keyword)(opt)
  ;       :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  ;       Default: :default
  ;      :disabled? (boolean)(opt)
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
  ;   :field-content-f (function)(opt)
  ;    Default: return
  ;   :field-value-f (function)(opt)
  ;    Default: return
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
  ;   :info-text (metamorphic-content)(opt)
  ;   :initial-value (string)(opt)
  ;   :label (metamorphic-content)(opt)
  ;   :marked? (boolean)(opt)
  ;    Default: false
  ;   :max-length (integer)(opt)
  ;   :min-width (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl, :none
  ;    Default: :none
  ;   :modifier (function)(opt)
  ;   :on-blur (metamorphic-event)(opt)
  ;   :on-change (metamorphic-event)(opt)
  ;    Az esemény utolsó paraméterként megkapja a mező aktuális értékét.
  ;   :on-empty (metamorphic-event)(opt)
  ;   :on-focus (metamorphic-event)(opt)
  ;   :on-type-ended (metamorphic-event)(opt)
  ;    Az esemény utolsó paraméterként megkapja a mező aktuális értékét.
  ;   :placeholder (metamorphic-content)(opt)
  ;   :required? (boolean or keyword)(opt)
  ;    true, false, :unmarked
  ;    Default: false
  ;   :start-adornments (maps in vector)(opt)
  ;    [{:color (keyword)(opt)
  ;       :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  ;       Default: :default
  ;      :disabled? (boolean)(opt)
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
  ;   :validator (map)(opt)
  ;    {:f (function)
  ;     :invalid-message (metamorphic-content)(opt)
  ;     :invalid-message-f (function)(opt)
  ;     :prevalidate? (boolean)(opt)
  ;      A mező kitöltése közben validálja annak értékét, még mielőtt a mező
  ;      {:visited? true} állapotba lépne.
  ;      Default: false}
  ;   :value-path (vector)(opt)}
  ;
  ; @usage
  ;  [text-field {...}]
  ;
  ; @usage
  ;  [text-field :my-text-field {...}]
  ;
  ; @usage
  ;  [text-field {:validator {:f               #(not (empty? %))
  ;                           :invalid-message "Invalid value"}}]
  ;
  ; @usage
  ;  (defn get-invalid-message [value] "Invalid value")
  ;  [text-field {:validator {:f                 #(not (empty? %))
  ;                           :invalid-message-f get-invalid-message}}]
  ;
  ; @usage
  ;  (defn my-surface [field-id])
  ;  [text-field {:surface {:content #'my-surface}}]
  ;
  ; @usage
  ;  (defn my-surface [field-id surface-props])
  ;  [text-field {:surface #'my-surface}]
  ;
  ; @usage
  ; [text-field {:modifier #(string/starts-with! % "/")}]
  ([field-props]
   [element (random/generate-keyword) field-props])

  ([field-id field-props]
   (let [field-props (text-field.prototypes/field-props-prototype field-id field-props)]
        [text-field field-id field-props])))
