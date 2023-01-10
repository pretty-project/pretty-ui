
(ns elements.plain-field.helpers
    (:require [dom.api                     :as dom]
              [elements.element.helpers    :as element.helpers]
              [elements.plain-field.config :as plain-field.config]
              [elements.plain-field.state  :as plain-field.state]
              [hiccup.api                  :as hiccup]
              [re-frame.api                :as r]
              [reagent.api                 :as reagent]
              [time.api                    :as time]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-field-content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (string)
  [field-id]
  ; HACK#9910
  (get @plain-field.state/FIELD-CONTENTS field-id))

(defn get-field-output
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (string)
  [field-id]
  ; HACK#9910
  (get @plain-field.state/FIELD-OUTPUTS field-id))

(defn set-field-content!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (string) field-content
  [field-id field-content]
  ; HACK#9910
  (swap! plain-field.state/FIELD-CONTENTS assoc field-id field-content))

(defn set-field-output!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (string) field-content
  [field-id field-content]
  ; HACK#9910
  (swap! plain-field.state/FIELD-OUTPUTS assoc field-id field-content))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-empty?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (boolean)
  [field-id]
  (let [field-content (get-field-content field-id)]
       (empty? field-content)))

(defn field-filled?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (boolean)
  [field-id]
  (let [field-content (get-field-content field-id)]
       (-> field-content empty? not)))

(defn surface-visible?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (boolean)
  [field-id]
  (= field-id @plain-field.state/VISIBLE-SURFACE))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn synchronizer-did-update-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (?) %
  [field-id %]
  ; HACK#9910 (source-code/cljs/elements/plain_field/views.cljs)
  ; When the stored value (in the application state) gets updated, checks
  ; whether if it's different from the field content.
  ; If it's different, updates the field content with the content derived from
  ; the stored value.
  (let [[_ {:keys [field-content-f] :as field-props} stored-value] (reagent/arguments %)]
       (let [stored-content (field-content-f stored-value)]
            (when (not= stored-content (get-field-content field-id))
                  (set-field-content! field-id stored-content)))))

(defn synchronizer-did-mount-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:field-content-f (function)
  ;  :value-path (vector)}
  [field-id {:keys [field-content-f value-path]}]
  ; HACK#9910 (source-code/cljs/elements/plain_field/views.cljs)
  (let [stored-value  @(r/subscribe [:x.db/get-item value-path])
        stored-content (field-content-f stored-value)]
       (set-field-content! field-id stored-content)))

(defn synchronizer-will-unmount-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id _]
  ; HACK#9910 (source-code/cljs/elements/plain_field/views.cljs)
  (set-field-content! field-id nil)
  (set-field-output!  field-id nil))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-changed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id _]
  (let [timestamp (time/elapsed)]
       (swap! plain-field.state/FIELD-STATES assoc field-id {:changed-at timestamp})))

(defn resolve-field-change!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  ; The 'resolve-field-change!' function called by the 'on-change-f' function
  ; after the field's content changed.
  ;
  ; A resolve-field-change! függvény a mező megváltozása után késleltetve fut le,
  ; és ha a mező megváltozása és a függvény késleltetett lefutása között a mező
  ; értékében újabb változás már nem történt, akkor a gépelés befejezettnek tekinthető.
  ; Ekkor a mező értéke a Re-Frame adatbázisba íródik és lefut az esetlegesen beállított
  ; on-type-ended esemény.
  ;
  ; A resolve-field-change! függvény az on-change-f függvénytől NEM kapja meg
  ; paraméterként a mező aktuális értékét, mert a késleltetett futás miatt előfordulhat,
  ; hogy a mező értéke időközben megváltozik (pl. az ESC billentyű lenyomása kiüríti a mezőt)
  (let [timestamp  (time/elapsed)
        changed-at (get-in @plain-field.state/FIELD-STATES [field-id :changed-at])]
       (when (> timestamp (+ changed-at plain-field.config/TYPE-ENDED-AFTER))
             (r/dispatch-sync [:elements.plain-field/type-ended field-id field-props]))))

(defn on-change-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:modifier (function)(opt)
  ;  :on-changed (metamorphic-event)(opt)}
  ; @param (DOM-event) event
  ;
  ; @return (function)
  [field-id {:keys [modifier on-changed] :as field-props} event]
  (let [field-content (if modifier (-> event dom/event->value modifier)
                                   (-> event dom/event->value))]
       (field-changed      field-id field-props)
       (set-field-content! field-id field-content)
       (letfn [(f [] (resolve-field-change! field-id field-props))]
              (time/set-timeout! f plain-field.config/TYPE-ENDED-AFTER))
       (if on-changed (let [on-changed (r/metamorphic-event<-params on-changed field-content)]
                           (r/dispatch-sync on-changed)))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-input-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:disabled? (boolean)(opt)}
  ;
  ; @return (map)
  ; {}
  [field-id {:keys [disabled?] :as field-props}]
  ; BUG#8806
  ; If the {:disabled? true} state of the plain-field element would set the
  ; disabled="true" attribute on the input DOM element ...
  ; ... the input loses its focus.
  ; ... the on-blur event doesn't occur in some browsers, therefore
  ;     the keypress handler stays in type mode and the field stays marked
  ;     as focused.
  ; ... after the {:disabled? true} state ends, the field doesn't get back
  ;     its focused state.
  ; Therefore, the input DOM element doesn't get the disabled="true" attribute!
  ;
  ; BUG#8809
  ; If the {:disabled? true} state of the plain-field element would remove the
  ; :on-change property of the input DOM element ...
  ; ... the React warns that the input stepped into uncontrolled state.
  ; Therefore, the input DOM element doesn't loses its :on-change property
  ; in {:disabled? true} state!
  (merge {:data-autofill :remove-style
          :type          :text
          :id            (hiccup/value      field-id "input")
          :value         (get-field-content field-id)}
         (if disabled? {:data-caret-color :hidden
                        :tab-index        -1
                        :on-change        (fn [])}
                       {:on-blur   #(r/dispatch [:elements.plain-field/field-blurred field-id field-props])
                        :on-focus  #(r/dispatch [:elements.plain-field/field-focused field-id field-props])
                        :on-change #(on-change-f field-id field-props %)})))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-surface-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (map)
  ; {:id (string)
  ;  :on-mouse-down (function)}
  [field-id _]
  ; XXX#4460 (source-code/cljs/elements/button/views.cljs)
  {:id (hiccup/value field-id "surface")
   :on-mouse-down #(.preventDefault %)})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:style (map)(opt)}
  ;
  ; @return (map)
  ; {:data-fill-color (keyword)
  ;  :style (map)}
  [field-id {:keys [style] :as field-props}]
  (merge (element.helpers/element-indent-attributes field-id field-props)
         {:data-fill-color :highlight
          :style           style}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (map)
  [field-id field-props]
  (merge (element.helpers/element-default-attributes field-id field-props)
         (element.helpers/element-outdent-attributes field-id field-props)))
