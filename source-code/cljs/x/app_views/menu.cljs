
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.21
; Description:
; Version: v1.9.2
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-views.menu
    (:require [mid-fruits.candy      :refer [param]]
              [mid-fruits.css        :as css]
              [mid-fruits.vector     :as vector]
              [x.app-core.api        :as a :refer [r]]
              [x.app-db.api          :as db]
              [x.app-details         :as details]
              [x.app-elements.api    :as elements]
              [x.app-environment.api :as environment]
              [x.app-gestures.api    :as gestures]
              [x.app-locales.api     :as locales]
              [x.app-ui.api          :as ui]
              [x.app-user.api        :as user]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (keyword)
(def DEFAULT-VIEW-ID :main)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- set-language-event
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [selected-language]
  [:user/upload-user-settings-item! :selected-language selected-language])



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-body-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  {:app-languages            (r locales/get-app-languages         db)
   :app-multilingual?        (r locales/app-multilingual?         db)
   :selected-language        (r locales/get-selected-language     db)
   :user-email-address       (r user/get-user-email-address       db)
   :user-name                (r user/get-user-name                db)
   :user-profile-picture-url (r user/get-user-profile-picture-url db)
   :view-id                  (r gestures/get-selected-view-id     db ::handler)})

(a/reg-sub ::get-body-props get-body-props)



;; -- Language selector components --------------------------------------------
;; ----------------------------------------------------------------------------

(defn- language-selector-languages
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [app-languages selected-language]}]
  (reduce #(let [language-selected? (= %2 selected-language)
                 button-props       {:icon :placeholder :label %2 :on-click (set-language-event %2)
                                     :preset (if language-selected? :primary-button :default-button)
                                     :indent :left}]
                (vector/conj-item %1 [elements/button button-props]))
           [:div#x-app-menu--languages]
           (param app-languages)))

(defn- language-selector
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [popup-id body-props]
  (vector/conj-item (language-selector-languages popup-id body-props)
                    [elements/button ::back-button
                                     {:indent   :left
                                      :label    :back!
                                      :on-click [:gestures/change-view! ::handler :main]
                                      :preset   :back-button}]))

(defn- language-selector-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [app-multilingual?]}]
  [elements/button ::language-selector-button
                   {:disabled? (not app-multilingual?)
                    :indent   :left
                    :preset    :language-button
                    :on-click  [:gestures/change-view! ::handler :language-selector]}])



;; -- Main view components ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- settings-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _]
  [elements/button ::settings-button
                   {:indent   :left
                    :on-click [:router/go-to! "/:app-home/settings"]
                    :preset   :settings-button}])

(defn- more-options-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _]
  [elements/button ::more-options-button
                   {:indent   :left
                    :on-click [:gestures/change-view! ::handler :more-options]
                    :preset   :more-options-button}])

(defn- about-app-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _]
  [elements/button ::about-app-button
                   {:icon     :copyright
                    :indent   :left
                    :label    :about-app
                    :on-click [:gestures/change-view! ::handler :about-app]
                    :preset   :default-button}])

(defn- logout-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _]
  [elements/button ::logout-button
                   {:indent   :left
                    :on-click [:user/logout!]
                    :preset   :logout-button}])

(defn- main
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [popup-id body-props]
  [:<> [language-selector-button popup-id body-props]
       [settings-button          popup-id body-props]
       [about-app-button         popup-id body-props]
       [more-options-button      popup-id body-props]
       [logout-button            popup-id body-props]])



;; -- About-app view components -----------------------------------------------
;; ----------------------------------------------------------------------------

(defn- about-app
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _]
  [:<> [elements/label {:content (str details/app-codename " | " details/app-description)
                        :horizontal-align :left
                        :color            :muted
                        :icon             :grade}]
       [elements/label {:content details/app-version
                        :horizontal-align :left
                        :color            :muted
                        :icon             :extension}]
       [elements/label {:content details/copyright-information
                        :horizontal-align :left
                        :color            :muted
                        :icon             :copyright}]
       [elements/button ::back-button
                        {:indent   :left
                         :label    :back!
                         :on-click [:gestures/change-view! ::handler  :main]
                         :preset   :back-button}]])



;; -- More options view components --------------------------------------------
;; ----------------------------------------------------------------------------

(defn- more-options
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _]
  [:<> [elements/button ::terms-of-service-button
                        {:icon     :subject
                         :indent   :left
                         :label    :terms-of-service
                         :on-click [:router/go-to! "/:app-home/terms-of-service"]
                         :preset   :default-button}]
       [elements/button ::privacy-policy-button
                        {:icon     :subject
                         :indent   :left
                         :label    :privacy-policy
                         :on-click [:router/go-to! "/:app-home/privacy-policy"]
                         :preset   :default-button}]
       [elements/button ::back-button
                        {:label    :back!
                         :indent   :left
                         :on-click [:gestures/change-view! ::handler  :main]
                         :preset   :back-button}]])



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- app-menu
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [popup-id {:keys [view-id] :as body-props}]
  (case view-id :about-app         [about-app         popup-id body-props]
                :language-selector [language-selector popup-id body-props]
                :main              [main              popup-id body-props]
                :more-options      [more-options      popup-id body-props]))

(defn user-card
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [user-email-address user-name user-profile-picture-url]}]
  [elements/column {:content [:<> [:div.x-user-profile-picture {:style {:backgroundImage (css/url user-profile-picture-url)}}]
                                  [elements/horizontal-separator {:size :s}]
                                  [elements/label     {:content user-name :layout :fit :size :xl :font-weight :extra-bold}]
                                  [elements/label     {:content user-email-address :color :highlight :layout :fit :font-size :xs}]]
                    :stretch-orientation :horizontal}])

(defn- body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [popup-id body-props]
  [:<> [user-card popup-id body-props]
       [elements/horizontal-separator {:size :l}]
       [app-menu  popup-id body-props]])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :views/render-menu!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {:db       (r gestures/init-view-handler! db ::handler {:default-view-id DEFAULT-VIEW-ID})
       :dispatch [:ui/add-popup! ::view
                                 {:body   {:content #'body :subscriber [::get-body-props]}
                                  :header {:content #'ui/close-popup-header}
                                  :horizontal-align :left
                                  :min-width        :xs}]}))
