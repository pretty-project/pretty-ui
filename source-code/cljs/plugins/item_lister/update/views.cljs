

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.update.views
    (:require [x.app-ui.api :as ui]))



;; -- Delete items components -------------------------------------------------
;; ----------------------------------------------------------------------------

(defn items-deleted-dialog-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (strings in vector) item-ids
  [lister-id item-ids]
  (let [undo-event [:item-lister/undo-delete-items! lister-id item-ids]]
       [ui/state-changed-bubble-body :plugins.item-lister/items-deleted-dialog
                                     {:label          {:content :n-items-deleted :replacements [(count item-ids)]}
                                      :primary-button {:label :recover! :on-click undo-event}}]))



;; -- Duplicate items components ----------------------------------------------
;; ----------------------------------------------------------------------------

(defn items-duplicated-dialog-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (strings in vector) copy-ids
  [lister-id copy-ids]
  ; XXX#7002
  [ui/state-changed-bubble-body :plugins.item-lister/items-duplicated-dialog
                                {:label {:content :n-items-duplicated :replacements [(count copy-ids)]}}])
