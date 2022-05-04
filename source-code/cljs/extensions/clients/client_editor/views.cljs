
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

(defn- client-name-label
  []
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? :clients.client-editor])
        client-name      @(a/subscribe [:clients.client-editor/get-client-name])]
       [:<> ;[ui/title-sensor {:title client-name :offset -18}]
            [elements/label ::client-name-label
                            {:content     client-name
                             :disabled?   editor-disabled?
                             :font-size   :l
                             :font-weight :extra-bold
                             :placeholder "Névtelen ügyfél"}]]))

(defn- client-color-selector
  []
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? :clients.client-editor])]
       [elements/color-selector ::client-color-selector
                                {:disabled?  editor-disabled?
                                 :size       :l
                                 :value-path [:clients :client-editor/edited-item :colors]}]))

(defn- client-modified-at-label
  []
  (let [client-modified-at @(a/subscribe [:item-editor/get-current-item-modified-at :clients.client-editor])]
       [elements/label ::client-modified-at-label
                       {:color     :muted
                        :content   {:content :last-modified-at-n :replacements [client-modified-at]}
                        :font-size :xxs}]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- client-basic-info-label
  []
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? :clients.client-editor])]
       [elements/label ::client-basic-info-label
                       {:content             :basic-info
                        :disabled?           editor-disabled?
                        :font-size           :m
                        :font-weight         :extra-bold
                        :horizontal-position :left
                        :indent              {:left :xs :top :xxl}}]))

(defn- client-last-name-field
  []
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? :clients.client-editor])]
       [elements/text-field ::client-last-name-field
                            {:disabled?  editor-disabled?
                             :form-id    :clients.client-editor/form
                             :indent     {:horizontal :xxs :vertical :xs}
                             :label      :last-name
                             :min-width  :xs
                             :required?  true
                             :value-path [:clients :client-editor/edited-item :last-name]}]))

(defn- client-first-name-field
  []
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? :clients.client-editor])]
       [elements/text-field ::client-first-name-field
                            {:disabled?  editor-disabled?
                             :form-id    :clients.client-editor/form
                             :indent     {:horizontal :xxs :vertical :xs}
                             :label      :first-name
                             :min-width  :xs
                             :required?  true
                             :value-path [:clients :client-editor/edited-item :first-name]}]))

(defn- client-name-fields
  []
  [:div (layouts/form-row-attributes)
        [locales/name-order [:div (layouts/form-block-attributes {:ratio 50})
                                  [client-first-name-field]]
                            [:div (layouts/form-block-attributes {:ratio 50})
                                  [client-last-name-field]]
                           @(a/subscribe [:locales/get-name-order])]])

(defn- client-phone-number-field
  []
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? :clients.client-editor])]
       [elements/text-field ::client-phone-number-field
                            {:disabled?  editor-disabled?
                             :form-id    :clients.client-editor/form
                             :indent     {:horizontal :xxs :vertical :xs}
                             :label      :phone-number
                             :min-width  :xs
                             ; Ha le lennének tiltva bizonoyos karakterek, nem lenne egyértelmű a mező használata!
                             ;:modifier   form/phone-number
                             :modifier   #(string/starts-with! % "+")
                             :required?  true
                             :validator  {:f form/phone-number? :invalid-message :invalid-phone-number}
                             :value-path [:clients :client-editor/edited-item :phone-number]}]))

(defn- client-email-address-field
  []
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? :clients.client-editor])]
       [elements/text-field ::client-email-address-field
                            {:disabled?  editor-disabled?
                             :form-id    :clients.client-editor/form
                             :indent     {:horizontal :xxs :vertical :xs}
                             :label      :email-address
                             :min-width  :xs
                             :required?  true
                             :validator  {:f form/email-address? :invalid-message :invalid-email-address}
                             :value-path [:clients :client-editor/edited-item :email-address]}]))

(defn- client-primary-contacts
  []
  [:div (layouts/form-row-attributes)
        [:div (layouts/form-block-attributes {:ratio 50})
              [client-email-address-field]]
        [:div (layouts/form-block-attributes {:ratio 50})
              [client-phone-number-field]]])

(defn- client-basic-info
  []
  [:<> [client-basic-info-label]
       [client-name-fields]
       [client-primary-contacts]])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- client-more-info-label
  []
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? :clients.client-editor])]
       [elements/label ::client-more-info-label
                       {:content             :more-info
                        :disabled?           editor-disabled?
                        :font-size           :m
                        :font-weight         :extra-bold
                        :horizontal-position :left
                        :indent              {:left :xs :top :xxl}}]))

(defn- client-vat-no-field
  []
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? :clients.client-editor])]
       [elements/text-field ::client-vat-no-field
                            {:disabled?  editor-disabled?
                             :indent     {:horizontal :xxs :vertical :xs}
                             :label      :vat-no
                             :min-width  :xs
                             :value-path [:clients :client-editor/edited-item :vat-no]
                             ; TEMP
                             :info-text "Lorem ipsum dolor ..."}]))

(defn- client-country-select
  []
  (let [editor-disabled?  @(a/subscribe [:item-editor/editor-disabled? :clients.client-editor])]
       [elements/select ::client-country-select
                        {:disabled?       editor-disabled?
                         :indent          {:horizontal :xxs :vertical :xs}
                         :label           :country ;:user-cancel? false
                         :initial-options locales/EU-COUNTRY-NAMES
                         :min-width       :xs
                         :value-path      [:clients :client-editor/edited-item :country]}]))

(defn- client-zip-code-field
  []
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? :clients.client-editor])]
       [elements/text-field ::client-zip-code-field
                            {:disabled?  editor-disabled?
                             :indent     {:horizontal :xxs :vertical :xs}
                             :min-width  :xs
                             :label      :zip-code
                             :value-path [:clients :client-editor/edited-item :zip-code]}]))

(defn- client-city-field
  []
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? :clients.client-editor])]
       [elements/combo-box ::client-city-field
                           {:disabled?    editor-disabled?
                            :emptiable?   false
                            :indent       {:horizontal :xxs :vertical :xs}
                            :min-width    :xs
                            :label        :city
                            :options-path [:clients :client-editor/suggestions :city]
                            :value-path   [:clients :client-editor/edited-item :city]}]))

(defn- client-address-field
  []
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? :clients.client-editor])]
       [elements/text-field ::client-address-field
                            {:disabled?  editor-disabled?
                             :indent     {:horizontal :xxs :vertical :xs}
                             :min-width  :xs
                             :label      :address
                             :value-path [:clients :client-editor/edited-item :address]}]))

(defn- client-secondary-contacts
  []
  [:<> [:div (layouts/form-row-attributes)
             [:div (layouts/form-block-attributes {:ratio 30})
                   [client-country-select]]
             [:div (layouts/form-block-attributes {:ratio 30})
                   [client-zip-code-field]]
             [:div (layouts/form-block-attributes {:ratio 40})
                   [client-city-field]]]
       [:div (layouts/form-row-attributes)
             [:div (layouts/form-block-attributes {:ratio 60})
                   [client-address-field]]
             [:div (layouts/form-block-attributes {:ratio 40})
                   [client-vat-no-field]]]])

(defn- client-more-info
  []
  [:<> [client-more-info-label]
       [client-secondary-contacts]])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- client-description-label
  []
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? :clients.client-editor])]
       [elements/label ::client-description-label
                       {:content             :description
                        :disabled?           editor-disabled?
                        :font-size           :m
                        :font-weight         :extra-bold
                        :horizontal-position :left
                        :indent              {:left :xs :top :xxl}}]))

(defn- client-description-field
  []
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? :clients.client-editor])]
       [elements/multiline-field ::client-description-field
                                 {:disabled?  editor-disabled?
                                  :indent     {:horizontal :xxs :vertical :xs}
                                  :min-width  :xs
                                  :value-path [:clients :client-editor/edited-item :description]}]))

(defn- client-additional-info
  []
  [:<> [client-description-label]
       [:div (layouts/form-row-attributes)
             [:div (layouts/form-block-attributes {:ratio 100})
                   [client-description-field]]]])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- client-form
  [_ _]
  [:<> [client-color-selector]
       [client-name-label]
       [client-modified-at-label]
       [client-basic-info]
       [client-more-info]
       [client-additional-info]])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn footer
  []
  [item-editor/footer :clients.client-editor
                      {}])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body
  []
  (let [selected-language @(a/subscribe [:locales/get-selected-language])]
       [:<> [elements/horizontal-separator {:size :xxl}]
            [item-editor/body :clients.client-editor
                              {:auto-title?      true
                               :default-view-id  :edit
                               :form-element     #'client-form
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
  [surface-id]
  [layouts/layout-a ::view
                    {:body   #'body
                     :footer #'footer}])
