
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.06.07
; Description:
; Version: v0.3.0
; Compatibility: x4.2.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.engine.context-surface
    (:require [mid-fruits.candy              :refer [param]]
              [x.app-core.api                :as a :refer [r]]
              [x.app-db.api                  :as db]
              [x.app-elements.engine.element :as element]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (string)
(def DROPDOWN-CLASS ".x-element--context-surface")

; @constant (function)
(def CLOSE-ON-CLICK-LISTENER
     #(let [target  (.-target %)
            closest (.closest target DROPDOWN-CLASS)]
          ; Ha nem egy context-surface elemen történt a kattintás ...
          (if-not (some? closest)
                  (a/dispatch [:x.app-elements/close-all-context-surface!]))))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :x.app-elements/render-context-surface!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  (fn [{:keys [db]} [event-id element-id]]
      (let [partition-id :x.app-elements.engine.element/elements]
           {:dispatch-n [[:x.app-environment.event-handler/add-event-listener!
                          "click"       CLOSE-ON-CLICK-LISTENER "x-document-element"]
                         [:x.app-environment.event-handler/add-event-listener!
                          "contextmenu" CLOSE-ON-CLICK-LISTENER "x-document-element"]
                         [:x.app-elements/set-element-prop! element-id :render-context-surface? true]]

            ; DEBUG
            ;  Ha egy elemen ki van renderelve az elem context-surface tartalma,
            ;  amikor egy másik elem azonosítójával megtörténik az [.../render-context-surface! ...]
            ;  esemény, akkor nem minden esetben záródik be a korábban kirenderelt
            ;  context-surface.
            ;  Hogy ne legyen egyszerre egynél több context-surface kirenderelve,
            ;  minden újabb context-surface kirenderelés előtt biztosítani szükséges,
            ;  hogy a többi context-surface bezáródjon.
            :db (r db/apply-data-items! db partition-id dissoc :render-context-surface?)})))

(a/reg-event-fx
  :x.app-elements/close-context-surface!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  (fn [{:keys [db]} [_ element-id]]
      (let [partition-id :x.app-elements.engine.element/elements]
           {:dispatch-n [[:x.app-environment.event-handler/remove-event-listener!
                          "click"       CLOSE-ON-CLICK-LISTENER "x-document-element"]
                         [:x.app-environment.event-handler/remove-event-listener!
                          "contextmenu" CLOSE-ON-CLICK-LISTENER "x-document-element"]]
            :db (r element/set-element-prop! db element-id :render-context-surface? false)})))

(a/reg-event-fx
  :x.app-elements/close-all-context-surface!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; Mivel ha egy event-listener paraméterezhető függvényként kerül átadásra
  ; az [:x.app-environment.event-handler/add-event-listener!] eseménynek, akkor
  ; nem eltávolítható. Emiatt szükséges paraméterezés nélkül átadni a listener
  ; függvényt és a [:x.app-elements/close-context-surface! element-id]
  ; esemény helyett a [:x.app-elements/close-all-context-surface!]
  ; eseményt használni.
  (fn [{:keys [db]} _]
      (let [partition-id :x.app-elements.engine.element/elements]
           {:dispatch-n [[:x.app-environment.event-handler/remove-event-listener!
                          "click"       CLOSE-ON-CLICK-LISTENER "x-document-element"]
                         [:x.app-environment.event-handler/remove-event-listener!
                          "contextmenu" CLOSE-ON-CLICK-LISTENER "x-document-element"]]
            :db (r db/apply-data-items! db partition-id dissoc :render-context-surface?)})))
