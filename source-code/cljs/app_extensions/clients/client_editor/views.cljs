
(ns app-extensions.clients.client-editor.views
    (:require [mid-fruits.candy   :refer [param]]
              [mid-fruits.form    :as form]
              [mid-fruits.string  :as string]
              [x.app-core.api     :as a :refer [r]]
              [x.app-elements.api :as elements]
              [x.app-layouts.api  :as layouts]
              [x.app-locales.api  :as locales]
              [app-plugins.item-editor.api :as item-editor]

              ; TEMP
              [app-extensions.storage.api :as storage]))



;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [header-id header-props]
  [item-editor/header :clients :client header-props])



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- client-vat-no-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [editor-disabled?]}]
  [elements/text-field ::vat-no-field
                       {:label :vat-no :min-width :s
                        :value-path [:clients :item-editor/data-items :vat-no]
                        :disabled?  editor-disabled?}])

(defn- client-country-select
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [editor-disabled? selected-language]}]
  [elements/select ::country-select
                   {:label :country ;:user-cancel? false
                    :initial-value   (locales/country-native-name selected-language)
                    :initial-options (param locales/EU-COUNTRY-NAMES)
                    :value-path      [:clients :item-editor/data-items :country]
                    :disabled?       editor-disabled?}])

(defn- client-zip-code-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [editor-disabled?]}]
  [elements/text-field ::zip-code-field
                       {:label :zip-code
                        :value-path [:clients :item-editor/data-items :zip-code]
                        :disabled?  editor-disabled?}])

(defn- client-city-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [editor-disabled?]}]
  [elements/combo-box ::city-field
                      {:label :city :emptiable? false :min-width :s
                       :options-path [:clients :item-editor/meta-items :suggestions :client/city]
                       :value-path   [:clients :item-editor/data-items :city]
                       :disabled?    editor-disabled?}])

(defn- client-address-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [editor-disabled?]}]
  [elements/text-field ::address-field
                       {:label :address
                        :value-path [:clients :item-editor/data-items :address]
                        :disabled?  editor-disabled?}])

(defn- client-secondary-contacts
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [body-id body-props]
  [:<> [:div (layouts/input-row-attributes)
             [:div (layouts/input-block-attributes {:ratio 30})
                   [client-country-select body-id body-props]]
             [:div (layouts/input-block-attributes {:ratio 30})
                   [client-zip-code-field body-id body-props]]
             [:div (layouts/input-block-attributes {:ratio 40})
                   [client-city-field     body-id body-props]]]
       [:div (layouts/input-row-attributes)
             [:div (layouts/input-block-attributes {:ratio 60})
                   [client-address-field body-id body-props]]
             [:div (layouts/input-block-attributes {:ratio 40})
                   [client-vat-no-field body-id body-props]]]])

(defn- client-phone-number-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [editor-disabled?]}]
  [elements/text-field ::phone-number-field
                       {:label :phone-number :required? true :min-width :s
                        :value-path [:clients :item-editor/data-items :phone-number]
                        :validator {:f form/phone-number-valid? :invalid-message :invalid-phone-number}
                        ; Ha egyszerűen le lennének tiltva bizonoyos karakterek, nem lenne egyértelmű a használata!
                        ;:modifier form/valid-phone-number
                        :modifier #(string/starts-with! % "+")
                        :form-id   (item-editor/form-id :clients :client)
                        :disabled? editor-disabled?}])

(defn- client-email-address-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [editor-disabled?]}]
  [elements/text-field ::email-address-field
                       {:label :email-address :required? true :min-width :s
                        :value-path [:clients :item-editor/data-items :email-address]
                        :validator {:f form/email-address-valid? :invalid-message :invalid-email-address}
                        :form-id   (item-editor/form-id :clients :client)
                        :disabled? editor-disabled?}])

(defn- client-primary-contacts
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [body-id body-props]
  [:div (layouts/input-row-attributes)
        [:div (layouts/input-block-attributes)
              [client-email-address-field body-id body-props]]
        [:div (layouts/input-block-attributes)
              [client-phone-number-field  body-id body-props]]])

(defn- client-last-name-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [editor-disabled?]}]
  [elements/text-field ::last-name-field
                       {:label :last-name :required? true :min-width :s
                        :value-path [:clients :item-editor/data-items :last-name]
                        :form-id    (item-editor/form-id :clients :client)
                        :disabled?  editor-disabled?}])

(defn- client-first-name-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [editor-disabled?]}]
  [elements/text-field ::first-name-field
                       {:label :first-name :required? true :min-width :s
                        :value-path [:clients :item-editor/data-items :first-name]
                        :form-id    (item-editor/form-id :clients :client)
                        :disabled?  editor-disabled?}])

(defn- client-name
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [body-id {:keys [name-order] :as body-props}]
  [:div (layouts/input-row-attributes)
        [locales/name-order [:div (layouts/input-block-attributes)
                                  [client-first-name-field body-id body-props]]
                            [:div (layouts/input-block-attributes)
                                  [client-last-name-field  body-id body-props]]
                            (param name-order)]])

(defn- client-additional-information
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ body-props]
  [:div (layouts/input-column-attributes)
        [layouts/input-group-header {:label :description}]
        [:div (layouts/input-block-attributes {:ratio 100})
              [item-editor/description-field :clients :client body-props]]])

(defn- body-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [body-id body-props]
  [:<> ; Color and name
       [item-editor/item-label     :clients :client body-props]
       [item-editor/color-selector :clients :client body-props]
       [elements/horizontal-separator {:size :xxl}]
       ; Basic info
       [layouts/input-group-header {:label :basic-info}]
       [client-name               body-id body-props]
       [client-primary-contacts   body-id body-props]
       [elements/horizontal-separator {:size :xxl}]
       ; More info
       [layouts/input-group-header {:label :more-info}]
       [client-secondary-contacts body-id body-props]
       [elements/horizontal-separator {:size :xxl}]
       ; Description
       [client-additional-information body-id body-props]

       ; TEMP
       [:div {:style {:width "100%"}}
             [storage/media-picker {:label "Borítóképek" :multiple? true}]]])

(defn- body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [extension-id item-namespace]
  (let [body-props (a/subscribe [:client-editor/get-body-props])]
       (fn [] [body-structure extension-id @body-props])))



;; -- View components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id]
  [item-editor/view :clients :client {:form-element #'body}])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :clients/load-client-editor!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:ui/set-surface! ::view {:view {:content #'view}}])
