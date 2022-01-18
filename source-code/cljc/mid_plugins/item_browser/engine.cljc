
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.23
; Description:
; Version: v0.3.8
; Compatibility: x4.5.4



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-plugins.item-browser.engine
    (:require [mid-fruits.candy   :refer [param return]]
              [mid-fruits.keyword :as keyword]
              [mid-fruits.vector  :as vector]))



;; -- Description -------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @description
; - Az elnevézesekben az item-namespace értéke helyettesíti az "item" szót.
;   Pl.: :my-extension/synchronize-item-browser! => :my-extension/synchronize-my-type-browser!
;   Így biztosítható, hogy egy névtér több különböző item-browser böngészőt tudjon megvalósítani.
; - Ha szükséges, akkor a Re-Frame adatbázis útvonalakban is be kell vezetni a megkülönbözetést,
;   hogy egy extension több böngészőt alkalmazhasson.



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (keywords in vector)
; A browser-props térkép mely kulcsai nem az item-lister plugin beállításához szükségesek:
(def BROWSER-PROPS-KEYS [:default-item-id :label-key :path-key])



;; -- Public helpers ----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn browser-uri
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string)(opt) item-id
  ;
  ; @example
  ;  (item-browser/browser-uri :my-extension :my-type "my-item")
  ;  =>
  ;  "/@app-home/my-extension/my-item"
  ;
  ; @return (string)
  ([extension-id _]
   (str "/@app-home/" (name extension-id)))

  ([extension-id _ item-id]
   (str "/@app-home/" (name  extension-id)
        "/"           (param item-id))))

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



;; -- Private helpers ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn data-item-path
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (list of *) xyz
  ;
  ; @example
  ;  (engine/data-item-path :my-extension :my-type :my-value)
  ;  =>
  ;  [:my-extension :my-type-browser/data-items :my-value]
  ;
  ; @return (item-path vector)
  [extension-id item-namespace & xyz]
  (let [data-items-key (keyword (str (name item-namespace) "-browser/data-items"))]
       (vector/concat-items [extension-id data-items-key] xyz)))

(defn meta-item-path
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (list of *) xyz
  ;
  ; @example
  ;  (engine/meta-item-path :my-extension :my-type :my-value)
  ;  =>
  ;  [:my-extension :my-type-browser/meta-items :my-value]
  ;
  ; @return (item-path vector)
  [extension-id item-namespace & xyz]
  (let [meta-items-key (keyword (str (name item-namespace) "-browser/meta-items"))]
       (vector/concat-items [extension-id meta-items-key] xyz)))



(defn resolver-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (engine/resolver-id :my-extension :my-type)
  ;  =>
  ;  :my-extension/get-my-type-item
  ;
  ; @return (keyword)
  [extension-id item-namespace]
  (keyword (name extension-id)
           (str "get-" (name item-namespace) "-item")))

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
