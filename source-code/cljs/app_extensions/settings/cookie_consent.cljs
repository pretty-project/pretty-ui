
(ns app-extensions.settings.cookie-consent
    (:require [x.app-core.api        :as a :refer [r]]
              [x.app-elements.api    :as elements]
              [x.app-environment.api :as environment]
              [x.app-ui.api          :as ui]
              [app-extensions.settings.cookie-settings :rename {body cookie-settings}]))



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



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- render-cookie-consent?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (and      (r ui/application-interface?              db)
       (not (r environment/necessary-cookies-enabled? db))))



;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- got-it-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/button ::got-it-button
                   {:label    :got-it!
                    :preset   :close-button
                    :variant  :transparent
                    :on-click {:dispatch-n [[:ui/close-popup! :settings.cookie-consent/view]
                                            [:environment/cookie-settings-changed]]}}])

(defn header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  [elements/horizontal-polarity ::header
                                {:end-content [got-it-button]}])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :settings.cookie-consent/render-consent!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:ui/add-popup! :settings.cookie-consent/view
                  {:body   #'cookie-settings
                   :header #'header
                   :horizontal-align :left
                   :user-close?      false}])

(a/reg-event-fx
  :settings.cookie-consent/initialize-consent!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {:dispatch-if [(r render-cookie-consent? db)
                     {; BUG#2457
                      :dispatch-later [{:ms BOOT-RENDERING-DELAY :dispatch [:settings.cookie-consent/render-consent!]}]}]}))

;(a/reg-lifecycles!
;  ::lifecycles
;  {:on-app-launch [:settings.cookie-consent/initialize-consent!]})
