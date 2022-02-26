
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-extensions.clients.client-lister.effects
    (:require [x.app-core.api              :as a :refer [r]]
              [app-plugins.item-editor.api :as item-editor]
              [app-plugins.item-lister.api :as item-lister]
              [app-extensions.clients.client-lister.views :as client-lister.views]))



;; -- Effect events -----------------------------------------------------------
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
          (let [client-uri (item-editor/editor-uri :clients :client id)]
               [:router/go-to! client-uri]))))

(a/reg-event-fx
  :clients.client-lister/render-lister!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:ui/set-surface! :clients.client-lister/view {:view #'client-lister.views/view}])
