
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-handler.effects
    (:require [app-plugins.item-handler.engine :as engine]
              [app-plugins.item-handler.views  :as views]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-handler/render-items-deleted-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (strings in vector) item-ids
  (fn [_ [_ extension-id item-namespace item-ids]]
      [:ui/blow-bubble! (engine/dialog-id extension-id item-namespace :items-deleted)
                        {:body       [views/items-deleted-dialog-body   extension-id item-namespace item-ids]
                         :destructor [:item-handler/clean-backup-items! extension-id item-namespace item-ids]}]))

(a/reg-event-fx
  :item-handler/render-items-duplicated-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (strings in vector) copy-ids
  (fn [_ [_ extension-id item-namespace copy-ids]]
      [:ui/blow-bubble! (engine/dialog-id extension-id item-namespace :items-duplicated)
                        {:body [views/items-duplicated-dialog-body extension-id item-namespace copy-ids]}]))
