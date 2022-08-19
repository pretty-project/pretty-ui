
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-views.login-box.views
    (:require [x.app-core.api     :as a]
              [x.app-elements.api :as elements]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- app-title-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [synchronizing? @(a/subscribe [:sync/listening-to-request? :user/authenticate!])
        app-title      @(a/subscribe [:core/get-app-config-item :app-title])]
       [elements/label ::app-title-label
                       {:content          app-title
                        :disabled?        synchronizing?
                        :font-weight      :extra-bold
                        :horizontal-align :center
                        :indent           {:horizontal :xs}}]))



;; -- Login form components ---------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- login-error-message
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/label ::login-error-message
                  {:content          :incorrect-email-address-or-password
                   :color            :warning
                   :horizontal-align :center
                   :indent           {:horizontal :xs}}])

(defn- email-address-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [synchronizing? @(a/subscribe [:sync/listening-to-request? :user/authenticate!])]
       [elements/text-field ::email-address-field
                            {:autofill?  true
                             :disabled?  synchronizing?
                             :indent     {:top :xs :vertical :xs}
                             :label      :email-address
                             :value-path [:views :login-box/data-items :email-address]}]))

(defn- password-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [synchronizing? @(a/subscribe [:sync/listening-to-request? :user/authenticate!])]
       [elements/password-field ::password-field
                                {:autofill?  true
                                 :disabled?  synchronizing?
                                 :indent     {:top :xs :vertical :xs}
                                 :value-path [:views :login-box/data-items :password]}]))

(defn- login-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [disabled? @(a/subscribe [:views.login-box/login-button-disabled?])]
       [elements/submit-button ::login-button
                               {:background-color :primary
                                :hover-color      :primary
                                :disabled?        disabled?
                                :label            :login!
                                :keypress         {:key-code 13 :required? true}
                                :indent           {:bottom :xs :top :xxl :vertical :xs}
                                :input-ids        [::email-address-field ::password-field]
                                :on-click         [:user/authenticate!]}]))

(defn- forgot-password-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/button ::forgot-password-button
                   {:color       :muted
                    :hover-color :highlight
                    :indent      {:vertical :xs}
                    :label       :forgot-password
                    :on-click    []
                    :variant     :transparent}])

(defn- login-form
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [login-attempted? @(a/subscribe [:user/login-attempted?])]
       [:<> [app-title-label]
            (if login-attempted? [login-error-message])
            [email-address-field]
            [password-field]
            [login-button]]))
           ;[forgot-password-button]



;; -- Logout form components --------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- logout-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/button ::logout-button
                   {:hover-color :highlight
                    :indent      {:bottom :xs :vertical :xs}
                    :label       :logout!
                    :on-click    [:user/logout!]}])

(defn- continue-as-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [user-name @(a/subscribe [:user/get-user-name])]
       [elements/button ::continue-as-button
                        {:color       :primary
                         :keypress    {:key-code 13}
                         :hover-color :highlight
                         :indent      {:vertical :xs}
                         :label       {:content :continue-as! :suffix user-name}
                         :on-click    [:router/go-home!]
                         :variant     :filled}]))

(defn- user-name-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [user-name @(a/subscribe [:user/get-user-name])]
       [elements/label ::user-name-label
                       {:content          {:content :signed-in-as :suffix user-name}
                        :horizontal-align :center
                        :indent           {:top :xs :vertical :xs}}]))

(defn- user-email-address-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [user-email-address @(a/subscribe [:user/get-user-email-address])]
       [elements/label ::user-email-address-label
                       {:color            :muted
                        :content          user-email-address
                        :font-size        :xs
                        :horizontal-align :center
                        :indent           {:bottom :m :vertical :xs}}]))

(defn- logged-in-form
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:<> [user-name-label]
       [user-email-address-label]
       [continue-as-button]
       [logout-button]])




;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (if-let [user-identified? @(a/subscribe [:user/user-identified?])]
          [logged-in-form]
          [login-form]))

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  [_]
  [:div#login-box [:div#login-box--body [view-structure]]])
