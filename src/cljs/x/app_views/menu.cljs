
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.21
; Description:
; Version: v1.4.4
; Compatibility: x3.9.9



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
              [x.app-developer.api   :as developer]
              [x.app-elements.api    :as elements]
              [x.app-environment.api :as environment]
              [x.app-locales.api     :as locales]
              [x.app-ui.api          :as ui]
              [x.app-user.api        :as user]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (boolean)
(def DISPLAY-LEGAL-LINKS? false)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- set-language-event
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) selected-language
  ;
  ; @return (map)
  [selected-language]
  {:dispatch-n [[:x.app-user/set-user-settings-item! :selected-language selected-language]]})



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-selected-view-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (keyword)
  [db _]
  (let [selected-view (get-in db (db/meta-item-path ::settings :selected-view))]
       (or selected-view :main)))

(defn- get-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  ;  {:app-languages (keywords in vector)
  ;   :app-multilingual? (boolean)
  ;   :render-developer-tools? (boolean)
  ;   :selected-language (keyword)
  ;   :selected-view (keyword)
  ;   :user-email-address (string)
  ;   :user-name (string)
  ;   :user-profile-picture-url (string)}
  [db _]
  {:app-languages            (r locales/get-app-languages         db)
   :app-multilingual?        (r locales/app-multilingual?         db)
   :render-developer-tools?  (r a/debug-mode-detected?            db)
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
  ;
  ; @param (keyword)(opt) item-id
  ; @param (map) item-props
  ;  {:class (string or vector)(opt)
  ;    Default: "x-app-menu--menu-item"
  ;   :color (keyword)(opt)
  ;    Default: :none
  ;   :label (metamorphic-content)
  ;   :on-click (metamorphic-event)}
  ;
  ; @return (component)
  ([item-props]
   [menu-item nil item-props])

  ([item-id item-props]
   [elements/button (a/id item-id)
                    (merge {:class "x-app-menu--menu-item"
                            :color            :none
                            :horizontal-align :left
                            :layout           :row
                            :variant          :transparent}
                           (param item-props))]))

(defn- user-info
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;  {:user-email-address (string)
  ;   :user-name (string)
  ;   :user-profile-picture-url (string)}
  ;
  ; @return (hiccup)
  [_ {:keys [user-email-address user-name user-profile-picture-url]}]
  [:div#x-app-menu--user-info
   [:div.x-user-profile-picture
    {:style {:backgroundImage (css/url user-profile-picture-url)}}]
   [elements/separator {:size :m}]
   [elements/label     {:content user-name
                        :layout :fit :size :xl
                        :font-weight :extra-bold}]
   [elements/label     {:content user-email-address
                        :color :highlight :layout :fit :font-size :xs}]])

(defn- language-selector-languages
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;  {:app-languages (keywords in vector)
  ;   :selected-language (keyword)}
  ;
  ; @return (hiccup)
  [_ {:keys [app-languages selected-language]}]
  (reduce #(let [language-selected? (= %2 selected-language)
                 menu-item-props    {:icon :none :label %2 :on-click (set-language-event %2)
                                     :color (if language-selected? :primary :default)}]
                (vector/conj-item %1 [menu-item menu-item-props]))
           [:div#x-app-menu--languages]
           (param app-languages)))

(defn- language-selector
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;
  ; @return (hiccup)
  [component-id view-props]
  (vector/conj-item (language-selector-languages component-id view-props)
                    [menu-item {:icon     :arrow_back
                                :label    :back!
                                :on-click [::go-to! :main]}]))

(defn- language-selector-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;  {:app-multilingual? (boolean)}
  ;
  ; @return (component)
  [_ {:keys [app-multilingual?]}]
  [menu-item {:disabled?      (not app-multilingual?)
              :end-adornments [{:icon :arrow_right}]
              :icon     :translate
              :label    :language
              :on-click [::go-to! :language-selector]}])

(defn- settings-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;
  ; @return (component)
  [_ _]
  [menu-item {:disabled? true
              :icon     :settings
              :label    :settings
              :on-click [:x.app-router/go-to! "/settings"]}])

(defn- help-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;
  ; @return (component)
  [_ _]
  [menu-item {:icon     :help_outline
              :label    :help
              :on-click [:x.app-router/go-to! "/help"]}])

(defn- legal-links
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;
  ; @return (hiccup)
  [_ _]
  [:div#x-app-menu--legal-links
   [menu-item {:icon     :subject
               :label    :terms-of-service
               :on-click [:x.app-router/go-to! "/terms-of-service"]}]
   [menu-item {:icon     :subject
               :label    :privacy-policy
               :on-click [:x.app-router/go-to! "/privacy-policy"]}]])

(defn- about-app
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;
  ; @return (hiccup)
  [_ _]
  [:div#x-app-menu--about-app
   [elements/label {:content (str details/app-name " | " details/app-description)
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
   [menu-item {:icon     :arrow_back
               :label    :back!
               :on-click [::go-to! :main]}]])

(defn- about-app-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;
  ; @return (component)
  [_ _]
  [menu-item {:icon     :copyright
              :label    :about-app
              :on-click [::go-to! :about-app]}])

(defn- database-browser
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;
  ; @return (hiccup)
  [_ _]
  [:div#x-app-menu--developer-tools--database-browser
    [developer/database-browser]
    [menu-item {:icon     :arrow_back
                :label    :developer-tools
                :on-click [::go-to! :developer-tools]}]])

(defn- developer-tools
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;
  ; @return (hiccup)
  [_ _]
  [:div#x-app-menu--developer-tools
    [menu-item {:icon     :storage
                :label    :application-database-browser
                :on-click [::go-to! :database-browser]}]
    [menu-item {:icon     :save_alt
                :label    :export-application-database!
                :on-click [:x.app-developer/export-app-db!]
                :disabled? true}]
    [menu-item {:icon     :save_alt
                :label    :export-application-log!
                :on-click [:x.app-developer/export-console-log!]
                :disabled? true}]
    [menu-item {:icon      :sports_esports
                :label     "Playground"
                :on-click  [:x.app-router/go-to! "/playground"]}]
    [menu-item {:icon     :arrow_back
                :label    :back!
                :on-click [::go-to! :main]}]])

(defn- developer-tools-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;  {:render-developer-tools? (boolean)}
  ;
  ; @return (component)
  [_ {:keys [render-developer-tools?]}]
  (if render-developer-tools?
      [menu-item {:icon     :code
                  :label    :developer-tools
                  :on-click [::go-to! :developer-tools]}]))

(defn- logout-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;
  ; @return (component)
  [_ _]
  [menu-item {:color    :warning
              :icon     :logout
              :label    :logout!
              :on-click [:x.app-user/logout!]}])

(defn- main
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;
  ; @return (hiccup)
  [component-id view-props]
  [:div#x-app-menu--main
    [language-selector-button component-id view-props]
    [settings-button          component-id view-props]
   ;[help-button              component-id view-props]
    (if DISPLAY-LEGAL-LINKS?
        [legal-links component-id view-props])
    [about-app-button        component-id view-props]
    [developer-tools-button  component-id view-props]
    [logout-button           component-id view-props]])

(defn- content-broker
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;  {:selected-view (keyword)}
  ;
  ; @return (component)
  [component-id {:keys [selected-view] :as view-props}]
  (case selected-view
        :about-app         [about-app         component-id view-props]
        :database-browser  [database-browser  component-id view-props]
        :developer-tools   [developer-tools   component-id view-props]
        :language-selector [language-selector component-id view-props]
        :main              [main              component-id view-props]))

(defn- view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;  {:selected-view (keyword)}
  ;
  ; @return ()
  [component-id view-props]
  [:div#x-app-menu
    [user-info component-id view-props]
    [elements/horizontal-line {:color :highlight :layout :row}]
    [content-broker component-id view-props]])



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- go-to!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ;
  ; @return (map)
  [db [_ component-id]]
  (assoc-in db (db/meta-item-path ::settings :selected-view) component-id))

(a/reg-event-db ::go-to! go-to!)

(defn- reset-menu-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (dissoc-in db (db/meta-item-path ::settings)))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ::render-as-popup!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {:db       (r reset-menu-props! db)
       :dispatch [:x.app-ui/add-popup!
                  ::view
                  {:content    #'view
                   :label-bar  {:content #'ui/close-popup-label-bar}
                   :layout     :boxed
                   :min-width  :s
                   :subscriber [::get-view-props]}]}))

(a/reg-event-fx
  ::render-as-sidebar!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {:db       (r reset-menu-props! db)
       :dispatch [:x.app-ui/set-sidebar!
                  {:content    #'view
                   :subscriber [::get-view-props]}]}))

(a/reg-event-fx
  ::render!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {:dispatch-if [(r environment/viewport-small? db)
                     [::render-as-sidebar!]
                     [::render-as-popup!]]}))
