

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns settings.cookie-consent.effects
    (:require [settings.cookie-settings.views :as cookie-settings.views]
              [settings.cookie-consent.config :as cookie-consent.config]
              [settings.cookie-consent.subs   :as cookie-consent.subs]
              [settings.cookie-consent.views  :as cookie-consent.views]
              [x.app-core.api                 :as a :refer [r]]))



;; -- Descriptions ------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @description
;  A cookie-consent felület applikáció módban elindított (bejelentkezett)
;  rendszer esetén használható. A webhely módban elindított rendszer számára
;  egyedileg a webhely számára szükséges cookie-consent felületet készíteni,
;  mivel a felületen megjelenő Adatvédelmi irányelvek és Felhasználási feltételek
;  hivatkozások az applikáció vonatkozó tartalmaira mutatnak.

; @description
;  WARNING! XXX#0459
;   A cookie-consent popup a React-fába csatolásakor a cookie beállítások
;   értékeit elmenti a Re-Frame adatbázisba!
;   A cookie-handler pedig a Re-Frame adatbázisból állapítja meg,
;   hogy engedélyezett-e a cookie-k használata!



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :settings.cookie-consent/init-consent!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {:dispatch-if [(r cookie-consent.subs/render-consent? db)
                     {; BUG#2457
                      :dispatch-later [{:ms cookie-consent.config/BOOT-RENDERING-DELAY
                                        :dispatch [:settings.cookie-consent/render-consent!]}]}]}))

(a/reg-event-fx
  :settings.cookie-consent/render-consent!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:ui/render-popup! :settings.cookie-consent/view
                     {:body             #'cookie-settings.views/body
                      :header           #'cookie-consent.views/header
                      :horizontal-align :left
                      :user-close?      false}])
