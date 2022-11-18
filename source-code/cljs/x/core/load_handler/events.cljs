
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.core.load-handler.events
    (:require [candy.api                :refer [return]]
              [time.api                 :as time]
              [re-frame.api             :as r :refer [r]]
              [vector.api               :as vector]
              [x.core.load-handler.subs :as load-handler.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reg-load-started!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (assoc-in db [:x.core :load-handler/meta-items :load-started-at]
               (time/elapsed)))

(defn set-load-status!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) load-status
  ;
  ; @return (map)
  [db [_ load-status]]
  (assoc-in db [:x.core :load-handler/meta-items :load-status] load-status))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn start-synchron-signal!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) signal-id
  ;
  ; @return (map)
  [db [_ signal-id]]
  (update-in db [:x.core :load-handler/data-items] vector/conj-item signal-id))

(defn end-synchron-signal!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) signal-id
  ;
  ; @return (map)
  [db [_ signal-id]]
  (update-in db [:x.core :load-handler/data-items] vector/remove-item signal-id))

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
(r/reg-event-db :x.core/set-load-status! set-load-status!)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-event-db :x.core/stop-loading! stop-loading!)