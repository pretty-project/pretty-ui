
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.17
; Description:
; Version: v0.7.0
; Compatibility: x4.5.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.value-editor.engine)



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn default-edit-path
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) editor-id
  ;
  ; @return (item-path vector)
  [extension-id editor-id]
  [extension-id :value-editor/data-items editor-id])

(defn popup-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) editor-id
  ;
  ; (engine/popup-id :my-extension :my-editor)
  ; =>
  ; :my-extension/my-editor
  ;
  ; @return (keyword)
  [extension-id editor-id]
  (keyword (name extension-id)
           (name editor-id)))
