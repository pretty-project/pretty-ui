
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.element-components.text-field
    (:require [mid-fruits.candy                       :as candy :refer [param return]]
              [mid-fruits.string                      :as string]
              [mid-fruits.vector                      :as vector]
              [x.app-components.api                   :as components]
              [x.app-core.api                         :as a :refer [r]]
              [x.app-elements.surface-handler.subs    :as surface-handler.subs]
              [x.app-elements.engine.api              :as engine]
              [x.app-elements.adornment-handler.views :as adornment-handler.views]
              [x.app-elements.target-handler.helpers  :as target-handler.helpers]

              ; TEMP
              [reagent.api :as reagent]))



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
  (cond-> end-adornments resetable? (vector/conj-item (engine/reset-field-adornment-preset field-id))
                         emptiable? (vector/conj-item (engine/empty-field-adornment-preset field-id))))

(defn- field-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {:autofill? (boolean)(opt)}
  ;
  ; @return (map)
  ;  {:end-adornments (maps in vector)
  ;   :name (keyword)
  ;   :type (keyword)}
  [field-id {:keys [autofill?] :as field-props}]
  (merge {:type       :text
          :value-path (engine/default-value-path field-id)}
          ; BUG#6782 https://stackoverflow.com/questions/12374442/chrome-ignores-autocomplete-off
          ; - A Chrome böngésző ...
          ;   ... ignorálja az {:autocomplete "off"} beállítást,
          ;   ... ignorálja az {:autocomplete "new-*"} beállítást,
          ;   ... figyelembe veszi a {:name ...} értékét.
          ;
          ; - Véletlenszerű {:name ...} érték használatával az autofill nem képes megállapítani,
          ;   mi alapján ajánljon értékeket a mezőhöz.
          ;
          ; - A mező {:name ...} tulajdonságát lehetséges paraméterként is beállítani, mert a
          ;   a több helyen felhasznált mezők nem kaphatnak egyedi azonosítót, és generált
          ;   azonosítóval nem műkdödik az autofill!
         {:name (if autofill? field-id (a/id))}
         (param field-props)
         {:end-adornments (end-adornments-prototype field-id field-props)}))



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
  [_ {:keys [field-empty?] :as field-props}]
  (letfn [(f [end-adornments {:keys [preset] :as end-adornment}]
             (case preset :empty-field-adornment (conj end-adornments (assoc end-adornment :disabled? true))
                          :reset-field-adornment (conj end-adornments (assoc end-adornment :disabled? true))
                                                 (conj end-adornments end-adornment)))]
         (if-not field-empty? (return field-props)
                              (update field-props :end-adornments #(reduce f [] %))))) ; XXX#8073



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-text-field-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (map)
  [db [_ field-id]]
  (merge (r engine/get-element-props               db field-id)
         (r engine/get-field-props                 db field-id)
         (r surface-handler.subs/get-surface-props db field-id)))

(a/reg-sub :elements/get-text-field-props get-text-field-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- text-field-surface
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {:surface (metamorphic-content)(opt)
  ;   :surface-visible? (boolean)(opt)}
  [field-id {:keys [surface surface-visible?]}]
  (if (and surface surface-visible?)
      [:div.x-text-field--surface ;{:on-mouse-down #(.preventDefault %)}
                                  [components/content field-id surface]]))

(defn- text-field-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {:info-text (metamorphic-content)(opt)
  ;   :label (metamorphic-content)
  ;   :required? (boolean)(opt)}
  [field-id {:keys [info-text label required?] :as field-props}]
  ; https://css-tricks.com/html-inputs-and-labels-a-love-story/
  ; ... it is always the best idea to use an explicit label instead of an implicit label.
  (if label [:label.x-text-field--label {:for (target-handler.helpers/element-id->target-id field-id)}
                                        [components/content label]
                                        (if required? [:span.x-input--label-asterisk "*"])
                                        (if info-text [engine/info-text-button field-id field-props])]))

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
  ; - Az input elemek az on-mouse-down esemény hatására kapnak fókuszt
  (if (engine/field-props->render-field-placeholder? field-props)
      [:div.x-text-field--placeholder [components/content placeholder]]))

(defn- text-field-input
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  [:input.x-text-field--input (engine/field-body-attributes field-id field-props)])

(defn- text-field-input-structure
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
                                      [:div.x-text-field--input-emphasize [text-field-input field-id field-props]]])

(defn- text-field-input-container
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  [:div.x-text-field--input-container [adornment-handler.views/field-start-adornments field-id field-props]
                                      [text-field-input-structure                     field-id field-props]
                                      [adornment-handler.views/field-end-adornments   field-id field-props]
                                      [text-field-surface                             field-id field-props]])

(defn- text-field-invalid-message
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {:invalid-message (metamorphic-content)(opt)}
  [_ {:keys [invalid-message]}]
  (if invalid-message [:div.x-text-field--invalid-message (components/content invalid-message)]))

(defn- text-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  [:div.x-text-field (engine/element-attributes  field-id field-props)
                     [text-field-label           field-id field-props]
                     [engine/info-text-content   field-id field-props]
                     [text-field-input-container field-id field-props]
                     [text-field-invalid-message field-id field-props]
                     [engine/element-helper      field-id field-props]])

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
  ;       Only w/ {:end-adornments [{:icon ...}]}
  ;      :label (string)(opt)
  ;      :on-click (metamorphic-event)(opt)
  ;      :tab-indexed? (boolean)(opt)
  ;       Default: true
  ;      :tooltip (metamorphic-content)(opt)}]
  ;   :emptiable? (boolean)(constant)(opt)
  ;    Default: false
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
  ;    [{:disabled? (boolean)(opt)
  ;       Default: false
  ;      :icon (keyword)(opt)
  ;      :icon-family (keyword)(opt)
  ;       :material-icons-filled, :material-icons-outlined
  ;       Default: :material-icons-filled
  ;       Only w/ {:start-adornments [{:icon ...}]}
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
  ;   :value-path (vector)(constant)(opt)}
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
  ;  [elements/text-field {:surface #'my-surface}]
  ;
  ; @usage
  ; [elements/text-field {:modifier #(string/starts-with! % "/")}]
  ([field-props]
   [element (a/id) field-props])

  ([field-id field-props]
   (let [field-props (field-props-prototype field-id field-props)]
        [engine/stated-element field-id
                               {:render-f      text-field
                                :element-props field-props
                                :modifier      field-props-modifier
                                :initializer   [:elements/init-field!          field-id]
                                :subscriber    [:elements/get-text-field-props field-id]

                                ; TEMP
                                :destructor (if (:autoclear? field-props) [:db/remove-item! (:value-path field-props)])}])))
                                ; TEMP
