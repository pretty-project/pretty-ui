
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-browser.update.views
    (:require [x.app-ui.api :as x.ui]))



;; -- Delete item components --------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-deleted-dialog-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (string) item-id
  [browser-id item-id]
  (let [undo-event [:item-browser/undo-delete-item! browser-id item-id]]
       [x.ui/state-changed-bubble-body :engines.item-browser/item-deleted-dialog
                                       {:label          :item-deleted
                                        :primary-button {:label :recover! :on-click undo-event}}]))



;; -- Duplicate item components -----------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-duplicated-dialog-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (string) copy-id
  [browser-id _]
  ; XXX#7002
  [x.ui/state-changed-bubble-body :engines.item-browser/item-duplicated-dialog
                                  {:label :item-duplicated}])
