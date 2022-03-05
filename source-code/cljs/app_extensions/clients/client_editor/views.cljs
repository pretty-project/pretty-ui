
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-extensions.clients.client-editor.views
    (:require [app-plugins.item-editor.api :as item-editor]
              [mid-fruits.candy            :refer [param]]
              [mid-fruits.form             :as form]
              [mid-fruits.string           :as string]
              [x.app-components.api        :as components]
              [x.app-core.api              :as a :refer [r]]
              [x.app-elements.api          :as elements]
              [x.app-layouts.api           :as layouts]
              [x.app-locales.api           :as locales]))



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- client-vat-no-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? :clients :client])]
       [elements/text-field ::vat-no-field
                            {:label :vat-no :min-width :s
                             :value-path [:clients :item-editor/data-items :vat-no]
                             :disabled?  editor-disabled?}]))

(defn- client-country-select
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [editor-disabled?  @(a/subscribe [:item-editor/editor-disabled? :clients :client])
        selected-language @(a/subscribe [:locales/get-selected-language])]
       [elements/select ::country-select
                        {:label :country ;:user-cancel? false
                         :initial-value   (locales/country-native-name selected-language)
                         :initial-options (param locales/EU-COUNTRY-NAMES)
                         :value-path      [:clients :item-editor/data-items :country]
                         :disabled?       editor-disabled?}]))

(defn- client-zip-code-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? :clients :client])]
       [elements/text-field ::zip-code-field
                            {:label :zip-code
                             :value-path [:clients :item-editor/data-items :zip-code]
                             :disabled?  editor-disabled?}]))

(defn- client-city-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? :clients :client])]
       [elements/combo-box ::city-field
                           {:label :city :emptiable? false :min-width :s
                            :options-path [:clients :item-editor/meta-items :suggestions :client/city]
                            :value-path   [:clients :item-editor/data-items :city]
                            :disabled?    editor-disabled?}]))

(defn- client-address-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? :clients :client])]
       [elements/text-field ::address-field
                            {:label :address
                             :value-path [:clients :item-editor/data-items :address]
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
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? :clients :client])]
       [elements/text-field ::phone-number-field
                            {:label :phone-number :required? true :min-width :s
                             :value-path [:clients :item-editor/data-items :phone-number]
                             :validator {:f form/phone-number? :invalid-message :invalid-phone-number}
                             ; Ha egyszerűen le lennének tiltva bizonoyos karakterek, nem lenne egyértelmű a használata!
                             ;:modifier form/phone-number
                             :modifier #(string/starts-with! % "+")
                             :form-id   (item-editor/form-id :clients :client)
                             :disabled? editor-disabled?}]))

(defn- client-email-address-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? :clients :client])]
       [elements/text-field ::email-address-field
                            {:label :email-address :required? true :min-width :s
                             :value-path [:clients :item-editor/data-items :email-address]
                             :validator {:f form/email-address? :invalid-message :invalid-email-address}
                             :form-id   (item-editor/form-id :clients :client)
                             :disabled? editor-disabled?}]))

(defn- client-primary-contacts
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:div (layouts/input-row-attributes)
        [:div (layouts/input-block-attributes)
              [client-email-address-field]]
        [:div (layouts/input-block-attributes)
              [client-phone-number-field]]])

(defn- client-last-name-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? :clients :client])]
       [elements/text-field ::last-name-field
                            {:label :last-name :required? true :min-width :s
                             :value-path [:clients :item-editor/data-items :last-name]
                             :form-id    (item-editor/form-id :clients :client)
                             :disabled?  editor-disabled?}]))

(defn- client-first-name-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? :clients :client])]
       [elements/text-field ::first-name-field
                            {:label :first-name :required? true :min-width :s
                             :value-path [:clients :item-editor/data-items :first-name]
                             :form-id    (item-editor/form-id :clients :client)
                             :disabled?  editor-disabled?}]))

(defn- client-name
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:div (layouts/input-row-attributes)
        [locales/name-order [:div (layouts/input-block-attributes)
                                  [client-first-name-field]]
                            [:div (layouts/input-block-attributes)
                                  [client-last-name-field]]
                           @(a/subscribe [:locales/get-name-order])]])

(defn- client-additional-information
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  [:div (layouts/input-column-attributes)
        [item-editor/input-group-header :clients :client {:label :description}]
        [:div (layouts/input-block-attributes {:ratio 100})
              [item-editor/description-field :clients :client]]])

(defn- client-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [client-name @(a/subscribe [:clients.client-editor/get-client-name])]
       [item-editor/item-label :clients :client {:name client-name}]))

(defn- client-colors
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [client-colors @(a/subscribe [:item-editor/get-data-value :clients :client :colors])]
       [item-editor/color-selector :clients :client {:colors client-colors}]))

(defn- client-form
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _]
  [:<> ; Color and name
       [client-label]
       [client-colors]
       [elements/horizontal-separator {:size :xxl}]
       ; Basic info
       [item-editor/input-group-header :clients :client {:label :basic-info}]
       [client-name]
       [client-primary-contacts]
       [elements/horizontal-separator {:size :xxl}]
       ; More info
       [item-editor/input-group-header :clients :client {:label :more-info}]
       [client-secondary-contacts]
       [elements/horizontal-separator {:size :xxl}]
       ; Description
       [client-additional-information]])



;; -- View components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id]
  (let [description @(a/subscribe [:item-editor/get-description :clients :client])
        item-id     @(a/subscribe [:router/get-current-route-path-param :client-id])
        new-client? @(a/subscribe [:router/current-route-path-param?    :client-id "new-client"])]
       [layouts/layout-a surface-id {:description description
                                     :header [item-editor/header :clients :client {:item-actions [:delete :duplicate :save]}]
                                     :body   [item-editor/body   :clients :client {:form-element #'client-form
                                                                                   :auto-title? true :suggestion-keys [:city]
                                                                                   :item-id item-id :new-item? new-client?
                                                                                   :parent-route "/@app-home/clients"}]}]))
