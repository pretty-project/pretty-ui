
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-views.login-box.views
    (:require [x.app-core.api     :as a]
              [x.app-elements.api :as elements]))



;; -- Login form components ---------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- login-error-message
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/label ::login-error-message
                  {:content :incorrect-email-address-or-password
                   :color   :warning}])

(defn- email-address-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [synchronizing? @(a/subscribe [:sync/listening-to-request? :user/authenticate!])]
       [elements/text-field ::email-address-field
                            {:disable-autofill? false
                             :min-width  :s
                             :label      :email-address
                             :value-path [:views :login-box/data-items :email-address]
                             :disabled?  synchronizing?}]))

(defn- password-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [synchronizing? @(a/subscribe [:sync/listening-to-request? :user/authenticate!])]
       [elements/password-field ::password-field
                                {:disable-autofill? false
                                 :min-width  :s
                                 :value-path [:views :login-box/data-items :password]
                                 :disabled?  synchronizing?}]))

(defn- login-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [disabled? @(a/subscribe [:views.login-box/login-button-disabled?])]
       [elements/submit-button ::login-button
                               {:color     :primary
                                :label     :login!
                                :layout    :fit
                                :variant   :filled
                                :keypress  {:key-code 13 :required? true}
                                :input-ids [::email-address-field ::password-field]
                                :on-click  [:user/authenticate!]
                                :disabled? disabled?}]))

(defn- forgot-password-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/button ::forgot-password-button
                   {:color    :muted
                    :label    :forgot-password
                    :layout   :fit
                    :on-click [:ui/blow-bubble! ::service-not-available
                                                {:content :service-not-available :color :warning}]
                    :variant  :transparent}])

(defn- login-form
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [login-attempted? @(a/subscribe [:user/login-attempted?])]
       [:<> (if login-attempted? [login-error-message])
            [elements/horizontal-separator {:size :m}]
            [email-address-field]
            [elements/horizontal-separator {:size :m}]
            [password-field]
            [elements/horizontal-separator {:size :xl}]
            [login-button]
           ;[forgot-password-button]
            [elements/horizontal-separator {:size :m}]]))



;; -- Logout form components --------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- logout-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/button ::logout-button
                   {:on-click [:user/logout!]
                    :color    :none
                    :label    :logout!
                    :layout   :row
                    :variant  :transparent}])

(defn- continue-as-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [user-name @(a/subscribe [:user/get-user-name])]
       [elements/button ::continue-as-button
                        {:keypress {:key-code 13}
                         :on-click [:router/go-home!]
                         :label    :continue-as!
                         :layout   :row
                         :suffix   user-name
                         :variant  :filled}]))

(defn- signed-in-as-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [user-name @(a/subscribe [:user/get-user-name])]
       [elements/label ::signed-in-as-label
                       {:content :signed-in-as :suffix user-name :horizontal-align :center}]))

(defn- logged-in-form
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:<> [elements/horizontal-separator {:size :xs}]
       [signed-in-as-label]
       [elements/horizontal-separator {:size :m}]
       [continue-as-button]
       [logout-button]])



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  [_]
  (if-let [user-identified? @(a/subscribe [:user/user-identified?])]
          [:div#x-login-box [logged-in-form]]
          [:div#x-login-box [login-form]]))
