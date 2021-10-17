
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.02.29
; Description:
; Version: v1.6.8
; Compatibility: x4.2.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-views.cookie-consent
    (:require [x.app-core.api        :as a :refer [r]]
              [x.app-elements.api    :as elements]
              [x.app-environment.api :as environment]
              [x.app-views.settings  :as settings]))



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



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- cookie-consent-got-it-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ;
  ; @return (component)
  [popup-id]
  [elements/button {:label   :got-it!
                    :preset  :close-button
                    :variant :transparent
                    :on-click {:dispatch-n [[:x.app-ui/close-popup! popup-id]
                                            [::accept-cookie-settings!]]}}])

(defn- cookie-consent-label-bar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ;
  ; @return (component)
  [popup-id]
  [elements/polarity {:end-content [cookie-consent-got-it-button popup-id]}])



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ::accept-cookie-settings!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:x.app-environment.cookie-handler/->settings-changed])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ::render!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:x.app-ui/add-popup!
   ::view
   {:content          #'settings/cookie-settings
    :horizontal-align :left
    :label-bar        {:content #'cookie-consent-label-bar}
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
