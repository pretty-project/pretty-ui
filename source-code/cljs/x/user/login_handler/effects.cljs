
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.user.login-handler.effects
    (:require [re-frame.api                :as r :refer [r]]
              [x.user.login-handler.events :as login-handler.events]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :x.user/authenticate!
  ; (A) Sikeres bejelentkezés
  ;     A bejelentkező felületen lévő bejelentkezés gombra kattintva, a gomb
  ;     várakozó állapotba lép. Ahhoz, hogy sikeres bejelentkezés után,
  ;     de még a rendszer indulása előtt, amíg látható a bejelentkező felület,
  ;     ne lehessen újból a bejelentkezés gombot megnyomni, szükséges
  ;     az {:idle-timeout ...} értékét az alapbeállításnál magasabbra állítani.
  ;
  ; (B) Sikertelen bejelentkezés
  ;     A szerver válaszának megérkezésekor, sikertelen bejelentkezés esetén
  ;     egy hibaüzenet jelenik meg a bejelentkező felületen.
  ;     A szerver válaszának megérkezésekor elinduló {:idle-timeout ...} idő
  ;     letelte után lehetséges a bejelentkezés gombot újból megnyomni.
  ;
  ; @param (map) authenticate-props
  ;  {:email-address (string)
  ;   :on-failure (metamorphic-event)(opt)
  ;    Az esemény utolsó paraméterként megkapja a szerver-válasz állapotkódját
  ;   :password (string)}
  ;
  ; @usage
  ;  [:x.user/authenticate! {:email-address "hello@domain.com" :password "my-password"}]
  (fn [{:keys [db]} [_ authenticate-props]]
      {:db       (r login-handler.events/clear-login-failure! db)
       :dispatch [:x.sync/send-request! :x.user/authenticate!
                                        {:method       :post
                                         :on-success   [:x.boot-loader/restart-app!]
                                         :on-failure   [:x.user/authentication-failed authenticate-props]
                                         :params       (select-keys authenticate-props [:email-address :password])
                                         :uri          "/user/authenticate"
                                         :idle-timeout 3000}]}))

(r/reg-event-fx :x.user/authentication-failed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) authenticate-props
  ;  {:on-failure (metamorphic-event)(opt)
  ;    Az esemény utolsó paraméterként megkapja a szerver-válasz állapotkódját}
  ; @param (map) server-response
  ;  {:status (integer)}
  (fn [_ [_ {:keys [on-failure]} {:keys [status]}]]
      {:dispatch-n [[:x.user/reg-login-failure! status]
                    (if on-failure (r/metamorphic-event<-params on-failure status))]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :x.user/logout!
  ; @usage
  ;  [:x.user/logout!]
  [:x.sync/send-request! :x.user/logout!
                         {:method     :post
                          :on-failure [:x.user/logout-failed]
                          :on-success [:x.boot-loader/restart-app! {:restart-target "/login"}]
                          :uri        "/user/logout"}])

(r/reg-event-fx :x.user/logout-failed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) server-response
  [:x.ui/render-bubble! ::notification {:content :logout-failed}])
