
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.23
; Description:
; Version: v0.2.0
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-plugins.item-editor.api
    (:require [server-plugins.item-editor.engine :as engine]))



;; -- Names -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @name multi-view?
;  TODO ...



;; -- Usage -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  More info: app-plugins.item-editor.api
;
; @usage
;  (ns my-namespace (:require [server-plugins.item-editor.api :as item-editor]))
;
; @usage
;  (defresolver get-my-type-item [_ {:keys [my-type/id]}] ...)
;  (defmutation update-my-type-item!    [_] ...)
;  (defmutation add-my-type-item!       [_] ...)
;  (defmutation delete-my-type-item!    [_] ...)
;  (defmutation duplicate-my-type-item! [_] ...)



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; server-plugins.item-editor.engine
(def item-id->new-item?      engine/item-id->new-item?)
(def item-id->form-label     engine/item-id->form-label)
(def item-id->item-uri       engine/item-id->item-uri)
(def request-id              engine/request-id)
(def mutation-name           engine/mutation-name)
(def form-id                 engine/form-id)
