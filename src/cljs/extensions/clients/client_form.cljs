
(ns extensions.clients.client-form
    (:require [mid-fruits.candy   :refer [param]]
              [mid-fruits.form    :as form]
              [mid-fruits.string  :as string]
              [x.app-core.api     :as a :refer [r]]
              [x.app-db.api       :as db]
              [x.app-elements.api :as elements]
              [x.app-layouts.api  :as layouts]
              [x.app-locales.api  :as locales]
              [x.app-router.api   :as router]
              [x.app-sync.api     :as sync]
              [x.app-ui.api       :as ui]
              [extensions.clients.engine :as engine]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-body-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  {:name-order      (r locales/get-name-order           db)
   :new-client?     (r router/current-route-path-param? db :client-id "new-client")
   :synchronizing?  (r sync/listening-to-request?       db :clients/synchronize-client-form!)})

(a/reg-sub ::get-body-props get-body-props)

(defn- get-header-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  {:form-completed? (r elements/form-completed?         db ::client-form)
   :new-client?     (r router/current-route-path-param? db :client-id "new-client")
   :synchronizing?  (r sync/listening-to-request?       db :clients/synchronize-client-form!)})

(a/reg-sub ::get-header-props get-header-props)

(defn- get-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  {:new-client? (r router/current-route-path-param? db :client-id "new-client")})

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

(defn- cancel-client-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id header-props]
  [elements/button {:label :cancel! :preset :cancel-icon-button :on-click [:x.app-router/go-to! "/clients"]}])

(defn- delete-client-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id header-props]
  [elements/button {:label :delete! :preset :delete-icon-button :on-click [:clients/request-delete-client! "client-id"]}])

(defn- copy-client-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id header-props]
  [elements/button {:label :duplicate! :preset :duplicate-icon-button :on-click [:clients/request-duplicate-client! "client-id"]}])

(defn- save-client-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id {:keys [form-completed? new-client?] :as header-props}]
  [elements/button {:label :save! :preset :save-icon-button :disabled? (not form-completed?)
                    :on-click [:clients/request-save-client!]}])

(defn- client-actions-buttons
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id header-props]
  [:div.x-icon-buttons
    [delete-client-button surface-id header-props]
    [copy-client-button   surface-id header-props]])

(defn- client-form-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id {:keys [new-client? synchronizing?] :as header-props}]
  [elements/overlay {:disabled? synchronizing?
                     :content   [elements/row {:content [:<> (if (boolean new-client?)
                                                                 [cancel-client-button   surface-id header-props]
                                                                 [client-actions-buttons surface-id header-props])
                                                             [save-client-button surface-id header-props]]
                                               :horizontal-align :space-between}]}])

(defn- client-legal-details
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id body-props]
  [:div#clients--client-form--legal-details
    [elements/text-field ::vat-no {:label :vat-no}]])

(defn- client-secondary-contacts
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id body-props]
 [:div
  [:div#clients--client-form--secondary-contacts
    [elements/select    ::country {:label :country :min-width :xxs ; :default-value {:label "Magyarország"}
                                   :options [{:value "Magyarország"
                                              :label "Magyarország"}]
                                   :value-path [:clients :form :country]}]
    [elements/combo-box ::city    {:label :city :options-path [:clients :form :suggestions :cities]}]]
  [:div#clients--client-form--secondary-contacts
    [elements/text-field ::zip-code {:label :zip-code :min-width :xxs}]
    [elements/text-field ::address  {:label :address :min-width :grow}]]])

(defn- client-primary-contacts
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id body-props]
  [:div#clients--client-form--primary-contacts
    [elements/text-field ::email-address {:label :email-address :required? true
                                          :value-path [:clients :form :email-address]
                                          :validator {:f form/email-address-valid? :invalid-message :invalid-email-address}
                                          :form-id ::client-form}]
    [elements/text-field ::phone-number {:label :phone-number :required? true
                                         :value-path [:clients :form :phone-number]
                                         :validator {:f form/phone-number-valid? :invalid-message :invalid-phone-number}
                                         ; Nem egyértelmű a használata, ha egyszerűen le vannak tiltva bizonoyos karakterek
                                         ;:modifier form/valid-phone-number
                                         :modifier #(string/starts-with! % "+")
                                         :form-id ::client-form}]])

(defn- client-name
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id {:keys [name-order] :as body-props}]
  [:div#clients--client-form--client-name
    [locales/name-order [elements/text-field ::first-name {:label :first-name :required? true
                                                           :value-path [:clients :form :first-name]
                                                           :form-id ::client-form}]
                        [elements/text-field ::last-name  {:label :last-name  :required? true
                                                           :value-path [:clients :form :last-name]
                                                           :form-id ::client-form}]
                        (param name-order)]])

(defn- client-no
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id {:keys [client-no] :as body-props}]
  [:div#clients--client-form--client-no
    [elements/label {:content :client-id :font-size :xxs :color :highlight :font-weight :bold       :layout :fit}]
    [elements/text  {:content client-no  :font-size :xs  :color :muted     :font-weight :extra-bold :layout :fit :prefix "#"}]])

(defn- client-form
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id {:keys [new-client? synchronizing?] :as body-props}]
  [elements/overlay {:disabled? synchronizing?
                     :content   [:div#clients--client-form
                                  (if-not (boolean new-client?)
                                          [client-no surface-id body-props])
                                  [client-name               surface-id body-props]
                                  [client-primary-contacts   surface-id body-props]
                                  [client-secondary-contacts surface-id body-props]
                                  [client-legal-details      surface-id body-props]
                                  [elements/separator {:orientation :horizontal :size :l}]]}])

(defn- view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id {:keys [new-client?] :as view-props}]
  [layouts/layout-a surface-id {:label (if new-client? :add-client :edit-client)
                                :icon :people
                                :body        {:content    #'client-form
                                              :subscriber [::get-body-props]}
                                :body-header {:content    #'client-form-header
                                              :subscriber [::get-header-props]}}])



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
  {:dispatch-n [[:x.app-ui/listen-to-process! :clients/synchronize-client-form!]
                [:x.app-db/set-item! [:clients :form :suggestions :cities]
                                     ["Szeged"]]
                [:x.app-db/set-item! [:clients :form :suggestions :countries]
                                     ["Magyarország"]]
                [:x.app-ui/set-surface!
                 ::view {:content   #'view
                         :label-bar {:content       #'ui/go-back-surface-label-bar
                                     :content-props {:label :clients}}
                         :subscriber [::get-view-props]}]]})

(a/reg-lifecycles
  ::lifecycles
  {:on-app-boot [:x.app-router/add-route!
                 ::extended-route
                 {:restricted?    true
                  :route-event    [:x.app-extensions.clients/render-client-form!]
                  :route-title    :clients
                  :route-template "/clients/:client-id"}]})
