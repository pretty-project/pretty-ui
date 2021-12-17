
; Kezelni kell a "/clients/nincs-ilyen-client-id" esetet!


(ns app-extensions.clients.client-editor
    (:require [mid-fruits.candy      :refer [param]]
              [mid-fruits.form       :as form]
              [mid-fruits.string     :as string]
              [x.app-core.api        :as a :refer [r]]
              [x.app-db.api          :as db]
              [x.app-elements.api    :as elements]
              [x.app-environment.api :as environment]
              [x.app-locales.api     :as locales]
              [app-plugins.item-editor.api :as item-editor]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-client-name
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (let [first-name (get-in db [:clients :editor-data :first-name])
        last-name  (get-in db [:clients :editor-data :last-name])]
       (r locales/get-ordered-name db first-name last-name)))

(defn- get-body-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (merge (r item-editor/get-body-props db :clients :client)
         {:form-label        (r get-client-name               db)
          :name-order        (r locales/get-name-order        db)
          :selected-language (r locales/get-selected-language db)
          :viewport-large?   (r environment/viewport-large?   db)}))

(a/reg-sub :client-editor/get-body-props get-body-props)



;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [header-id header-props]
  [item-editor/header :clients :client header-props])



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- client-legal-details
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [body-id body-props]
  [:div#clients--client-editor--legal-details
    [elements/text-field ::vat-no-field
                         {:label :vat-no :value-path [:clients :editor-data :vat-no]}]])

(defn- client-country-select
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [selected-language synchronizing?]}]
  [elements/select ::country-select
                   {:label :country ;:min-width :xxs :user-cancel? false
                    :initial-value   (locales/country-native-name selected-language)
                    :initial-options (param locales/EU-COUNTRY-NAMES)
                    :value-path      [:clients :editor-data :country]
                    :disabled?       synchronizing?}])

(defn- client-zip-code-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [synchronizing?]}]
  [elements/text-field ::zip-code-field
                       {:label :zip-code ; :min-width :xxs
                        :value-path [:clients :editor-data :zip-code]
                        :disabled?  synchronizing?}])

(defn- client-city-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [synchronizing?]}]
  [elements/combo-box ::city-field
                      {:label :city :emptiable? false
                       :options-path [:clients :editor-meta :suggestions :city]
                       :value-path   [:clients :editor-data :city]
                       :disabled? synchronizing?}])

(defn- client-address-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [synchronizing?]}]
  [elements/text-field ::address-field
                       {:label :address :min-width :grow
                        :value-path [:clients :editor-data :address]
                        :disabled? synchronizing?}])

(defn- client-secondary-contacts
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [body-id body-props]
 [:div
  [:div {:style {:display :flex :grid-column-gap "24px" :flex-wrap :wrap}}
    [client-country-select body-id body-props]
    [client-zip-code-field body-id body-props]
    [client-city-field     body-id body-props]]

  [:div
    [client-address-field body-id body-props]]])


(defn- client-phone-number-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [synchronizing?]}]
  [elements/text-field ::phone-number-field
                       {:label :phone-number :required? true :min-width :l :indent :both
                        :value-path [:clients :editor-data :phone-number]
                        :validator {:f form/phone-number-valid? :invalid-message :invalid-phone-number}
                        ; Ha egyszerűen le lennének tiltva bizonoyos karakterek, nem lenne egyértelmű a használata!
                        ;:modifier form/valid-phone-number
                        :modifier #(string/starts-with! % "+")
                        :form-id   (item-editor/form-id :clients :client)
                        :disabled? synchronizing?}])

(defn- client-email-address-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [synchronizing?]}]
  [elements/text-field ::email-address-field
                       {:label :email-address :required? true :min-width :l :indent :both
                        :value-path [:clients :editor-data :email-address]
                        :validator {:f form/email-address-valid? :invalid-message :invalid-email-address}
                        :form-id   (item-editor/form-id :clients :client)
                        :disabled? synchronizing?}])

(defn- client-primary-contacts
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [body-id body-props]
  [:div#clients--client-editor--primary-contacts [client-email-address-field body-id body-props]
                                                 [client-phone-number-field  body-id body-props]])

(defn- client-last-name-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [synchronizing?]}]
  [elements/text-field ::last-name-field
                       {:label :last-name :required? true :min-width :l :indent :both
                        :value-path [:clients :editor-data :last-name]
                        :form-id    (item-editor/form-id :clients :client)
                        :disabled?  synchronizing?}])

(defn- client-first-name-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [synchronizing?]}]
  [elements/text-field ::first-name-field
                       {:label :first-name :required? true :min-width :l :indent :both
                        :value-path [:clients :editor-data :first-name]
                        :form-id    (item-editor/form-id :clients :client)
                        :disabled?  synchronizing?}])

(defn- client-name
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [body-id {:keys [name-order] :as body-props}]
  [:div#clients--client-editor--client-name
    [locales/name-order [client-first-name-field body-id body-props]
                        [client-last-name-field  body-id body-props]
                        (param name-order)]])

(defn- body-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [body-id body-props]
  [:div#clients--client-editor {:style {:display :flex :flex-direction :column}}
    ; Color and name
    [item-editor/form-header    :clients :client body-props]
    [item-editor/color-selector :clients :client body-props]
    [item-editor/input-group-footer]
    ; Basic info
    [item-editor/input-group-label :clients :client {:content :basic-info}]
    [client-name               body-id body-props]
    [client-primary-contacts   body-id body-props]
    [item-editor/input-group-footer]
    ; More info
    [item-editor/input-group-label :clients :client {:content :more-info}]
    [client-secondary-contacts body-id body-props]
    [client-legal-details      body-id body-props]
    [item-editor/form-footer]])

(defn- body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [body-id]
  (let [body-props (a/subscribe [:client-editor/get-body-props])]
       (fn [] [body-structure body-id @body-props])))



;; -- View components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id]
  [item-editor/view :clients :client {:form-element #'body}])



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
  [:ui/set-surface! ::view {:view {:content #'view}}])
