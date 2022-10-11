
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-core.transfer-handler.effects
    (:require [mid-fruits.map                     :as map]
              [re-frame.api                       :as r :refer [r]]
              [x.app-core.transfer-handler.events :as transfer-handler.events]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :core/synchronize-app!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (component) app
  ;
  ; @usage
  ;  [:core/synchronize-app! #'app]
  (fn [cofx [_ app]]
      [:sync/send-request! :core/synchronize-app!
                           {:method     :get
                            :on-failure [:core/error-catched {:cofx cofx :error "Failed to synchronize app!"}]
                            :on-success [:core/app-synchronized app]
                            :uri        "/synchronize-app"}]))

(r/reg-event-fx :core/app-synchronized
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (component) app
  ;
  ; @usage
  ;  [:core/app-synchronized #'app {...}]
  (fn [{:keys [db] :as cofx} [_ app server-response]]
      {:db          (r transfer-handler.events/store-transfer-data! db server-response)
       :dispatch-if [(-> server-response map/nonempty? not)
                     [:core/error-catched {:cofx cofx :error "Failed to synchronize app!"}]
                     [:boot-loader/app-synchronized app]]}))
