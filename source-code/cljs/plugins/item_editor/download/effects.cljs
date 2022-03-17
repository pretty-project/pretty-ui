
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.download.effects
    (:require [plugins.item-editor.backup.events       :as backup.events]
              [plugins.item-editor.core.subs           :as core.subs]
              [plugins.item-editor.download.queries    :as download.queries]
              [plugins.item-editor.download.validators :as download.validators]
              [x.app-core.api                          :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-editor/request-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      (let [query        (r download.queries/get-request-item-query          db extension-id item-namespace)
            validator-f #(r download.validators/request-item-response-valid? db extension-id item-namespace %)]
           [:sync/send-query! (r core.subs/get-request-id db extension-id item-namespace)
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
      (if (r core.subs/get-meta-item db extension-id item-namespace :recovery-mode?)
          {:db (r backup.events/recover-item! db extension-id item-namespace)
           :dispatch [:ui/simulate-process!]}
          {:dispatch [:ui/simulate-process!]})))
