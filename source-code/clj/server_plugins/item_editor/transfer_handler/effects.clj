
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-plugins.item-editor.transfer-handler.effects
    (:require [mid-fruits.candy                                   :refer [param return]]
              [server-plugins.item-editor.transfer-handler.engine :as transfer-handler.engine]
              [x.server-core.api                                  :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-editor/reg-transfer-editor-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) editor-props
  (fn [_ [_ extension-id item-namespace editor-props]]
      {:fx [:core/reg-transfer! (transfer-handler.engine/transfer-id extension-id item-namespace)
                                {:data-f      (fn [_] (return editor-props))
                                 :target-path [extension-id :item-editor/meta-items]}]}))
