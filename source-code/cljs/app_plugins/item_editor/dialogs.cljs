
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-editor.dialogs
    (:require [x.app-core.api     :as a :refer [r]]
              [x.app-elements.api :as elements]
              [x.app-ui.api       :as ui]
              [app-plugins.item-editor.engine :as engine]
              [app-plugins.item-editor.subs   :as subs]))



;; -- Bubble components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn undo-delete-dialog-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  [extension-id item-namespace item-id]
  (let [undo-event [:item-editor/undo-delete! extension-id item-namespace item-id]]
       [ui/state-changed-bubble-body (engine/dialog-id extension-id item-namespace :item-deleted)
                                     {:label :item-deleted
                                      :primary-button {:on-click undo-event :label :recover!}}]))

(defn changes-discarded-dialog-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  [extension-id item-namespace item-id]
  (let [undo-event [:item-editor/undo-discard! extension-id item-namespace item-id]]
       [ui/state-changed-bubble-body (engine/dialog-id extension-id item-namespace :changes-discarded)
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
       [ui/state-changed-bubble-body (engine/dialog-id extension-id item-namespace :item-duplicated)
                                     {:label :item-duplicated
                                      :primary-button {:on-click edit-event :label :edit-copy!}}]))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-editor/render-undo-delete-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      (let [current-item-id (r subs/get-current-item-id db extension-id item-namespace)]
           [:ui/blow-bubble! (engine/dialog-id extension-id item-namespace :item-deleted)
                             {:body       [undo-delete-dialog-body           extension-id item-namespace current-item-id]
                              :destructor [:item-editor/clean-recovery-data! extension-id item-namespace current-item-id]}])))

(a/reg-event-fx
  :item-editor/render-changes-discarded-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      (let [current-item-id (r subs/get-current-item-id db extension-id item-namespace)]
           [:ui/blow-bubble! (engine/dialog-id extension-id item-namespace :changes-discarded)
                             {:body       [changes-discarded-dialog-body     extension-id item-namespace current-item-id]
                              :destructor [:item-editor/clean-recovery-data! extension-id item-namespace current-item-id]}])))

(a/reg-event-fx
  :item-editor/render-edit-copy-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) copy-id
  (fn [_ [_ extension-id item-namespace copy-id]]
      [:ui/blow-bubble! (engine/dialog-id extension-id item-namespace :item-duplicated)
                        {:body [item-duplicated-dialog-body extension-id item-namespace copy-id]}]))
