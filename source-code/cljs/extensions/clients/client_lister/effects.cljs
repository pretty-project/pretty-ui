
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.clients.client-lister.effects
    (:require [extensions.clients.client-lister.views :as client-lister.views]
              [plugins.item-editor.api                :as item-editor]
              [plugins.item-lister.api                :as item-lister]
              [x.app-core.api                         :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :clients.client-lister/load-lister!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:clients.client-lister/render-lister!])

(a/reg-event-fx
  ; WARNING! NON-PUBLIC! DO NOT USE!
  :clients.client-lister/item-clicked
  (fn [{:keys [db]} [_ item-dex {:keys [id]}]]
      (if (r item-lister/toggle-item-selection? db :clients :client item-dex)
          {:db (r item-lister/toggle-item-selection! db :clients :client item-dex)}
          (let [item-route (r item-editor/get-item-route db :clients :client id)]
               [:router/go-to! item-route]))))

(a/reg-event-fx
  :clients.client-lister/render-lister!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:ui/set-surface! :clients.client-lister/view
                    {:view #'client-lister.views/view}])
