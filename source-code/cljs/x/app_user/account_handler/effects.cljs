
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-user.account-handler.effects
    (:require [x.app-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :user/authenticate!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
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
  [:sync/send-request! :user/authenticate!
                       {:method       :post
                        :on-success   [:boot-loader/restart-app!]
                        :on-failure   [:user/reg-login-attempt!]
                        :source-path  [:views :login-box/data-items]
                        :uri          "/user/authenticate"
                        :idle-timeout 3000}])

(a/reg-event-fx
  :user/logout!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:sync/send-request! :user/logout!
                       {:method :post :uri "/user/logout"
                        :on-failure [:ui/blow-bubble!          {:content :logout-failed :color :warning}]
                        :on-success [:boot-loader/restart-app! {:restart-target "/login"}]}])
