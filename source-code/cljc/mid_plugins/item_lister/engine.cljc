
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



;; -- Description -------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @description
;  Az elnevézesekben az item-namespace értéke helyettesíti az "item" szót.
;  Pl.: :my-extension/synchronize-item-lister! => :my-extension/synchronize-my-type-lister!
;  Így biztosítható, hogy egy névtér több különböző item-lister listázót tudjon megvalósítani.



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request-id
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (item-lister/request-id :my-extension :my-type)
  ;  =>
  ;  :my-extension/synchronize-my-type-lister!
  ;
  ; @return (keyword)
  [extension-id item-namespace]
  (keyword (name extension-id)
           (str "synchronize-" (name item-namespace) "-lister!")))

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

(defn route-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (item-lister/route-id :my-extension :my-type)
  ;  =>
  ;  :my-extension/lister-route
  ;
  ; @return (keyword)
  [extension-id item-namespace]
  (keyword (name extension-id)
           (str (name item-namespace) "-lister-route")))

(defn add-new-item-event
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (item-lister/add-new-item-event :my-extension :my-type)
  ;  =>
  ;  [:my-extension/add-new-my-type!]
  ;
  ; @return (event-vector)
  [extension-id item-namespace]
  (let [event-id (keyword (name extension-id)
                          (str "add-new-" (name item-namespace) "!"))]
       [event-id]))

(defn render-event
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (item-lister/render-event :my-extension :my-type)
  ;  =>
  ;  [:my-extension/render-my-type-lister!]
  ;
  ; @return (event-vector)
  [extension-id item-namespace]
  (let [event-id (keyword (name extension-id)
                          (str "render-" (name item-namespace) "-lister!"))]
       [event-id]))
