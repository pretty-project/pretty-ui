
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.clients.client-editor.views
    (:require [mid-fruits.candy        :refer [param]]
              [mid-fruits.form         :as form]
              [mid-fruits.string       :as string]
              [plugins.item-editor.api :as item-editor]
              [x.app-components.api    :as components]
              [x.app-core.api          :as a :refer [r]]
              [x.app-elements.api      :as elements]
              [x.app-layouts.api       :as layouts]
              [x.app-locales.api       :as locales]))



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- client-vat-no-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? :clients.client-editor])]
       [elements/text-field ::vat-no-field
                            {:label :vat-no :min-width :s
                             :value-path [:clients :client-editor/edited-item :vat-no]
                             :disabled?  editor-disabled?}]))

(defn- client-country-select
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [editor-disabled?  @(a/subscribe [:item-editor/editor-disabled? :clients.client-editor])]
       [elements/select ::country-select
                        {:label :country ;:user-cancel? false
                         :initial-options (param locales/EU-COUNTRY-NAMES)
                         :value-path      [:clients :client-editor/edited-item :country]
                         :disabled?       editor-disabled?}]))

(defn- client-zip-code-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? :clients.client-editor])]
       [elements/text-field ::zip-code-field
                            {:label :zip-code
                             :value-path [:clients :client-editor/edited-item :zip-code]
                             :disabled?  editor-disabled?}]))

(defn- client-city-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? :clients.client-editor])]
       [elements/combo-box ::city-field
                           {:label :city :emptiable? false :min-width :s
                            :options-path [:clients :client-editor/suggestions :city]
                            :value-path   [:clients :client-editor/edited-item :city]
                            :disabled?    editor-disabled?}]))

(defn- client-address-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? :clients.client-editor])]
       [elements/text-field ::address-field
                            {:label :address
                             :value-path [:clients :client-editor/edited-item :address]
                             :disabled?  editor-disabled?}]))

(defn- client-secondary-contacts
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:<> [:div (layouts/input-row-attributes)
             [:div (layouts/input-block-attributes {:ratio 30})
                   [client-country-select]]
             [:div (layouts/input-block-attributes {:ratio 30})
                   [client-zip-code-field]]
             [:div (layouts/input-block-attributes {:ratio 40})
                   [client-city-field]]]
       [:div (layouts/input-row-attributes)
             [:div (layouts/input-block-attributes {:ratio 60})
                   [client-address-field]]
             [:div (layouts/input-block-attributes {:ratio 40})
                   [client-vat-no-field]]]])

(defn- client-phone-number-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? :clients.client-editor])]
       [elements/text-field ::phone-number-field
                            {:label :phone-number :required? true :min-width :s
                             :value-path [:clients :client-editor/edited-item :phone-number]
                             :validator {:f form/phone-number? :invalid-message :invalid-phone-number}
                             ; Ha egyszerűen le lennének tiltva bizonoyos karakterek, nem lenne egyértelmű a használata!
                             ;:modifier form/phone-number
                             :modifier #(string/starts-with! % "+")
                             :form-id   :clients.client-editor/form
                             :disabled? editor-disabled?}]))

(defn- client-email-address-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? :clients.client-editor])]
       [elements/text-field ::email-address-field
                            {:label :email-address :required? true :min-width :s
                             :value-path [:clients :client-editor/edited-item :email-address]
                             :validator {:f form/email-address? :invalid-message :invalid-email-address}
                             :form-id   :clients.client-editor/form
                             :disabled? editor-disabled?}]))

(defn- client-primary-contacts
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:div (layouts/input-row-attributes)
        [:div (layouts/input-block-attributes {:ratio 50})
              [client-email-address-field]]
        [:div (layouts/input-block-attributes {:ratio 50})
              [client-phone-number-field]]])

(defn- client-last-name-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? :clients.client-editor])]
       [elements/text-field ::last-name-field
                            {:label :last-name :required? true :min-width :s
                             :value-path [:clients :client-editor/edited-item :last-name]
                             :form-id    :clients.client-editor/form
                             :disabled?  editor-disabled?}]))

(defn- client-first-name-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? :clients.client-editor])]
       [elements/text-field ::first-name-field
                            {:label :first-name :required? true :min-width :s
                             :value-path [:clients :client-editor/edited-item :first-name]
                             :form-id    :clients.client-editor/form
                             :disabled?  editor-disabled?}]))

(defn- client-name
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:div (layouts/input-row-attributes)
        [locales/name-order [:div (layouts/input-block-attributes {:ratio 50})
                                  [client-first-name-field]]
                            [:div (layouts/input-block-attributes {:ratio 50})
                                  [client-last-name-field]]
                           @(a/subscribe [:locales/get-name-order])]])

(defn- client-additional-information
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  [:div (layouts/input-row-attributes)
        [:div (layouts/input-block-attributes {:ratio 100})
              [item-editor/description-field :clients.client-editor]]])

(defn- client-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [client-name @(a/subscribe [:clients.client-editor/get-client-name])]
       [item-editor/item-label :clients.client-editor {:name client-name}]))

(defn- client-colors
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [item-editor/color-selector :clients.client-editor])

(defn- client-form
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _]
  [:<> ; Color and name
       [client-label]
       [client-colors]
       [elements/horizontal-separator {:size :xxl}]
       ; Basic info
       [item-editor/input-group-header :clients.client-editor {:label :basic-info}]
       [client-name]
       [client-primary-contacts]
       [elements/horizontal-separator {:size :xxl}]
       ; More info
       [item-editor/input-group-header :clients.client-editor {:label :more-info}]
       [client-secondary-contacts]
       [elements/horizontal-separator {:size :xxl}]
       ; Description
       [item-editor/input-group-header :clients.client-editor {:label :description}]
       [client-additional-information]])



;; -- View components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [item-editor/header :clients.client-editor
                      {}])

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [selected-language @(a/subscribe [:locales/get-selected-language])]
       [item-editor/body :clients.client-editor
                         {:auto-title?      true
                          :form-element     #'client-form
                          :form-id          :clients.client-editor/form
                          :new-item-id      "new-client"
                          :initial-item     {:country (locales/country-native-name selected-language)}
                          :item-actions     [:delete :duplicate :save]
                          :item-path        [:clients :client-editor/edited-item]
                          :suggestion-keys  [:city]
                          :suggestions-path [:clients :client-editor/suggestions]}]))

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id]
  (let [description @(a/subscribe [:item-editor/get-description :clients.client-editor])]
       [layouts/layout-a surface-id {:description description
                                     ;:header      #'header
                                     :body        #'body
                                     :footer      #'header}]))
