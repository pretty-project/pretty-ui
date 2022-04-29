
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

(defn- name-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [viewer-disabled? @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])
        client-name      @(a/subscribe [:clients.client-viewer/get-client-name])]
       [:<> [ui/title-sensor {:title client-name :offset -18}]
            [elements/label ::name-label
                            {:content     client-name
                             :disabled?   viewer-disabled?
                             :font-size   :l
                             :font-weight :extra-bold
                             :placeholder "Névtelen ügyfél"}]]))

(defn- color-stamp
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [viewer-disabled? @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])
        client-colors    @(a/subscribe [:db/get-item [:clients :client-viewer/viewed-item :colors]])]
       [elements/color-stamp ::color-stamp
                             {:colors    client-colors
                              :disabled? viewer-disabled?
                              :size      :xxl}]))

(defn- modified-at-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [viewer-disabled?   @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])
        client-modified-at @(a/subscribe [:item-viewer/get-current-item-modified-at :clients.client-viewer])]
       [elements/label ::modified-at-label
                       {:color     :muted
                        :content   client-modified-at
                        :disabled? viewer-disabled?
                        :font-size :xxs}]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- basic-info-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [viewer-disabled? @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])]
       [elements/label ::basic-info-label
                       {:content             :basic-info
                        :disabled?           viewer-disabled?
                        :font-size           :m
                        :font-weight         :extra-bold
                        :horizontal-position :left
                        :indent              {:left :m}}]))

(defn- last-name-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [viewer-disabled? @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])]
       [elements/label ::last-name-label
                       {:color               :muted
                        :content             :last-name
                        :disabled?           viewer-disabled?
                        :horizontal-position :left
                        :indent              {:vertical :m :top :xs}}]))

(defn- last-name-value
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [viewer-disabled? @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])
        client-last-name @(a/subscribe [:db/get-item [:clients :client-viewer/viewed-item :last-name]])]
       [elements/label ::last-name-value
                       {:disabled?           viewer-disabled?
                        :content             client-last-name
                        :font-size           :m
                        :horizontal-position :left
                        :indent              {:vertical :m}
                        :min-width           :s
                        :placeholder         "-"}]))

(defn- first-name-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [viewer-disabled?  @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])]
       [elements/label ::first-name-label
                       {:color               :muted
                        :content             :first-name
                        :disabled?           viewer-disabled?
                        :horizontal-position :left
                        :indent              {:vertical :m :top :xs}}]))

(defn- first-name-value
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [viewer-disabled?  @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])
        client-first-name @(a/subscribe [:db/get-item [:clients :client-viewer/viewed-item :first-name]])]
       [elements/label ::first-name-value
                       {:disabled?           viewer-disabled?
                        :content             client-first-name
                        :font-size           :m
                        :horizontal-position :left
                        :indent              {:vertical :m}
                        :min-width           :s
                        :placeholder         "-"}]))

(defn- name-values
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:div (layouts/form-row-attributes)
        [locales/name-order [:<> [:div (layouts/form-block-attributes {:ratio 50})
                                       [first-name-label]
                                       [first-name-value]]]
                            [:<> [:div (layouts/form-block-attributes {:ratio 50})
                                       [last-name-label]
                                       [last-name-value]]]
                            @(a/subscribe [:locales/get-name-order])]])

(defn- phone-number-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [viewer-disabled? @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])]
       [elements/label ::phone-number-label
                       {:color               :muted
                        :content             :phone-number
                        :disabled?           viewer-disabled?
                        :horizontal-position :left
                        :indent              {:vertical :m :top :xs}}]))

(defn- phone-number-value
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [viewer-disabled?    @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])
        client-phone-number @(a/subscribe [:db/get-item [:clients :client-viewer/viewed-item :phone-number]])]
       [elements/label ::phone-number-value
                       {:disabled?           viewer-disabled?
                        :content             client-phone-number
                        :font-size           :m
                        :horizontal-position :left
                        :indent              {:vertical :m}
                        :min-width           :s
                        :placeholder         "-"}]))

(defn- email-address-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [viewer-disabled? @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])]
       [elements/label ::email-address-label
                       {:color               :muted
                        :content             :email-address
                        :disabled?           viewer-disabled?
                        :horizontal-position :left
                        :indent              {:vertical :m :top :xs}}]))

(defn- email-address-value
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [viewer-disabled?     @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])
        client-email-address @(a/subscribe [:db/get-item [:clients :client-viewer/viewed-item :email-address]])]
       [elements/label ::email-address-value
                       {:disabled?           viewer-disabled?
                        :content             client-email-address
                        :font-size           :m
                        :horizontal-position :left
                        :indent              {:vertical :m}
                        :min-width           :s
                        :placeholder         "-"}]))

(defn- primary-contacts
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:div (layouts/form-row-attributes)
        [:div (layouts/form-block-attributes {:ratio 50})
              [email-address-label]
              [email-address-value]]
        [:div (layouts/form-block-attributes {:ratio 50})
              [phone-number-label]
              [phone-number-value]]])

(defn- basic-info
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:<> [elements/horizontal-separator {:size :xxl}]
       [basic-info-label]
       [name-values]
       [primary-contacts]])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- more-info-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [viewer-disabled? @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])]
       [elements/label ::more-info-label
                       {:content             :more-info
                        :disabled?           viewer-disabled?
                        :font-size           :m
                        :font-weight         :extra-bold
                        :horizontal-position :left
                        :indent              {:left :m}}]))

(defn- vat-no-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [viewer-disabled? @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])]
       [elements/label ::vat-no-label
                       {:color               :muted
                        :content             :vat-no
                        :disabled?           viewer-disabled?
                        :horizontal-position :left
                        :indent              {:vertical :m :top :xs}}]))

(defn- vat-no-value
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [viewer-disabled? @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])
        client-vat-no    @(a/subscribe [:db/get-item [:clients :client-viewer/viewed-item :vat-no]])]
       [elements/label ::vat-no-value
                       {:disabled?           viewer-disabled?
                        :content             client-vat-no
                        :font-size           :m
                        :horizontal-position :left
                        :indent              {:vertical :m}
                        :min-width           :s
                        :placeholder         "-"}]))

(defn- country-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [viewer-disabled? @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])]
       [elements/label ::country-label
                       {:color               :muted
                        :content             :country
                        :disabled?           viewer-disabled?
                        :horizontal-position :left
                        :indent              {:vertical :m :top :xs}}]))

(defn- country-value
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [viewer-disabled? @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])
        client-country   @(a/subscribe [:db/get-item [:clients :client-viewer/viewed-item :country]])]
       [elements/label ::country-value
                       {:disabled?           viewer-disabled?
                        :content             client-country
                        :font-size           :m
                        :horizontal-position :left
                        :indent              {:vertical :m}
                        :min-width           :s
                        :placeholder         "-"}]))

(defn- zip-code-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [viewer-disabled? @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])]
       [elements/label ::zip-code-label
                       {:color               :muted
                        :content             :zip-code
                        :disabled?           viewer-disabled?
                        :horizontal-position :left
                        :indent              {:vertical :m :top :xs}}]))

(defn- zip-code-value
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [viewer-disabled? @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])
        client-zip-code  @(a/subscribe [:db/get-item [:clients :client-viewer/viewed-item :zip-code]])]
       [elements/label ::zip-code-value
                       {:disabled?           viewer-disabled?
                        :content             client-zip-code
                        :font-size           :m
                        :horizontal-position :left
                        :indent              {:vertical :m}
                        :min-width           :s
                        :placeholder         "-"}]))

(defn- city-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [viewer-disabled? @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])]
       [elements/label ::city-label
                       {:color               :muted
                        :content             :city
                        :disabled?           viewer-disabled?
                        :horizontal-position :left
                        :indent              {:vertical :m :top :xs}}]))

(defn- city-value
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [viewer-disabled? @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])
        client-city      @(a/subscribe [:db/get-item [:clients :client-viewer/viewed-item :city]])]
       [elements/label ::city-value
                       {:disabled?           viewer-disabled?
                        :content             client-city
                        :font-size           :m
                        :horizontal-position :left
                        :indent              {:vertical :m}
                        :min-width           :s
                        :placeholder         "-"}]))

(defn- address-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [viewer-disabled? @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])]
       [elements/label ::address-label
                       {:color               :muted
                        :content             :address
                        :disabled?           viewer-disabled?
                        :horizontal-position :left
                        :indent              {:vertical :m :top :xs}}]))

(defn- address-value
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [viewer-disabled? @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])
        client-address   @(a/subscribe [:db/get-item [:clients :client-viewer/viewed-item :address]])]
       [elements/label ::address-value
                       {:disabled?           viewer-disabled?
                        :content             client-address
                        :font-size           :m
                        :horizontal-position :left
                        :indent              {:vertical :m}
                        :min-width           :s
                        :placeholder         "-"}]))

(defn- secondary-contacts
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:<> [:div (layouts/form-row-attributes)
             [:div (layouts/form-block-attributes {:ratio 30})
                   [country-label]
                   [country-value]]
             [:div (layouts/form-block-attributes {:ratio 30})
                   [zip-code-label]
                   [zip-code-value]]
             [:div (layouts/form-block-attributes {:ratio 40})
                   [city-label]
                   [city-value]]]
       [:div (layouts/form-row-attributes)
             [:div (layouts/form-block-attributes {:ratio 60})
                   [address-label]
                   [address-value]]
             [:div (layouts/form-block-attributes {:ratio 40})
                   [vat-no-label]
                   [vat-no-value]]]])

(defn- more-info
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:<> [elements/horizontal-separator {:size :xxl}]
       [more-info-label]
       [secondary-contacts]])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- description-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [viewer-disabled? @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])]
       [elements/label ::description-label
                       {;:color               :muted
                        :content             :description
                        :disabled?           viewer-disabled?
                        :font-size           :m
                        :font-weight         :extra-bold
                        :horizontal-position :left
                        :indent              {:vertical :m :top :xs}}]))

(defn- description-value
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [viewer-disabled?   @(a/subscribe [:item-viewer/viewer-disabled? :clients.client-viewer])
        client-description @(a/subscribe [:db/get-item [:clients :client-viewer/viewed-item :description]])]
       [elements/label ::description-value
                       {:disabled?           viewer-disabled?
                        :content             client-description
                        :font-size           :m
                        :horizontal-position :left
                        :indent              {:vertical :m}
                        :min-width           :s
                        :placeholder         "-"}]))

(defn- additional-info
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:<> [elements/horizontal-separator {:size :xxl}]
       [description-label]
       [description-value]])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- client-overview
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:<> [color-stamp]
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
                          :item-element #'client-overview
                          :item-path    [:clients :client-viewer/viewed-item]
                          :label-key    :name}]
       [elements/horizontal-separator {:size :xxl}]])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id]
  [layouts/layout-a ::view
                    {:body   #'body
                     :footer #'footer}])
