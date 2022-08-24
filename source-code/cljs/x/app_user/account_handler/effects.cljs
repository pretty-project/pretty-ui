
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-user.account-handler.effects
    (:require [x.app-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :user/authenticate!
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
  (fn [_ [_ login-data]]
      [:sync/send-request! :user/authenticate!
                           {:method       :post
                            :on-success   [:boot-loader/restart-app!]
                            :on-failure   [:user/reg-login-attempt!]
                            :params       login-data
                            :uri          "/user/authenticate"
                            :idle-timeout 3000}]))

(a/reg-event-fx
  :user/logout!
  ; @usage
  ;  [:user/logout!]
  [:sync/send-request! :user/logout!
                       {:method     :post
                        :on-failure [:ui/render-bubble!        {:content :logout-failed}]
                        :on-success [:boot-loader/restart-app! {:restart-target "/login"}]
                        :uri        "/user/logout"}])
