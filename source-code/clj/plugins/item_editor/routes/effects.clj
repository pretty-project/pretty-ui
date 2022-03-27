
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.routes.effects
    (:require [plugins.plugin-handler.routes.effects]
              [x.server-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-editor/add-extended-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  ;  {:extended-route (string)}
  (fn [_ [_ editor-id {:keys [extended-route]}]]
      [:plugin-handler/add-extended-route! editor-id
                                           {:client-event [:item-editor/handle-route! editor-id]
                                            :extended-route extended-route}]))
