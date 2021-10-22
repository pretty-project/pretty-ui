
(ns extensions.clients.client-form
    (:require [x.app-core.api     :as a :refer [r]]
              [x.app-elements.api :as elements]
              [x.app-layouts.api  :as layouts]
              [x.app-ui.api       :as ui]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn client-form-header
  []

  [elements/row {:content [:<> [:div {:style {:display :flex :gap "12px"}}
                                     [elements/button {:label :delete! :variant :transparent :color :warning
                                                       :layout :icon-button :icon :delete_outline}]
                                     [elements/button {:label :duplicate! :variant :transparent
                                                       :layout :icon-button :icon :content_copy
                                                       :color :none}]]
                               [elements/button {:label :save! :variant :transparent
                                                 :layout :icon-button :icon :save}]]
                 :horizontal-align :space-between}])

(defn client-form
  []
  [:<> [:div#clients--client-form--names
         [elements/text-field {:label :first-name}]
         [elements/text-field {:label :last-name}]]])

(defn- view
  []
  [layouts/layout-a {:label :edit-client :icon :people
                     :body        {:content #'client-form}
                     :body-header {:content #'client-form-header}}])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :x.app-extensions.clients/render-client-form!
  [:x.app-ui/set-surface!
   :clients/view {:content   #'view
                  :label-bar {:content       #'ui/go-back-surface-label-bar
                              :content-props {:label :clients}}}])
                  ;:subscriber [::get-view-props]}])

(a/reg-lifecycles
  ::lifecycles
  {:on-app-boot [:x.app-router/add-route!
                 ::extended-route
                 {:restricted?    true
                  :route-event    [:x.app-extensions.clients/render-client-form!]
                  :route-template "/clients/:client-id"}]})
