
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
  ; @param (keyword) editor-id
  ; @param (string) item-id
  [editor-id item-id]
  (let [undo-event [:item-editor/undo-delete-item! editor-id item-id]]
       [ui/state-changed-bubble-body :plugins.item-editor/item-deleted-dialog
                                     {:label          :item-deleted
                                      :primary-button {:label :recover! :on-click undo-event}}]))

(defn changes-discarded-dialog-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (string) item-id
  [editor-id item-id]
  (let [undo-event [:item-editor/undo-discard-changes! editor-id item-id]]
       [ui/state-changed-bubble-body :plugins.item-editor/changes-discarded-dialog
                                     {:label          :unsaved-changes-discarded
                                      :primary-button {:label :restore! :on-click undo-event}}]))

(defn item-duplicated-dialog-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (string) copy-id
  [editor-id copy-id]
  (let [edit-event [:item-editor/edit-item! editor-id copy-id]]
       [ui/state-changed-bubble-body :plugins.item-editor/item-duplicated-dialog
                                     {:label          :item-duplicated
                                      :primary-button {:label :edit-copy! :on-click edit-event}}]))
