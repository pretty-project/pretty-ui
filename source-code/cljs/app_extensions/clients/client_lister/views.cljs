
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-extensions.clients.client-lister.views
    (:require [x.app-core.api    :as a]
              [x.app-locales.api :as locales]
              [app-plugins.item-editor.api :as item-editor]
              [app-plugins.item-lister.api :as item-lister]))



;; -- List-item components ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- client-item-secondary-details
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [modified-at]}]
  (let [modified-at @(a/subscribe [:activities/get-actual-timestamp modified-at])]
       [:div.clients--client-item--secondary-details [:div.clients--client-item--modified-at modified-at]]))

(defn- client-item-primary-details
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [email-address first-name last-name] :as client-item}]
  (let [selected-language @(a/subscribe [:locales/get-selected-language])
        client-name        (locales/name->ordered-name first-name last-name selected-language)]
       [:div.clients--client-item--primary-details [:div.clients--client-item--full-name     client-name]
                                                   [:div.clients--client-item--email-address email-address]]))

(defn- client-item-details
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [client-item]
  [:div.clients--client-item--details [client-item-primary-details   client-item]
                                      [client-item-secondary-details client-item]])


(defn- client-item-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [client-item]
  [:div.clients--client-item [item-editor/color-stamp :clients :client client-item]
                             [client-item-details client-item]])

(defn client-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _ _ client-item]
  [elements/toggle {:on-click [:clients.client-lister/item-clicked client-item]
                    :content  [client-item-structure client-item]
                    :hover-color :highlight}])



;; -- View components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id]
  [item-lister/view :clients :client {:list-element #'client-item
                                      :item-actions [:delete :duplicate]
                                      :sortable? true}])
