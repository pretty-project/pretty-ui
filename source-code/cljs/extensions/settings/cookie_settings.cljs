
(ns extensions.settings.cookie-settings
    (:require [x.app-components.api  :as components]
              [x.app-core.api        :as a :refer [r]]
              [x.app-dictionary.api  :as dictionary]
              [x.app-elements.api    :as elements]
              [x.app-environment.api :as environment]))



;; -- Descriptions ------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @description
;  WARNING! XXX#0459



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-body-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (let [privacy-policy-link   (r a/get-site-link db :privacy-policy)
        terms-of-service-link (r a/get-site-link db :terms-of-service)
        what-cookies-are-link (r a/get-site-link db :what-cookies-are?)]
       {:privacy-policy-link   (r dictionary/translate db privacy-policy-link)
        :terms-of-service-link (r dictionary/translate db terms-of-service-link)
        :what-cookies-are-link (r dictionary/translate db what-cookies-are-link)}))

(a/reg-sub ::get-body-props get-body-props)



;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- cancel-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [header-id]
  [elements/button ::cancel-button
                   {:preset   :cancel-button
                    :on-click [:x.app-ui/close-popup! header-id]}])

(defn- save-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [header-id]
  [elements/button ::save-button
                   {:preset  :save-button
                    :variant :transparent
                    :on-click {:dispatch-n [[:x.app-ui/close-popup! header-id]
                                            [::accept-cookie-settings!]]}}])

(defn- header-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  [elements/label {:content :privacy-settings}])

(defn- header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [header-id]
  [elements/polarity ::header
                     {:start-content  [:<> [cancel-button header-id]]
                      :middle-content [:<> [elements/separator {:size :xs :orientation :vertical}]
                                           [header-label  header-id]
                                           [elements/separator {:size :xs :orientation :vertical}]]
                      :end-content    [:<> [save-button   header-id]]}])



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- privacy-policy-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [privacy-policy-link]}]
  (if (some? privacy-policy-link)
      [elements/button ::policy-button
                       {:label :privacy-policy :preset :primary-button :layout :row
                        :on-click [:router/go-to! privacy-policy-link]}]))

(defn- terms-of-service-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [terms-of-service-link]}]
  (if (nil? terms-of-service-link)
      [elements/button ::terms-of-service-button
                       {:label :terms-of-service :preset :primary-button :layout :row
                        :on-click [:router/go-to! terms-of-service-link]}]))

(defn- what-cookies-are-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [what-cookies-are-link]}]
  (if (nil? what-cookies-are-link)
      [elements/button ::what-cookies-are-button
                       {:label :what-cookies-are? :preset :primary-button :layout :row
                        :on-click [:router/go-to! what-cookies-are-link]}]))

(defn- cookie-settings
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [body-id body-props]
  [:<> ; This website uses cookies
       [elements/separator {:size :s}]
       [elements/text {:content :this-website-uses-cookies
                       :font-size :xs :layout :row :font-weight :bold}]
       ; Legal links
       [elements/separator {:size :xxs}]
       [privacy-policy-button   body-id body-props]
       [terms-of-service-button body-id body-props]
       [what-cookies-are-button body-id body-props]
       ; Cookie settings
       [elements/horizontal-line {:color :highlight :layout :row}]
       [elements/switch ::necessary-cookies-switch
                        {:disabled?     true
                         :initial-value true
                         :label         :necessary-cookies
                         :value-path (environment/cookie-setting-path :necessary-cookies-enabled?)}]
       [elements/switch ::user-experience-cookies-switch
                        {:initial-value true
                         :label         :user-experience-cookies
                         :value-path (environment/cookie-setting-path :user-experience-cookies-enabled?)
                         :on-check   [:x.app-environment.cookie-handler/->settings-changed]
                         :on-uncheck [:x.app-environment.cookie-handler/->settings-changed]}]
       [elements/switch ::analytics-cookies-switch
                        {:initial-value true
                         :label         :analytics-cookies
                         :value-path (environment/cookie-setting-path :analytics-cookies-enabled?)
                         :on-check   [:x.app-environment.cookie-handler/->settings-changed]
                         :on-uncheck [:x.app-environment.cookie-handler/->settings-changed]}]
       ; Remove stored cookies
       [elements/separator {:size :s}]
       [elements/button {:label :remove-stored-cookies! :preset :secondary-button :layout :row
                         :on-click [:settings/render-remove-stored-cookies-dialog!]}]
       [elements/separator {:size :s}]])

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [body-id]
  [components/content body-id {:component  #'body
                               :subscriber [::get-body-props]}])



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
  [:x.app-ui/add-popup! ::view
                        {:content          #'body
                         :horizontal-align :left
                         :label-bar        {:content #'header}
                         :layout           :boxed
                         :user-close?      false}])
