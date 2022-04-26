
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-viewer.update.views
    (:require [x.app-core.api :as a]
              [x.app-ui.api   :as ui]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-deleted-dialog-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ; @param (string) item-id
  [viewer-id item-id]
  (let [undo-event [:item-viewer/undo-delete-item! viewer-id item-id]]
       [ui/state-changed-bubble-body :plugins.item-viewer/item-deleted-dialog
                                     {:label :item-deleted
                                      :primary-button {:on-click undo-event :label :recover!}}]))

(defn item-duplicated-dialog-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ; @param (string) copy-id
  [viewer-id copy-id]
  (let [edit-event [:item-viewer/edit-item! viewer-id copy-id]]
       [ui/state-changed-bubble-body :plugins.item-viewer/item-duplicated-dialog
                                     {:label :item-duplicated
                                      :primary-button {:on-click edit-event :label :edit-copy!}}]))
