
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.clients.client-editor.views
    (:require [mid-fruits.form         :as form]
              [mid-fruits.string       :as string]
              [plugins.item-editor.api :as item-editor]
              [x.app-components.api    :as components]
              [x.app-core.api          :as a :refer [r]]
              [x.app-elements.api      :as elements]
              [x.app-layouts.api       :as layouts]
              [x.app-locales.api       :as locales]
              [x.app-ui.api            :as ui]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- more-info-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? :clients.client-editor])]
       [elements/label ::more-info-label
                       {:content             :more-info
                        :disabled?           editor-disabled?
                        :font-size           :m
                        :font-weight         :extra-bold
                        :horizontal-position :left
                        :indent              {:left :xs :top :xxl}}]))

(defn- client-vat-no-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? :clients.client-editor])]
       [elements/text-field ::vat-no-field
                            {:disabled?  editor-disabled?
                             :label      :vat-no
                             :min-width  :s
                             :value-path [:clients :client-editor/edited-item :vat-no]
                             ; TEMP
                             :info-text "Lorem ipsum dolor ..."}]))

(defn- client-country-select
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [editor-disabled?  @(a/subscribe [:item-editor/editor-disabled? :clients.client-editor])]
       [elements/select ::country-select
                        {:disabled?       editor-disabled?
                         :label           :country ;:user-cancel? false
                         :initial-options locales/EU-COUNTRY-NAMES
                         :value-path      [:clients :client-editor/edited-item :country]}]))

(defn- client-zip-code-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? :clients.client-editor])]
       [elements/text-field ::zip-code-field
                            {:disabled?  editor-disabled?
                             :label      :zip-code
                             :value-path [:clients :client-editor/edited-item :zip-code]}]))

(defn- client-city-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? :clients.client-editor])]
       [elements/combo-box ::city-field
                           {:disabled?    editor-disabled?
                            :emptiable?   false
                            :min-width    :s
                            :label        :city
                            :options-path [:clients :client-editor/suggestions :city]
                            :value-path   [:clients :client-editor/edited-item :city]}]))

(defn- client-address-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? :clients.client-editor])]
       [elements/text-field ::address-field
                            {:disabled?  editor-disabled?
                             :min-width  :s
                             :label      :address
                             :value-path [:clients :client-editor/edited-item :address]}]))

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



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- client-phone-number-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? :clients.client-editor])]
       [elements/text-field ::phone-number-field
                            {:disabled?  editor-disabled?
                             :form-id    :clients.client-editor/form
                             :label      :phone-number
                             :min-width  :s
                            ; Ha le lennének tiltva bizonoyos karakterek, nem lenne egyértelmű a mező használata!
                            ;:modifier   form/phone-number
                             :modifier   #(string/starts-with! % "+")
                             :required?  true
                             :validator  {:f form/phone-number? :invalid-message :invalid-phone-number}
                             :value-path [:clients :client-editor/edited-item :phone-number]}]))

(defn- client-email-address-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? :clients.client-editor])]
       [elements/text-field ::email-address-field
                            {:disabled?  editor-disabled?
                             :form-id    :clients.client-editor/form
                             :label      :email-address
                             :min-width  :s
                             :required?  true
                             :validator  {:f form/email-address? :invalid-message :invalid-email-address}
                             :value-path [:clients :client-editor/edited-item :email-address]}]))

(defn- client-primary-contacts
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:div (layouts/input-row-attributes)
        [:div (layouts/input-block-attributes {:ratio 50})
              [client-email-address-field]]
        [:div (layouts/input-block-attributes {:ratio 50})
              [client-phone-number-field]]])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- basic-info-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? :clients.client-editor])]
       [elements/label ::basic-info-label
                       {:content             :basic-info
                        :disabled?           editor-disabled?
                        :font-size           :m
                        :font-weight         :extra-bold
                        :horizontal-position :left
                        :indent              {:left :xs :top :xxl}}]))

(defn- client-last-name-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? :clients.client-editor])]
       [elements/text-field ::last-name-field
                            {:disabled?  editor-disabled?
                             :form-id    :clients.client-editor/form
                             :label      :last-name
                             :min-width  :s
                             :required?  true
                             :value-path [:clients :client-editor/edited-item :last-name]}]))

(defn- client-first-name-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? :clients.client-editor])]
       [elements/text-field ::first-name-field
                            {:disabled?  editor-disabled?
                             :form-id    :clients.client-editor/form
                             :label      :first-name
                             :min-width  :s
                             :required?  true
                             :value-path [:clients :client-editor/edited-item :first-name]}]))

(defn- client-name
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:div (layouts/input-row-attributes)
        [locales/name-order [:div (layouts/input-block-attributes {:ratio 50})
                                  [client-first-name-field]]
                            [:div (layouts/input-block-attributes {:ratio 50})
                                  [client-last-name-field]]
                           @(a/subscribe [:locales/get-name-order])]])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- description-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? :clients.client-editor])]
       [elements/label ::description-label
                       {:content             :description
                        :disabled?           editor-disabled?
                        :font-size           :m
                        :font-weight         :extra-bold
                        :horizontal-position :left
                        :indent              {:left :xs :top :xxl}}]))

(defn- client-description-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? :clients.client-editor])]
       [elements/multiline-field ::client-description-field
                                 {:disabled?  editor-disabled?
                                  :value-path [:clients :client-editor/edited-item :description]}]))

(defn- client-additional-information
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:div (layouts/input-row-attributes)
        [:div (layouts/input-block-attributes {:ratio 100})
              [client-description-field]]])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- client-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [client-name @(a/subscribe [:clients.client-editor/get-client-name])]
       [:<> [ui/title-sensor {:title client-name :offset -48}]
            [item-editor/item-label :clients.client-editor {:name client-name}]]))

(defn- client-colors
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? :clients.client-editor])]
       [elements/color-selector ::client-colors
                                {:disabled?  editor-disabled?
                                 :value-path [:clients :client-editor/edited-item :colors]}]))

(defn- client-modified-at-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [modified-at @(a/subscribe [:item-editor/get-current-item-modified-at :clients.client-editor])]
       [elements/label ::client-modified-at-label
                       {:color     :muted
                        :content   modified-at
                        :font-size :xxs}]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- client-form
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _]
  [:<> ; Color and name
       [client-colors]
       [client-label]
       [client-modified-at-label]
       ; Basic info
       [basic-info-label]
       [client-name]
       [client-primary-contacts]
       ; More info
       [more-info-label]
       [client-secondary-contacts]
       ; Description
       [description-label]
       [client-additional-information]])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [item-editor/header :clients.client-editor
                      {:menu-items [{:label "Adatok" :view-id :edit
                                     :change-keys [:address :city :colors :country :description :email-address
                                                   :first-name :last-name :phone-number :vat-no :zip-code]}
                                    {:label "Árajánlatok" :view-id :price-quotes}]}])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn footer
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [item-editor/footer :clients.client-editor
                      {}])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [selected-language @(a/subscribe [:locales/get-selected-language])]
       [:<> [elements/horizontal-separator {:size :xxl}]
            [item-editor/body :clients.client-editor
                              {:auto-title?      true
                               :default-view-id  :edit
                               :form-element     #'client-form
                               :form-id          :clients.client-editor/form
                               :initial-item     {:country (locales/country-native-name selected-language)}
                               :item-actions     [:delete :duplicate :revert :save]
                               :item-path        [:clients :client-editor/edited-item]
                               :label-key        :name
                               :suggestion-keys  [:city]
                               :suggestions-path [:clients :client-editor/suggestions]}]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id]
  [layouts/layout-a surface-id
                    {:body   #'body
                     :footer #'footer
                     :header #'header}])
