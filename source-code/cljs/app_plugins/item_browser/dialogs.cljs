
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-browser.dialogs
    (:require [app-plugins.item-browser.engine :as engine]
              [mid-fruits.candy                :refer [param return]]
              [x.app-core.api                  :as a :refer [r]]
              [x.app-ui.api                    :as ui]))



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
       [ui/state-changed-bubble-body (engine/dialog-id extension-id item-namespace :item-deleted)
                                     {:label :item-deleted :primary-button {:on-click undo-event :label :recover!}}]))

(defn item-duplicated-dialog-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) copy-id
  [extension-id item-namespace copy-id]
  (let [undo-event [:item-browser/undo-duplicate-item! extension-id item-namespace copy-id]]
       [ui/state-changed-bubble-body (engine/dialog-id extension-id item-namespace :item-duplicated)
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
      [:ui/blow-bubble! (engine/dialog-id extension-id item-namespace :item-deleted)
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
      [:ui/blow-bubble! (engine/dialog-id extension-id item-namespace :item-duplicated)
                        {:body [item-duplicated-dialog-body extension-id item-namespace copy-id]}]))
