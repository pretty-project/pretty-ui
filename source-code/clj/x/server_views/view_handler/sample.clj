
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-views.view-handler.sample)



;; -- Hiba-képernyő beállítása ------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :my-error-screen
  (fn [_ [_ error-id]]
      [:ui/render-surface :my-error-screen
                          {:content [:div "My error screen"]}]))

(a/reg-event-fx
  :set-my-error-event!
  [:views/set-error-screen! [:my-error-screen]])



;; -- Bejelentkező-képernyő beállítása ----------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :my-login-screen
  [:ui/render-surface :my-login-screen
                      {:content [:div "My login screen"]}])

(a/reg-event-fx
  :set-my-login-event!
  [:views/set-login-screen! [:my-login-screen]])

  

;; -- Menü-képernyő beállítása ------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :my-menu-screen
  [:ui/render-surface :my-menu-screen
                      {:content [:div "My menu screen"]}])

(a/reg-event-fx
  :set-my-login-event!
  [:views/set-menu-screen! [:my-menu-screen]])
