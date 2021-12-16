
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.02.14
; Description:
; Version: v0.7.6
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-views.login-box
    (:require [mid-fruits.keyword   :as keyword]
              [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]
              [x.app-db.api         :as db]
              [x.app-elements.api   :as elements]
              [x.app-sync.api       :as sync]
              [x.app-ui.api         :as ui]
              [x.app-user.api       :as user]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-body-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  ;  {:login-attempted? (boolean)
  ;   :user-identified? (boolean)
  ;   :username (string)}
  [db _]
  {:login-attempted? (r user/login-attempted?      db)
   :user-identified? (r user/user-identified?      db)
   :username         (r user/get-user-name         db)
   :synchronizing?   (r sync/listening-to-request? db :user/authenticate!)})

(a/reg-sub ::get-body-props get-body-props)



;; -- Login form components ---------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- login-error-message
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) body-props
  ;
  ; @return (hiccup)
  [_ _]
  [elements/label ::login-error-message
                  {:content :incorrect-email-address-or-password
                   :color   :warning}])

(defn- email-address-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) body-props
  ;
  ; @return (component)
  [_ {:keys [synchronizing?]}]
  [elements/text-field ::email-address-field
                       {:label         :email-address
                        :value-path    [:login-box :email-address]
                        :autocomplete? true
                        :disabled?     synchronizing?}])

(defn- password-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) body-props
  ;
  ; @return (component)
  [_ {:keys [synchronizing?]}]
  [elements/password-field ::password-field
                           {:value-path    [:login-box :password]
                            :autocomplete? true
                            :disabled?     synchronizing?}])


(defn- login-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) body-props
  ;  {:synchronizing? (boolean)}
  ;
  ; @return (component)
  [_ {:keys [synchronizing?]}]
  [elements/submit-button ::login-button
                          {:color      :primary
                           :disabled?  synchronizing?
                           :input-ids  [::email-address-field ::password-field]
                           :keypress   {:key-code 13 :required? true}
                           :label      :login!
                           :layout     :fit
                           :on-click   [:user/authenticate!]
                           :variant    :filled}])

(defn- forgot-password-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) body-props
  ;
  ; @return (hiccup)
  [_ _]
  [elements/button ::forgot-password-button
                   {:color    :muted
                    :label    :forgot-password
                    :layout   :fit
                    :on-click [:ui/blow-bubble! ::service-not-available
                                                {:content :service-not-available :color :warning}]
                    :variant  :transparent}])

(defn- login-form
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) body-props
  ;  {:login-attempted? (boolean)}
  ;
  ; @return (component)
  [popup-id {:keys [login-attempted?] :as body-props}]
  [:<> (if login-attempted?    [login-error-message popup-id body-props])
       [elements/separator     {:size :m}]
       [email-address-field    popup-id body-props]
       [elements/separator     {:size :m}]
       [password-field         popup-id body-props]
       [elements/separator     {:size :xl}]
       [login-button           popup-id body-props]
      ;[forgot-password-button popup-id body-props]
       [elements/separator     {:size :m}]])



;; -- Logout form components --------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- logout-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) body-props
  ;
  ; @return (component)
  [_ _]
  [elements/button ::logout-button
                   {:on-click [:user/logout!]
                    :color    :none
                    :label    :logout!
                    :layout   :row
                    :variant  :transparent}])

(defn- continue-as-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) body-props
  ;  {:username (string)}
  ;
  ; @return (component)
  [_ {:keys [username]}]
  [elements/button ::continue-as-button
                   {:keypress {:key-code 13}
                    :on-click [:router/go-home!]
                    :label    :continue-as
                    :layout   :row
                    :suffix   username
                    :variant  :filled}])

(defn- signed-in-as-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) body-props
  ;  {:username (string)}
  ;
  ; @return (hiccup)
  [_ {:keys [username]}]
  [elements/label ::signed-in-as-label
                  {:content :signed-in-as :suffix username :horizontal-align :center}])

(defn- logged-in-form
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) body-props
  ;
  ; @return (component)
  [popup-id body-props]
  [:<> [elements/separator {:size :xs}]
       [signed-in-as-label popup-id body-props]
       [elements/separator {:size :m}]
       [continue-as-button popup-id body-props]
       [logout-button      popup-id body-props]])



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) body-props
  ;  {:user-identified? (boolean)}
  ;
  ; @return (component)
  [popup-id {:keys [user-identified?] :as body-props}]
  [:div#x-login-box (if (boolean user-identified?)
                        [logged-in-form popup-id body-props]
                        [login-form     popup-id body-props])])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :views/render-login-box!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:ui/add-popup! ::view
                  {:body {:content #'body :subscriber [::get-body-props]}
                   :min-width         :xs
                   :render-exclusive? true
                   :user-close?       false}])
