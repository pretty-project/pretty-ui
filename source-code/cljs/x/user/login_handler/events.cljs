
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.user.login-handler.events
    (:require [mid-fruits.map  :refer [dissoc-in]]
              [re-frame.api    :as r]
              [time.api        :as time]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reg-login-failure!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (integer) status
  ;
  ; @return (map)
  [db [_ status]]
  (assoc-in db [:x.user :login-handler/meta-items :login-failure]
               {:timestamp (time/elapsed)
                :status    status}))

(defn clear-login-failure!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (dissoc-in db [:x.user :login-handler/meta-items :login-failure]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-event-db :x.user/reg-login-failure! reg-login-failure!)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-event-db :x.user/clear-login-failure! clear-login-failure!)
