
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.23
; Description:
; Version: v0.2.4
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-plugins.item-lister.engine
    (:require [mid-fruits.keyword :as keyword]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def extension-namespace
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (item-lister/extension-namespace :my-extension :my-type)
  ;  =>
  ;  :my-type-lister
  ;
  ; @return (keyword)
  [_ item-namespace]
  (keyword/join item-namespace "-lister"))

(defn request-id
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (item-lister/request-id :my-extension :my-type)
  ;  =>
  ;  :my-type-lister/synchronize!
  ;
  ; @return (keyword)
  [extension-id item-namespace]
  (let [extension-namespace (extension-namespace extension-id item-namespace)]
       (keyword/add-namespace extension-namespace :synchronize!)))

(defn resolver-id
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (item-lister/resolver-id :my-extension :my-type)
  ;  =>
  ;  :my-extension/get-my-type-items!
  ;
  ; @return (keyword)
  [extension-id item-namespace]
  (keyword (name extension-id)
           (str "get-" (name item-namespace) "-items")))

(defn new-item-uri
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (item-lister/new-item-uri :my-extension :my-type)
  ;  =>
  ;  "/my-extension/new-my-type"
  ;
  ; @return (string)
  [extension-id item-namespace]
  (str "/"     (name extension-id)
       "/new-" (name item-namespace)))

(defn add-new-item-event-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (item-lister/add-new-item-event-id :my-extension :my-type)
  ;  =>
  ;  :my-extension/add-new-item!
  ;
  ; @return (keyword)
  [extension-id _]
  (keyword/add-namespace extension-id :add-new-item!))

(defn route-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (item-lister/route-id :my-extension :my-type)
  ;  =>
  ;  :my-extension/route
  ;
  ; @return (keyword)
  [extension-id _]
  (keyword/add-namespace extension-id :route))

(defn render-event-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (item-lister/render-event-id :my-extension :my-type)
  ;  =>
  ;  :my-extension/render-my-type-lister!
  ;
  ; @return (keyword)
  [extension-id item-namespace]
  (keyword (name extension-id)
           (str "render-" (name item-namespace) "-lister!")))
