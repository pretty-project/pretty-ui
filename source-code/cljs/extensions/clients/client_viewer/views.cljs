
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.clients.client-viewer.views
    (:require [layouts.surface-a.api   :as surface-a]
              [plugins.item-viewer.api :as item-viewer]
              [x.app-core.api          :as a]
              [x.app-elements.api      :as elements]
              [x.app-locales.api       :as locales]

              ; TEMP
              [x.app-layouts.api :as layouts]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn client-name-label
  []
  (let [viewer-disabled? @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])
        client-name      @(a/subscribe [:clients.client-viewer/get-client-name])]
       [:<> [surface-a/title-sensor {:title client-name :offset 0}]
            [elements/label ::client-name-label
                            {:content     client-name
                             :disabled?   viewer-disabled?
                             :font-size   :xxl
                             :font-weight :extra-bold
                             :indent      {:left :xxs :right :s}}]]))

(defn client-color-marker
  []
  (let [viewer-disabled? @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])
        client-colors    @(a/subscribe [:db/get-item [:clients :client-viewer/viewed-item :colors]])]
       [elements/color-marker ::client-color-marker
                              {:colors    client-colors
                               :disabled? viewer-disabled?
                               :size      :l}]))

(defn client-modified-at-label
  []
  (let [viewer-disabled?   @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])
        client-modified-at @(a/subscribe [:item-viewer/get-current-item-modified-at :clients.client-viewer])]
       [elements/label ::client-modified-at-label
                       {:color     :muted
                        :content   {:content :last-modified-at-n :replacements [client-modified-at]}
                        :disabled? viewer-disabled?
                        :font-size :xxs
                        :indent    {:left :xxs}}]))

(defn client-header
  []
  [:<> [elements/row {:content [:<> [client-name-label]
                                    [client-color-marker]]}]
       [client-modified-at-label]])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn client-last-name-label
  []
  (let [viewer-disabled? @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])]
       [elements/label ::client-last-name-label
                       {:color               :muted
                        :content             :last-name
                        :disabled?           viewer-disabled?
                        :horizontal-position :left
                        :indent              {:vertical :xs :top :m}}]))

(defn client-last-name-value
  []
  (let [viewer-disabled? @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])
        client-last-name @(a/subscribe [:db/get-item [:clients :client-viewer/viewed-item :last-name]])]
       [elements/label ::client-last-name-value
                       {:disabled?           viewer-disabled?
                        :content             client-last-name
                        :font-size           :m
                        :horizontal-position :left
                        :indent              {:vertical :xs}
                        :min-width           :xxs
                        :placeholder         "-"}]))

(defn client-first-name-label
  []
  (let [viewer-disabled?  @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])]
       [elements/label ::client-first-name-label
                       {:color               :muted
                        :content             :first-name
                        :disabled?           viewer-disabled?
                        :horizontal-position :left
                        :indent              {:vertical :xs :top :m}}]))

(defn client-first-name-value
  []
  (let [viewer-disabled?  @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])
        client-first-name @(a/subscribe [:db/get-item [:clients :client-viewer/viewed-item :first-name]])]
       [elements/label ::client-first-name-value
                       {:disabled?           viewer-disabled?
                        :content             client-first-name
                        :font-size           :m
                        :horizontal-position :left
                        :indent              {:vertical :xs}
                        :min-width           :xxs
                        :placeholder         "-"}]))

(defn client-name
  []
  [:div (layouts/form-row-attributes)
        [locales/name-order [:<> [:div (layouts/form-block-attributes {:ratio 50})
                                       [client-first-name-label]
                                       [client-first-name-value]]]
                            [:<> [:div (layouts/form-block-attributes {:ratio 50})
                                       [client-last-name-label]
                                       [client-last-name-value]]]
                            @(a/subscribe [:locales/get-name-order])]])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn client-phone-number-label
  []
  (let [viewer-disabled? @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])]
       [elements/label ::client-phone-number-label
                       {:color               :muted
                        :content             :phone-number
                        :disabled?           viewer-disabled?
                        :horizontal-position :left
                        :indent              {:vertical :xs :top :m}}]))

(defn client-phone-number-value
  []
  (let [viewer-disabled?    @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])
        client-phone-number @(a/subscribe [:db/get-item [:clients :client-viewer/viewed-item :phone-number]])]
       [elements/label ::client-phone-number-value
                       {:disabled?           viewer-disabled?
                        :content             client-phone-number
                        :font-size           :m
                        :horizontal-position :left
                        :indent              {:vertical :xs}
                        :min-width           :xxs
                        :placeholder         "-"}]))

(defn client-email-address-label
  []
  (let [viewer-disabled? @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])]
       [elements/label ::client-email-address-label
                       {:color               :muted
                        :content             :email-address
                        :disabled?           viewer-disabled?
                        :horizontal-position :left
                        :indent              {:vertical :xs :top :m}}]))

(defn client-email-address-value
  []
  (let [viewer-disabled?     @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])
        client-email-address @(a/subscribe [:db/get-item [:clients :client-viewer/viewed-item :email-address]])]
       [elements/label ::client-email-address-value
                       {:disabled?           viewer-disabled?
                        :content             client-email-address
                        :font-size           :m
                        :horizontal-position :left
                        :indent              {:vertical :xs}
                        :min-width           :xxs
                        :placeholder         "-"}]))

(defn client-primary-contacts
  []
  [:div (layouts/form-row-attributes)
        [:div (layouts/form-block-attributes {:ratio 50})
              [client-email-address-label]
              [client-email-address-value]]
        [:div (layouts/form-block-attributes {:ratio 50})
              [client-phone-number-label]
              [client-phone-number-value]]])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn client-vat-no-label
  []
  (let [viewer-disabled? @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])]
       [elements/label ::client-vat-no-label
                       {:color               :muted
                        :content             :vat-no
                        :disabled?           viewer-disabled?
                        :horizontal-position :left
                        :indent              {:vertical :xs :top :m}}]))

(defn client-vat-no-value
  []
  (let [viewer-disabled? @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])
        client-vat-no    @(a/subscribe [:db/get-item [:clients :client-viewer/viewed-item :vat-no]])]
       [elements/label ::client-vat-no-value
                       {:disabled?           viewer-disabled?
                        :content             client-vat-no
                        :font-size           :m
                        :horizontal-position :left
                        :indent              {:vertical :xs}
                        :min-width           :xxs
                        :placeholder         "-"}]))

(defn client-country-label
  []
  (let [viewer-disabled? @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])]
       [elements/label ::client-country-label
                       {:color               :muted
                        :content             :country
                        :disabled?           viewer-disabled?
                        :horizontal-position :left
                        :indent              {:vertical :xs :top :m}}]))

(defn client-country-value
  []
  (let [viewer-disabled? @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])
        client-country   @(a/subscribe [:db/get-item [:clients :client-viewer/viewed-item :country]])]
       [elements/label ::client-country-value
                       {:disabled?           viewer-disabled?
                        :content             client-country
                        :font-size           :m
                        :horizontal-position :left
                        :indent              {:vertical :xs}
                        :min-width           :xxs
                        :placeholder         "-"}]))

(defn client-zip-code-label
  []
  (let [viewer-disabled? @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])]
       [elements/label ::client-zip-code-label
                       {:color               :muted
                        :content             :zip-code
                        :disabled?           viewer-disabled?
                        :horizontal-position :left
                        :indent              {:vertical :xs :top :m}}]))

(defn client-zip-code-value
  []
  (let [viewer-disabled? @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])
        client-zip-code  @(a/subscribe [:db/get-item [:clients :client-viewer/viewed-item :zip-code]])]
       [elements/label ::client-zip-code-value
                       {:disabled?           viewer-disabled?
                        :content             client-zip-code
                        :font-size           :m
                        :horizontal-position :left
                        :indent              {:vertical :xs}
                        :min-width           :xxs
                        :placeholder         "-"}]))

(defn client-city-label
  []
  (let [viewer-disabled? @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])]
       [elements/label ::client-city-label
                       {:color               :muted
                        :content             :city
                        :disabled?           viewer-disabled?
                        :horizontal-position :left
                        :indent              {:vertical :xs :top :m}}]))

(defn client-city-value
  []
  (let [viewer-disabled? @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])
        client-city      @(a/subscribe [:db/get-item [:clients :client-viewer/viewed-item :city]])]
       [elements/label ::client-city-value
                       {:disabled?           viewer-disabled?
                        :content             client-city
                        :font-size           :m
                        :horizontal-position :left
                        :indent              {:vertical :xs}
                        :min-width           :xxs
                        :placeholder         "-"}]))

(defn client-address-label
  []
  (let [viewer-disabled? @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])]
       [elements/label ::client-address-label
                       {:color               :muted
                        :content             :address
                        :disabled?           viewer-disabled?
                        :horizontal-position :left
                        :indent              {:vertical :xs :top :m}}]))

(defn client-address-value
  []
  (let [viewer-disabled? @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])
        client-address   @(a/subscribe [:db/get-item [:clients :client-viewer/viewed-item :address]])]
       [elements/label ::client-address-value
                       {:disabled?           viewer-disabled?
                        :content             client-address
                        :font-size           :m
                        :horizontal-position :left
                        :indent              {:vertical :xs}
                        :min-width           :xxs
                        :placeholder         "-"}]))

(defn client-secondary-contacts
  []
  [:<> [:div (layouts/form-row-attributes)
             [:div (layouts/form-block-attributes {:ratio 25})
                   [client-country-label]
                   [client-country-value]]
             [:div (layouts/form-block-attributes {:ratio 25})
                   [client-zip-code-label]
                   [client-zip-code-value]]
             [:div (layouts/form-block-attributes {:ratio 50})
                   [client-city-label]
                   [client-city-value]]]
       [:div (layouts/form-row-attributes)
             [:div (layouts/form-block-attributes {:ratio 50})
                   [client-address-label]
                   [client-address-value]]
             [:div (layouts/form-block-attributes {:ratio 50})
                   [client-vat-no-label]
                   [client-vat-no-value]]]])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn client-description-label
  []
  (let [viewer-disabled? @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])]
       [elements/label ::client-description-label
                       {:color               :muted
                        :content             :description
                        :disabled?           viewer-disabled?
                        :horizontal-position :left
                        :indent              {:vertical :xs :top :m}}]))

(defn client-description-value
  []
  (let [viewer-disabled?   @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])
        client-description @(a/subscribe [:db/get-item [:clients :client-viewer/viewed-item :description]])]
       [elements/label ::client-description-value
                       {:disabled?           viewer-disabled?
                        :content             client-description
                        :font-size           :m
                        :horizontal-position :left
                        :indent              {:vertical :xs}
                        :min-width           :xxs
                        :placeholder         "-"}]))

(defn client-additional-info
  []
  [:<> [client-description-label]
       [client-description-value]])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn client-menu-bar
  []
  [:<> [elements/menu-bar ::client-menu-bar
                          {:indent {:top :xxl}
                           :menu-items [{:label :overview     :on-click [] :active? true}]}]
                                       ;{:label :price-quotes :on-click []}
       [elements/horizontal-line {:color :highlight}]])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn delete-button
  []
  (let [viewer-disabled? @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])]
       [elements/button ::delete-button
                        {:disabled?   viewer-disabled?
                         :font-size   :xs
                         :hover-color :highlight
                         :indent      {:vertical :xxs :horizontal :xxs}
                         :on-click    [:item-viewer/delete-item! :clients.client-viewer]
                         :preset      :delete}]))

(defn duplicate-button
  []
  (let [viewer-disabled? @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])]
       [elements/button ::duplicate-button
                        {:disabled?   viewer-disabled?
                         :font-size   :xs
                         :hover-color :highlight
                         :indent      {:vertical :xxs :horizontal :xxs}
                         :on-click    [:item-viewer/duplicate-item! :clients.client-viewer]
                         :preset      :duplicate}]))

(defn edit-button
  []
  (let [viewer-disabled? @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])
        client-id        @(a/subscribe [:router/get-current-route-path-param :item-id])]
       [elements/button ::edit-button
                        {:disabled?   viewer-disabled?
                         :font-size   :xs
                         :hover-color :highlight
                         :indent      {:vertical :xxs :horizontal :xxs}
                         :on-click    [:router/go-to! (str "/@app-home/clients/"client-id"/edit")]
                         :preset      :edit}]))

(defn footer
  []
  [elements/horizontal-polarity ::footer
                                {:style {:background-color "white" :border-top "1px solid #ddd"
                                         :bottom "0" :position "sticky" :width "100%"}
                                 :start-content [:<> [delete-button]
                                                     [duplicate-button]]
                                 :end-content   [:<> [edit-button]]}])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn client-overview
  []
  [:<> [client-header]
       [client-menu-bar]
       [elements/horizontal-separator {:size :s}]
       [client-name]
       [client-primary-contacts]
       [client-secondary-contacts]
       [client-additional-info]
       [elements/horizontal-separator {:size :xxl}]
       [footer]])

(defn view-structure
  []
  [:<> [elements/horizontal-separator {:size :xxl}]
       [item-viewer/body :clients.client-viewer
                         {:auto-title?  true
                          :item-actions [:delete :duplicate]
                          :item-element #'client-overview
                          :item-path    [:clients :client-viewer/viewed-item]
                          :label-key    :name}]])

(defn view
  [surface-id]
  [surface-a/layout surface-id
                    {:content #'view-structure}])
