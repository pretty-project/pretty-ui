
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.23
; Description:
; Version: v0.2.4
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-plugins.view-selector.engine
    (:require [mid-fruits.keyword :as keyword]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request-id
  ; @param (keyword) extension-id
  ;
  ; @example
  ;  (view-selector/request-id :my-extension)
  ;  =>
  ;  :my-extension/synchronize!
  ;
  ; @return (keyword)
  [extension-id]
  (keyword/add-namespace extension-id :synchronize!))

(defn render-event-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @example
  ;  (view-selector/render-event-id :my-extension)
  ;  =>
  ;  :my-extension/render!
  ;
  ; @return (keyword)
  [extension-id]
  (keywordadd-namespace extension-id :render!))
