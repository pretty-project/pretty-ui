
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.23
; Description:
; Version: v0.2.8
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-plugins.item-browser.engine
    (:require [mid-fruits.keyword :as keyword]))



;; -- Description -------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @description
;  Az elnevézesekben az item-namespace értéke helyettesíti az "item" szót.
;  Pl.: :my-extension/synchronize-item-browser! => :my-extension/synchronize-my-type-browser!
;  Így biztosítható, hogy egy névtér több különböző item-browser böngészőt tudjon megvalósítani.



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request-id
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (item-browser/request-id :my-extension :my-type)
  ;  =>
  ;  :my-extension/synchronize-my-type-browser!
  ;
  ; @return (keyword)
  [extension-id item-namespace]
  (keyword (name extension-id)
           (str "synchronize-" (name item-namespace) "-browser!")))

(defn route-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (item-browser/route-id :my-extension :my-type)
  ;  =>
  ;  :my-extension/my-type-browser-route
  ;
  ; @return (keyword)
  [extension-id item-namespace]
  (keyword (name extension-id)
           (str (name item-namespace) "-browser-route")))

(defn extended-route-id
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (item-editor/extended-route-id :my-extension :my-type)
  ;  =>
  ;  :my-extension/my-type-browser-extended-route
  ;
  ; @return (keyword)
  [extension-id item-namespace]
  (keyword (name extension-id)
           (str (name item-namespace) "-browser-extended-route")))

(defn route-template
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (item-browser/route-template :my-extension :my-type)
  ;  =>
  ;  "/my-extension"
  ;
  ; @return (string)
  [extension-id _]
  (str "/" (name extension-id)))

(defn extended-route-template
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (item-browser/extended-route-template :my-extension :my-type)
  ;  =>
  ;  "/my-extension/:my-type-id"
  ;
  ; @return (string)
  [extension-id item-namespace]
  (str "/"  (name extension-id)
       "/:" (name item-namespace) "-id"))

(defn go-up-event
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (item-browser/go-up-event :my-extension :my-type)
  ;  =>
  ;  [:my-extension/go-up!]
  ;
  ; @return (keyword)
  [extension-id _]
  (let [event-id (keyword/add-namespace extension-id :go-up!)]
       [event-id]))

(defn go-home-event
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (item-browser/go-home-event :my-extension :my-type)
  ;  =>
  ;  [:my-extension/go-home!]
  ;
  ; @return (keyword)
  [extension-id _]
  (let [event-id (keyword/add-namespace extension-id :go-home!)]
       [event-id]))

(defn render-event
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (item-browser/render-event :my-extension :my-type)
  ;  =>
  ;  [:my-extension/render-my-type-browser!]
  ;
  ; @return (event-vector)
  [extension-id item-namespace]
  (let [event-id (keyword (name extension-id)
                          (str "render-" (name item-namespace) "-browser!"))]
       [event-id]))
