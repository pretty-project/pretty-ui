
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.element-components.multiline-field
    (:require [mid-fruits.candy                  :as candy :refer [param]]
              [x.app-core.api                    :as a :refer [r]]
              [x.app-elements.engine.api         :as engine]
              [x.app-elements.input.helpers         :as input.helpers]
              [x.app-elements.input.views         :as input.views]
              [x.app-elements.text-field.helpers :as text-field.helpers]
              [x.app-elements.text-field.views   :as text-field.views]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.app-elements.text-field.views
(def text-field-placeholder     text-field.views/text-field-placeholder)
(def invalid-message input.views/invalid-message)



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- field-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (map)
  ;  {:max-height (integer)
  ;   :min-height (integer)
  ;   :multiline? (boolean)
  ;   :status-animation? (boolean)}
  [field-id field-props]
  (merge {:max-height 32
          :min-height 1
          :value-path (input.helpers/default-value-path field-id)}
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
  [field-id field-props]
  [:textarea.x-text-field--textarea (text-field.helpers/field-body-attributes field-id field-props)])

(defn- multiline-field-textarea-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  ; - XXX#3415
  ;   Az .x-text-field--input-structure elem tartalmazza az input elemet és az abszolút pozícionálású
  ;   placeholder elemet, amely elhelyezéséhez szükséges, hogy közös elemben legyen az input elemmel.
  ; - BUG#3418
  ;   A DOM-fában az .x-text-field--input (input) elem ELŐTT elhelyezett .x-text-field--placeholder (div)
  ;   elem valamiért az .x-text-field--input elem FELETT jelent meg ezért az .x-text-field--input elem
  ;   az .x-text-field--input-emphasize (div) elembe került, így a placeholder elem az input elem alatt jelenik meg.
  ;   Google Chrome 98.0.4758.80
  [:div.x-text-field--input-structure [text-field-placeholder field-id field-props]
                                      [:div.x-text-field--input-emphasize [multiline-field-textarea field-id field-props]]])

(defn- multiline-field-textarea-container
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  [:div.x-text-field--input-container [text-field.views/field-start-adornments field-id field-props]
                                      [multiline-field-textarea-structure             field-id field-props]
                                      [text-field.views/field-end-adornments   field-id field-props]])

(defn- multiline-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  [:div.x-text-field (engine/element-attributes          field-id field-props)
                     [engine/element-header              field-id field-props]
                     [multiline-field-textarea-container field-id field-props]
                     [invalid-message         field-id field-props]])

(defn element
  ; @param (keyword)(opt) field-id
  ; @param (map) initial-props
  ;  {:autoclear? (boolean)(opt)
  ;    Default: false
  ;   :auto-focus? (boolean)(constant)(opt)
  ;   :border-color (keyword or string)(opt)
  ;    :default, :primary, :secondary,
  ;    Default: :default
  ;   :class (keyword or keywords in vector)(opt)
  ;   :default-value (string)(constant)(opt)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
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
  ;    Az esemény-vektor utolsó paraméterként megkapja a mező aktuális értékét.
  ;   :on-enter (metamorphic-event)(constant)(opt)
  ;   :on-focus (metamorphic-event)(constant)(opt)
  ;   :placeholder (metamorphic-content)(opt)
  ;    Only w/o {:label ...}
  ;   :required? (boolean or keyword)(constant)(opt)
  ;    true, false, :unmarked
  ;    Default: false
  ;   :style (map)(opt)
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
  ;   :value-path (vector)(constant)(opt)}
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
  ([field-props]
   [element (a/id) field-props])

  ([field-id field-props]
   (let [field-props (field-props-prototype field-id field-props)]
        [engine/stated-element field-id
                               {:render-f      #'multiline-field
                                :element-props field-props
                                :initializer   [:elements/init-field!          field-id]
                                :subscriber    [:elements/get-text-field-props field-id]}])))
