
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-tools.scheduler.subs
    (:require [mid-fruits.map :as map]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn scheduler-inited?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (boolean)
  [db _]
  (boolean (get-in db [:tools :schedules/meta-items :scheduler-inited?])))

(defn any-schedule-registered?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (boolean)
  [db _]
  (let [schedules (get-in db [:tools :schedules/data-items])]
       (map/nonempty? schedules)))
