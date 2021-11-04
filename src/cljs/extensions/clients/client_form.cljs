
(ns extensions.clients.client-form
    (:require [mid-fruits.candy   :refer [param]]
              [mid-fruits.form    :as form]
              [mid-fruits.map     :refer [dissoc-in]]
              [mid-fruits.string  :as string]
              [x.app-core.api     :as a :refer [r]]
              [x.app-db.api       :as db]
              [x.app-elements.api :as elements]
              [x.app-layouts.api  :as layouts]
              [x.app-locales.api  :as locales]
              [x.app-router.api   :as router]
              [x.app-sync.api     :as sync]
              [extensions.clients.engine :as engine]
              [extensions.pattern :as pattern]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-body-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  {:name-order        (r locales/get-name-order        db)
   :selected-language (r locales/get-selected-language db)})

(a/reg-sub ::get-body-props get-body-props)

(defn- get-header-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  {:form-completed? (r elements/form-completed? db ::client-form)
   :new-client?     (r pattern/new-item?        db "clients" "client")})

(a/reg-sub ::get-header-props get-header-props)

(defn- get-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  {:new-client?    (r pattern/new-item?          db "clients" "client")
   :synchronizing? (r sync/listening-to-request? db :clients/synchronize-client-form!)})

(a/reg-sub ::get-view-props get-view-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- undo-delete-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/button ::undo-delete-button
                   {:label :undo-delete! :variant :transparent :horizontal-align :left :color :warning
                    :on-click [:clients/undo-last-deleted!]}])

(defn- edit-copy-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/button ::edit-copy-button
                   {:label :edit-copy! :variant :transparent :horizontal-align :left :color :primary
                    :on-click [:clients/edit-last-duplicated!]}])

(defn- cancel-client-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id header-props]
  [elements/button ::cancel-client-button
                   {:tooltip :cancel! :preset :cancel-icon-button :on-click [:x.app-router/go-to! "/clients"]}])

(defn- delete-client-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id header-props]
  [elements/button ::delete-client-button
                   {:tooltip :delete! :preset :delete-icon-button :on-click [:clients/request-delete-client! "client-id"]}])

(defn- copy-client-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id header-props]
  [elements/button ::copy-client-button
                   {:tooltip :duplicate! :preset :duplicate-icon-button :on-click [:clients/request-duplicate-client! "client-id"]}])

(defn- save-client-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id {:keys [form-completed?] :as header-props}]
  [elements/button ::save-client-button
                   {:tooltip :save! :preset :save-icon-button :disabled? (not form-completed?)
                    :on-click [:clients/request-save-client!]}])

(defn- client-actions-buttons
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id header-props]
  [:<> [delete-client-button surface-id header-props]
       [copy-client-button   surface-id header-props]])

(defn- client-form-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id {:keys [new-client?] :as header-props}]
  [elements/polarity ::form-header
                     {:start-content [:<> (if (boolean new-client?)
                                              ;[cancel-client-button   surface-id header-props])
                                              nil
                                              [client-actions-buttons surface-id header-props])]
                      :end-content [save-client-button surface-id header-props]}])

(defn- client-legal-details
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id body-props]
  [:div#clients--client-form--legal-details
    [elements/text-field ::vat-no-field
                         {:label :vat-no :value-path [:client :form-data :client/vat-no]}]])

(defn- client-secondary-contacts
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id {:keys [selected-language] :as body-props}]
 [:div
  [:div {:style {:display :flex :grid-column-gap "24px" :flex-wrap :wrap}}
    [elements/select ::country-field
                     {:label :country :min-width :xxs
                      :initial-value   (locales/country-native-name selected-language)
                      :initial-options (param locales/EU-COUNTRY-NAMES)
                      :value-path [:clients :form-data :client/country]}]

    [elements/text-field ::zip-code-field
                         {:label :zip-code :min-width :xxs}]
    [elements/combo-box ::city-field
                        {:label :city :options-path [:clients :form-meta :suggestions :cities]
                         :style {:flex-grow 1}
                         :value-path [:clients :form-data :client/city]
                         :initial-value "Makó"
                         :initial-options ["HMVH" "BP"]

                         ; TODO BUG
                         ; Ez why nem megy?
                         :emptiable? false}]]
  [:div
    [elements/text-field ::address-field
                         {:label :address :min-width :grow :value-path [:clients :form-data :client/address]}]]])

(defn- client-primary-contacts
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id body-props]
  [:div#clients--client-form--primary-contacts
    [elements/text-field ::email-address-field
                         {:label :email-address :required? true
                          :value-path [:clients :form-data :client/email-address]
                          :validator {:f form/email-address-valid? :invalid-message :invalid-email-address}
                          :form-id ::client-form
                          :min-width :l}]
    [elements/text-field ::phone-number-field
                         {:label :phone-number :required? true
                          :value-path [:clients :form-data :client/phone-number]
                          :validator {:f form/phone-number-valid? :invalid-message :invalid-phone-number}
                          ; Nem egyértelmű a használata, ha egyszerűen le vannak tiltva bizonoyos karakterek
                          ;:modifier form/valid-phone-number
                          :modifier #(string/starts-with! % "+")
                          :form-id ::client-form
                          :min-width :l}]])

(defn- client-name
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id {:keys [name-order] :as body-props}]
  [:div#clients--client-form--client-name
    [locales/name-order [elements/text-field ::first-name-field
                                             {:label :first-name :required? true
                                              :value-path [:clients :form-data :client/first-name]
                                              :form-id ::client-form
                                              :min-width :l}]
                        [elements/text-field ::last-name-field
                                             {:label :last-name :required? true
                                              :value-path [:clients :form-data :client/last-name]
                                              :form-id ::client-form
                                              :min-width :l}]
                        (param name-order)]])

(defn- client-form
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id body-props]
  [:div#clients--client-form
    [client-name               surface-id body-props]
    [client-primary-contacts   surface-id body-props]
    [elements/separator {:orientation :horizontal :size :xxl}]
    [client-secondary-contacts surface-id body-props]
    [client-legal-details      surface-id body-props]
    [elements/separator {:orientation :horizontal :size :l}]])

(defn- view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id {:keys [new-client? synchronizing?] :as view-props}]
  [layouts/layout-a surface-id {:disabled? synchronizing?
                                :body {:content    #'client-form
                                       :subscriber [::get-body-props]}
                                :header {:content    #'client-form-header
                                         :sticky?    true
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
  :clients/render-client-form!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:x.app-ui/set-surface! ::view {:content #'view :subscriber [::get-view-props]}])

(a/reg-lifecycles
  ::lifecycles
  {:on-app-boot [:extensions/add-item-form-route! "clients" "client"]})
