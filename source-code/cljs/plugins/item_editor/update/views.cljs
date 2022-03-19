
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.update.views
    (:require [x.app-core.api :as a]
              [x.app-ui.api   :as ui]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-deleted-dialog-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  [extension-id item-namespace item-id]
  (let [undo-event [:item-editor/undo-delete-item! extension-id item-namespace item-id]]
       [ui/state-changed-bubble-body :plugins.item-editor/item-deleted-dialog
                                     {:label :item-deleted
                                      :primary-button {:on-click undo-event :label :recover!}}]))

(defn changes-discarded-dialog-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  [extension-id item-namespace item-id]
  (let [undo-event [:item-editor/undo-discard-changes! extension-id item-namespace item-id]]
       [ui/state-changed-bubble-body :plugins.item-editor/changes-discarded-dialog
                                     {:label :unsaved-changes-discarded
                                      :primary-button {:on-click undo-event :label :restore!}}]))

(defn item-duplicated-dialog-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) copy-id
  [extension-id item-namespace copy-id]
  (let [edit-event [:item-editor/edit-item! extension-id item-namespace copy-id]]
       [ui/state-changed-bubble-body :plugins.item-editor/item-duplicated-dialog
                                     {:label :item-duplicated
                                      :primary-button {:on-click edit-event :label :edit-copy!}}]))
