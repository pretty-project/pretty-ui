
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-core.load-handler.subs
    (:require [mid-fruits.time                :as time]
              [x.app-core.event-handler       :refer [r]]
              [x.app-core.load-handler.config :as load-handler.config]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-load-status
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (keyword)
  [db _]
  (get-in db [:core :load-handler/meta-items :load-status]))

(defn get-signal-count
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (integer)
  [db _]
  (count (get-in db [:core :load-handler/data-items])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn app-loading?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (boolean)
  [db _]
  (let [load-status (r get-load-status db)]
       (= load-status :loading)))

(defn all-synchron-signal-ended?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (boolean)
  [db _]
  (let [signal-count (r get-signal-count db)]
       (= 0 signal-count)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-elapsed-time
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (ms)
  ;  Az applikáció betöltésének kezdete óta eltelt idő
  [db _]
  (let [elapsed-time    (time/elapsed)
        load-started-at (get-in db [:core :load-handler/meta-items :load-started-at])]
       (- elapsed-time load-started-at)))

(defn load-timeout-reached?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (boolean)
  [db _]
  (let [elapsed-time (r get-elapsed-time db)]
       (> elapsed-time load-handler.config/LOAD-TIMEOUT)))

(defn load-timeout-error?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (boolean)
  [db _]
  (and (r load-timeout-reached? db)
       (r app-loading?          db)))
