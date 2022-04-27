
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.update.views
    (:require [x.app-ui.api :as ui]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-deleted-dialog-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (string) item-id
  [browser-id item-id]
  (let [undo-event [:item-browser/undo-delete-item! browser-id item-id]]
       [ui/state-changed-bubble-body :plugins.item-browser/item-deleted-dialog
                                     {:label          :item-deleted
                                      :primary-button {:label :recover! :on-click undo-event}}]))

(defn item-duplicated-dialog-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (string) copy-id
  [browser-id _]
  ; XXX#7002
  [ui/state-changed-bubble-body :plugins.item-browser/item-duplicated-dialog
                                {:label :item-duplicated}])
