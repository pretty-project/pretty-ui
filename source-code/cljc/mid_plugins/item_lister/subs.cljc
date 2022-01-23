
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2022.01.22
; Description:
; Version: v0.3.6
; Compatibility: x4.5.5



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-plugins.item-lister.subs
    (:require [x.mid-core.api :as a]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-meta-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (keyword) item-key
  ;
  ; @return (*)
  [db [_ extension-id item-namespace item-key]]
  (get-in db [extension-id :item-lister/meta-items item-key]))

(a/reg-sub :item-lister/get-meta-item get-meta-item)

(defn get-lister-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (map)
  [db [_ extension-id]]
  (get-in db [extension-id :item-lister/meta-items]))