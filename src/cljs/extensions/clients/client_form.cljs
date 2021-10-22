
(ns extensions.clients.client-form
    (:require [x.app-core.api     :as a :refer [r]]
              [x.app-elements.api :as elements]
              [x.app-layouts.api  :as layouts]
              [x.app-ui.api       :as ui]
              [extensions.clients.engine :as engine]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- save-enabled?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  false)

(defn- get-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  {:save-enabled? (r save-enabled? db)})

(a/reg-sub ::get-view-props get-view-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- undo-delete-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/button {:label :undo-delete! :variant :transparent :horizontal-align :left :color :none
                    :on-click [:clients/undo-last-deleted!]}])

(defn- edit-copy-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/button {:label :edit-copy! :variant :transparent :horizontal-align :left :color :none
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
  [surface-id {:keys [save-enabled?]}]
  [elements/button {:label :save! :variant :transparent :layout :icon-button :icon :save
                    :disabled? (not save-enabled?)}])

(defn- client-form-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id view-props]
  [elements/row {:content [:<> [:div#clients--client-form--header
                                 [delete-client-button surface-id view-props]
                                 [copy-client-button   surface-id view-props]]
                               [save-client-button surface-id view-props]]
                 :horizontal-align :space-between}])

(defn- client-form
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id view-props]
  [:<> [:div#clients--client-form--names
         [elements/text-field {:label :first-name :required? true}]
         [elements/text-field {:label :last-name  :required? true}]]])

(defn- view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id view-props]
  [layouts/layout-a surface-id {:label :edit-client :icon :people
                                :body        {:content #'client-form}
                                :body-header {:content #'client-form-header}}])



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
                [:x.app-ui/blow-bubble! ::delete-notification {:content #'undo-delete-button :color :warning}]]})

(a/reg-event-fx
  :clients/duplicate-client!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:x.app-ui/blow-bubble! ::duplicate-notification {:content #'edit-copy-button}])

(a/reg-event-fx
  :x.app-extensions.clients/render-client-form!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:x.app-ui/set-surface!
   :clients/view {:content   #'view
                  :label-bar {:content       #'ui/go-back-surface-label-bar
                              :content-props {:label :clients}}
                  :subscriber [::get-view-props]}])

(a/reg-lifecycles
  ::lifecycles
  {:on-app-boot [:x.app-router/add-route!
                 ::extended-route
                 {:restricted?    true
                  :route-event    [:x.app-extensions.clients/render-client-form!]
                  :route-template "/clients/:client-id"}]})
