
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

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
  ;  :on-change (metamorphic-event)(opt)}
  ;
  ; @return (function)
  [field-id {:keys [modifier on-change] :as field-props}]
  #(let [now           (time/elapsed)
         field-content (if modifier (-> % dom/event->value modifier)
                                    (-> % dom/event->value))]
        (swap! text-field.state/FIELD-CONTENTS assoc field-id {:changed-at now :content field-content})
        (letfn [(f [] (resolve-field-change! field-id field-props))]
               (time/set-timeout! f text-field.config/TYPE-ENDED-AFTER))
        (if on-change (let [on-change (r/metamorphic-event<-params on-change field-content)]
                           (r/dispatch-sync on-change)))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-body-line-count
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:multiline? (boolean)(opt)}
  ;
  ; @return (integer)
  [field-id {:keys [multiline?]}]
  (let [field-content (get-field-content field-id)]
       (if multiline? ; If field is multiline ...
                      (let [line-count (-> field-content string/line-count inc)]
                           ; BUG#1481
                           ; A textarea element magassága minimum 2 sor magasságú kell legyen,
                           ; különben az egy sorba írt - a textarea szélességébe ki nem férő -
                           ; szöveg nem törik meg automatikusan
                           ; Google Chrome Version 89.0.4389.114
                           (inc line-count))
                      ; If field is NOT multiline ...
                      (return 1))))

(defn field-body-height
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:font-size (keyword)}
  ;
  ; @return (integer)
  [field-id {:keys [font-size] :as field-props}]
  (case font-size :xs (+ (* text-field.config/LINE-HEIGHT-XS (field-body-line-count field-id field-props))
                         (* text-field.config/FIELD-HORIZONTAL-PADDING 2))
                  :s  (+ (* text-field.config/LINE-HEIGHT-S (field-body-line-count field-id field-props))
                         (* text-field.config/FIELD-HORIZONTAL-PADDING 2))))

(defn field-body-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {}
  ;
  ; @return (map)
  ; {:height (string)}
  [field-id {:keys [style] :as field-props}]
  (let [field-body-height (field-body-height field-id field-props)]
       (assoc style :height (css/px field-body-height))))

(defn field-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:border-color (keyword)
  ;  :border-radius (keyword)
  ;  :font-size (keyword)
  ;  :min-width (keyword)
  ;  :stretch-orientation (keyword)}
  ;
  ; @return (map)
  ; {}
  [field-id {:keys [border-color border-radius font-size min-width stretch-orientation] :as field-props}]
  (let [any-warning? @(r/subscribe [:elements.text-field/any-warning? field-id field-props])]
       (merge (element.helpers/element-default-attributes field-id field-props)
              (element.helpers/element-indent-attributes  field-id field-props)
              (element.helpers/apply-color {} :border-color :data-border-color border-color)
              {:data-border-radius       border-radius
               :data-font-size           font-size
               :data-min-width           min-width
               :data-stretch-orientation stretch-orientation}
              (if any-warning? {:data-border-color :warning}))))

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
  ;  :disabled (boolean)
  ;  :id (string)
  ;  :max-length (integer)
  ;  :min (string)
  ;  :max (string)
  ;  :name (keyword)
  ;  :on-blur (function)
  ;  :on-change (function)
  ;  :on-focus (function)
  ;  :style (map)
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
  ;    esetlegesen fókuszált állapotban lévő mezőn, ...
  ;    ... ezért a mező Re-Frame adatbázisban tárolt állapota {:focused? true} beállításon maradna.
  ;    ... az x.environment.keypress-handler {:type-mode? true} beállításon maradna.
  ; ... a {:disabled? true} állapot megszűnésekor a mező nem lépne vissza a fókuszált állapotba.
  ;
  ; A következő beállítások biztosítják, hogy a mező disabled állapotúnak tűnjön:
  ; - A {:tab-index "-1"} beállítás miatt nem reagál a billentyűzet általi fókuszálásra.
  ; - A [data-disabled="true"] attribútum letiltott állapotúként jeleníti meg a mezőt
  ;  és kikapcsolja a caret láthatóságát.
  ; - Az on-change függvény nem végez műveletet.
  (if disabled? {;:disabled true
                 :tab-index  "-1"
                 :max-length max-length
                 :type       type
                 :id         (hiccup/value      field-id "input")
                 :style      (field-body-style  field-id field-props)
                 :value      (get-field-content field-id)
                 :on-change #(let [])}
                {:auto-complete  autofill-name
                 :max-length     max-length
                 :min            date-from
                 :max            date-to
                 :name           autofill-name
                 :type           type
                 :id             (hiccup/value      field-id "input")
                 :style          (field-body-style  field-id field-props)
                 :value          (get-field-content field-id)
                 :on-mouse-down #(r/dispatch [:elements.text-field/show-surface! field-id])
                 :on-blur       #(r/dispatch [:elements.text-field/field-blurred field-id field-props])
                 :on-focus      #(r/dispatch [:elements.text-field/field-focused field-id field-props])
                 :on-change      (on-change-f field-id field-props)}))



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
  ;  :data-selectable (boolean)
  ;  :data-icon-family (keyword)}
  [_ _ {:keys [color icon icon-family]}]
  (if icon {:data-color       color
            :data-selectable  false
            :data-icon-family icon-family}
           {:data-color       color
            :data-selectable  false}))

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
  (merge {:data-color      color
          :data-clickable  true
          :data-selectable false
          :on-mouse-down #(.preventDefault %)
          :title          (x.components/content tooltip)}
         (if     icon         {:data-icon-family icon-family})
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
