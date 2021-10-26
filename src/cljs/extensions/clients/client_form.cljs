
(ns extensions.clients.client-form
    (:require [mid-fruits.candy   :refer [param]]
              [mid-fruits.form    :as form]
              [x.app-core.api     :as a :refer [r]]
              [x.app-db.api       :as db]
              [x.app-elements.api :as elements]
              [x.app-layouts.api  :as layouts]
              [x.app-locales.api  :as locales]
              [x.app-ui.api       :as ui]
              [extensions.clients.engine :as engine]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  {:name-order (r locales/get-name-order db)
   :client-no  "608030"})

(a/reg-sub ::get-view-props get-view-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- undo-delete-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/button {:label :undo-delete! :variant :transparent :horizontal-align :left :color :warning
                    :on-click [:clients/undo-last-deleted!]}])

(defn- edit-copy-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/button {:label :edit-copy! :variant :transparent :horizontal-align :left :color :primary
                    :on-click [:clients/edit-last-duplicated!]}])

(defn- delete-client-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id view-props]
  [elements/button {:label :delete! :variant :transparent :color :warning
                    :layout :icon-button :icon :delete_outline :on-click [:clients/delete-client!]}])

(defn- copy-client-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id view-props]
  [elements/button {:label :duplicate! :variant :transparent :layout :icon-button
                    :icon :content_copy :color :none :on-click [:clients/duplicate-client!]}])

(defn- save-client-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id view-props]
  [elements/button {:label :save! :variant :transparent :layout :icon-button :icon :save}])
  ; Ne legyen disabled állapota a mentés gombnak, inkább a gomb megnyomása jelölje warning
  ; színnel a nem megfelelően kitöltött mezőket és esetleg írja ki, hogy hol a hiba

(defn- client-form-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id view-props]
  [elements/row {:content [:<> [:div.x-icon-buttons
                                 [delete-client-button surface-id view-props]
                                 [copy-client-button   surface-id view-props]]
                               [save-client-button surface-id view-props]]
                 :horizontal-align :space-between}])

(defn- client-form
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id {:keys [client-no name-order] :as view-props}]
  [:div#clients--client-form
    [:div#clients--client-form--client-no
      [elements/label {:content :client-id :font-size :xxs :color :highlight :font-weight :bold       :layout :fit}]
      [elements/text  {:content client-no  :font-size :xs  :color :muted     :font-weight :extra-bold :layout :fit :prefix "#"}]]
    [:div#clients--client-form--first-name-and-last-name
      [locales/name-order [elements/text-field {:label :first-name :required? true :value-path (db/path ::client-form :first-name)}]
                          [elements/text-field {:label :last-name  :required? true :value-path (db/path ::client-form :last-name)}]
                          (param name-order)]]
    [:div#clients--client-form--email-address-and-phone-number
      [elements/text-field {:label :email-address :required? true :value-path (db/path ::client-form :email-address)
                            :validator {:f form/email-address-valid? :invalid-message :invalid-email-address}}]
      [elements/text-field {:label :phone-number :required? true :value-path (db/path ::client-form :phone-number)
                            :validator {:f form/phone-number-valid? :invalid-message :invalid-phone-number}
                            :modifier form/valid-phone-number}]]

    [elements/separator {:orientation :horizontal :size :xxl}]])

(defn- view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id view-props]
  [layouts/layout-a surface-id {:label :edit-client :icon :people
                                :body        {:content     #'client-form
                                              :content-props view-props}
                                :body-header {:content     #'client-form-header
                                              :content-props view-props}}])



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :clients/edit-last-duplicated!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      [:x.app-ui/pop-bubble! ::notification]))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :clients/delete-client!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  {:dispatch-n [[:x.app-router/go-to! "/clients"]
                [:x.app-ui/blow-bubble! ::delete-notification {:content #'undo-delete-button :color :muted}]]})

(a/reg-event-fx
  :clients/duplicate-client!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:x.app-ui/blow-bubble! ::duplicate-notification {:content #'edit-copy-button :color :muted}])

(a/reg-event-fx
  :x.app-extensions.clients/render-client-form!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:x.app-ui/set-surface!
   ::view {:content   #'view
           :label-bar {:content       #'ui/go-back-surface-label-bar
                       :content-props {:label :clients}}
           :subscriber [::get-view-props]}])

(a/reg-lifecycles
  ::lifecycles
  {:on-app-boot [:x.app-router/add-route!
                 ::extended-route
                 {:restricted?    true
                  :route-event    [:x.app-extensions.clients/render-client-form!]
                  :route-title    :clients
                  :route-template "/clients/:client-id"}]})








; Ezt majd tedd át az ...clients.engine.cljs-be
;
(a/reg-event-fx
  :clients/download-client-data!
  (fn [_ [_ client-id]]
      {:dispatch-later [; Request emulálása a UI számára
                        {:ms    0 :dispatch [:x.app-core/set-process-activity! :clients/download-client-data! :active]}
                        {:ms  750 :dispatch [:x.app-core/set-process-activity! :clients/download-client-data! :idle]}
                        {:ms 1000 :dispatch [:x.app-core/set-process-activity! :clients/download-client-data! :stalled]}
                        ; Minta adatok hozzáadasa
                        {:ms 1000 :dispatch [:x.app-db/set-item! [:clients :form]
                                             {:client/id            "9b2f16b0-bb4c-46fe-95c0-a6879e4cb8de"
                                              :client/client-no     "051301"
                                              :client/first-name    "Debil"
                                              :client/last-name     "Duck"
                                              :client/email-address "debil-duck@gmail.com"
                                              :client/added-at      "2020-04-10T16:20:00.123Z"}]}]}))
