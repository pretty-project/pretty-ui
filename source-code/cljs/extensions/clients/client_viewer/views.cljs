
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.clients.client-viewer.views
    (:require [plugins.item-viewer.api :as item-viewer]
              [x.app-core.api          :as a]
              [x.app-elements.api      :as elements]
              [x.app-layouts.api       :as layouts]
              [x.app-locales.api       :as locales]
              [x.app-ui.api            :as ui]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- client-vat-no-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [viewer-disabled? @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])]
       [elements/label ::client-vat-no-label
                       {:color               :muted
                        :content             :vat-no
                        :disabled?           viewer-disabled?
                        :horizontal-position :left
                        :indent              {:vertical :m :top :xs}}]))

(defn- client-vat-no-value
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [viewer-disabled? @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])
        client-vat-no    @(a/subscribe [:db/get-item [:clients :client-viewer/viewed-item :vat-no]])]
       [elements/label ::client-vat-no-value
                       {:disabled?           viewer-disabled?
                        :content             client-vat-no
                        :font-size           :m
                        :horizontal-position :left
                        :indent              {:vertical :m}}]))

(defn- client-vat-no
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:<> [client-vat-no-label]
       [client-vat-no-value]])

(defn- client-country-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [viewer-disabled? @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])]
       [elements/label ::client-country-label
                       {:color               :muted
                        :content             :country
                        :disabled?           viewer-disabled?
                        :horizontal-position :left
                        :indent              {:vertical :m :top :xs}}]))

(defn- client-country-value
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [viewer-disabled? @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])
        client-country   @(a/subscribe [:db/get-item [:clients :client-viewer/viewed-item :country]])]
       [elements/label ::client-vat-no-value
                       {:disabled?           viewer-disabled?
                        :content             client-country
                        :font-size           :m
                        :horizontal-position :left
                        :indent              {:vertical :m}}]))

(defn- client-zip-code-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [viewer-disabled? @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])]
       [elements/label ::client-zip-code-label
                       {:color               :muted
                        :content             :zip-code
                        :disabled?           viewer-disabled?
                        :horizontal-position :left
                        :indent              {:vertical :m :top :xs}}]))

(defn- client-zip-code-value
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [viewer-disabled? @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])
        client-zip-code  @(a/subscribe [:db/get-item [:clients :client-viewer/viewed-item :zip-code]])]
       [elements/label ::client-zip-code-value
                       {:disabled?           viewer-disabled?
                        :content             client-zip-code
                        :font-size           :m
                        :horizontal-position :left
                        :indent              {:vertical :m}}]))

(defn- client-city-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [viewer-disabled? @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])]
       [elements/label ::client-city-label
                       {:color               :muted
                        :content             :city
                        :disabled?           viewer-disabled?
                        :horizontal-position :left
                        :indent              {:vertical :m :top :xs}}]))

(defn- client-city-value
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [viewer-disabled? @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])
        client-city      @(a/subscribe [:db/get-item [:clients :client-viewer/viewed-item :city]])]
       [elements/label ::client-zip-code-value
                       {:disabled?           viewer-disabled?
                        :content             client-city
                        :font-size           :m
                        :horizontal-position :left
                        :indent              {:vertical :m}}]))

(defn- client-address-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [viewer-disabled? @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])]
       [elements/label ::client-address-label
                       {:color               :muted
                        :content             :address
                        :disabled?           viewer-disabled?
                        :horizontal-position :left
                        :indent              {:vertical :m :top :xs}}]))

(defn- client-address-value
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [viewer-disabled? @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])
        client-address   @(a/subscribe [:db/get-item [:clients :client-viewer/viewed-item :address]])]
       [elements/label ::client-address-value
                       {:disabled?           viewer-disabled?
                        :content             client-address
                        :font-size           :m
                        :horizontal-position :left
                        :indent              {:vertical :m}}]))

(defn- client-secondary-contacts
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:<> [elements/horizontal-separator {:size :xxl}]
       [:div (layouts/form-row-attributes)
             [:div (layouts/form-block-attributes {:ratio 30})
                   [client-country-label]
                   [client-country-value]
                   [client-city-label]
                   [client-city-value]]
             [:div (layouts/form-block-attributes {:ratio 30})
                   [client-zip-code-label]
                   [client-zip-code-value]
                   [client-address-label]
                   [client-address-value]]
             [:div (layouts/form-block-attributes {:ratio 40})]]])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- client-phone-number-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [viewer-disabled? @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])]
       [elements/label ::client-phone-number-label
                       {:color               :muted
                        :content             :phone-number
                        :disabled?           viewer-disabled?
                        :horizontal-position :left
                        :indent              {:vertical :m :top :xs}}]))

(defn- client-phone-number-value
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [viewer-disabled?    @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])
        client-phone-number @(a/subscribe [:db/get-item [:clients :client-viewer/viewed-item :phone-number]])]
       [elements/label ::client-phone-number-value
                       {:disabled?           viewer-disabled?
                        :content             client-phone-number
                        :font-size           :m
                        :horizontal-position :left
                        :indent              {:vertical :m}}]))

(defn- client-email-address-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [viewer-disabled? @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])]
       [elements/label ::client-email-address-label
                       {:color               :muted
                        :content             :email-address
                        :disabled?           viewer-disabled?
                        :horizontal-position :left
                        :indent              {:vertical :m :top :xs}}]))

(defn- client-email-address-value
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [viewer-disabled?     @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])
        client-email-address @(a/subscribe [:db/get-item [:clients :client-viewer/viewed-item :email-address]])]
       [elements/label ::client-email-address-value
                       {:disabled?           viewer-disabled?
                        :content             client-email-address
                        :font-size           :m
                        :horizontal-position :left
                        :indent              {:vertical :m}}]))

(defn- client-primary-contacts
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:<> [client-phone-number-label]
       [client-phone-number-value]
       [client-email-address-label]
       [client-email-address-value]])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- client-last-name-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [viewer-disabled? @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])]
       [elements/label ::client-last-name-label
                       {:color               :muted
                        :content             :last-name
                        :disabled?           viewer-disabled?
                        :horizontal-position :left
                        :indent              {:vertical :m :top :xs}}]))

(defn- client-last-name-value
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [viewer-disabled? @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])
        client-last-name @(a/subscribe [:db/get-item [:clients :client-viewer/viewed-item :last-name]])]
       [elements/label ::client-last-name-value
                       {:disabled?           viewer-disabled?
                        :content             client-last-name
                        :font-size           :m
                        :horizontal-position :left
                        :indent              {:vertical :m}}]))

(defn- client-first-name-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [viewer-disabled?  @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])]
       [elements/label ::client-first-name-label
                       {:color               :muted
                        :content             :first-name
                        :disabled?           viewer-disabled?
                        :horizontal-position :left
                        :indent              {:vertical :m :top :xs}}]))

(defn- client-first-name-value
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [viewer-disabled?  @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])
        client-first-name @(a/subscribe [:db/get-item [:clients :client-viewer/viewed-item :first-name]])]
       [elements/label ::client-first-name-value
                       {:disabled?           viewer-disabled?
                        :content             client-first-name
                        :font-size           :m
                        :horizontal-position :left
                        :indent              {:vertical :m}}]))

(defn- client-name
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [locales/name-order [:<> [client-first-name-label]
                           [client-first-name-value]]
                      [:<> [client-last-name-label]
                           [client-last-name-value]]
                      @(a/subscribe [:locales/get-name-order])])

(defn- client-basic-info
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:<> [elements/horizontal-separator {:size :xxl}]
       [:div (layouts/form-row-attributes)
             [:div (layouts/form-block-attributes {:ratio 30})
                   [client-name]]
             [:div (layouts/form-block-attributes {:ratio 30})
                   [client-primary-contacts]]
             [:div (layouts/form-block-attributes {:ratio 40})
                   [client-vat-no]]]])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- client-description-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [viewer-disabled? @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])]
       [elements/label ::client-description-label
                       {:color               :muted
                        :content             :description
                        :disabled?           viewer-disabled?
                        :horizontal-position :left
                        :indent              {:vertical :m :top :xs}}]))

(defn- client-description-value
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [viewer-disabled?   @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])
        client-description @(a/subscribe [:db/get-item [:clients :client-viewer/viewed-item :description]])]
       [elements/label ::client-description-value
                       {:disabled?           viewer-disabled?
                        :content             client-description
                        :font-size           :m
                        :horizontal-position :left
                        :indent              {:vertical :m}}]))

(defn- client-additional-info
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:<> [elements/horizontal-separator {:size :xxl}]
       [client-description-label]
       [client-description-value]])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- client-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [viewer-disabled? @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])
        client-name      @(a/subscribe [:clients.client-viewer/get-client-name])]
       [:<> [ui/title-sensor {:title client-name :offset -18}]
            [elements/label ::client-label
                            {:content     client-name
                             :disabled?   viewer-disabled?
                             :font-size   :l
                             :font-weight :extra-bold}]]))

(defn- client-colors
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [viewer-disabled? @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])
        client-colors    @(a/subscribe [:db/get-item [:clients :client-viewer/viewed-item :colors]])]
       [elements/color-stamp ::client-colors
                             {:colors    client-colors
                              :disabled? viewer-disabled?
                              :size      :xxl}]))

(defn- client-modified-at-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [viewer-disabled?   @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])
        client-modified-at @(a/subscribe [:item-viewer/get-current-item-modified-at :clients.client-viewer])]
       [elements/label ::client-modified-at-label
                       {:color     :muted
                        :content   client-modified-at
                        :disabled? viewer-disabled?
                        :font-size :xxs}]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- client-viewer
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:<> [client-colors]
       [client-label]
       [client-modified-at-label]
       [client-basic-info]
       [client-secondary-contacts]
       [client-additional-info]])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/horizontal-separator {:size :xxl}])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn footer
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [client-id @(a/subscribe [:router/get-current-route-path-param :item-id])]
       [item-viewer/footer :clients.client-viewer
                           {:on-edit-event [:router/go-to! (str "/@app-home/clients/"client-id"/edit")]}]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:<> [elements/horizontal-separator {:size :xxl}]
       [item-viewer/body :clients.client-viewer
                         {:auto-title?  true
                          :item-actions [:delete :duplicate]
                          :item-element #'client-viewer
                          :item-path    [:clients :client-viewer/viewed-item]
                          :label-key    :name}]])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id]
  [layouts/layout-a ::client-viewer
                    {:body   #'body
                     :footer #'footer}])
                     ;:header #'header}])
