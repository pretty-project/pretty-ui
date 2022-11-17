
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.core.transfer-handler.effects
    (:require [map.api                        :as map]
              [re-frame.api                   :as r :refer [r]]
              [x.core.transfer-handler.events :as transfer-handler.events]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :x.core/synchronize-app!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (component) app
  ;
  ; @usage
  ;  [:x.core/synchronize-app! #'app]
  (fn [cofx [_ app]]
      [:x.sync/send-request! :x.core/synchronize-app!
                             {:method     :get
                              :on-failure [:x.core/error-catched {:cofx cofx :error "Failed to synchronize app!"}]
                              :on-success [:x.core/app-synchronized app]
                              :uri        "/synchronize-app"}]))

(r/reg-event-fx :x.core/app-synchronized
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (component) app
  ;
  ; @usage
  ;  [:x.core/app-synchronized #'app {...}]
  (fn [{:keys [db] :as cofx} [_ app server-response]]
      {:db          (r transfer-handler.events/store-transfer-data! db server-response)
       :dispatch-if [(-> server-response map/nonempty? not)
                     [:x.core/error-catched {:cofx cofx :error "Failed to synchronize app!"}]
                     [:boot-loader/app-synchronized app]]}))
