
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.23
; Description:
; Version: v0.3.6
; Compatibility: x4.5.4



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-plugins.item-browser.engine
    (:require [mid-fruits.candy   :refer [param return]]
              [mid-fruits.keyword :as keyword]))



;; -- Description -------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @description
;  Az elnevézesekben az item-namespace értéke helyettesíti az "item" szót.
;  Pl.: :my-extension/synchronize-item-browser! => :my-extension/synchronize-my-type-browser!
;  Így biztosítható, hogy egy névtér több különböző item-browser böngészőt tudjon megvalósítani.



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn browser-uri
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @example
  ;  (item-browser/browser-uri :my-extension :my-type "my-item")
  ;  =>
  ;  "/@app-home/my-extension/my-item"
  ;
  ; @return (string)
  [extension-id _ item-id]
  (str "/@app-home/" (name  extension-id)
       "/"           (param item-id)))

(defn request-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (engine/request-id :my-extension :my-type)
  ;  =>
  ;  :my-extension/synchronize-my-type-browser!
  ;
  ; @return (keyword)
  [extension-id item-namespace]
  (keyword (name extension-id)
           (str "synchronize-" (name item-namespace) "-browser!")))

(defn resolver-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (engine/resolver-id :my-extension :my-type)
  ;  =>
  ;  :my-extension/get-my-type-items
  ;
  ; @return (keyword)
  [extension-id item-namespace]
  (keyword (name extension-id)
           (str "get-" (name item-namespace) "-items")))

(defn collection-name
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @example
  ;  (engine/collection-name :my-extension)
  ;  =>
  ;  "my-extension"
  ;
  ; @return (string)
  [extension-id]
  (name extension-id))

(defn route-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (engine/route-id :my-extension :my-type)
  ;  =>
  ;  :my-extension/my-type-browser-route
  ;
  ; @return (keyword)
  [extension-id item-namespace]
  (keyword (name extension-id)
           (str (name item-namespace) "-browser-route")))

(defn extended-route-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (engine/extended-route-id :my-extension :my-type)
  ;  =>
  ;  :my-extension/my-type-browser-extended-route
  ;
  ; @return (keyword)
  [extension-id item-namespace]
  (keyword (name extension-id)
           (str (name item-namespace) "-browser-extended-route")))

(defn route-template
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @example
  ;  (engine/route-template :my-extension)
  ;  =>
  ;  "/@app-home/my-extension"
  ;
  ; @return (string)
  [extension-id]
  (str "/@app-home/" (name extension-id)))

(defn extended-route-template
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @example
  ;  (engine/extended-route-template :my-extension)
  ;  =>
  ;  "/@app-home/my-extension/:item-id"
  ;
  ; @return (string)
  [extension-id]
  (str "/@app-home/" (name extension-id)
       "/:item-id"))

(defn go-up-event
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @example
  ;  (engine/go-up-event :my-extension)
  ;  =>
  ;  [:my-extension/go-up!]
  ;
  ; @return (keyword)
  [extension-id]
  (let [event-id (keyword/add-namespace extension-id :go-up!)]
       [event-id]))

(defn go-home-event
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @example
  ;  (item-browser/go-home-event :my-extension)
  ;  =>
  ;  [:my-extension/go-home!]
  ;
  ; @return (keyword)
  [extension-id]
  (let [event-id (keyword/add-namespace extension-id :go-home!)]
       [event-id]))

(defn load-extension-event
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (engine/load-extension-event :my-extension :my-type)
  ;  =>
  ;  [:my-extension/load-my-type-browser!]
  ;
  ; @return (event-vector)
  [extension-id item-namespace]
  (let [event-id (keyword (name extension-id)
                          (str "load-" (name item-namespace) "-browser!"))]
       [event-id]))
