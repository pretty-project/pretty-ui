
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.20
; Description:
; Version: v0.5.4
; Compatibility: x3.9.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-developer.cheat-codes
    (:require [mid-fruits.candy  :refer [param]]
              [mid-fruits.vector :as vector]
              [x.app-core.api    :as a :refer [r]]
              [x.app-db.api      :as db]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (vector)
;  A-Z: [65 66 67 ... 91]
(def KEY-CODES-RANGE (vec (range 65 91)))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- cheat-path
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (integers in vector)
  ;
  ; @return (vector)
  [key-codes]
  (db/path ::cheat-codes key-codes))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-keys-pressed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (integer) last-pressed-key-code
  ;
  ; @return (vector)
  [db [_ last-pressed-key-code]]
  (let [pressed-keys (get-in db (db/meta-item-path ::cheat-codes :pressed-keys))]
       (vector/conj-item pressed-keys last-pressed-key-code)))

(defn- get-cheat-event
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (integers in vector) keys-pressed
  ;
  ; @return (metamorphic-event)
  [db [_ keys-pressed]]
  (get-in db (cheat-path keys-pressed)))

(defn- countdown-started?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (boolean)
  [db _]
  (boolean (get-in db (db/meta-item-path ::cheat-codes :countdown-started?))))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :x.app-developer/reg-cheat!
  ; @param (integers in vector) key-codes
  ; @param (metamorphic-event) event
  ;
  ; @usage
  ;  [:x.app-developer/reg-cheat! [71 79 68] [::god-mode!]]
  (fn [{:keys [db]} [_ key-codes event]]
      {:x.app-db [[:set-item! (cheat-path key-codes) event]]}))

(a/reg-event-fx
  :x.app-developer/resolve-cheat!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (integer) key-code
  (fn [{:keys [db]} [_ key-code]]
      (if-let [cheat-event (r get-cheat-event db (r get-keys-pressed db key-code))]
              ; If cheat-code exists...
              {:dispatch cheat-event}
              ; If cheat-code doesn't exists ...
              ; Registrate the pressed key
              {:x.app-db [[:apply! (db/meta-item-path ::cheat-codes :pressed-keys)
                                   vector/conj-item key-code]]})))

(a/reg-event-fx
  :x.app-developer/clean-cheat!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (if-not (r countdown-started? db)
              {:dispatch-later
                ; 1.    0ms
               [{:ms    0 :dispatch
                 [:x.app-db/set-item! (db/meta-item-path ::cheat-codes :countdown-started?)
                                      (param true)]}
                ; 2. 2000ms
                {:ms 2000 :dispatch
                 [:x.app-db/remove-item! (db/meta-item-path ::cheat-codes :pressed-keys)]}
                ; 3. 2050ms
                {:ms 2050 :dispatch
                 [:x.app-db/remove-item! (db/meta-item-path ::cheat-codes :countdown-started?)]}]})))

(a/reg-event-fx
  :x.app-developer/listen-to-cheat!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (integer) key-code
  (fn [_ [_ key-code]]
      {:dispatch-n [[:x.app-developer/resolve-cheat! key-code]
                    [:x.app-developer/clean-cheat!]]}))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :x.app-developer/reg-cheat-code-listeners!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  {:dispatch-n (reduce #(vector/conj-item %1
                         [:x.app-environment.keypress-handler/reg-keydown-event!
                          ::keypress-listener
                          {:key-code %2 :event [:x.app-developer/listen-to-cheat! %2]}])
                        (param [])
                        (param KEY-CODES-RANGE))})
