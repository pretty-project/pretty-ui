
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.23
; Description:
; Version: v0.5.6
; Compatibility: x4.5.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-plugins.item-lister.engine
    (:require [mid-fruits.keyword :as keyword]
              [mid-fruits.vector  :as vector]))



;; -- Description -------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @description
; - Az elnevézesekben az item-namespace értéke helyettesíti az "item" szót.
;   Pl.: :my-extension.item-lister/synchronize-lister!
;        =>
;        :my-extension.my-type-lister/synchronize-lister!
;   Így biztosítható, hogy egy névtér több különböző item-lister listázót tudjon megvalósítani.
; - Ha szükséges, akkor a Re-Frame adatbázis útvonalakban is be kell vezetni a megkülönbözetést,
;   hogy egy extension több listázót alkalmazhasson.



;; -- Public helpers ----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request-id
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (item-lister/request-id :my-extension :my-type)
  ;  =>
  ;  :my-extension.my-type-lister/synchronize-lister!
  ;
  ; @return (keyword)
  [extension-id item-namespace]
  (keyword (str (name extension-id)   "."
                (name item-namespace) "-lister")
           "synchronize-lister!"))



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
  ;  [:my-extension :my-type-lister/data-items :my-value]
  ;
  ; @return (item-path vector)
  [extension-id item-namespace & xyz]
  (let [data-items-key (keyword (str (name item-namespace) "-lister/data-items"))]
       (vector/concat-items [extension-id data-items-key] xyz)))

(defn meta-item-path
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (list of *) xyz
  ;
  ; @example
  ;  (engine/meta-item-path :my-extension :my-type :my-value)
  ;  =>
  ;  [:my-extension :my-type-lister/meta-items :my-value]
  ;
  ; @return (item-path vector)
  [extension-id item-namespace & xyz]
  (let [meta-items-key (keyword (str (name item-namespace) "-lister/meta-items"))]
       (vector/concat-items [extension-id meta-items-key] xyz)))

(defn mutation-name
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (keyword) action-id
  ;
  ; @example
  ;  (engine/mutation-name :my-extension :my-type :delete)
  ;  =>
  ;  "my-extension.my-type-lister/delete-items!"
  ;
  ; @return (string)
  [extension-id item-namespace action-id]
  (str (name extension-id)   "."
       (name item-namespace) "-lister/"
       (name action-id)      "-items!"))

(defn resolver-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (keyword) action-id
  ;
  ; @example
  ;  (engine/resolver-id :my-extension :my-type :get)
  ;  =>
  ;  :my-extension.my-type-lister/get-items!
  ;
  ; @return (keyword)
  [extension-id item-namespace action-id]
  (keyword (str (name extension-id)   "."
                (name item-namespace) "-lister")
           (str (name action-id)      "-items")))

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

(defn new-item-uri
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (engine/new-item-uri :my-extension :my-type)
  ;  =>
  ;  "/@app-home/my-extension/new-my-type"
  ;
  ; @return (string)
  [extension-id item-namespace]
  (str "/@app-home/" (name extension-id)
       "/new-"       (name item-namespace)))

(defn transfer-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (engine/transfer-id :my-extension :my-type)
  ;  =>
  ;  :my-extension.my-type-lister/transfer-lister-props
  ;
  ; @return (keyword)
  [extension-id item-namespace]
  (keyword (str (name extension-id)   "."
                (name item-namespace) "-lister")
           "transfer-lister-props"))

(defn route-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (engine/route-id :my-extension :my-type)
  ;  =>
  ;  :my-extension.my-type-lister/route
  ;
  ; @return (keyword)
  [extension-id item-namespace]
  (keyword (str (name extension-id)   "."
                (name item-namespace) "-lister")
           "route"))

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
  ; @return (keyword)
  [extension-id]
  (str "/@app-home/" (name extension-id)))

(defn add-new-item-event
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (engine/add-new-item-event :my-extension :my-type)
  ;  =>
  ;  [:my-extension.my-type-browser/add-new-item!]
  ;
  ; @return (event-vector)
  [extension-id item-namespace]
  (let [event-id (keyword (str (name extension-id)   "."
                               (name item-namespace) "-browser")
                          "add-new-item!")]
       [event-id]))

(defn dialog-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (keyword) action-id
  ;
  ; @example
  ;  (engine/dialog-id :my-extension :my-type :delete-items)
  ;  =>
  ;  :my-extension.my-type-lister/delete-items-dialog
  ;
  ; @return (namespaced keyword)
  [extension-id item-namespace action-id]
  (keyword (str (name extension-id)   "."
                (name item-namespace) "-lister")
           (str (name action-id)      "-dialog")))

(defn load-extension-event
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (engine/load-extension-event :my-extension :my-type)
  ;  =>
  ;  [:my-extension.my-type-lister/load-lister!]
  ;
  ; @return (event-vector)
  [extension-id item-namespace]
  (let [event-id (keyword (str (name extension-id)   "."
                               (name item-namespace) "-lister")
                          "load-lister!")]
       [event-id]))

(defn item-clicked-event
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (integer) item-dex
  ; @param (map) item
  ;
  ; @example
  ;  (engine/item-clicked-event :my-namespace :my-type 0 {...})
  ;  =>
  ;  [:my-namespace.my-type-lister/->item-clicked 0 {...}]
  ;
  ; @return (event-vector)
  [extension-id item-namespace item-dex item]
  (let [event-id (keyword (str (name extension-id)   "."
                               (name item-namespace) "-lister")
                          "->item-clicked")]
       [event-id item-dex item]))

(defn item-right-clicked-event
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (integer) item-dex
  ; @param (map) item
  ;
  ; @example
  ;  (engine/item-right-clicked-event :my-namespace :my-type 0 {...})
  ;  =>
  ;  [:my-namespace.my-type-lister/->item-right-clicked 0 {...}]
  ;
  ; @return (event-vector)
  [extension-id item-namespace item-dex item]
  (let [event-id (keyword (str (name extension-id)   "."
                               (name item-namespace) "-lister")
                          "->item-right-clicked")]
       [event-id item-dex item]))
