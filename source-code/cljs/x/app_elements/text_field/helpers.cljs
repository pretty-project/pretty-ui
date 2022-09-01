
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.text-field.helpers
    (:require [dom.api :as dom]
              [mid-fruits.string :as string]
              [mid-fruits.time :as time]

              [x.app-elements.engine.api :as engine]

              [x.app-components.api :as components]
              [x.app-core.api :as a]
              [x.app-environment.api :as environment]
              [x.app-elements.focus-handler.side-effects :as focus-handler.side-effects]

              [x.app-elements.text-field.config :as text-field.config]
              [x.app-elements.text-field.state :as text-field.state]

              [mid-fruits.candy :refer [param]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-filled?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (boolean)
  [field-id]
  (let [field-value (get-in @text-field.state/FIELD-VALUES [field-id :value])]
       (string/nonempty? field-value)))

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

(defn get-field-value
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (string)
  [field-id]
  (get-in @text-field.state/FIELD-VALUES [field-id :value]))

(defn set-field-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; @param (string) value
  [field-id _ value]
  (swap! text-field.state/FIELD-VALUES assoc-in [field-id :value] value))

(defn store-field-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; @param (string) value
  [field-id field-props value]
  (let [now        (time/elapsed)
        type-ended (get-in @text-field.state/FIELD-VALUES [field-id :type-ended])]
       (when (> now (+ type-ended text-field.config/TYPE-ENDED-AFTER))
             (a/dispatch-sync [:elements.text-field/field-changed field-id field-props value]))))

(defn on-change-function
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {:modifier (function)(opt)}
  ;
  ; @return (function)
  [field-id {:keys [modifier] :as field-props}]
  #(let [now   (time/elapsed)
         value (if modifier (-> % dom/event->value modifier)
                            (-> % dom/event->value))]
        (swap! text-field.state/FIELD-VALUES assoc field-id {:type-ended now :value value})
        (letfn [(f [] (store-field-value! field-id field-props value))]
               (time/set-timeout! f text-field.config/TYPE-ENDED-AFTER))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [field-id {:keys [border-color max-width stretch-orientation] :as field-props}]
  (merge (engine/element-default-attributes field-id field-props)
         (engine/element-indent-attributes  field-id field-props)
         {:data-border-color        border-color
          :data-max-width           max-width
          :data-stretch-orientation stretch-orientation}))
;

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
  (merge {:on-mouse-down #(.preventDefault %)
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
  ;  {}
  [field-id _]
  {:on-mouse-down #(.preventDefault %)
   :on-mouse-up   #(focus-handler.side-effects/focus-element! field-id)})

(defn field-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {:disabled? (boolean)(opt)
  ;   :max-length (integer)(opt)
  ;   :name (keyword)
  ;   :type (keyword)(opt)
  ;    :password, :text
  ;   :value (string)}
  ;
  ; @return (map)
  ;  {:auto-complete (keyword)
  ;   :disabled (boolean)
  ;   :id (string)
  ;   :max-length (integer)
  ;   :name (keyword)
  ;   :on-blur (function)
  ;   :on-focus (function)
  ;   :on-change (function)
  ;   :style (map)}
  [field-id {:keys [disabled? max-length name surface type] :as field-props}]
          ; Az x4.4.9 verzióig az elemek target-id azonosítása a {:targetable? ...}
          ; tulajdonságuk értékétől függött. Az x4.4.9 verzió óta a target-id azonosítás
          ; minden esetben elérhető.
          ; A field típusú elemek target-id azonosítása nem kizárólag a {:targetable? ...}
          ; tulajdonságuk függvénye volt. A {:disabled? true} állapotban levő field elemek
          ; nem voltak azonosíthatók target-id használatával. Az x4.4.9 verzióban ez a feltétel
          ; (indoklás és ismert felhasználás hiányában) eltávolításra került.

  (cond-> {:id (a/dom-value field-id "input")}

          ; If field is disabled ...
          (boolean disabled?) (merge {:disabled true
                                      :type     type
                                      :value    (get-field-value field-id)
                                      ;:style    (field-props->field-style field-props)
                                      ; BUG#8809
                                      ;  Ha a mező disabled állapotba lépéskor elveszítené az on-change tulajdonságát,
                                      ;  akkor a React figyelmeztetne, hogy controlled elemből uncontrolled elemmé változott!
                                      :on-change #(let [])})
          ; If field is NOT disabled ...
          (not disabled?) (merge {:auto-complete name
                                  :max-length    max-length
                                  :name          name
                                  :type          type
                                  :value    (get-field-value field-id)
                                  :on-blur  #(a/dispatch [:elements.text-field/field-blurred field-id field-props])
                                  :on-focus #(a/dispatch [:elements.text-field/field-focused field-id field-props])
                                  ;:style    (field-props->field-style field-props)
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
                                  ;  Pl.: a combo-box elem opciós listájából kiválasztott opció,
                                  ;  ami az elem {:value-path [...]} ... útvonalon tárolódik,
                                  ;  felülíródik az input tartalmával, ami minden esetben string
                                  ;  típus, ellentétben a kiválaszott opcióval.
                                  ;  Ezt elkerülendő, az elem a változásait az {:on-input #(...)}
                                  ;  függvény használatával kezeli.
                                  :on-input (on-change-function field-id field-props)
                                  ; BUG#8041
                                  ;  A React hibás input elemként értelmezi, az {:on-change #(...)}
                                  ;  függvény nélküli input elemeket.
                                  :on-change #(let [])})))
          ; If field has surface ...
          ;(some? surface) (merge {:on-mouse-down #(a/dispatch [:elements/show-surface! field-id])})))
