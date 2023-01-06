
(ns elements.text-field.helpers
    (:require [candy.api                  :refer [return]]
              [css.api                    :as css]
              [dom.api                    :as dom]
              [elements.element.helpers   :as element.helpers]
              [elements.text-field.config :as text-field.config]
              [elements.text-field.state  :as text-field.state]
              [hiccup.api                 :as hiccup]
              [re-frame.api               :as r]
              [string.api                 :as string]
              [time.api                   :as time]
              [x.components.api           :as x.components]
              [x.environment.api          :as x.environment]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn text-field-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  (r/dispatch [:elements.text-field/text-field-did-mount field-id field-props]))

(defn text-field-will-unmount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  (r/dispatch [:elements.text-field/text-field-will-unmount field-id field-props]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-empty?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (boolean)
  [field-id]
  ; BUG#3401
  (let [field-content (get-in @text-field.state/FIELD-CONTENTS [field-id :content])]
       (empty? field-content)))

(defn field-filled?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (boolean)
  [field-id]
  (let [field-content (get-in @text-field.state/FIELD-CONTENTS [field-id :content])]
       (-> field-content empty? not)))

(defn field-enabled?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (boolean)
  [field-id]
  (let [field-input-id (hiccup/value field-id "input")]
       (x.environment/element-enabled? field-input-id)))

(defn field-emptiable?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (boolean)
  [field-id]
  (and (field-enabled? field-id)
       (field-filled?  field-id)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-field-content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (string)
  [field-id]
  (get-in @text-field.state/FIELD-CONTENTS [field-id :content]))

(defn set-field-content!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; @param (*) content
  [field-id content]
  ; BUG#3401
  ; A mező értékének eltárolása előtt szükséges azt string típusra alakítani!
  ; Pl.: Előfordulhat, hogy number típusú érték íródik a mezőbe és az értéket
  ;     vizsgáló empty? függvény hibát dobna egy number típus vizsgálatakor!
  (swap! text-field.state/FIELD-CONTENTS assoc-in [field-id :content] (str content)))

(defn resolve-field-change!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  ; - A resolve-field-change! függvény a mező megváltozása után késleltetve fut le,
  ;  és ha a mező megváltozása és a függvény késleltetett lefutása között a mező
  ;  értékében újabb változás már nem történt, akkor a gépelés befejezettnek tekinthető.
  ;  Ekkor a mező értéke a Re-Frame adatbázisba íródik és lefut az esetlegesen beállított
  ;  on-type-ended esemény.
  ;
  ; - A resolve-field-change! függvény az on-change-f függvénytől NEM kapja meg
  ;  paraméterként a mező aktuális értékét, mert a késleltetett futás miatt előfordulhat,
  ;  hogy a mező értéke időközben megváltozik (pl. az ESC billentyű lenyomása kiüríti a mezőt)
  (let [field-content (get-field-content field-id)
        now           (time/elapsed)
        changed-at    (get-in @text-field.state/FIELD-CONTENTS [field-id :changed-at])]
       (when (> now (+ changed-at text-field.config/TYPE-ENDED-AFTER))
             (r/dispatch-sync [:elements.text-field/type-ended field-id field-props field-content]))))

(defn on-change-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:modifier (function)(opt)
  ;  :on-changed (metamorphic-event)(opt)}
  ;
  ; @return (function)
  [field-id {:keys [modifier on-changed] :as field-props}]
  #(let [now           (time/elapsed)
         field-content (if modifier (-> % dom/event->value modifier)
                                    (-> % dom/event->value))]
        (swap! text-field.state/FIELD-CONTENTS assoc field-id {:changed-at now :content field-content})
        (letfn [(f [] (resolve-field-change! field-id field-props))]
               (time/set-timeout! f text-field.config/TYPE-ENDED-AFTER))
        (if on-changed (let [on-changed (r/metamorphic-event<-params on-changed field-content)]
                            (r/dispatch-sync on-changed)))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-line-count
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:multiline? (boolean)(opt)}
  ;
  ; @return (integer)
  [field-id {:keys [multiline?]}]
  (let [field-content (get-field-content field-id)]
       (if multiline? ; If the field is multiline ...
                      (let [line-count (-> field-content string/line-count inc)]
                           ; BUG#1481
                           ; A textarea element magassága minimum 2 sor magasságú kell legyen,
                           ; különben az egy sorba írt - a textarea szélességébe ki nem férő -
                           ; szöveg nem törik meg automatikusan
                           ; Google Chrome Version 89.0.4389.114
                           (inc line-count))
                      ; If the field is NOT multiline ...
                      (return 1))))

(defn field-auto-height
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:font-size (keyword)
  ;  :line-height (keyword)}
  ;
  ; @example
  ; (field-auto-height :my-field {:font-size :s :line-height :normal})
  ; =>
  ; "calc(var( --line-height-s ) * 1)"
  ;
  ; @example
  ; (field-auto-height :my-field {:font-size :s :line-height :block})
  ; =>
  ; "calc(var( --block-height-s ) * 1)"
  ;
  ; @example
  ; (field-auto-height :my-field {:font-size :s :line-height :xxl})
  ; =>
  ; "calc(var( --line-height-xxl ) * 1)"
  ;
  ; @example
  ; (field-auto-height :my-field {:font-size :s :line-height 48})
  ; =>
  ; "calc(48px * 1)"
  ;
  ; @return (string)
  [field-id {:keys [font-size line-height] :as field-props}]
  ; XXX#0886 (resources/css/presets/font.css)
  ; XXX#6618 (resources/css/elements/style.css)
  (let [line-count (field-line-count field-id field-props)]
       (case line-height :block  (str "calc(var( --block-height-" (name font-size)   " ) * "line-count" + 12px)")
                         :normal (str "calc(var( --line-height-"  (name font-size)   " ) * "line-count" + 12px)")
                                 (str "calc(var( --line-height-"  (name line-height) " ) * "line-count" + 12px)"))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn input-emphasize-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (map)
  [field-id field-props]
  {:style {:height (field-auto-height field-id field-props)}})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn input-container-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [_ {:keys [border-color border-radius style]}]
  (-> {:data-border-radius border-radius
       :style              style}
      (element.helpers/apply-color :border-color :data-border-color border-color)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-font-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:font-size (keyword)
  ;  :line-height (keyword)}
  ;
  ; @return (map)
  ; {:data-font-size (keyword)
  ;  :data-line-height (keyword)}
  [_ {:keys [font-size line-height]}]
  {:data-font-size   font-size
   :data-line-height line-height})

(defn field-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:autofill-name (keyword)
  ;  :date-from (string)(opt)
  ;  :date-to (string)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :max-length (integer)(opt)
  ;  :type (keyword)(opt)
  ;   :password, :text}
  ;
  ; @return (map)
  ; {:auto-complete (keyword)
  ;  :data-fillable (boolean)
  ;  :disabled (boolean)
  ;  :id (string)
  ;  :max-length (integer)
  ;  :min (string)
  ;  :max (string)
  ;  :name (keyword)
  ;  :on-blur (function)
  ;  :on-change (function)
  ;  :on-focus (function)
  ;  :type (keyword)
  ;  :value (string)}
  [field-id {:keys [autofill-name date-from date-to disabled? max-length type] :as field-props}]
  ; XXX#4460 (source-code/cljs/elements/button/views.cljs)
  ;
  ; BUG#8809
  ; Ha a mező disabled állapotba lépéskor elveszítené az on-change tulajdonságát,
  ; akkor a React figyelmeztetne, hogy controlled elemből uncontrolled elemmé változott!
  ;
  ; XXX#4461
  ; A {:type :date} típusú mezők min és max dátuma beállítható
  ; a date-field date-from és date-field tulajdonságainak használatával.
  ;
  ; BUG#8800
  ; 2022.11.28
  ; Google Chrome  - Version 107.0.5304.110 (Official Build) (x86_64)
  ; MacOS Monterey - Version 12.3.1 (21E258)
  ;
  ; Ha a text-field elem {:disabled? true} beállítása az input mezőt disabled állapotba
  ; állítaná, akkor ...
  ; ... a mező kilépne a fókuszált állapotból.
  ; ... nem történne meg a mező on-blur eseménye az {:disabled? true} állapot beállításakor
  ;     esetlegesen fókuszált állapotban lévő mezőn, ...
  ;     ... ezért a mező Re-Frame adatbázisban tárolt állapota {:focused? true} beállításon maradna.
  ;     ... az x.environment.keypress-handler {:type-mode? true} beállításon maradna.
  ; ... a {:disabled? true} állapot megszűnésekor a mező nem lépne vissza a fókuszált állapotba.
  ;
  ; A következő beállítások biztosítják, hogy a mező disabled állapotúnak tűnjön:
  ; - A {:tab-index "-1"} beállítás miatt nem reagál a billentyűzet általi fókuszálásra.
  ; - A [data-disabled="true"] attribútum letiltott állapotúként jeleníti meg a mezőt
  ;   és kikapcsolja a caret láthatóságát.
  ; - Az on-change függvény nem végez műveletet.
  (merge (element.helpers/element-indent-attributes field-id field-props)
         (field-font-attributes                     field-id field-props)
         (if disabled? {;:disabled true
                        :data-fillable true
                        :tab-index     "-1"
                        :max-length    max-length
                        :type          type
                        :id            (hiccup/value      field-id "input")
                        :value         (get-field-content field-id)
                        :on-change     (fn [])}
                       {:auto-complete autofill-name
                        :data-fillable true
                        :max-length    max-length
                        :min           date-from
                        :max           date-to
                        :name          autofill-name
                        :type          type
                        :id            (hiccup/value      field-id "input")
                        :value         (get-field-content field-id)
                        :on-mouse-down #(r/dispatch [:elements.text-field/show-surface! field-id])
                        :on-blur       #(r/dispatch [:elements.text-field/field-blurred field-id field-props])
                        :on-focus      #(r/dispatch [:elements.text-field/field-focused field-id field-props])
                        :on-change     (on-change-f field-id field-props)})))

(defn field-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:min-width (keyword)
  ;  :stretch-orientation (keyword)}
  ;
  ; @return (map)
  ; {}
  [field-id {:keys [min-width stretch-orientation] :as field-props}]
  (merge (element.helpers/element-default-attributes field-id field-props)
         (element.helpers/element-outdent-attributes field-id field-props)
         {:data-element-width       min-width
          :data-stretch-orientation stretch-orientation}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn surface-attributes
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
  {:id             (hiccup/value field-id "surface")
   :on-mouse-down #(.preventDefault %)})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn static-adornment-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; @param (map) adornment-props
  ; {:color (keyword)
  ;  :icon (keyword)(opt)
  ;  :icon-family (keyword)(opt)}
  ;
  ; @return (map)
  ; {:data-color (keyword)
  ;  :data-font-size (keyword)
  ;  :data-line-height (keyword)
  ;  :data-selectable (boolean)
  ;  :data-icon-family (keyword)}
  [_ _ {:keys [color icon icon-family]}]
  (merge {:data-font-size   :xs
          :data-line-height :block
          :data-selectable  false}
         (if icon {:data-icon-family icon-family})
         (if icon {:data-icon-size   :s})))

(defn button-adornment-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; @param (map) adornment-props
  ; {:color (keyword)
  ;  :disabled? (boolean)(opt)
  ;   Default: false
  ;  :icon (keyword)(opt)
  ;  :icon-family (keyword)(opt)
  ;   :material-icons-filled, :material-icons-outlined
  ;   Default: :material-icons-filled
  ;  :on-click (metamorphic-event)
  ;  :tab-indexed? (boolean)(opt)
  ;   Default: true
  ;  :tooltip (metamorphic-content)(opt)}
  ;
  ; @return (map)
  ; {}
  [_ _ {:keys [color disabled? icon icon-family on-click tab-indexed? tooltip]}]
  ; BUG#2105
  ; A *-field elemhez adott field-adornment-button gombon történő on-mouse-down esemény
  ; a mező on-blur eseményének triggerelésével jár, ami a mezőhöz esetlegesen használt surface
  ; felület React-fából történő lecsatolását okozná.
  (merge {:data-color       color
          :data-clickable   true
          :data-font-size   :xs
          :data-line-height :block
          :data-selectable  false
          :on-mouse-down    #(.preventDefault %)
          :title             (x.components/content tooltip)}
         (if     icon         {:data-icon-family icon-family})
         (if     icon         {:data-icon-size   :s})
         (if     disabled?    {:disabled   "1" :data-disabled true})
         (if-not tab-indexed? {:tab-index "-1"})
         (if-not disabled?    {:on-mouse-up #(do (r/dispatch on-click)
                                                 (x.environment/blur-element!))})))

(defn adornment-placeholder-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (map)
  ; {:on-mouse-down (function)}
  [field-id field-props]
  {:on-mouse-down (fn [e] (.preventDefault e)
                          (r/dispatch-fx [:elements.text-field/focus-field!  field-id field-props])
                          (r/dispatch    [:elements.text-field/show-surface! field-id]))})

(defn empty-field-adornment-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (map)
  ; {}
  [field-id field-props]
  {:disabled? (field-empty? field-id)
   :icon      :close
   :on-click  [:elements.text-field/empty-field! field-id field-props]
   :tooltip   :empty-field!})
