
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-core.load-handler.events
    (:require [mid-fruits.candy             :refer [return]]
              [mid-fruits.vector            :as vector]
              [mid-fruits.time              :as time]
              [x.app-core.event-handler     :as event-handler :refer [r]]
              [x.app-core.load-handler.subs :as load-handler.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reg-load-started!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (assoc-in db [:core :load-handler/meta-items :load-started-at]
               (time/elapsed)))

(defn set-load-status!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) load-status
  ;
  ; @return (map)
  [db [_ load-status]]
  (assoc-in db [:core :load-handler/meta-items :load-status] load-status))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn start-synchron-signal!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) signal-id
  ;
  ; @return (map)
  [db [_ signal-id]]
  (update-in db [:core :load-handler/data-items] vector/conj-item signal-id))

(defn end-synchron-signal!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) signal-id
  ;
  ; @return (map)
  [db [_ signal-id]]
  (update-in db [:core :load-handler/data-items] vector/remove-item signal-id))

(defn stop-loading!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (if (r load-handler.subs/app-loading? db)
      (r set-load-status!               db :halted)
      (return                           db)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(event-handler/reg-event-db :core/set-load-status! set-load-status!)

; WARNING! NON-PUBLIC! DO NOT USE!
(event-handler/reg-event-db :core/stop-loading! stop-loading!)
