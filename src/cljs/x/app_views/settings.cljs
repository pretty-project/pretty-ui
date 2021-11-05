
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.02.27
; Description:
; Version: v1.0.2
; Compatibility: x4.4.4



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-views.settings
    (:require [mid-fruits.candy      :refer [param]]
              [mid-fruits.css        :as css]
              [x.app-components.api  :as components]
              [x.app-core.api        :as a :refer [r]]
              [x.app-db.api          :as db]
              [x.app-dictionary.api  :as dictionary]
              [x.app-elements.api    :as elements]
              [x.app-environment.api :as environment]
              [x.app-router.api      :as router]
              [x.app-user.api        :as user]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (keyword)
(def DEFAULT-VIEW :personal)



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-selected-view
  [db _]
  (get-in db (db/meta-item-path ::primary :selected-view) DEFAULT-VIEW))

(defn- get-personal-settings-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  {:user-email-address       (r user/get-user-email-address       db)
   :user-first-name          (r user/get-user-first-name          db)
   :user-last-name           (r user/get-user-last-name           db)
   :user-name                (r user/get-user-name                db)
   :user-profile-picture-url (r user/get-user-profile-picture-url db)
   :user-phone-number        (r user/get-user-phone-number        db)})

(defn- get-privacy-settings-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (let [privacy-policy-link   (r a/get-site-link db :privacy-policy)
        terms-of-service-link (r a/get-site-link db :terms-of-service)
        what-cookies-are-link (r a/get-site-link db :what-cookies-are?)]
       {:privacy-policy-link   (r dictionary/translate db privacy-policy-link)
        :terms-of-service-link (r dictionary/translate db terms-of-service-link)
        :what-cookies-are-link (r dictionary/translate db what-cookies-are-link)}))

(defn- get-body-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (merge {:selected-view (r get-selected-view db)}
         (r get-personal-settings-view-props db)
         (r get-privacy-settings-view-props  db)))

(a/reg-sub ::get-body-view-props get-body-view-props)

(defn- get-header-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  {:selected-view (r get-selected-view db)})

(a/reg-sub ::get-header-view-props get-header-view-props)



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- change-view!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ view-id]]
  (assoc-in db (db/meta-item-path ::primary :selected-view) view-id))

(a/reg-event-db :settings/change-view! change-view!)



;; -- Header bar components ---------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- user-profile-picture
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;  {:user-profile-picture-url (string)}
  ;
  ; @return (hiccup)
  [_ {:keys [user-profile-picture-url]}]
 [:div.x-user-card
   [:div.x-user-profile-picture {:style {:backgroundImage (css/url user-profile-picture-url)}}]
   [elements/button ::change-profile-picture-button
                    {:color     :muted
                     :label     :change-profile-picture
                     :preset    :default-button
                     :font-size :xs}]])



;; -- Personal view components ------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- settings-user-name
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:<> [elements/label ::user-name-label
                       {:content   :name
                        :color     :muted
                        :layout    :fit
                        :font-size :xs}]
       [elements/button ::user-name-button
                        {:label  "Tech Mono"
                         :preset :default-button
                         :layout :fit}]])

(defn- settings-user-email-address
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:<> [elements/label ::user-email-address-label
                       {:content   :email-address
                        :color     :muted
                        :layout    :fit
                        :font-size :xs}]
       [elements/button ::user-email-address-button
                        {:label  "demo@monotech.hu"
                         :preset :default-button
                         :layout :fit}]])

(defn- settings-user-phone-number
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:<> [elements/label ::user-phone-number-label
                       {:content   :phone-number
                        :color     :muted
                        :layout    :fit
                        :font-size :xs}]
       [elements/button ::user-phone-number-button
                        {:label  "+36301234567"
                         :preset :default-button
                         :layout :fit}]])

(defn- settings-user-password
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:<> [elements/label ::user-password-label
                       {:content   :password
                        :color     :muted
                        :layout    :fit
                        :font-size :xs}]
       [elements/button ::user-password-button
                        {:label  "••••••••"
                         :preset :default-button
                         :layout :fit}]])

(defn- settings-user-pin
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:<> [elements/label ::user-pin-label
                       {:content   :pin
                        :color     :muted
                        :layout    :fit
                        :font-size :xs}]
       [elements/button ::user-pin-button
                        {:label  "••••"
                         :preset :default-button
                         :layout :fit}]])

(defn- settings-personal-view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id view-props]
  [:<> [elements/separator {:orientation :horizontal :size :s}]
       [user-profile-picture surface-id view-props]
       [elements/separator {:orientation :horizontal :size :s}]
       [elements/horizontal-line {:color :highlight}]
       [elements/separator {:orientation :horizontal :size :l}]
       [settings-user-name surface-id]
       [elements/separator {:orientation :horizontal :size :l}]
       [settings-user-email-address surface-id]
       [elements/separator {:orientation :horizontal :size :l}]
       [settings-user-phone-number surface-id]
       [elements/separator {:orientation :horizontal :size :l}]
       [settings-user-password surface-id]
       [elements/separator {:orientation :horizontal :size :l}]
       [settings-user-pin surface-id]
       [elements/separator {:orientation :horizontal :size :l}]
       [elements/button {:label :delete-user-account!
                         :preset :secondary-button}]
       [elements/button {:label :clear-user-data!
                         :preset :secondary-button}]])



;; -- Notifications view components -------------------------------------------
;; ----------------------------------------------------------------------------

(defn- settings-privacy-policy-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [privacy-policy-link]}]
  (if (some? privacy-policy-link)
      [elements/button {:label :privacy-policy :preset :primary-button :layout :row
                        :on-click [:x.app-router/go-to! privacy-policy-link]}]))

(defn- settings-terms-of-service-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [terms-of-service-link]}]
  (if (nil? terms-of-service-link)
      [elements/button {:label :terms-of-service :preset :primary-button :layout :row
                        :on-click [:x.app-router/go-to! terms-of-service-link]}]))

(defn- settings-what-cookies-are-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [what-cookies-are-link]}]
  (if (nil? what-cookies-are-link)
      [elements/button {:label :what-cookies-are? :preset :primary-button :layout :row
                        :on-click [:x.app-router/go-to! what-cookies-are-link]}]))

(defn- cookie-settings
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [component-id view-props]
  [:<> ; This website uses cookies
       [elements/separator {:size :s}]
       [elements/text {:content :this-website-uses-cookies
                       :font-size :xs :layout :row :font-weight :bold}]
       ; Legal links
       [elements/separator {:size :xxs}]
       [settings-privacy-policy-button   component-id view-props]
       [settings-terms-of-service-button component-id view-props]
       [settings-what-cookies-are-button component-id view-props]
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
                         :on-click [:x.app-views.remove-stored-cookies/render-dialog!]}]
       [elements/separator {:size :s}]])

(defn- settings-privacy-view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id view-props]
  [cookie-settings surface-id view-props])



;; -- Notifications view components -------------------------------------------
;; ----------------------------------------------------------------------------

(defn- settings-notifications-view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id view-props]
  [:<> [elements/separator {:orientation :horizontal :size :s}]
       [elements/switch ::notification-bubbles-switch
                        {:helper     "Lorem Ipsum is simply dummy text of the printing and typesetting industry."
                         :label      :notification-bubbles
                         :layout     :fit
                         :value-path [:a1]}]
       [elements/separator {:orientation :horizontal :size :l}]
       [elements/switch ::notification-sounds-switch
                        {:helper     "Lorem Ipsum is simply dummy text of the printing and typesetting industry."
                         :label      :notification-sounds
                         :layout     :fit
                         :value-path [:a2]}]
       [elements/separator {:orientation :horizontal :size :s}]])



;; -- Appearance view components ----------------------------------------------
;; ----------------------------------------------------------------------------

(defn- settings-appearance-view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id view-props]
  [:<> [elements/radio-button ::selected-theme-radio-button
                              {:helper      "Lorem Ipsum is simply dummy text of the printing and typesetting industry."
                               :label       :selected-theme
                               :layout      :fit
                               :get-label-f :label
                               :get-value-f :value
                               :initial-options [{:label :dark-theme  :value "dark"}
                                                 {:label :light-theme :value "light"}]
                               :on-select  [:x.app-ui/set-theme!]}]
       [elements/separator {:orientation :horizontal :size :s}]])



;; -- Settings body components ------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- settings-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id {:keys [selected-view] :as view-props}]
  (case selected-view :personal      [settings-personal-view      surface-id view-props]
                      :privacy       [settings-privacy-view       surface-id view-props]
                      :notifications [settings-notifications-view surface-id view-props]
                      :appearance    [settings-appearance-view    surface-id view-props]))



;; -- Settings header components ----------------------------------------------
;; ----------------------------------------------------------------------------

(defn settings-personal-view-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id {:keys [selected-view]}]
  [elements/button {:color    (if (= selected-view :personal) :default :muted)
                    :icon     :person
                    :on-click [:settings/change-view! :personal]
                    :preset   :default-icon-button}])

(defn settings-privacy-view-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id {:keys [selected-view]}]
  [elements/button {:color    (if (= selected-view :privacy) :default :muted)
                    :icon     :security
                    :on-click [:settings/change-view! :privacy]
                    :preset   :default-icon-button}])

(defn settings-notifications-view-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id {:keys [selected-view]}]
  [elements/button {:color    (if (= selected-view :notifications) :default :muted)
                    :icon     :notifications
                    :on-click [:settings/change-view! :notifications]
                    :preset   :default-icon-button}])

(defn settings-appearance-view-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id {:keys [selected-view]}]
  [elements/button {:color    (if (= selected-view :appearance) :default :muted)
                    :icon     :auto_awesome
                    :on-click [:settings/change-view! :appearance]
                    :preset   :default-icon-button}])

(defn- settings-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id view-props]
  [elements/polarity ::settings-header
                     {:middle-content [:<> [settings-personal-view-button      surface-id view-props]
                                           [settings-privacy-view-button       surface-id view-props]
                                           [settings-notifications-view-button surface-id view-props]
                                           [settings-appearance-view-button    surface-id view-props]]}])



;; -- Settings components -----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id]
  [elements/box surface-id {:body   {:content    #'settings-body
                                     :subscriber [::get-body-view-props]}
                            :header {:content    #'settings-header
                                     :subscriber [::get-header-view-props]
                                     :sticky?    true}
                            :horizontal-align :left}])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ::load!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  {:dispatch-n [[:x.app-ui/set-header-title! :settings]
                [:x.app-ui/set-window-title! :settings]
                [:x.app-ui/set-surface! ::view
                                        {:content #'view
                                         :content-label :settings}]]})

(a/reg-lifecycles
  ::lifecycles
  {:on-app-boot [:x.app-router/add-route! ::route
                                          {:restricted?    true
                                           :route-event    [::load!]
                                           :route-template "/settings"}]})
