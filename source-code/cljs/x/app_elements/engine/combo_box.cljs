
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.07.02
; Description:
; Version: v0.9.8
; Compatibility: x4.4.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.engine.combo-box
    (:require [mid-fruits.candy                      :refer [param return]]
              [mid-fruits.keyword                    :as keyword]
              [mid-fruits.string                     :as string]
              [mid-fruits.vector                     :as vector]
              [x.app-core.api                        :as a :refer [r]]
              [x.app-elements.surface-handler.events :as surface-handler.events]
              [x.app-elements.surface-handler.subs   :as surface-handler.subs]
              [x.app-elements.engine.element         :as element]
              [x.app-elements.engine.field           :as field]
              [x.app-elements.engine.input           :as input]
              [x.app-elements.engine.input-group     :as input-group]
              [x.app-elements.engine.selectable      :as selectable]))



;; -- Combo box ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; A combo-box elem összetettségéből adódóan saját névteret (eszközkészletet)
; igényel, mivel az x.app-elements.engine.field eszközök normál szövegmező
; készítéséhez alkalmasak.
;
; A multi-combo-box egy chip-group és egy combo-box inputok csoportja, amelyek
; rendelkeznek saját egyedi azonosítóval és egy közös group-id azonosítóval.
; A multi-combo-box az input-group eszközkészletet alkalmazza.

; TODO
; A multi-combo-box elem surface felületén ne jelenjenek meg azok az opciók,
; amelyek már ki vannak választva.
; Pl.: Most a kiválasztottak lehetnek olyan elemek, amelyek az options listában
;      is megjelennek és olyanok is, amik a mező értékéből kerültek kiválasztásra.
;      Ezért a kiválasztott elemek listája és az opciók listája eltérhet.



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-highlighted-option-dex
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (integer)
  [db [_ field-id]]
  (r element/get-element-subprop db field-id [:surface-props :highlighted-option]))

(defn any-option-highlighted?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (integer)
  [db [_ field-id]]
  (let [highlighted-option-dex (r get-highlighted-option-dex db field-id)]
       (vector/item-dex? highlighted-option-dex)))

(defn get-combo-box-value
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (string)
  [db [_ field-id]]
  (if-let [value (r input/get-input-value db field-id)]
          (if (string? value)
              (return  value)
              (let [get-label-f (r element/get-element-prop db field-id :get-label-f)]
                   (get-label-f value)))))

(defn get-multi-combo-box-value
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (string)
  [db [_ field-id]]
  ; A multi-combo-box mezőbe írás közben a "," billentyű leütése hozzáadja a kiválasztott
  ; opciók listájához a mező tartalmát, amihez szükséges a tartalom végéről a "," karaktert
  ; eltávolítani!
  (if-let [value (r get-combo-box-value db field-id)]
          (string/not-ends-with! value ",")))

(defn combo-box-filled?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (string)
  [db [_ field-id]]
  (let [combo-box-value (r get-combo-box-value db field-id)]
       (string/nonempty? combo-box-value)))

(defn combo-box-empty?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (string)
  [db [_ field-id]]
  (let [combo-box-filled? (r combo-box-filled? db field-id)]
       (not combo-box-filled?)))

(defn render-combo-box-option?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (*) option
  ;
  ; @return (boolean)
  [db [_ field-id option]]
  ; A combo-box elem React-fába csatolása után van olyan pillanat, amikor az elem
  ; feliratkozása már aktív, de az elem tulajdonságai még nem elérhetők
  ; a Re-Frame adatbázisban. És a get-label-f értéke nem lehet nil!
  (if-let [get-label-f (r element/get-element-prop db field-id :get-label-f)]
          (let [value (r get-combo-box-value db field-id)]
               (and (not (string/pass-with? option value {:case-sensitive? false}))
                    (string/starts-with? (get-label-f option)
                                         (param       value)
                                         {:case-sensitive? false})))))

(defn get-combo-box-rendered-option-data
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (*) option
  ; @param (integer) render-dex
  ;
  ; @return (map)
  ;  {:option (*)
  ;   :highlighted? (boolean)
  ;   :rendered? (boolean)}
  [db [_ field-id option render-dex]]
  (let [highlighted-option-dex (r get-highlighted-option-dex db field-id)
        option-highlighted?    (= render-dex highlighted-option-dex)]
       {:option       option
        :highlighted? option-highlighted?
        :rendered?    true}))

(defn get-combo-box-non-rendered-option-data
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (*) option
  ;
  ; @return (map)
  ;  {:option (*)
  ;   :rendered? (boolean)}
  [db [_ _ option]]
  {:option    option
   :rendered? false})

(defn get-combo-box-option-data
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (*) option
  ; @param (integer) render-dex
  ;
  ; @return (map)
  [db [_ field-id option render-dex]]
  (if-let [option-rendered? (r render-combo-box-option? db field-id option)]
          (r get-combo-box-rendered-option-data     db field-id option render-dex)
          (r get-combo-box-non-rendered-option-data db field-id option)))

(defn get-combo-box-rendered-options
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; A combo-box elem option listájában megjelenített értékeket a Re-Frame
  ; feliratkozásban szükséges szűrni, hogy az [:elements/highlight-prev-option! ...]
  ; és [:elements/highlight-next-option! ...] események is ki tudják olvasni
  ; a Re-Frame adatbázisból, hogy összesen hány és melyik értékek kerülnek kirenderelésre.
  ;
  ; @param (keyword) field-id
  ;
  ; @return (maps in vector)
  ;  [{:option (*)
  ;    :highlighted? (boolean)
  ;    :rendered? (boolean)
  ;    :selected? (boolean)}]
  [db [_ field-id]]
  (let [options (r selectable/get-selectable-options db field-id)]
       (letfn [(f [rendered-options option]
                  ; render-dex: A combo-box elem surface felületén hányadik elemként
                  ; kerül kirenderelésre az opció (az opciók listájának szűrése után)
                  (let [render-dex  (count rendered-options)
                        option-data (r get-combo-box-option-data db field-id option render-dex)]
                       (if (get option-data :rendered?)
                           (conj   rendered-options option-data)
                           (return rendered-options))))]
              (reduce f [] options))))

(defn any-combo-box-option-rendered?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (boolean)
  [db [_ field-id]]
  (let [rendered-options (r get-combo-box-rendered-options db field-id)]
       (vector/nonempty? rendered-options)))

(defn get-highlighted-option
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (*)
  [db [_ field-id]]
  (if-let [highlighted-option-dex (r get-highlighted-option-dex db field-id)]
          (let [rendered-options        (r get-combo-box-rendered-options db field-id)
                highlighted-option-data (vector/nth-item rendered-options highlighted-option-dex)]
               (get highlighted-option-data :option))))

(defn get-combo-box-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (map)
  ;  {:field-empty? (boolean)
  ;   :options (vector)
  ;   :value (string)}
  [db [_ field-id]]
   ; XXX#8093
   ; A {:field-empty? ...} tulajdonság használatával állapítja meg a text-field
   ; elem, hogy melyik adornment gombot szükséges megjeleníteni.
  {:field-empty?     (r combo-box-empty?                  db field-id)
   :options          (r selectable/get-selectable-options db field-id)
   :rendered-options (r get-combo-box-rendered-options    db field-id)
   :value            (r get-combo-box-value               db field-id)})



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn discard-option-highlighter!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (map)
  [db [_ field-id]]
  (r element/remove-element-subprop! db field-id [:surface-props :highlighted-option]))

(defn use-highlighted-combo-box-option!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (map)
  [db [_ field-id]]
  (let [highlighted-option       (r get-highlighted-option   db field-id)
        get-label-f              (r element/get-element-prop db field-id :get-label-f)
        highlighted-option-label (get-label-f highlighted-option)]
       (r field/set-field-value! db field-id highlighted-option-label)))

(defn use-highlighted-multi-combo-box-option!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (map)
  [db [_ field-id]]
  (let [group-id           (r input-group/get-input-group-id db field-id)
        highlighted-option (r get-highlighted-option         db field-id)]
       (r input-group/stack-to-group-value! db group-id field-id highlighted-option)))

(defn use-multi-combo-box-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (map)
  [db [_ field-id]]
  (let [value (r get-multi-combo-box-value db field-id)]
       (if (string/nonempty? value)
           (let [group-id (r input-group/get-input-group-id db field-id)]
                (r input-group/stack-to-group-value! db group-id field-id value))
           (return db))))

(defn UP-combo-box!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (map)
  [db [_ field-id]]
  (cond (r surface-handler.subs/surface-hidden? db field-id)
        (r surface-handler.events/show-surface! db field-id)
        (r any-combo-box-option-rendered?       db field-id)
        (let [rendered-options       (r get-combo-box-rendered-options db field-id)
              highlighted-option-dex (r get-highlighted-option-dex     db field-id)
              prev-option-dex        (vector/prev-dex rendered-options highlighted-option-dex)]
             (r element/set-element-subprop! db field-id [:surface-props :highlighted-option] prev-option-dex))
        :else (r element/remove-element-subprop! db field-id [:surface-props :highlighted-option])))

(a/reg-event-db :elements/UP-combo-box! UP-combo-box!)

(defn DOWN-combo-box!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (map)
  [db [_ field-id]]
  (cond (r surface-handler.subs/surface-hidden? db field-id)
        (r surface-handler.events/show-surface! db field-id)
        (r any-combo-box-option-rendered?       db field-id)
        (let [highlighted-option-dex (r get-highlighted-option-dex db field-id)
              highlighted-option-dex (or highlighted-option-dex -1)
              rendered-options       (r get-combo-box-rendered-options db field-id)
              next-option-dex        (vector/next-dex rendered-options highlighted-option-dex)]
             (r element/set-element-subprop! db field-id [:surface-props :highlighted-option] next-option-dex))
        :else (r element/remove-element-subprop! db field-id [:surface-props :highlighted-option])))

(a/reg-event-db :elements/DOWN-combo-box! DOWN-combo-box!)

(defn ENTER-combo-box!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (map)
  [db [_ field-id]]
  (if (r any-option-highlighted? db field-id)
      (as-> db % (r use-highlighted-combo-box-option!    % field-id)
                 (r discard-option-highlighter!          % field-id)
                 (r surface-handler.events/hide-surface! % field-id))
      (as-> db % (r discard-option-highlighter!          % field-id)
                 (r surface-handler.events/hide-surface! % field-id))))

(a/reg-event-db :elements/ENTER-combo-box! ENTER-combo-box!)

(defn ENTER-multi-combo-box!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (map)
  [db [_ field-id]]
  (if (r any-option-highlighted? db field-id)
      (as-> db % (r use-highlighted-multi-combo-box-option! % field-id)
                 (r field/empty-field-value!                % field-id)
                 (r discard-option-highlighter!             % field-id)
                 (r surface-handler.events/hide-surface!    % field-id))
      (as-> db % (r use-multi-combo-box-value!              % field-id)
                 (r field/empty-field-value!                % field-id)
                 (r discard-option-highlighter!             % field-id)
                 (r surface-handler.events/hide-surface!    % field-id))))

(a/reg-event-db :elements/ENTER-multi-combo-box! ENTER-multi-combo-box!)

(defn COMMA-multi-combo-box!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (map)
  [db [_ field-id]]
  (as-> db % (r use-multi-combo-box-value!           % field-id)
             (r field/empty-field-value!             % field-id)
             (r discard-option-highlighter!          % field-id)
             (r surface-handler.events/hide-surface! % field-id)))

(a/reg-event-db :elements/COMMA-multi-combo-box! COMMA-multi-combo-box!)

(defn ESC-combo-box!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (map)
  [db [_ field-id]]
  (if-let [highlighted-option (r get-highlighted-option db field-id)]
          (r discard-option-highlighter!          db field-id)
          (r surface-handler.events/hide-surface! db field-id)))

(a/reg-event-db :elements/ESC-combo-box! ESC-combo-box!)

(defn combo-box-changed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (map)
  [db [_ field-id]]
  (r element/remove-element-subprop! db field-id [:surface-props :highlighted-option]))

(a/reg-event-db :elements/combo-box-changed combo-box-changed)



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :elements/reg-combo-box-controllers!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  (fn [_ [_ field-id]]
      {:dispatch-n [[:environment/reg-keypress-event! ::on-DOWN-pressed
                                                      {:key-code 40 :on-keydown [:elements/DOWN-combo-box!  field-id] :prevent-default? true}]
                    [:environment/reg-keypress-event! ::on-UP-pressed
                                                      {:key-code 38 :on-keydown [:elements/UP-combo-box!    field-id] :prevent-default? true}]
                    [:environment/reg-keypress-event! ::on-ESC-pressed
                                                      {:key-code 27 :on-keydown [:elements/ESC-combo-box!   field-id]}]
                    [:environment/reg-keypress-event! ::on-ENTER-pressed
                                                      {:key-code 13 :on-keydown [:elements/ENTER-combo-box! field-id]}]]}))

(a/reg-event-fx
  :elements/reg-multi-combo-box-controllers!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  (fn [_ [_ field-id]]
      {:dispatch-n [[:environment/reg-keypress-event! ::on-DOWN-pressed
                                                      {:key-code         40
                                                       :on-keydown       [:elements/DOWN-combo-box! field-id]
                                                       :prevent-default? true
                                                       :required?        true}]
                    [:environment/reg-keypress-event! ::on-UP-pressed
                                                      {:key-code         38
                                                       :on-keydown       [:elements/UP-combo-box! field-id]
                                                       :prevent-default? true
                                                       :required?        true}]
                    [:environment/reg-keypress-event! ::on-ESC-pressed
                                                      {:key-code   27
                                                       :on-keydown [:elements/ESC-combo-box! field-id]
                                                       :required?  true}]
                    [:environment/reg-keypress-event! ::on-ENTER-pressed
                                                      {:key-code   13
                                                       :on-keydown [:elements/ENTER-multi-combo-box! field-id]
                                                       :required?  true}]
                    [:environment/reg-keypress-event! ::on-COMMA-pressed
                                                      {:key-code   188
                                                       :on-keydown [:elements/COMMA-multi-combo-box! field-id]
                                                       :required?  true}]]}))

(a/reg-event-fx
  :elements/remove-combo-box-controllers!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  (fn [_ [_ field-id]]
      {:dispatch-n [[:environment/remove-keypress-event! ::on-DOWN-pressed]
                    [:environment/remove-keypress-event! ::on-UP-pressed]
                    [:environment/remove-keypress-event! ::on-ESC-pressed]
                    [:environment/remove-keypress-event! ::on-ENTER-pressed]]}))

(a/reg-event-fx
  :elements/remove-multi-combo-box-controllers!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  (fn [_ [_ field-id]]
      {:dispatch-n [[:environment/remove-keypress-event! ::on-DOWN-pressed]
                    [:environment/remove-keypress-event! ::on-UP-pressed]
                    [:environment/remove-keypress-event! ::on-ESC-pressed]
                    [:environment/remove-keypress-event! ::on-ENTER-pressed]
                    [:environment/remove-keypress-event! ::on-COMMA-pressed]]}))
