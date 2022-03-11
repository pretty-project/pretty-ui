
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-browser.dialogs
    (:require [mid-fruits.candy :refer [param return]]
              [x.app-core.api   :as a :refer [r]]
              [x.app-ui.api     :as ui]))



;; -- Components --------------------------------------------------------------
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



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-browser/render-item-deleted-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  (fn [_ [_ extension-id item-namespace item-id]]
      [:ui/blow-bubble! :plugins.item-browser/item-deleted-dialog
                        {:body       [item-deleted-dialog-body        extension-id item-namespace item-id]}]))
                         ;:destructor [:item-browser/clean-backup-items! extension-id item-namespace item-id]}]))

(a/reg-event-fx
  :item-browser/render-item-duplicated-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) copy-id
  (fn [_ [_ extension-id item-namespace copy-id]]
      [:ui/blow-bubble! :plugins.item-browser/item-duplicated-dialog
                        {:body [item-duplicated-dialog-body extension-id item-namespace copy-id]}]))
