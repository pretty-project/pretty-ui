
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.12.18
; Description:
; Version: v0.6.8
; Compatibility: x4.6.0



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-plugins.item-editor.events
    (:require [x.server-core.api :as a :refer [r]]
              [mid-plugins.item-editor.events :as events]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid-plugins.item-editor.events
(def store-editor-props! events/store-editor-props!)




;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn initialize-editor!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map)(opt) editor-props
  ;
  ; @return (map)
  [db [_ extension-id item-namespace editor-props]]
  (r store-editor-props! db extension-id item-namespace editor-props))
