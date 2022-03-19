
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.core.effects
    (:require [plugins.item-editor.core.events   :as core.events]
              [plugins.item-editor.core.subs     :as core.subs]
              [plugins.item-editor.routes.subs   :as routes.subs]
              [plugins.item-editor.transfer.subs :as transfer.subs]
              [x.app-core.api                    :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-editor/edit-item!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @usage
  ;  [:item-editor/edit-item! :my-extension :my-type "my-item"]
  (fn [{:keys [db]} [_ extension-id item-namespace item-id]]
      (if-let [item-route (r routes.subs/get-item-route db extension-id item-namespace item-id)]
              {:db (r core.events/edit-item! db extension-id item-namespace item-id)
               :dispatch-n [[:router/go-to! item-route]
                            [:item-editor/request-item! extension-id item-namespace]]}
              {:db (r core.events/edit-item! db extension-id item-namespace item-id)
               :dispatch [:item-editor/request-item! extension-id item-namespace]})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  :item-editor/load-editor!
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      ; XXX#4579
      (let [on-route    (r transfer.subs/get-transfer-item db extension-id item-namespace :on-route)
            route-title (r transfer.subs/get-transfer-item db extension-id item-namespace :route-title)]
           {:db (r core.events/load-editor! db extension-id item-namespace)
            :dispatch-n [on-route (if (r core.subs/set-route-title? db extension-id item-namespace)
                                      [:ui/set-window-title! route-title])]})))
