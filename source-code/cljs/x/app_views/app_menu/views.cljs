
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-views.app-menu.views
    (:require [mid-fruits.css     :as css]
              [x.app-core.api     :as a :refer [r]]
              [x.app-details      :as details]
              [x.app-elements.api :as elements]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- back-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/button ::back-button
                   {:label  :back!
                    :indent :left
                    :preset :back-button
                    :on-click [:gestures/change-view! :views.app-menu/handler :main]}])



;; -- Language selector components --------------------------------------------
;; ----------------------------------------------------------------------------

(defn- language-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) language-key
  [language-key]
  (let [selected-language @(a/subscribe [:locales/get-selected-language])
        language-selected? (= language-key selected-language)]
       [elements/button {:icon :placeholder :label language-key :indent :left
                         :on-click [:user/upload-user-settings-item! :selected-language language-key]
                         :preset   (if language-selected? :primary-button :default-button)}]))

(defn- language-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (letfn [(f [language-key] ^{:key (str "x-app-menu--languages--" language-key)}
                             [language-button language-key])]
         (let [app-languages @(a/subscribe [:locales/get-app-languages])]
              [:div#x-app-menu--languages (map f app-languages)])))

(defn- language-selector
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:<> [elements/horizontal-separator {:size :xxl}]
       [language-list]
       [back-button]])

(defn- language-selector-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [app-multilingual? @(a/subscribe [:locales/app-multilingual?])]
       [elements/button ::language-selector-button
                        {:indent :left
                         :preset :language-button
                         :on-click  [:gestures/change-view! :views.app-menu/handler :language-selector]
                         ;:disabled? (not app-multilingual?)}]))
                         :disabled? true}]))



;; -- Main view components ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- settings-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/button ::settings-button
                   {:indent :left
                    :preset :settings-button
                    :on-click [:router/go-to! "/@app-home/settings"]
                    :disabled? true}])

(defn- more-options-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/button ::more-options-button
                   {:indent :left
                    :preset :more-options-button
                    :on-click [:gestures/change-view! :views.app-menu/handler :more-options]}])

(defn- about-app-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/button ::about-app-button
                   {:icon   :copyright
                    :indent :left
                    :label  :about-app
                    :preset :default-button
                    :on-click [:gestures/change-view! :views.app-menu/handler :about-app]}])

(defn- logout-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/button ::logout-button
                   {:indent :left
                    :preset :logout-button
                    :on-click [:user/logout!]}])

(defn- main
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:<> [language-selector-button]
       [settings-button]
       [more-options-button]
       [logout-button]])



;; -- About-app view components -----------------------------------------------
;; ----------------------------------------------------------------------------

(defn- app-description-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/label {:content (str details/app-codename " | " details/app-description)
                   :color            :muted
                   :horizontal-align :left
                   :icon             :grade
                   :indent           :left}])

(defn- app-version-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/label {:content details/app-version
                   :color            :muted
                   :horizontal-align :left
                   :icon             :extension
                   :indent           :left}])

(defn- copyright-information-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/label {:content details/copyright-information
                   :color            :muted
                   :horizontal-align :left
                   :icon             :copyright
                   :indent           :left}])

(defn- about-app
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:<> [app-description-label]
       [app-version-label]
       [copyright-information-label]
       [back-button]])



;; -- More options view components --------------------------------------------
;; ----------------------------------------------------------------------------

(defn- terms-of-service-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/button ::terms-of-service-button
                   {:icon   :subject
                    :indent :left
                    :label  :terms-of-service
                    :preset :default-button
                    :on-click [:router/go-to! "/@app-home/terms-of-service"]
                    :disabled? true}])

(defn- privacy-policy-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/button ::privacy-policy-button
                   {:icon   :subject
                    :indent :left
                    :label  :privacy-policy
                    :preset :default-button
                    :on-click [:router/go-to! "/@app-home/privacy-policy"]
                    :disabled? true}])

(defn- more-options
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:<> [terms-of-service-button]
       [privacy-policy-button]
       [about-app-button]
       [back-button]])



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- app-menu
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [view-id @(a/subscribe [:gestures/get-selected-view-id :views.app-menu/handler])]
       (case view-id :about-app         [about-app]
                     :language-selector [language-selector]
                     :main              [main]
                     :more-options      [more-options])))

(defn- user-profile-picture
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [user-profile-picture @(a/subscribe [:user/get-user-profile-picture])]
       [:div.x-user-profile-picture {:style {:backgroundImage (css/url user-profile-picture)}}]))

(defn- user-name-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [user-name @(a/subscribe [:user/get-user-name])]
       [elements/label {:content user-name :min-height :s :font-size :s :font-weight :extra-bold}]))

(defn- user-email-address-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [user-email-address @(a/subscribe [:user/get-user-email-address])]
       [elements/label {:content user-email-address :min-height :xs :font-size :xs :color :highlight}]))

(defn- user-card
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/column {:content [:<> [user-profile-picture]
                                  [elements/horizontal-separator {:size :s}]
                                  [user-name-label]
                                  [user-email-address-label]]
                    :stretch-orientation :horizontal}])

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  [_]
  [:<> [user-card]
       [elements/horizontal-separator {:size :l}]
       [app-menu]])
