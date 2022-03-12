
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.update-handler.views
    (:require [x.app-ui.api :as ui]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-deleted-dialog-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  [extension-id item-namespace item-id]
  (let [undo-event [:item-browser/undo-delete-item! extension-id item-namespace item-id]]
       [ui/state-changed-bubble-body :plugins.item-browser/item-deleted-dialog
                                     {:label :item-deleted :primary-button {:on-click undo-event :label :recover!}}]))

(defn item-duplicated-dialog-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) copy-id
  [extension-id item-namespace copy-id]
  (let [undo-event [:item-browser/undo-duplicate-item! extension-id item-namespace copy-id]]
       [ui/state-changed-bubble-body :plugins.item-browser/item-duplicated-dialog
                                     {:label :item-duplicated :primary-button {:on-click undo-event :label :undo!}}]))
