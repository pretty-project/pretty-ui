
(ns app-extensions.settings.cookie-settings
    (:require [x.app-components.api  :as components]
              [x.app-core.api        :as a :refer [r]]
              [x.app-dictionary.api  :as dictionary]
              [x.app-elements.api    :as elements]
              [x.app-environment.api :as environment]))



;; -- Descriptions ------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @description
;  WARNING! XXX#0459



;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- cancel-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [popup-id]
  [elements/button ::cancel-button
                   {:preset   :cancel-button
                    :on-click [:ui/close-popup! popup-id]}])

(defn- save-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [popup-id]
  [elements/button ::save-button
                   {:preset  :save-button
                    :variant :transparent
                    :on-click {:dispatch-n [[:ui/close-popup! popup-id]
                                            [:environment/cookie-settings-changed]]}}])

(defn- header-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  [elements/label {:content :privacy-settings :indent :both}])

(defn- header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [popup-id]
  [elements/horizontal-polarity ::header
                                {:start-content  [cancel-button popup-id]
                                 :middle-content [header-label  popup-id]
                                 :end-content    [save-button   popup-id]}])



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- privacy-policy-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  [elements/button ::policy-button
                   {:label :privacy-policy :preset :primary-button :layout :fit
                    :on-click [:router/go-to! "/@app-home/privacy-policy"]}])

(defn- terms-of-service-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  [elements/button ::terms-of-service-button
                   {:label :terms-of-service :preset :primary-button :layout :fit
                    :on-click [:router/go-to! "/@app-home/terms-of-service"]}])

(defn- cookie-settings
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [popup-id]
  [:<> ; This website uses cookies
       [elements/horizontal-separator {:size :s}]
       [elements/text {:content :this-website-uses-cookies
                       :font-size :xs :layout :row :font-weight :bold}]
       ; Legal links
       [elements/horizontal-separator {:size :xxs}]
       [privacy-policy-button   popup-id]
       [elements/horizontal-separator {:size :s}]
       [terms-of-service-button popup-id]
       ; Cookie settings
       [elements/horizontal-line {:color :highlight :layout :row}]
       [elements/switch ::necessary-cookies-switch
                        {:disabled?     true
                         :indent        :none
                         :initial-value true
                         :label         :necessary-cookies
                         :value-path (environment/cookie-setting-path :necessary-cookies-enabled?)}]
       [elements/switch ::user-experience-cookies-switch
                        {:indent        :none
                         :initial-value true
                         :label         :user-experience-cookies
                         :value-path (environment/cookie-setting-path :user-experience-cookies-enabled?)
                         :on-check   [:environment/cookie-settings-changed]
                         :on-uncheck [:environment/cookie-settings-changed]}]
       [elements/switch ::analytics-cookies-switch
                        {:indent        :none
                         :initial-value true
                         :label         :analytics-cookies
                         :value-path (environment/cookie-setting-path :analytics-cookies-enabled?)
                         :on-check   [:environment/cookie-settings-changed]
                         :on-uncheck [:environment/cookie-settings-changed]}]
       ; Remove stored cookies
       [elements/horizontal-separator {:size :s}]
       [elements/button {:label :remove-stored-cookies! :preset :secondary-button :layout :row
                         :on-click [:settings.remove-stored-cookies/render-dialog!]}]
       [elements/horizontal-separator {:size :s}]])

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [popup-id]
  [cookie-settings popup-id])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :settings.cookie-settings/render-settings!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:ui/add-popup! ::view
                  {:body   #'body
                   :header #'header
                   :horizontal-align :left
                   :user-close?      false}])
