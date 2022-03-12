
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-plugins.item-editor.route-handler.effects
    (:require [server-plugins.item-editor.route-handler.engine :as route-handler.engine]
              [x.server-core.api                               :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-editor/add-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) editor-props
  ;  {:route-template (string)}
  (fn [_ [_ extension-id item-namespace {:keys [route-template]}]]
      [:router/add-route! (route-handler.engine/route-id extension-id item-namespace)
                          {:client-event   [:item-editor/load-editor! extension-id item-namespace]
                           :route-template route-template
                           :restricted?    true}]))
