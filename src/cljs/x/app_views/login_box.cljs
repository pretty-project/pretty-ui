
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.02.14
; Description:
; Version: v0.7.2
; Compatibility: x4.4.4



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

(defn- get-label-bar-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  ; XXX#1450
  ; Ha a :surfaces UI renderer legalább egy surface elemet kirenderel,
  ; akkor a login-box label-bar sávjában "bezárás gomb", ellenkező esetben
  ; "vissza gomb" jelenik meg.
  (if (r ui/any-element-rendered? db :surfaces)
      {:content       #'ui/close-popup-label-bar
       :content-props {:label (r a/get-app-detail db :app-title)}}))

(defn- get-view-props
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
   :synchronizing?   (r sync/listening-to-request? db :x.app-user/authenticate!)})

(a/reg-sub ::get-view-props get-view-props)



;; -- Login form components ---------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- login-error-message
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;
  ; @return (hiccup)
  [_ _]
  [elements/label ::login-error-message
                  {:content :incorrect-email-address-or-password
                   :color   :warning}])

(defn- email-address-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;
  ; @return (component)
  [_ {:keys [synchronizing?]}]
  [elements/text-field ::email-address-field
                       {:label      :email-address
                        :value-path (db/meta-item-path ::primary :email-address)
                        :disabled?  synchronizing?}])

(defn- password-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;
  ; @return (component)
  [_ {:keys [synchronizing?]}]
  [elements/password-field ::password-field
                           {:value-path (db/meta-item-path ::primary :password)
                            :disabled?  synchronizing?}])

(defn- login-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
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
                           :on-click   [:x.app-user/authenticate!]
                           :variant    :filled}])

(defn- forgot-password-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;
  ; @return (hiccup)
  [_ _]
  [elements/button ::forgot-password-button
                   {:color    :muted
                    :label    :forgot-password
                    :layout   :fit
                    :on-click [:x.app-ui/blow-bubble!
                               ::service-not-available
                               {:content :service-not-available :color :warning}]
                    :variant  :transparent}])

(defn- login-form
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;  {:login-attempted? (boolean)}
  ;
  ; @return (component)
  [component-id {:keys [login-attempted?] :as view-props}]
  [:<> (if login-attempted?    [login-error-message component-id view-props])
       [elements/separator     {:size :m}]
       [email-address-field    component-id view-props]
       [elements/separator     {:size :m}]
       [password-field         component-id view-props]
       [elements/separator     {:size :xl}]
       [login-button           component-id view-props]
      ;[forgot-password-button component-id view-props]
       [elements/separator     {:size :m}]])



;; -- Logout form components --------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- logout-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;
  ; @return (component)
  [_ _]
  [elements/button ::logout-button
                   {:on-click [:x.app-user/logout!]
                    :color    :none
                    :label    :logout!
                    :layout   :row
                    :variant  :transparent}])

(defn- continue-as-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;  {:username (string)}
  ;
  ; @return (component)
  [_ {:keys [username]}]
  [elements/button ::continue-as-button
                   {:keypress {:key-code 13}
                    :on-click [:x.app-router/go-home!]
                    :label    :continue-as
                    :layout   :row
                    :suffix   username
                    :variant  :filled}])

(defn- signed-in-as-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;  {:username (string)}
  ;
  ; @return (hiccup)
  [_ {:keys [username]}]
  [elements/label ::signed-in-as-label
                  {:content :signed-in-as :suffix username
                   :horizontal-align :center}])

(defn- logged-in-form
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;
  ; @return (component)
  [component-id view-props]
  [:<> [elements/separator {:size :xs}]
       [signed-in-as-label component-id view-props]
       [elements/separator {:size :m}]
       [continue-as-button component-id view-props]
       [logout-button      component-id view-props]])



;; -- Login box components ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;  {:user-identified? (boolean)}
  ; @param (vector) subscriber
  ;
  ; @return (component)
  [component-id {:keys [user-identified?] :as view-props}]
  [:div#x-login-box
    (if (boolean user-identified?)
        [logged-in-form component-id view-props]
        [login-form     component-id view-props])])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ::render!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map)(opt) login-box-props
  ;  {:render-exclusive? (boolean)(opt)
  ;    Default: false}
  (fn [{:keys [db]} [_ {:keys [render-exclusive?]}]]
      [:x.app-ui/add-popup! ::view
                            {:content           #'view
                             :layout            :boxed
                             :label-bar         (r get-label-bar-props db)
                             :min-width         :xs
                             :render-exclusive? render-exclusive?
                             :subscriber        [::get-view-props]
                             :user-close?       false}]))

(a/reg-lifecycles
  ::lifecycles
  {:on-app-boot [:x.app-router/add-route! ::route
                                          {:route-event    [::render!]
                                           :route-template "/login"}]})
