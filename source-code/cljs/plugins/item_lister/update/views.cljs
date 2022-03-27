
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.update.views
    (:require [x.app-ui.api :as ui]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn items-deleted-dialog-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (strings in vector) item-ids
  [lister-id item-ids]
  (let [undo-event [:item-lister/undo-delete-items! lister-id item-ids]]
       [ui/state-changed-bubble-body :plugins.item-lister/items-deleted-dialog
                                     {:label {:content :n-items-deleted :replacements [(count item-ids)]}
                                      :primary-button {:on-click undo-event :label :recover!}}]))

(defn items-duplicated-dialog-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (strings in vector) copy-ids
  [lister-id copy-ids]
  (let [undo-event [:item-lister/undo-duplicate-items! lister-id copy-ids]]
       [ui/state-changed-bubble-body :plugins.item-lister/items-duplicated-dialog
                                     {:label {:content :n-items-duplicated :replacements [(count copy-ids)]}
                                      :primary-button {:on-click undo-event :label :undo!}}]))
