
(ns app-extensions.clients.client-lister.effects
    (:require [x.app-core.api :as a :refer [r]]
              [app-plugins.item-editor.api :as item-editor]))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :clients.client-lister/load-lister!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:clients.client-lister/render-lister!])

(a/reg-event-fx
  ; WARNING! NON-PUBLIC! DO NOT USE!
  :clients.client-lister/item-clicked
  (fn [_ [_ _ {:keys [id]}]]
      (let [client-uri (item-editor/editor-uri :clients :client id)]
           [:router/go-to! client-uri])))
