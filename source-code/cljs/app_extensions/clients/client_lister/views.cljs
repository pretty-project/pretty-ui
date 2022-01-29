
(ns app-extensions.clients.client-lister.views
    (:require [mid-fruits.candy  :refer [param return]]
              [x.app-core.api    :as a :refer [r]]
              [x.app-locales.api :as locales]
              [app-plugins.item-editor.api :as item-editor]
              [app-plugins.item-lister.api :as item-lister]))



;; -- List-item components ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- client-item-secondary-details
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _ {:keys [modified-at]}]
  [:div.clients--client-item--secondary-details [:div.clients--client-item--modified-at modified-at]])

(defn- client-item-primary-details
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [email-address first-name last-name]} {:keys [selected-language]}]
  (let [client-name (locales/name->ordered-name first-name last-name selected-language)]
       [:div.clients--client-item--primary-details [:div.clients--client-item--full-name     client-name]
                                                   [:div.clients--client-item--email-address email-address]]))

(defn- client-item-details
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [item-dex client-props item-props]
  [:div.clients--client-item--details [client-item-primary-details   item-dex client-props item-props]
                                      [client-item-secondary-details item-dex client-props item-props]])

(defn- client-item-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [item-dex client-props item-props]
  [:div.clients--client-item [item-editor/color-stamp :clients :client client-props]
                             [client-item-details item-dex client-props item-props]])

(defn client-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [item-dex {:keys [id] :as client-props}]
  (let [item-props (a/subscribe [:clients.client-lister/get-client-item-props item-dex client-props])]
       (fn [] [client-item-structure item-dex client-props @item-props])))



;; -- View components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id]
  [item-lister/view :clients :client {:list-element #'client-item
                                      :item-actions [:delete :duplicate]}])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :clients.client-lister/render-lister!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:ui/set-surface! :clients.client-lister/view {:view #'view}])
