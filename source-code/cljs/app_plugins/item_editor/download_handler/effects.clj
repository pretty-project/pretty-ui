
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-editor.download-handler.effects
    (:require [app-plugins.item-editor.editor-handler.subs :as editor-handler.subs]
              [x.app-core.api                              :as a]
              [x.app-ui.api                                :as ui]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-editor/request-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      (let [request-id   (r editor-handler.subs/get-request-id                       db extension-id item-namespace)
            query        (r download-handler.queries/get-request-item-query          db extension-id item-namespace)
            validator-f #(r download-handler.validators/request-item-response-valid? db extension-id item-namespace %)]
           [:sync/send-query! request-id
                              {:display-progress? true
                               ; XXX#4057
                               ; Az on-stalled időzítéssel a UI változásai egyszerre történnek
                               ; meg a lekérés okozta {:editor-disabled? true} állapot megszűnésével
                               :on-stalled [:item-editor/receive-item!   extension-id item-namespace]
                               :on-failure [:item-editor/set-error-mode! extension-id item-namespace]
                               :query query :validator-f validator-f}])))

(a/reg-event-fx
  :item-editor/load-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      (if (r subs/get-meta-item db extension-id item-namespace :recovery-mode?)
          {:db (r backup-handler.events/recover-item! db extension-id item-namespace)
           :dispatch [:ui/simulate-process!]}
          {:dispatch [:ui/simulate-process!]})))
