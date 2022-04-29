
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

(defn- name-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? :clients.client-editor])
        client-name      @(a/subscribe [:clients.client-editor/get-client-name])]
       [:<> [ui/title-sensor {:title client-name :offset -18}]
            [elements/label ::name-label
                            {:content     client-name
                             :disabled?   editor-disabled?
                             :font-size   :l
                             :font-weight :extra-bold
                             :placeholder "Névtelen ügyfél"}]]))

(defn- color-selector
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? :clients.client-editor])]
       [elements/color-selector ::color-selector
                                {:disabled?  editor-disabled?
                                 :value-path [:clients :client-editor/edited-item :colors]}]))

(defn- modified-at-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [modified-at @(a/subscribe [:item-editor/get-current-item-modified-at :clients.client-editor])]
       [elements/label ::modified-at-label
                       {:color     :muted
                        :content   modified-at
                        :font-size :xxs}]))



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

(defn- last-name-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? :clients.client-editor])]
       [elements/text-field ::last-name-field
                            {:disabled?  editor-disabled?
                             :form-id    :clients.client-editor/form
                             :indent     {:horizontal :xxs :vertical :xs}
                             :label      :last-name
                             :min-width  :s
                             :required?  true
                             :value-path [:clients :client-editor/edited-item :last-name]}]))

(defn- first-name-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? :clients.client-editor])]
       [elements/text-field ::first-name-field
                            {:disabled?  editor-disabled?
                             :form-id    :clients.client-editor/form
                             :indent     {:horizontal :xxs :vertical :xs}
                             :label      :first-name
                             :min-width  :s
                             :required?  true
                             :value-path [:clients :client-editor/edited-item :first-name]}]))

(defn- name-fields
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:div (layouts/form-row-attributes)
        [locales/name-order [:div (layouts/form-block-attributes {:ratio 50})
                                  [first-name-field]]
                            [:div (layouts/form-block-attributes {:ratio 50})
                                  [last-name-field]]
                           @(a/subscribe [:locales/get-name-order])]])

(defn- phone-number-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? :clients.client-editor])]
       [elements/text-field ::phone-number-field
                            {:disabled?  editor-disabled?
                             :form-id    :clients.client-editor/form
                             :indent     {:horizontal :xxs :vertical :xs}
                             :label      :phone-number
                             :min-width  :s
                             ; Ha le lennének tiltva bizonoyos karakterek, nem lenne egyértelmű a mező használata!
                             ;:modifier   form/phone-number
                             :modifier   #(string/starts-with! % "+")
                             :required?  true
                             :validator  {:f form/phone-number? :invalid-message :invalid-phone-number}
                             :value-path [:clients :client-editor/edited-item :phone-number]}]))

(defn- email-address-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? :clients.client-editor])]
       [elements/text-field ::email-address-field
                            {:disabled?  editor-disabled?
                             :form-id    :clients.client-editor/form
                             :indent     {:horizontal :xxs :vertical :xs}
                             :label      :email-address
                             :min-width  :s
                             :required?  true
                             :validator  {:f form/email-address? :invalid-message :invalid-email-address}
                             :value-path [:clients :client-editor/edited-item :email-address]}]))

(defn- primary-contacts
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:div (layouts/form-row-attributes)
        [:div (layouts/form-block-attributes {:ratio 50})
              [email-address-field]]
        [:div (layouts/form-block-attributes {:ratio 50})
              [phone-number-field]]])

(defn- basic-info
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:<> [basic-info-label]
       [name-fields]
       [primary-contacts]])



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

(defn- vat-no-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? :clients.client-editor])]
       [elements/text-field ::vat-no-field
                            {:disabled?  editor-disabled?
                             :indent     {:horizontal :xxs :vertical :xs}
                             :label      :vat-no
                             :min-width  :s
                             :value-path [:clients :client-editor/edited-item :vat-no]
                             ; TEMP
                             :info-text "Lorem ipsum dolor ..."}]))

(defn- country-select
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [editor-disabled?  @(a/subscribe [:item-editor/editor-disabled? :clients.client-editor])]
       [elements/select ::country-select
                        {:disabled?       editor-disabled?
                         :indent          {:horizontal :xxs :vertical :xs}
                         :label           :country ;:user-cancel? false
                         :initial-options locales/EU-COUNTRY-NAMES
                        ;:min-width       :s
                         :value-path      [:clients :client-editor/edited-item :country]}]))

(defn- zip-code-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? :clients.client-editor])]
       [elements/text-field ::zip-code-field
                            {:disabled?  editor-disabled?
                             :indent     {:horizontal :xxs :vertical :xs}
                             :min-width  :s
                             :label      :zip-code
                             :value-path [:clients :client-editor/edited-item :zip-code]}]))

(defn- city-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? :clients.client-editor])]
       [elements/combo-box ::city-field
                           {:disabled?    editor-disabled?
                            :emptiable?   false
                            :indent       {:horizontal :xxs :vertical :xs}
                            :min-width    :s
                            :label        :city
                            :options-path [:clients :client-editor/suggestions :city]
                            :value-path   [:clients :client-editor/edited-item :city]}]))

(defn- address-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? :clients.client-editor])]
       [elements/text-field ::address-field
                            {:disabled?  editor-disabled?
                             :indent     {:horizontal :xxs :vertical :xs}
                             :min-width  :s
                             :label      :address
                             :value-path [:clients :client-editor/edited-item :address]}]))

(defn- secondary-contacts
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:<> [:div (layouts/form-row-attributes)
             [:div (layouts/form-block-attributes {:ratio 30})
                   [country-select]]
             [:div (layouts/form-block-attributes {:ratio 30})
                   [zip-code-field]]
             [:div (layouts/form-block-attributes {:ratio 40})
                   [city-field]]]
       [:div (layouts/form-row-attributes)
             [:div (layouts/form-block-attributes {:ratio 60})
                   [address-field]]
             [:div (layouts/form-block-attributes {:ratio 40})
                   [vat-no-field]]]])

(defn- more-info
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:<> [more-info-label]
       [secondary-contacts]])



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

(defn- description-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? :clients.client-editor])]
       [elements/multiline-field ::description-field
                                 {:disabled?  editor-disabled?
                                  :indent     {:horizontal :xxs :vertical :xs}
                                  :min-width  :s
                                  :value-path [:clients :client-editor/edited-item :description]}]))

(defn- additional-info
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:<> [description-label]
       [:div (layouts/form-row-attributes)
             [:div (layouts/form-block-attributes {:ratio 100})
                   [description-field]]]])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- form
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _]
  [:<> [color-selector]
       [name-label]
       [modified-at-label]
       [basic-info]
       [more-info]
       [additional-info]])



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
                               :form-element     #'form
                               :form-id          :clients.client-editor/form
                               :initial-item     {:country (locales/country-native-name selected-language)}
                               :item-path        [:clients :client-editor/edited-item]
                               :label-key        :name
                               :suggestion-keys  [:city]
                               :suggestions-path [:clients :client-editor/suggestions]}]
            [elements/horizontal-separator {:size :xxl}]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id]
  [layouts/layout-a ::view
                    {:body   #'body
                     :footer #'footer}])
