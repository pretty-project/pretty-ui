
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.file-editor.transfer.effects
    (:require [plugins.plugin-handler.transfer.effects]
              [re-frame.api :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx
  :file-editor/reg-transfer-editor-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  (fn [_ [_ editor-id editor-props]]
      [:plugin-handler/reg-transfer-plugin-props! editor-id editor-props]))
