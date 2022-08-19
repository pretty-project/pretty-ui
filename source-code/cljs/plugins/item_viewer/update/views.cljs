

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-viewer.update.views
    (:require [x.app-core.api :as a]
              [x.app-ui.api   :as ui]))



;; -- Delete item components --------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-deleted-dialog-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ; @param (string) item-id
  [viewer-id item-id]
  (let [undo-event [:item-viewer/undo-delete-item! viewer-id item-id]]
       [ui/state-changed-bubble-body :plugins.item-viewer/item-deleted-dialog
                                     {:label          :item-deleted
                                      :primary-button {:label :recover! :on-click undo-event}}]))



;; -- Undo delete item components ---------------------------------------------
;; ----------------------------------------------------------------------------

(defn undo-delete-item-failed-dialog-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ; @param (string) item-id
  [viewer-id item-id]
  (let [retry-event [:item-viewer/undo-delete-item! viewer-id item-id]]
       [ui/state-changed-bubble-body :plugins.item-viewer/undo-delete-item-failed-dialog
                                     {:label          :failed-to-undo-delete
                                      :primary-button {:label :retry! :on-click retry-event}}]))



;; -- Duplicate item components -----------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-duplicated-dialog-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ; @param (string) copy-id
  [viewer-id copy-id]
  (let [view-event [:item-viewer/view-duplicated-item! viewer-id copy-id]]
       [ui/state-changed-bubble-body :plugins.item-viewer/item-duplicated-dialog
                                     {:label          :item-duplicated
                                      :primary-button {:label :view-copy! :on-click view-event}}]))
