
(ns extensions.clients.client-form
    (:require [mid-fruits.candy   :refer [param]]
              [mid-fruits.form    :as form]
              [x.app-core.api     :as a :refer [r]]
              [x.app-db.api       :as db]
              [x.app-elements.api :as elements]
              [x.app-layouts.api  :as layouts]
              [x.app-locales.api  :as locales]
              [x.app-ui.api       :as ui]
              [extensions.clients.engine :as engine]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  {:name-order (r locales/get-name-order db)
   :client-no  "608030"})

(a/reg-sub ::get-view-props get-view-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- undo-delete-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/button {:label :undo-delete! :variant :transparent :horizontal-align :left :color :warning
                    :on-click [:clients/undo-last-deleted!]}])

(defn- edit-copy-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/button {:label :edit-copy! :variant :transparent :horizontal-align :left :color :primary
                    :on-click [:clients/edit-last-duplicated!]}])

(defn- delete-client-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id view-props]
  [elements/button {:label :delete! :variant :transparent :color :warning
                    :layout :icon-button :icon :delete_outline :on-click [:clients/request-delete-client! "client-id"]}])

(defn- copy-client-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id view-props]
  [elements/button {:label :duplicate! :variant :transparent :layout :icon-button
                    :icon :content_copy :color :none :on-click [:clients/request-duplicate-client! "client-id"]}])

(defn- save-client-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id view-props]
  [elements/button {:label :save! :variant :transparent :layout :icon-button :icon :save
                    :on-click [:clients/request-save-client! "client-id"]}])
  ; Ne legyen disabled állapota a mentés gombnak, inkább a gomb megnyomása jelölje warning
  ; színnel a nem megfelelően kitöltött mezőket és esetleg írja ki, hogy hol a hiba

(defn- client-form-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id view-props]
  [elements/row {:content [:<> [:div.x-icon-buttons
                                 [delete-client-button surface-id view-props]
                                 [copy-client-button   surface-id view-props]]
                               [save-client-button surface-id view-props]]
                 :horizontal-align :space-between}])

(defn- client-secondary-contacts
  [_ _]
 [:div
  [:div#clients--client-form--secondary-contacts
    [elements/text-field ::country {:label :country}]
    [elements/text-field ::city    {:label :city}]]
  [:div#clients--client-form--secondary-contacts
    [elements/text-field ::city {:label :zip-code :min-width :xxs}]
    [elements/text-field ::city {:label :address :min-width :grow}]]])


(defn- client-primary-contacts
  [_ _]
  [:div#clients--client-form--primary-contacts
    [elements/text-field ::email-address {:label :email-address :required? true :value-path (db/path ::client-form :email-address)
                                          :validator {:f form/email-address-valid? :invalid-message :invalid-email-address}}]
    [elements/text-field :phone-number {:label :phone-number :required? true :value-path (db/path ::client-form :phone-number)
                                        :validator {:f form/phone-number-valid? :invalid-message :invalid-phone-number}
                                        :modifier form/valid-phone-number}]])

(defn- client-name
  [_ {:keys [name-order]}]
  [:div#clients--client-form--client-name
    [locales/name-order [elements/text-field ::first-name {:label :first-name :required? true :value-path (db/path ::client-form :first-name)}]
                        [elements/text-field ::last-name  {:label :last-name  :required? true :value-path (db/path ::client-form :last-name)}]
                        (param name-order)]])

(defn- client-no
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [client-no]}]
  [:div#clients--client-form--client-no
    [elements/label {:content :client-id :font-size :xxs :color :highlight :font-weight :bold       :layout :fit}]
    [elements/text  {:content client-no  :font-size :xs  :color :muted     :font-weight :extra-bold :layout :fit :prefix "#"}]])

(defn- client-form
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id {:keys [] :as view-props}]
  [:div#clients--client-form
    [client-no                 surface-id view-props]
    [client-name               surface-id view-props]
    [client-primary-contacts   surface-id view-props]
    [client-secondary-contacts surface-id view-props]
    [elements/separator {:orientation :horizontal :size :l}]])

(defn- view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id view-props]
  [layouts/layout-a surface-id {:label :edit-client :icon :people
                                :body        {:content     #'client-form
                                              :content-props view-props}
                                :body-header {:content     #'client-form-header
                                              :content-props view-props}}])



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :clients/edit-last-duplicated!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      [:x.app-ui/pop-bubble! ::notification]))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------


(a/reg-event-fx
  :x.app-extensions.clients/render-client-form!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:x.app-ui/set-surface!
   ::view {:content   #'view
           :label-bar {:content       #'ui/go-back-surface-label-bar
                       :content-props {:label :clients}}
           :subscriber [::get-view-props]}])

(a/reg-lifecycles
  ::lifecycles
  {:on-app-boot [:x.app-router/add-route!
                 ::extended-route
                 {:restricted?    true
                  :route-event    [:x.app-extensions.clients/render-client-form!]
                  :route-title    :clients
                  :route-template "/clients/:client-id"}]})
