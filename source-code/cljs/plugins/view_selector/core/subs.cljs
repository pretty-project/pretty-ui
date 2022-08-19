

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.view-selector.core.subs
    (:require [plugins.plugin-handler.core.subs :as core.subs]
              [x.app-core.api                   :as a]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.core.subs
(def get-meta-item       core.subs/get-meta-item)
(def get-current-view-id core.subs/get-current-view-id)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) selector-id
; @param (keyword) item-key
;
; @usage
;  [:view-selector/get-meta-item :my-selector :my-item]
(a/reg-sub :view-selector/get-meta-item get-meta-item)

; @param (keyword) selector-id
;
; @usage
;  [:view-selector/get-current-view-id :my-selector]
(a/reg-sub :view-selector/get-current-view-id get-current-view-id)
