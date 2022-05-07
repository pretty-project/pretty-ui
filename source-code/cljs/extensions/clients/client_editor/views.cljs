
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.clients.client-editor.views
    (:require [layouts.surface-a.api   :as surface-a]
              [mid-fruits.form         :as form]
              [mid-fruits.string       :as string]
              [plugins.item-editor.api :as item-editor]
              [x.app-components.api    :as components]
              [x.app-core.api          :as a :refer [r]]
              [x.app-elements.api      :as elements]
              [x.app-locales.api       :as locales]
              [x.app-ui.api            :as ui]

              ; TEMP
              [x.app-layouts.api       :as layouts]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn client-name-label
  []
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? :clients.client-editor])
        client-name      @(a/subscribe [:clients.client-editor/get-client-name])]
       [:<> [surface-a/title-sensor {:title client-name :offset 0}]
            [elements/label ::client-name-label
                            {:content     client-name
                             :disabled?   editor-disabled?
                             :font-size   :xxl
                             :font-weight :extra-bold
                             :indent      {:right :s}
                             :placeholder "Névtelen ügyfél"}]]))

(defn client-color-marker
  []
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? :clients.client-editor])
        client-colors    @(a/subscribe [:db/get-item [:clients :client-editor/edited-item :colors]])]
       [elements/color-marker ::client-color-marker
                              {:colors    client-colors
                               :disabled? editor-disabled?
                               :size      :l}]))

(defn client-modified-at-label
  []
  (let [editor-disabled?   @(a/subscribe [:item-editor/editor-disabled? :clients.client-editor])
        client-modified-at @(a/subscribe [:item-editor/get-current-item-modified-at :clients.client-editor])]
       [elements/label ::client-modified-at-label
                       {:color     :muted
                        :content   {:content :last-modified-at-n :replacements [client-modified-at]}
                        :disabled? editor-disabled?
                        :font-size :xxs}]))

(defn client-header
  []
  [:<> [elements/row {:content [:<> [client-name-label]
                                    [client-color-marker]]}]
       [client-modified-at-label]])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn client-last-name-field
  []
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? :clients.client-editor])]
       [elements/text-field ::client-last-name-field
                            {:disabled?  editor-disabled?
                             :form-id    :clients.client-editor/form
                             :indent     {:top :xxl :vertical :xxs}
                             :label      :last-name
                             :min-width  :xs
                             :required?  true
                             :value-path [:clients :client-editor/edited-item :last-name]}]))

(defn client-first-name-field
  []
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? :clients.client-editor])]
       [elements/text-field ::client-first-name-field
                            {:disabled?  editor-disabled?
                             :form-id    :clients.client-editor/form
                             :indent     {:top :xxl :vertical :xxs}
                             :label      :first-name
                             :min-width  :xs
                             :required?  true
                             :value-path [:clients :client-editor/edited-item :first-name]}]))

(defn client-name-fields
  []
  [:div (layouts/form-row-attributes)
        [locales/name-order [:div (layouts/form-block-attributes {:ratio 50})
                                  [client-first-name-field]]
                            [:div (layouts/form-block-attributes {:ratio 50})
                                  [client-last-name-field]]
                           @(a/subscribe [:locales/get-name-order])]])

(defn client-phone-number-field
  []
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? :clients.client-editor])]
       [elements/text-field ::client-phone-number-field
                            {:disabled?  editor-disabled?
                             :form-id    :clients.client-editor/form
                             :indent     {:top :m :vertical :xxs}
                             :label      :phone-number
                             :min-width  :xs
                             ; Ha le lennének tiltva bizonoyos karakterek, nem lenne egyértelmű a mező használata!
                             ;:modifier   form/phone-number
                             :modifier   #(string/starts-with! % "+")
                             :required?  true
                             :validator  {:f form/phone-number? :invalid-message :invalid-phone-number}
                             :value-path [:clients :client-editor/edited-item :phone-number]}]))

(defn client-email-address-field
  []
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? :clients.client-editor])]
       [elements/text-field ::client-email-address-field
                            {:disabled?  editor-disabled?
                             :form-id    :clients.client-editor/form
                             :indent     {:top :m :vertical :xxs}
                             :label      :email-address
                             :min-width  :xs
                             :required?  true
                             :validator  {:f form/email-address? :invalid-message :invalid-email-address}
                             :value-path [:clients :client-editor/edited-item :email-address]}]))

(defn client-primary-contacts
  []
  [:div (layouts/form-row-attributes)
        [:div (layouts/form-block-attributes {:ratio 50})
              [client-email-address-field]]
        [:div (layouts/form-block-attributes {:ratio 50})
              [client-phone-number-field]]])

(defn client-basic-info
  []
  [:<> [client-name-fields]
       [client-primary-contacts]])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn client-vat-no-field
  []
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? :clients.client-editor])]
       [elements/text-field ::client-vat-no-field
                            {:disabled?  editor-disabled?
                             :indent     {:top :m :vertical :xxs}
                             :label      :vat-no
                             :min-width  :xs
                             :value-path [:clients :client-editor/edited-item :vat-no]}]))

(defn client-country-select
  []
  (let [editor-disabled?  @(a/subscribe [:item-editor/editor-disabled? :clients.client-editor])]
       [elements/select ::client-country-select
                        {:disabled?       editor-disabled?
                         :indent          {:top :xxl :vertical :xxs}
                         :label           :country ;:user-cancel? false
                         :initial-options locales/EU-COUNTRY-NAMES
                         :min-width       :xs
                         :value-path      [:clients :client-editor/edited-item :country]}]))

(defn client-zip-code-field
  []
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? :clients.client-editor])]
       [elements/text-field ::client-zip-code-field
                            {:disabled?  editor-disabled?
                             :indent     {:top :xxl :vertical :xxs}
                             :min-width  :xs
                             :label      :zip-code
                             :value-path [:clients :client-editor/edited-item :zip-code]}]))

(defn client-city-field
  []
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? :clients.client-editor])]
       [elements/combo-box ::client-city-field
                           {:disabled?    editor-disabled?
                            :emptiable?   false
                            :indent       {:top :xxl :vertical :xxs}
                            :min-width    :xs
                            :label        :city
                            :options-path [:clients :client-editor/suggestions :city]
                            :value-path   [:clients :client-editor/edited-item :city]}]))

(defn client-address-field
  []
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? :clients.client-editor])]
       [elements/text-field ::client-address-field
                            {:disabled?  editor-disabled?
                             :indent     {:top :m :vertical :xxs}
                             :min-width  :xs
                             :label      :address
                             :value-path [:clients :client-editor/edited-item :address]}]))

(defn client-secondary-contacts
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



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn client-description-field
  []
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? :clients.client-editor])]
       [elements/multiline-field ::client-description-field
                                 {:disabled?  editor-disabled?
                                  :indent     {:top :xxl :vertical :xxs}
                                  :label      :description
                                  :min-width  :xs
                                  :value-path [:clients :client-editor/edited-item :description]}]))

(defn client-color-button
  []
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? :clients.client-editor])]
       [elements/button ::color-picker-button
                        {:font-size        :xs
                         :horizontal-align :left
                         :icon             :palette
                         :indent           {:horizontal :xxl :vertical :xxs}
                         :label            "Szín kiválasztása"
                         :on-click         [:elements.color-selector/render-selector! ::client-color-selector
                                                                                      {:value-path [:clients :client-editor/edited-item :colors]}]}]))

(defn client-additional-info
  []
  [:<> [:div (layouts/form-row-attributes)
             [:div (layouts/form-block-attributes {:ratio 100})
                   [client-description-field]]]
       [:div (layouts/form-row-attributes)
             [client-color-button]]])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn client-menu-bar
  []
  [:<> [elements/menu-bar ::client-menu-bar
                          {:indent {:top :xxl}
                           :menu-items [{:label "Adatok" :on-click [] :active? true}]}]
                                       ;{:label :price-quotes :on-click []}
       [elements/horizontal-line {:color :highlight}]])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn cancel-button
  []
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? :clients.client-editor])
        client-id        @(a/subscribe [:router/get-current-route-path-param :item-id])]
       [elements/button ::cancel-button
                        {:disabled?   editor-disabled?
                         :font-size   :xs
                         :hover-color :highlight
                         :indent      {:vertical :xxs :horizontal :xxs}
                         :on-click    [:router/go-to! (str "/@app-home/clients/"client-id)]
                         :preset      :cancel}]))

(defn revert-button
  []
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? :clients.client-editor])
        item-changed?    @(a/subscribe [:item-editor/item-changed?    :clients.client-editor])]
       [elements/button ::revert-button
                        {:disabled?   (or editor-disabled? (not item-changed?))
                         :font-size   :xs
                         :hover-color :highlight
                         :indent      {:vertical :xxs :horizontal :xxs}
                         :on-click    [:item-editor/revert-item! :clients.client-editor]
                         :preset      :revert}]))

(defn save-button
  []
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? :clients.client-editor])]
       [elements/button ::save-button
                        {:disabled?   editor-disabled?
                         :font-size   :xs
                         :hover-color :highlight
                         :indent      {:vertical :xxs :horizontal :xxs}
                         :on-click    [:item-editor/save-item! :clients.client-editor]
                         :preset      :save}]))

(defn footer
  []
  [elements/horizontal-polarity ::footer
                                {:style {:background-color "white" :border-top "1px solid #ddd"
                                         :bottom "0" :position "sticky" :width "100%"}
                                 :start-content [:<> [cancel-button]]
                                 :end-content   [:<> [revert-button]
                                                     [save-button]]}])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn client-form
  [_ _]
  [:<> [client-header]
       [client-menu-bar]
       [client-basic-info]
       [client-secondary-contacts]
       [client-additional-info]
       [footer]])

(defn view-structure
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
                               :suggestions-path [:clients :client-editor/suggestions]}]]))

(defn view
  [surface-id]
  [surface-a/layout surface-id
                    {:content #'view-structure}])
