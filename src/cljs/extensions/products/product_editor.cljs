
(ns extensions.products.product-editor
    (:require [mid-fruits.candy   :refer [param]]
              [mid-fruits.form    :as form]
              [mid-fruits.string  :as string]
              [x.app-core.api     :as a :refer [r]]
              [x.app-db.api       :as db]
              [x.app-elements.api :as elements]
              [x.app-layouts.api  :as layouts]
              [x.app-locales.api  :as locales]
              [plugins.item-editor.api :as item-editor]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-body-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  {})

(a/reg-sub ::get-body-props get-body-props)

(defn- get-header-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (r item-editor/get-header-props db "products" "product"))

(a/reg-sub ::get-header-props get-header-props)

(defn- get-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (r item-editor/get-view-props db "products" "product"))

(a/reg-sub ::get-view-props get-view-props)



;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- action-buttons
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _]
  [:<> [item-editor/delete-item-button  "products" "product"]
       [item-editor/copy-item-button    "products" "product"]
       [item-editor/archive-item-button "products" "product"]])

(defn- header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [header-id {:keys [new-item?] :as header-props}]
  [elements/polarity ::form-header
                     {:start-content [:<> (if (boolean new-item?)
                                             ;[item-editor/cancel-item-button "products" "product"]
                                              nil
                                              [action-buttons header-id header-props])]
                      :end-content [item-editor/save-item-button "products" "product" header-props]}])



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [body-id body-props]
  [:div#product--product-form
    [elements/menu-bar {:menu-items [{:label "Adatok" :on-click []} {:label "Képek" :color :muted :on-click []}
                                     {:label "Változatok" :on-click [] :color :muted}]}]
     ;                   :horizontal-align "left"}]
    [elements/text-field {:label :name :required? true}]
    [elements/combo-box {:label "Vendor" :initial-options ["Wörmann" "AL-KO"]}]
    [elements/multiline-field {:label :description}]
    [elements/select {:initial-options [:active :draft]
                      :initial-value    :active
                      :label :status}]
    [elements/combo-box {:label "Kategória" :initial-options ["Hűtős utánfutók" "Járműszállító utánfutók"]}]
    [elements/multi-combo-box {:label "Címkék" :initial-options ["Mélyhűtős utánfutók"]
                               :no-options-selected-label "Nincsenek kiválasztott címkék"}]])



;; -- View components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id {:keys [description synchronizing?] :as view-props}]
  [layouts/layout-a surface-id {:description description
                                :disabled?   synchronizing?
                                :body {:content      #'body
                                       :subscriber   [::get-body-props]}
                                :header {:content    #'header
                                         :sticky?    true
                                         :subscriber [::get-header-props]}}])



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :products/render-product-editor!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:x.app-ui/set-surface! ::view {:content #'view :subscriber [::get-view-props]}])

(a/reg-lifecycles
  ::lifecycles
  {:on-app-boot [:item-editor/add-route! "products" "product"]})
