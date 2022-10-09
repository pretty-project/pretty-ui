
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-views.view-handler.sample
    (:required [re-frame.api :as r]))



;; -- Hiba-képernyő beállítása ------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :my-error-screen
  (fn [_ [_ error-id]]
      [:ui/render-surface :my-error-screen
                          {:content [:div "My error screen"]}]))

(r/reg-event-fx :set-my-error-screen!
  [:views/set-error-screen! [:my-error-screen]])



;; -- Bejelentkező-képernyő beállítása ----------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :my-login-screen
  [:ui/render-surface :my-login-screen
                      {:content [:div "My login screen"]}])

(r/reg-event-fx :set-my-login-screen!
  [:views/set-login-screen! [:my-login-screen]])



;; -- Menü-képernyő beállítása ------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :my-menu-screen
  [:ui/render-surface :my-menu-screen
                      {:content [:div "My menu screen"]}])

(r/reg-event-fx :set-my-menu-screen!
  [:views/set-menu-screen! [:my-menu-screen]])
