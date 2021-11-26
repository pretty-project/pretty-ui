
(ns extensions.settings.cookie-consent
    (:require [x.app-core.api        :as a :refer [r]]
              [x.app-elements.api    :as elements]
              [x.app-environment.api :as environment]
              [extensions.settings.cookie-settings :rename {body cookie-settings}]))



;; -- Descriptions ------------------------------------------------------------
;; ----------------------------------------------------------------------------

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



;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- got-it-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [header-id]
  [elements/button ::got-it-button
                   {:label   :got-it!
                    :preset  :close-button
                    :variant :transparent
                    :on-click {:dispatch-n [[:x.app-ui/close-popup! header-id]
                                            [::accept-cookie-settings!]]}}])

(defn- header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [header-id]
  [elements/polarity ::header
                     {:end-content [got-it-button header-id]}])



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ::accept-cookie-settings!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:environment/->cookie-settings-changed])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ::render!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:x.app-ui/add-popup! ::view
                        {:content          #'cookie-settings
                         :horizontal-align :left
                         :label-bar        {:content #'header}
                         :layout           :boxed
                         :user-close?      false}])

(a/reg-event-fx
  ::initialize!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {:dispatch-if [(not (r environment/necessary-cookies-enabled? db))
                     ; BUG#2457
                     {:dispatch-later [{:ms BOOT-RENDERING-DELAY :dispatch [::render!]}]}]}))

(a/reg-lifecycles
  ::lifecycles
  {:on-app-launch [::initialize!]})
