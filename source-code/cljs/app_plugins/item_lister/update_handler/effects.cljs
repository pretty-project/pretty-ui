
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-lister.update-handler.effects
    (:require [app-plugins.item-lister.update-handler.views :as update-handler.views]
              [x.app-core.api                               :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/render-items-deleted-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (strings in vector) item-ids
  (fn [_ [_ extension-id item-namespace item-ids]]
      [:ui/blow-bubble! :plugins.item-lister/items-deleted-dialog
                        {:body       [update-handler.views/items-deleted-dialog-body extension-id item-namespace item-ids]
                         :destructor [:item-lister/clean-backup-items!               extension-id item-namespace item-ids]}]))

(a/reg-event-fx
  :item-lister/render-items-duplicated-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (strings in vector) copy-ids
  (fn [_ [_ extension-id item-namespace copy-ids]]
      [:ui/blow-bubble! :plugins.item-lister/items-duplicated-dialog
                        {:body [update-handler.views/items-duplicated-dialog-body extension-id item-namespace copy-ids]}]))