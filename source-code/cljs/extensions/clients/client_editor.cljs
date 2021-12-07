
; Kezelni kell a "/clients/nincs-ilyen-client-id" esetet!


(ns extensions.clients.client-editor
    (:require [mid-fruits.candy   :refer [param]]
              [mid-fruits.form    :as form]
              [mid-fruits.string  :as string]
              [x.app-core.api     :as a :refer [r]]
              [x.app-db.api       :as db]
              [x.app-elements.api :as elements]
              [x.app-layouts.api  :as layouts]
              [x.app-locales.api  :as locales]
              [app-plugins.item-editor.api :as item-editor]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-body-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  {:name-order        (r locales/get-name-order        db)
   :selected-language (r locales/get-selected-language db)})

(a/reg-sub ::get-body-props get-body-props)



;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- action-buttons
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _]
  [:<> [item-editor/delete-item-button :clients :client]
       [item-editor/copy-item-button   :clients :client]])

(defn- header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [header-id {:keys [new-item?] :as header-props}]
  [elements/polarity ::form-header
                     {:start-content [:<> (if (boolean new-item?)
                                             ;[item-editor/cancel-item-button :clients :client]
                                              nil
                                              [action-buttons header-id header-props])]
                      :end-content [item-editor/save-item-button :clients :client header-props]}])



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- client-legal-details
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [body-id body-props]
  [:div#clients--client-form--legal-details
    [elements/text-field ::vat-no-field
                         {:label :vat-no :value-path [:clients :editor-data :client/vat-no]}]])

(defn- client-secondary-contacts
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [body-id {:keys [selected-language] :as body-props}]
 [:div
  [:div {:style {:display :flex :grid-column-gap "24px" :flex-wrap :wrap}}
    [elements/select ::country-field
                     {:label :country :min-width :xxs
                      :initial-value   (locales/country-native-name selected-language)
                      :initial-options (param locales/EU-COUNTRY-NAMES)
                      :value-path [:clients :editor-data :client/country]}]

    [elements/text-field ::zip-code-field
                         {:label :zip-code :min-width :xxs}]
    [elements/combo-box ::city-field
                        {:label :city :options-path [:clients :editor-meta :suggestions :cities]
                         :style {:flex-grow 1}
                         :value-path [:clients :editor-data :client/city]
                         :initial-value "Makó"
                         :initial-options ["HMVH" "BP"]

                         ; TODO BUG
                         ; Ez why nem megy?
                         :emptiable? false}]]
  [:div
    [elements/text-field ::address-field
                         {:label :address :min-width :grow :value-path [:clients :editor-data :client/address]}]]])

(defn- client-primary-contacts
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [body-id body-props]
  [:div#clients--client-form--primary-contacts
    [elements/text-field ::email-address-field
                         {:label :email-address :required? true
                          :value-path [:clients :editor-data :client/email-address]
                          :validator {:f form/email-address-valid? :invalid-message :invalid-email-address}
                          :form-id :item-editor
                          :min-width :l}]
    [elements/text-field ::phone-number-field
                         {:label :phone-number :required? true
                          :value-path [:clients :editor-data :client/phone-number]
                          :validator {:f form/phone-number-valid? :invalid-message :invalid-phone-number}
                          ; Nem egyértelmű a használata, ha egyszerűen le vannak tiltva bizonoyos karakterek
                          ;:modifier form/valid-phone-number
                          :modifier #(string/starts-with! % "+")
                          :form-id :item-editor
                          :min-width :l}]])

(defn- client-name
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [body-id {:keys [name-order] :as body-props}]
  [:div#clients--client-form--client-name
    [locales/name-order [elements/text-field ::first-name-field
                                             {:label :first-name :required? true
                                              :value-path [:clients :editor-data :client/first-name]
                                              :form-id :item-editor
                                              :min-width :l}]
                        [elements/text-field ::last-name-field
                                             {:label :last-name :required? true
                                              :value-path [:clients :editor-data :client/last-name]
                                              :form-id :item-editor
                                              :min-width :l}]
                        (param name-order)]])

(defn- body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [body-id body-props]
  [:div#clients--client-form
    [client-name               body-id body-props]
    [client-primary-contacts   body-id body-props]
    [elements/separator {:orientation :horizontal :size :xl}]
    [elements/separator {:orientation :horizontal :size :xl}]
    [client-secondary-contacts body-id body-props]
    [client-legal-details      body-id body-props]
    [elements/separator {:orientation :horizontal :size :l}]])



;; -- View components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id {:keys [description synchronizing?] :as view-props}]
  [layouts/layout-a surface-id {:description description
                                :disabled?   synchronizing?
                                :body   {:content #'body   :subscriber [::get-body-props]}
                                :header {:content #'header :subscriber [:item-editor/get-header-props :products :product]}}])



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :clients/edit-last-duplicated!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      [:ui/pop-bubble! ::notification]))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :clients/render-client-editor!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:ui/set-surface! ::view {:view {:content #'view :subscriber [:item-editor/get-view-props :products :product]}}])
