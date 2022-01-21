
(ns app-extensions.clients.client-lister.events
    (:require [x.app-core.api :as a :refer [r]]
              [app-plugins.item-editor.api :as item-editor]))



;; -- Status events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ; WARNING! NON-PUBLIC! DO NOT USE!
  :clients/->client-item-clicked
  (fn [_ [_ _ {:keys [id]}]]
      (let [client-uri (item-editor/editor-uri :clients :client id)]
           [:router/go-to! client-uri])))
