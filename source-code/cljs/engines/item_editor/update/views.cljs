
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-editor.update.views
    (:require [re-frame.api :as r]
              [x.app-ui.api :as x.ui]))



;; -- Restore discarded changes components ------------------------------------
;; ----------------------------------------------------------------------------

(defn changes-discarded-dialog-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (string) item-id
  [editor-id item-id]
  (let [undo-event [:item-editor/undo-discard-changes! editor-id item-id]]
       [x.ui/state-changed-bubble-body :engines.item-editor/changes-discarded-dialog
                                       {:label          :unsaved-changes-discarded
                                        :primary-button {:label :restore! :on-click undo-event}}]))