
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-user.login-handler.effects
    (:require [re-frame.api                    :as r :refer [r]]
              [x.app-user.login-handler.events :as login-handler.events]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :user/authenticate!
  ; A) Sikeres bejelentkezés
  ;    A bejelentkező felületen lévő bejelentkezés gombra kattintva, a gomb
  ;    várakozó állapotba lép. Ahhoz, hogy sikeres bejelentkezés után,
  ;    de még a rendszer indulása előtt, amíg látható a bejelentkező felület,
  ;    ne lehessen újból a bejelentkezés gombot megnyomni, szükséges
  ;    az {:idle-timeout ...} értékét az alapbeállításnál magasabbra állítani.
  ;
  ; B) Sikertelen bejelentkezés
  ;    A szerver válaszának megérkezésekor, sikertelen bejelentkezés esetén
  ;    egy hibaüzenet jelenik meg a bejelentkező felületen.
  ;    A szerver válaszának megérkezésekor elinduló {:idle-timeout ...} idő
  ;    letelte után lehetséges a bejelentkezés gombot újból megnyomni.
  ;
  ; @param (map) login-data
  ;  {:email-address (string)
  ;   :password (string)}
  ;
  ; @usage
  ;  [:user/authenticate! {:email-address "hello@domain.com" :password "my-password"}]
  (fn [{:keys [db]} [_ login-data]]
      {:db (r login-handler.events/clear-login-failure! db)
       :dispatch [:sync/send-request! :user/authenticate!
                                      {:method       :post
                                       :on-success   [:boot-loader/restart-app!]
                                       :on-failure   [:user/authentication-failed]
                                       :params       login-data
                                       :uri          "/user/authenticate"
                                       :idle-timeout 3000}]}))

(r/reg-event-fx :user/authentication-failed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) server-response
  ;  {:status (integer)}
  (fn [_ [_ {:keys [status]}]]
      (println (integer? status))
      [:user/reg-login-failure! status]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :user/logout!
  ; @usage
  ;  [:user/logout!]
  [:sync/send-request! :user/logout!
                       {:method     :post
                        :on-failure [:user/logout-failed]
                        :on-success [:boot-loader/restart-app! {:restart-target "/login"}]
                        :uri        "/user/logout"}])

(r/reg-event-fx :user/logout-failed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) server-response
  [:ui/render-bubble! ::notification {:content :logout-failed}])
