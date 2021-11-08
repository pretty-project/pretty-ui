
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
              [x.app-locales.api     :as locales]
              [x.app-ui.api          :as ui]
              [x.app-user.api        :as user]
              [x.app-views.user-card :refer [view] :rename {view user-card}]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (boolean)
(def DISPLAY-LEGAL-LINKS? false)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- set-language-event
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [selected-language]
  [:x.app-user/set-user-settings-item! :selected-language selected-language])



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-selected-view-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (let [selected-view (get-in db (db/meta-item-path ::settings :selected-view))]
       (or selected-view :main)))

(defn- get-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  {:app-languages            (r locales/get-app-languages         db)
   :app-multilingual?        (r locales/app-multilingual?         db)
   :selected-language        (r locales/get-selected-language     db)
   :selected-view            (r get-selected-view-id              db)
   :user-email-address       (r user/get-user-email-address       db)
   :user-name                (r user/get-user-name                db)
   :user-profile-picture-url (r user/get-user-profile-picture-url db)})

(a/reg-sub ::get-view-props get-view-props)



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
  [component-id view-props]
  (vector/conj-item (language-selector-languages component-id view-props)
                    [elements/button ::back-button
                                     {:label    :back!
                                      :on-click [::go-to! :main]
                                      :preset   :back-button}]))

(defn- language-selector-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [app-multilingual?]}]
  [elements/button ::language-selector-button
                   {:disabled? (not app-multilingual?)
                    :preset    :language-button
                    :on-click  [::go-to! :language-selector]}])

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
                         :on-click [::go-to! :main]
                         :preset :back-button}]])

(defn- about-app-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _]
  [elements/button ::about-app-button
                   {:icon     :copyright
                    :label    :about-app
                    :on-click [::go-to! :about-app]
                    :preset   :default-button}])

(defn- logout-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _]
  [elements/button ::logout-button
                   {:on-click [:x.app-user/logout!]
                    :preset   :logout-button}])

(defn- main
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [component-id view-props]
  [:div#x-app-menu--main
    [language-selector-button component-id view-props]
    [settings-button          component-id view-props]
   ;[help-button              component-id view-props]
    (if DISPLAY-LEGAL-LINKS?
        [legal-links component-id view-props])
    [about-app-button        component-id view-props]
    [logout-button           component-id view-props]])

(defn- app-menu
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [component-id {:keys [selected-view] :as view-props}]
  (case selected-view
        :about-app         [about-app         component-id view-props]
        :language-selector [language-selector component-id view-props]
        :main              [main              component-id view-props]))

(defn- view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [component-id view-props]
  [:<> [user-card          component-id view-props]
       [elements/separator {:size :l :orientation :horizontal}]
       [app-menu           component-id view-props]])



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- go-to!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ component-id]]
  (assoc-in db (db/meta-item-path ::settings :selected-view) component-id))

(a/reg-event-db ::go-to! go-to!)

(defn- reset-menu-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (dissoc-in db (db/meta-item-path ::settings)))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ::render-as-popup!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:x.app-ui/add-popup! ::view
                        {:content          #'view
                         :label-bar        {:content #'ui/close-popup-label-bar}
                         :horizontal-align :left
                         :layout           :boxed
                         :min-width        :xs
                         :subscriber       [::get-view-props]}])

(a/reg-event-fx
  ::render-as-sidebar!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:x.app-ui/set-sidebar! ::view
                          {:content    #'view
                           :subscriber [::get-view-props]}])

(a/reg-event-fx
  ::render!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {:db          (r reset-menu-props! db)
       :dispatch-if [(r environment/viewport-small? db)
                     [::render-as-sidebar!]
                     [::render-as-sidebar!]]}))
                     ;[::render-as-popup!]]}))
