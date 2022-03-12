
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-lister.update-handler.views
    (:require [x.app-ui.api :as ui]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn items-deleted-dialog-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (strings in vector) item-ids
  [extension-id item-namespace item-ids]
  (let [undo-event [:item-lister/undo-delete-items! extension-id item-namespace item-ids]]
       [ui/state-changed-bubble-body :plugins.item-lister/items-deleted-dialog
                                     {:label {:content :n-items-deleted :replacements [(count item-ids)]}
                                      :primary-button {:on-click undo-event :label :recover!}}]))

(defn items-duplicated-dialog-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (strings in vector) copy-ids
  [extension-id item-namespace copy-ids]
  (let [undo-event [:item-lister/undo-duplicate-items! extension-id item-namespace copy-ids]]
       [ui/state-changed-bubble-body :plugins.item-lister/items-duplicated-dialog
                                     {:label {:content :n-items-duplicated :replacements [(count copy-ids)]}
                                      :primary-button {:on-click undo-event :label :undo!}}]))