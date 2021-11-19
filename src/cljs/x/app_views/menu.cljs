
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.21
; Description:
; Version: v1.6.2
; Compatibility: x4.4.4



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-views.menu
    (:require [mid-fruits.candy      :refer [param]]
              [mid-fruits.css        :as css]
              [mid-fruits.map        :refer [dissoc-in]]
              [mid-fruits.vector     :as vector]
              [x.app-components.api  :as components]
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

; @constant (boolean)
(def DISPLAY-LEGAL-LINKS? false)

; @constant (keyword)
(def DEFAULT-VIEW :main)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- set-language-event
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [selected-language]
  [:x.app-user/set-user-settings-item! :selected-language selected-language])



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-body-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  {:app-languages            (r locales/get-app-languages         db)
   :app-multilingual?        (r locales/app-multilingual?         db)
   :selected-language        (r locales/get-selected-language     db)
   :selected-view            (r gestures/get-selected-view        db ::handler)
   :user-email-address       (r user/get-user-email-address       db)
   :user-name                (r user/get-user-name                db)
   :user-profile-picture-url (r user/get-user-profile-picture-url db)})

(a/reg-sub ::get-body-props get-body-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ([item-props]
   [menu-item nil item-props])

  ([item-id item-props]
   [elements/button (a/id item-id)
                    (merge {
                            :color            :none
                            :horizontal-align :left
                            :layout           :row
                            :variant          :transparent}
                           (param item-props))]))

(defn- language-selector-languages
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [app-languages selected-language]}]
  (reduce #(let [language-selected? (= %2 selected-language)
                 button-props       {:icon :placeholder :label %2 :on-click (set-language-event %2)
                                     :preset (if language-selected? :primary-button :default-button)}]
                (vector/conj-item %1 [elements/button button-props]))
           [:div#x-app-menu--languages]
           (param app-languages)))

(defn- language-selector
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [popup-id body-props]
  (vector/conj-item (language-selector-languages popup-id body-props)
                    [elements/button ::back-button
                                     {:label    :back!
                                      :on-click [:x.app-gestures/change-view! ::handler :main]
                                      :preset   :back-button}]))

(defn- language-selector-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [app-multilingual?]}]
  [elements/button ::language-selector-button
                   {:disabled? (not app-multilingual?)
                    :preset    :language-button
                    :on-click  [:x.app-gestures/change-view! ::handler :language-selector]}])

(defn user-card
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [user-email-address user-name user-profile-picture-url]}]
  [:<> [:div.x-user-profile-picture {:style {:backgroundImage (css/url user-profile-picture-url)}}]
       [elements/separator {:size :s :orientation :horizontal}]
       [elements/label     {:content user-name :layout :fit :size :xl :font-weight :extra-bold}]
       [elements/label     {:content user-email-address :color :highlight :layout :fit :font-size :xs}]])

(defn- settings-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _]
  [elements/button ::settings-button
                   {:on-click [:x.app-router/go-to! "/settings"]
                    :preset   :settings-button}])

(defn- help-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _]
  [elements/button ::help-button
                   {:on-click [:x.app-router/go-to! "/help"]
                    :preset   :help-button}])

(defn- legal-links
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _]
  [:<> [elements/button ::terms-of-service-button
                        {:icon     :subject
                         :label    :terms-of-service
                         :on-click [:x.app-router/go-to! "/terms-of-service"]
                         :preset   :default-button}]
       [elements/button ::privacy-policy-button
                        {:icon     :subject
                         :label    :privacy-policy
                         :on-click [:x.app-router/go-to! "/privacy-policy"]
                         :preset   :default-button}]])

(defn- about-app
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _]
  [:<> [elements/label {:content (str details/app-name " | " details/app-description)
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
                        {:label    :back!
                         :on-click [:x.app-gestures/change-view! ::handler  :main]
                         :preset :back-button}]])

(defn- about-app-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _]
  [elements/button ::about-app-button
                   {:icon     :copyright
                    :label    :about-app
                    :on-click [:x.app-gestures/change-view! ::handler :about-app]
                    :preset   :default-button}])

(defn- logout-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _]
  [elements/button ::logout-button
                   {:on-click [:x.app-user/logout!]
                    :preset   :logout-button}])

(defn- main
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [popup-id body-props]
  [:div#x-app-menu--main
    [language-selector-button popup-id body-props]
    [settings-button          popup-id body-props]
   ;[help-button              popup-id body-props]
    (if DISPLAY-LEGAL-LINKS?
        [legal-links popup-id body-props])
    [about-app-button        popup-id body-props]
    [logout-button           popup-id body-props]])

(defn- app-menu
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [popup-id {:keys [selected-view] :as body-props}]
  (case selected-view
        :about-app         [about-app         popup-id body-props]
        :language-selector [language-selector popup-id body-props]
        :main              [main              popup-id body-props]))

(defn- body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [popup-id body-props]
  [:<> [user-card          popup-id body-props]
       [elements/separator {:size :l :orientation :horizontal}]
       [app-menu           popup-id body-props]])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ::render-as-popup!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:x.app-ui/add-popup! ::view
                        {:content          #'body
                         :label-bar        {:content #'ui/close-popup-header}
                         :horizontal-align :left
                         :layout           :boxed
                         :min-width        :xs
                         :subscriber       [::get-body-props]}])

(a/reg-event-fx
  ::render-as-sidebar!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:x.app-ui/set-sidebar! ::view
                          {:content    #'body
                           :subscriber [::get-body-props]}])

(a/reg-event-fx
  ::render!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {:db          (r gestures/init-view-handler!  db ::handler {:default-view DEFAULT-VIEW})
       :dispatch-if [(r environment/viewport-small? db)
                     [::render-as-sidebar!]
                     [::render-as-sidebar!]]}))
                     ;[::render-as-popup!]]}))
