
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.value-editor.sample
    (:require [plugins.value-editor.api :as value-editor]
              [x.app-core.api           :as a]))



;; -- A plugin elindítása -----------------------------------------------------
;; ----------------------------------------------------------------------------

; - Az {:edit-original? true} beállítás használatával a {:value-path ...} tulajdonságként átadott
;   Re-Frame adatbázis útvonalon tárolt érték szerkesztése közben, az aktuális érték az útvonalra íródik.
; - Az {:on-save ...} tulajdonságként átadott esemény a mentés gomb megnyomásakor történik meg,
;   és használatával egyedi mentési eljárás is megvalósítható.
(a/reg-event-fx
  :load-my-value-editor!
  [:value-editor/load-editor! :my-editor {:edit-original? true
                                          :on-save    [:my-event]
                                          :value-path [:my-item]}])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :my-event
  (fn [_ [_ my-value]]
      (println my-value)))
