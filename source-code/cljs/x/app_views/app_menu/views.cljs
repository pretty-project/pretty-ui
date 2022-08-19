
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-views.app-menu.views
    (:require [layouts.popup-a.api :as popup-a]
              [mid-fruits.css      :as css]
              [x.app-core.api      :as a :refer [r]]
              [x.app-details       :as details]
              [x.app-elements.api  :as elements]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- back-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/button ::back-button
                   {:hover-color :highlight
                    :indent      {:vertical :xs}
                    :on-click    [:gestures/change-view! :views.app-menu/handler :main]
                    :preset      :back}])



;; -- Language selector components --------------------------------------------
;; ----------------------------------------------------------------------------

(defn- language-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) language-key
  [language-key]
  (let [selected-language @(a/subscribe [:locales/get-selected-language])
        language-selected? (= language-key selected-language)]
       [elements/button {:hover-color :highlight
                         :icon        :placeholder
                         :label       language-key
                         :indent      {:vertical :xs}
                         :on-click    [:user/upload-user-settings-item! :selected-language language-key]
                         :preset      (if language-selected? :primary :default)}]))

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
  [:<> [elements/horizontal-separator {:size :xl}]
       [language-list]
       [back-button]])

(defn- language-selector-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [app-multilingual? @(a/subscribe [:locales/app-multilingual?])]
       [elements/button ::language-selector-button
                        {:hover-color :highlight
                         :indent      {:vertical :xs}
                         :on-click    [:gestures/change-view! :views.app-menu/handler :language-selector]
                         :preset      :language
                        ;:disabled?   (not app-multilingual?)
                         ; TEMP
                         :disabled? true}]))



;; -- Main view components ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- settings-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/button ::settings-button
                   {:hover-color :highlight
                    :indent      {:vertical :xs}
                    :on-click    [:router/go-to! "/@app-home/settings"]
                    :preset      :settings}])
                    ; TEMP
                    ;:disabled? true}])

(defn- more-options-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/button ::more-options-button
                   {:hover-color :highlight
                    :indent      {:vertical :xs}
                    :on-click    [:gestures/change-view! :views.app-menu/handler :more-options]
                    :preset      :more-options}])

(defn- logout-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/button ::logout-button
                   {:hover-color :highlight
                    :indent      {:vertical :xs}
                    :on-click    [:user/logout!]
                    :preset      :logout}])

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
  [elements/label ::app-description-label
                  {:content          (str details/app-codename " | " details/app-description)
                   :color            :muted
                   :horizontal-align :left
                   :icon             :grade
                   :indent           {:horizontal :xxs :vertical :s}}])

(defn- app-version-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/label ::app-version-label
                  {:content          details/app-version
                   :color            :muted
                   :horizontal-align :left
                   :icon             :extension
                   :indent           {:horizontal :xxs :vertical :s}}])

(defn- copyright-information-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/label ::copyright-information-label
                  {:content          details/copyright-information
                   :color            :muted
                   :horizontal-align :left
                   :icon             :copyright
                   :indent           {:horizontal :xxs :vertical :s}}])

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
                   {:hover-color :highlight
                    :icon        :subject
                    :indent      {:vertical :xs}
                    :label       :terms-of-service
                    :on-click    [:router/go-to! "/@app-home/terms-of-service"]
                    :preset      :default
                    :disabled? true}])

(defn- privacy-policy-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/button ::privacy-policy-button
                   {:hover-color :highlight
                    :icon        :subject
                    :indent      {:vertical :xs}
                    :label       :privacy-policy
                    :on-click    [:router/go-to! "/@app-home/privacy-policy"]
                    :preset      :default
                    :disabled? true}])

(defn- about-app-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/button ::about-app-button
                   {:hover-color :highlight
                    :icon        :copyright
                    :indent      {:vertical :xs}
                    :label       :about-app
                    :on-click    [:gestures/change-view! :views.app-menu/handler :about-app]
                    :preset      :default}])

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
  (let [view-id @(a/subscribe [:gestures/get-current-view-id :views.app-menu/handler])]
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
       [elements/label ::user-name-label
                       {:content     user-name
                        :font-size   :s
                        :font-weight :extra-bold}]))

(defn- user-email-address-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [user-email-address @(a/subscribe [:user/get-user-email-address])]
       [elements/label ::user-email-address-label
                       {:color     :muted
                        :content   user-email-address
                        :font-size :xs}]))

(defn- user-card
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/column ::user-card
                   {:content [:<> [user-profile-picture]
                                  [elements/horizontal-separator {:size :s}]
                                  [user-name-label]
                                  [user-email-address-label]]
                    :indent {:bottom :m}
                    :stretch-orientation :horizontal}])

(defn- body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:<> [user-card]
       [app-menu]
       [elements/horizontal-separator {:size :s}]])

(defn- close-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/icon-button ::close-icon-button
                        {:border-radius :s
                         :hover-color   :highlight
                         :keypress      {:key-code 27}
                         :on-click      [:ui/close-popup! :views.app-menu/view]
                         :preset        :close}])

(defn- header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/horizontal-polarity ::header
                                {:end-content [close-icon-button]}])

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  [popup-id]
  [popup-a/layout popup-id {:body      #'body
                            :header    #'header
                            :min-width :xs}])
