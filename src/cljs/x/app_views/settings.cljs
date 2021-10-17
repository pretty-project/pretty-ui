
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.02.27
; Description:
; Version: v0.4.8
; Compatibility: x4.3.3



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
              [x.app-ui.api          :as ui]
              [x.app-user.api        :as user]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-header-bar-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  ;  {:user-name (string)
  ;   :user-profile-picture-url (string)}
  [db _]
  {:user-name                (r user/get-user-name                db)
   :user-profile-picture-url (r user/get-user-profile-picture-url db)})

(a/reg-sub ::get-header-bar-view-props get-header-bar-view-props)

(defn- get-user-profile-box-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  {
   :user-email-address (r user/get-user-email-address db)
   :user-first-name    (r user/get-user-first-name    db)
   :user-last-name     (r user/get-user-last-name     db)
   :user-phone-number  (r user/get-user-profile-item  db :phone-number)})

(a/reg-sub ::get-user-profile-box-view-props get-user-profile-box-view-props)

(defn- get-privacy-settings-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return
  ;  {:privacy-policy-link (string)
  ;   :terms-of-service-link (string)
  ;   :what-cookies-are-link (string)}
  [db _]
  (let [privacy-policy-link   (r a/get-site-link db :privacy-policy)
        terms-of-service-link (r a/get-site-link db :terms-of-service)
        what-cookies-are-link (r a/get-site-link db :what-cookies-are?)]
       {:privacy-policy-link   (r dictionary/translate db privacy-policy-link)
        :terms-of-service-link (r dictionary/translate db terms-of-service-link)
        :what-cookies-are-link (r dictionary/translate db what-cookies-are-link)}))

(a/reg-sub ::get-privacy-settings-view-props get-privacy-settings-view-props)



;; -- Setting components ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- setting-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) label-props
  ;
  ; @return (component)
  [label-props]
  [elements/label (merge {:color            :muted
                          :font-weight      :bold
                          :horizontal-align :left
                          :size             :xs}
                         (param label-props))])

(defn- setting-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) button-props
  ;  {:label (metamorphic-content)(opt)
  ;   :password? (boolean)(opt)
  ;    Default: false
  ;   :placeholder (metamorphic-content)(opt)
  ;   :value-path (item-path vector)}
  ;
  ; @return (component)
  [{:keys [label password? placeholder value-path] :as button-props}]
  [elements/button (merge {:color            :none
                           :horizontal-align :left
                           :on-click [:x.app-tools.editor/render!
                                      {:label placeholder :password? password? :value-path value-path}]
                           :size    :xs
                           :variant :transparent}
                          (param button-props)
                          (if (nil? label)
                              {:color :muted
                               :label placeholder}))])

(defn- setting-switch
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) switch-props
  ;
  ; @return (component)
  [switch-props]
  [elements/switch (merge {:size :xs}
                          (param switch-props))])



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
  [:div.x-user-profile-picture
     {:style {:backgroundImage (css/url user-profile-picture-url)
              :height "120px" :width "120px"}}])

(defn- user-name
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;  {:user-name (string)}
  ;
  ; @return (hiccup)
  [_ {:keys [user-name]}]
  [elements/label {:content user-name :size :xl}])

(defn- header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;
  ; @return (hiccup)
  [component-id view-props]
  [:<> [user-profile-picture component-id view-props]
       [user-name            component-id view-props]])

(defn- header-bar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ;
  ; @return (component)
  [_]
  [elements/content-bar {:content    #'header
                         :layout     :header
                         :subscriber [::get-header-bar-view-props]}])



;; -- User profile settings components ----------------------------------------
;; ----------------------------------------------------------------------------

(defn- user-profile-settings
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;  {:user-email-address (string)
  ;   :user-first-name (string)
  ;   :user-last-name (string)
  ;   :user-phone-number (string)}
  ;
  ; @return (hiccup)
  [_ {:keys [user-email-address user-first-name user-last-name user-phone-number]}]
  [:div#x-settings-page--user-profile-settings
    [elements/horizontal-line {:color :highlight}]

    ; TODO! ...
    ; Használj x.app-elements/table elemet!
    ; TODO! ...

    [:div.x-settings-page--table-view
      ; Labels
      [:div.x-settings-page--table-view--labels
        [setting-label {:content :first-name}]
        [setting-label {:content :last-name}]
        [setting-label {:content :email}]
        [setting-label {:content :phone}]
        [setting-label {:content :password}]
        [setting-label {:content :pin}]]
      ; Buttons
      [:div.x-settings-page--table-view--buttons
        [setting-button {:label       user-first-name
                         :placeholder :first-name
                         :value-path  (db/path :x.app-user.profile-handler/profile :first-name)}]
        [setting-button {:label       user-last-name
                         :placeholder :last-name
                         :value-path  (db/path :x.app-user.profile-handler/profile :last-name)}]
        [setting-button {:label       user-email-address
                         :placeholder :email-address
                         :value-path  (db/path :x.app-user.account-handler/account :email-address)}]
        [setting-button {:label       user-phone-number
                         :placeholder :phone-number
                         :value-path (db/path :x.app-user.profile-handler/profile :phone-number)}]
        [setting-button {:label "••••••••"
                         :placeholder :new-password
                         :password? true
                         :value-path (db/path :x.app-user.account-handler/account :new-password)}]
        [setting-button {:label "••••"
                         :placeholder :new-pin
                         :password? true
                         :value-path (db/path :x.app-user.account-handler/account :new-pin)}]]]
    ; Change user profile picture
    [setting-button {:label :change-profile-picture
                     :color :primary}]])

(defn- user-profile-box
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ; @param (map) view-props
  ;
  ; @return (component)
  [_]
  [elements/box {:content          #'user-profile-settings
                 :horizontal-align :left
                 :icon             :account_circle
                 :label            :my-profile
                 :subscriber       [::get-user-profile-box-view-props]}])



;; -- Privacy settings components ---------------------------------------------
;; ----------------------------------------------------------------------------

(defn- privacy-policy-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;  {:privacy-policy-link (string)}
  ;
  ; @return (component)
  [_ {:keys [privacy-policy-link]}]
  (if (some? privacy-policy-link)
      [setting-button {:label :privacy-policy :color :primary :layout :fit
                       :on-click [:x.app-router/go-to! privacy-policy-link]}]))

(defn- terms-of-service-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;  {:terms-of-service-link (string)}
  ;
  ; @return (component)
  [_ {:keys [terms-of-service-link]}]
  (if (some? terms-of-service-link)
      [setting-button {:label :terms-of-service :color :primary :layout :fit
                       :on-click [:x.app-router/go-to! terms-of-service-link]}]))

(defn- what-cookies-are-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;  {:what-cookies-are-link (string)}
  ;
  ; @return (component)
  [_ {:keys [what-cookies-are-link]}]
  (if (some? what-cookies-are-link)
      [setting-button {:label :what-cookies-are? :color :primary :layout :fit
                       :on-click [:x.app-router/go-to! what-cookies-are-link]}]))

(defn- ku8701
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;
  ; @return (component)
  [component-id view-props]
  [:<> ; This website uses cookies
       [elements/separator {:size :s}]
       [elements/text {:content :this-website-uses-cookies
                       :size :xs :layout :row :font-weight :bold}]
       ; Legal links
       [elements/separator {:size :xxs}]
       [privacy-policy-button   component-id view-props]
       [terms-of-service-button component-id view-props]
       [what-cookies-are-button component-id view-props]
       ; Cookie settings
       [elements/separator {:size :l}]
       [elements/horizontal-line {:color :highlight :layout :fit}]
       [elements/separator {:size :l}]
       [setting-switch {:disabled? true
                        :initial-value true
                        :label :necessary-cookies
                        :value-path (environment/cookie-setting-path :necessary-cookies-enabled?)}]
       [setting-switch {:initial-value true
                        :label :user-experience-cookies
                        :value-path (environment/cookie-setting-path :user-experience-cookies-enabled?)
                        :on-check [:x.app-environment.cookie-handler/->settings-changed]
                        :on-uncheck [:x.app-environment.cookie-handler/->settings-changed]}]
       [setting-switch {:initial-value true
                        :label :analytics-cookies
                        :value-path (environment/cookie-setting-path :analytics-cookies-enabled?)
                        :on-check [:x.app-environment.cookie-handler/->settings-changed]
                        :on-uncheck [:x.app-environment.cookie-handler/->settings-changed]}]
       ; Remove stored cookies
       [elements/separator {:size :l}]
       [setting-button {:label :remove-stored-cookies!
                        :icon  :delete :layout  :fit
                        :on-click [:x.app-views.remove-stored-cookies/render-dialog!]}]
       [elements/separator {:size :xs}]])

(defn cookie-settings
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ;
  ; @return (component)
  [_]
  [components/subscriber {:component #'ku8701
                          :subscriber [::get-privacy-settings-view-props]}])

(defn- privacy-settings
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ;
  ; @return (hiccup)
  [component-id]
  [:div#x-settings-page--privacy-settings
    [elements/horizontal-line {:color :highlight :layout :fit}]
    [cookie-settings component-id]])

(defn- privacy-box
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ;
  ; @return (component)
  [_]
  [elements/box {:content          #'privacy-settings
                 :horizontal-align :left
                 :icon             :security
                 :label            :privacy}])



;; -- Notification settings components ----------------------------------------
;; ----------------------------------------------------------------------------

(defn- notification-settings
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ;
  ; @return (hiccup)
  [_]
  [:div#x-settings-page--notification-settings
    [elements/horizontal-line {:color :highlight :layout :fit}]
    ; Notification settings
    [setting-switch {:label :notification-bubbles
                     :value-path [:a1]}]
    [setting-switch {:label :notification-sounds
                     :value-path [:a2]}]])

(defn- notification-box
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ;
  ; @return (component)
  [_]
  [elements/box {:content          #'notification-settings
                 :horizontal-align :left
                 :icon             :circle_notifications
                 :label            :notifications}])



;; -- Appearance settings components ------------------------------------------
;; ----------------------------------------------------------------------------

(defn- appearance-settings
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ;
  ; @return (hiccup)
  [_]
  [:div#x-settings-page--appearance-settings
    [elements/horizontal-line {:color :highlight :layout :fit}]
    ; Theme selector
    [elements/radio-button
      {:label      :selected-theme
       :layout     :fit
       :options    [{:label :dark-theme  :value "dark"}
                    {:label :light-theme :value "light"}]
       :on-select  [:x.app-ui/->theme-changed]
       :size       :xs
       :value-path (db/path :x.app-user.settings-handler/settings :selected-theme)}]
    [elements/separator {:size :xs}]])

(defn- appearance-box
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ;
  ; @return (component)
  [_]
  [elements/box {:content          #'appearance-settings
                 :horizontal-align :left
                 :icon             :monitor
                 :label            :appearance}])



;; -- Danger zone components --------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- danger-zone
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ;
  ; @return (hiccup)
  [_]
  [:div#x-settings-page--danger-zone
    [elements/horizontal-line {:color :highlight :layout :fit}]
    ; Danger buttons
    [setting-button {:label :delete-user-account!
                     :color :secondary :layout  :fit}]
    [setting-button {:label :clear-user-data!
                     :color :secondary :layout  :fit}]
    [elements/separator {:size :xs}]])

(defn- danger-zone-box
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ;
  ; @return (component)
  [_]
  [elements/box {:color            :secondary
                 :content          #'danger-zone
                 :horizontal-align :left
                 :icon             :error_outline
                 :label            :danger-zone}])



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ;
  ; @return (component)
  [surface-id]
  [:div#x-settings-page
    ; Header bar
    [header-bar       surface-id]
    ; User settings
    [router/fragment-marker :user]
    [user-profile-box surface-id]
    ; Privacy settings
    [router/fragment-marker :privacy]
    [privacy-box      surface-id]
    ; Notification settings
    [router/fragment-marker :notifications]
    [notification-box surface-id]
    ; Appearance settings
    [router/fragment-marker :appearance]
    [appearance-box   surface-id]
    ; Danger zone
    [danger-zone-box  surface-id]
    [elements/separator {:size :xl}]])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ::render!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:x.app-ui/set-surface!
   ::view
   {:content #'view
    :content-label :settings
    :label-bar {:content #'ui/go-back-surface-label-bar
                :content-props {:label :settings}}}])

(a/reg-event-fx
  ::initialize!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  {:dispatch-n
   [[:x.app-router/add-route!
     ::route
     {:restricted?         true
      :route-event         [::render!]
      :route-template      "/settings"
      :route-title         :settings
      :scroll-to-fragment? true}]]})

(a/reg-lifecycles
  ::lifecycles
  {:on-app-boot [::initialize!]})
