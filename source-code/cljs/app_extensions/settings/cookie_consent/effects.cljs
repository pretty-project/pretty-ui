
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-extensions.settings.cookie-consent.effects
    (:require [x.app-core.api :as a :refer [r]]
              [app-extensions.settings.cookie-settings.views :as cookie-settings.views]
              [app-extensions.settings.cookie-consent.views  :as cookie-consent.views]))



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



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (ms)
;  BUG#2457
;  A cookie-consent popup nem renderelődhet ki a legelőször kirenderelt surface előtt!
(def BOOT-RENDERING-DELAY 1000)



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :settings.cookie-consent/render-consent!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:ui/add-popup! :settings.cookie-consent/view
                  {:body   #'cookie-settings.views/body
                   :header #'cookie-consent.views/header
                   :horizontal-align :left
                   :user-close?      false}])

(a/reg-event-fx
  :settings.cookie-consent/init-consent!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {:dispatch-if [(r cookie-consent.subs/render-consent? db)
                     {; BUG#2457
                      :dispatch-later [{:ms BOOT-RENDERING-DELAY
                                        :dispatch [:settings.cookie-consent/render-consent!]}]}]}))
