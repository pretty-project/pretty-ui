
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.text-field.helpers
    (:require [dom.api                          :as dom]
              [mid-fruits.candy                 :refer [return]]
              [mid-fruits.css                   :as css]
              [mid-fruits.string                :as string]
              [mid-fruits.time                  :as time]
              [x.app-components.api             :as components]
              [x.app-core.api                   :as a]
              [x.app-elements.element.helpers   :as element.helpers]
              [x.app-elements.text-field.config :as text-field.config]
              [x.app-elements.text-field.state  :as text-field.state]
              [x.app-environment.api            :as environment]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-did-mount-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (function)
  [field-id field-props]
  #(a/dispatch [:elements.text-field/init-field! field-id field-props]))

(defn field-will-unmount-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (function)
  [field-id field-props]
  #(a/dispatch [:elements.text-field/destruct-field! field-id field-props]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-empty?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (boolean)
  [field-id]
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
       (string/nonempty? field-content)))

(defn field-enabled?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (boolean)
  [field-id]
  (let [field-input-id (a/dom-value field-id "input")]
       (environment/element-enabled? field-input-id)))

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
  ; @param (string) content
  [field-id content]
  (swap! text-field.state/FIELD-CONTENTS assoc-in [field-id :content] content))

(defn resolve-field-change!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  ; - A resolve-field-change! függvény a mező megváltozása után késleltetve fut le,
  ;   és ha a mező megváltozása és a függvény késleltetett lefutása között a mező
  ;   értékében újabb változás már nem történt, akkor a gépelés befejezettnek tekinthető.
  ;   Ekkor a mező értéke a Re-Frame adatbázisba íródik és lefut az esetlegesen beállított
  ;   on-type-ended esemény.
  ;
  ; - A resolve-field-change! függvény az on-change-function függvénytől NEM kapja meg
  ;   paraméterként a mező aktuális értékét, mert a késleltetett futás miatt előfordulhat,
  ;   hogy a mező értéke időközben megváltozik (pl. az ESC billentyű lenyomása kiüríti a mezőt)
  (let [field-content (get-field-content field-id)
        now           (time/elapsed)
        changed-at    (get-in @text-field.state/FIELD-CONTENTS [field-id :changed-at])]
       (when (> now (+ changed-at text-field.config/TYPE-ENDED-AFTER))
             (a/dispatch-sync [:elements.text-field/type-ended field-id field-props field-content]))))

(defn on-change-function
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {:modifier (function)(opt)
  ;   :on-change (metamorphic-event)(opt)}
  ;
  ; @return (function)
  [field-id {:keys [modifier on-change] :as field-props}]
  #(let [now           (time/elapsed)
         field-content (if modifier (-> % dom/event->value modifier)
                                    (-> % dom/event->value))]
        (swap! text-field.state/FIELD-CONTENTS assoc field-id {:changed-at now :content field-content})
        (letfn [(f [] (resolve-field-change! field-id field-props))]
               (time/set-timeout! f text-field.config/TYPE-ENDED-AFTER))
        (if on-change (let [on-change (a/metamorphic-event<-params on-change field-content)]
                           (a/dispatch-sync on-change)))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-body-line-count
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {:multiline? (boolean)(opt)}
  ;
  ; @return (integer)
  [field-id {:keys [multiline?]}]
  (let [field-content (get-field-content field-id)]
       (if multiline? ; If field is multiline ...
                      (let [line-count (-> field-content string/count-newlines inc)]
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
  ;
  ; @return (integer)
  [field-id field-props]
  (+ (* text-field.config/FIELD-LINE-HEIGHT (field-body-line-count field-id field-props))
     (* text-field.config/FIELD-HORIZONTAL-PADDING 2)))

(defn field-body-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (map)
  ;  {:height (string)}
  [field-id field-props]
  (let [field-body-height (field-body-height field-id field-props)]
       {:height (css/px field-body-height)}))

(defn field-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [field-id {:keys [border-color min-width stretch-orientation] :as field-props}]
  (let [any-warning? @(a/subscribe [:elements.text-field/any-warning? field-id field-props])]
       (merge (element.helpers/element-default-attributes field-id field-props)
              (element.helpers/element-indent-attributes  field-id field-props)
              (element.helpers/apply-color {} :border-color :data-border-color border-color)
              {:data-min-width           min-width
               :data-stretch-orientation stretch-orientation})))

(defn field-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {:autofill-name (keyword)
  ;   :disabled? (boolean)(opt)
  ;   :max-length (integer)(opt)
  ;   :type (keyword)(opt)
  ;    :password, :text}
  ;
  ; @return (map)
  ;  {:auto-complete (keyword)
  ;   :disabled (boolean)
  ;   :id (string)
  ;   :max-length (integer)
  ;   :name (keyword)
  ;   :on-blur (function)
  ;   :on-change (function)
  ;   :on-focus (function)
  ;   :on-input (function)
  ;   :type (keyword)
  ;   :value (string)}
  [field-id {:keys [autofill-name disabled? max-length type] :as field-props}]
  (if disabled? {:disabled true
                 :id       (a/dom-value field-id "input")
                 :type     type
                 :value    (get-field-content field-id)
                 ; BUG#8809
                 ;  Ha a mező disabled állapotba lépéskor elveszítené az on-change tulajdonságát,
                 ;  akkor a React figyelmeztetne, hogy controlled elemből uncontrolled elemmé változott!
                 :on-change #(let [])}
                {:auto-complete  autofill-name
                 :id             (a/dom-value field-id "input")
                 :max-length     max-length
                 :name           autofill-name
                 :type           type
                 :style          (field-body-style  field-id field-props)
                 :value          (get-field-content field-id)
                 :on-mouse-down #(a/dispatch [:elements.text-field/show-surface! field-id])
                 :on-blur       #(a/dispatch [:elements.text-field/field-blurred field-id field-props])
                 :on-focus      #(a/dispatch [:elements.text-field/field-focused field-id field-props])
                 ; BUG#8041
                 ;  Abban az esetben, ha egy input elem {:value-path [...]}
                 ;  tulajdonságaként átadott Re-Frame adatbázis útvonalon tárolt
                 ;  érték megváltozik egy külső esemény hatására, az input elem
                 ;  {:on-change #(...)} függvényétől függetlenül, miközben
                 ;  az input elemen van a fókusz, akkor az elem fókuszának
                 ;  elvesztésekor megvizsgálja és "észreveszi", hogy megváltozott
                 ;  az értéke, ezért lefuttatja az {:on-change #(...)} függvényét,
                 ;  aminek hatására nem várt események történhetnek, amik hibás
                 ;  működéshez vezethetnek.
                 ;
                 ;  Pl.: a combo-box elem opciós listájából kiválasztott opció,
                 ;  ami az elem {:value-path [...]} ... útvonalon tárolódik,
                 ;  felülíródik az input tartalmával, ami minden esetben string
                 ;  típus, ellentétben a kiválaszott opcióval.
                 ;  Ezt elkerülendő, az elem a változásait az {:on-input #(...)}
                 ;  függvény használatával kezeli.
                 ;
                 ;  A React hibás input elemként értelmezi, az {:on-change #(...)}
                 ;  függvény nélküli input elemeket.
                 :on-input   (on-change-function field-id field-props)
                 :on-change #(let [])}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn static-adornment-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; @param (map) adornment-props
  ;  {:icon (keyword)(opt)
  ;   :icon-family (keyword)(opt)}
  ;
  ; @return (map)
  ;  {:data-selectable (boolean)
  ;   :data-icon-family (keyword)}
  [_ _ {:keys [icon icon-family]}]
  (if icon {:data-selectable  false
            :data-icon-family icon-family}
           {:data-selectable  false}))

(defn button-adornment-attributes
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
  ;   :on-click (metamorphic-event)
  ;   :tab-indexed? (boolean)(opt)
  ;    Default: true
  ;   :tooltip (metamorphic-content)(opt)}
  ;
  ; @return (map)
  ;  {}
  [_ _ {:keys [disabled? icon icon-family on-click tab-indexed? tooltip]}]
  ; BUG#2105
  ; A *-field elemhez adott field-adornment-button gombon történő on-mouse-down esemény
  ; a mező on-blur eseményének triggerelésével jár, ami a mezőhöz esetlegesen használt surface
  ; felület React-fából történő lecsatolását okozná.
  (merge {:data-clickable  true
          :data-selectable false
          :on-mouse-down #(.preventDefault %)
          :title          (components/content tooltip)}
         (if     icon         {:data-icon-family icon-family})
         (if     disabled?    {:disabled   "1" :data-disabled true})
         (if-not tab-indexed? {:tab-index "-1"})
         (if-not disabled?    {:on-mouse-up #(do (a/dispatch on-click)
                                                 (environment/blur-element!))})))

(defn adornment-placeholder-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (map)
  ;  {:on-mouse-down (function)}
  [field-id field-props]
  {:on-mouse-down (fn [e] (.preventDefault e)
                          (a/dispatch-fx [:elements.text-field/focus-field!  field-id field-props])
                          (a/dispatch    [:elements.text-field/show-surface! field-id]))})

(defn empty-field-adornment-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (map)
  ;  {}
  [field-id field-props]
  {:disabled? (field-empty? field-id)
   :icon      :close
   :on-click  [:elements.text-field/empty-field! field-id field-props]
   :tooltip   :empty-field!})
