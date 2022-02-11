
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.23
; Description:
; Version: v0.5.0
; Compatibility: x4.6.0



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
;   Pl.: :my-extension.item-browser/synchronize-browser!
;        =>
;        :my-extension.my-type-browser/synchronize-browser!
;   Így biztosítható, hogy egy névtér több különböző item-browser böngészőt tudjon megvalósítani.
; - Ha szükséges, akkor a Re-Frame adatbázis útvonalakban is be kell vezetni a megkülönbözetést,
;   hogy egy extension több böngészőt alkalmazhasson.



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
  ;  :my-extension.my-type-browser/synchronize-browser!
  ;
  ; @return (keyword)
  [extension-id item-namespace]
  (keyword (str (name extension-id)   "."
                (name item-namespace) "-browser")
           "synchronize-browser!"))



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
  ;  "my-extension.my-type-browser/delete-item!"
  ;
  ; @return (string)
  [extension-id item-namespace action-id]
  (str (name extension-id)   "."
       (name item-namespace) "-browser/"
       (name action-id)      "-item!"))

(defn resolver-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (engine/resolver-id :my-extension :my-type)
  ;  =>
  ;  :my-extension.my-type-browser/get-item
  ;
  ; @return (keyword)
  [extension-id item-namespace]
  (keyword (str (name extension-id)   "."
                (name item-namespace) "-browser")
           "get-item"))

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

(defn transfer-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (keyword) plugin-key
  ;
  ; @example
  ;  (engine/transfer-id :my-extension :my-type :browser)
  ;  =>
  ;  :my-extension.my-type-browser/transfer-browser-props
  ;
  ; @return (keyword)
  [extension-id item-namespace plugin-key]
  (keyword (str (name extension-id)   "."
                (name item-namespace) "-browser")
           (str                       "transfer-"
                (name plugin-key)     "-props")))

(defn route-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (engine/route-id :my-extension :my-type)
  ;  =>
  ;  :my-extension.my-type-browser/route
  ;
  ; @return (keyword)
  [extension-id item-namespace]
  (keyword (str (name extension-id)   "."
                (name item-namespace) "-browser")
           "route"))

(defn extended-route-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (engine/extended-route-id :my-extension :my-type)
  ;  =>
  ;  :my-extension.my-type-browser/extended-route
  ;
  ; @return (keyword)
  [extension-id item-namespace]
  (keyword (str (name extension-id)   "."
                (name item-namespace) "-browser")
           "extended-route"))

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
  ;  [:my-extension.my-type-browser/load-browser!]
  ;
  ; @return (event-vector)
  [extension-id item-namespace]
  (let [event-id (keyword (str (name extension-id)   "."
                               (name item-namespace) "-browser")
                          (str "load-browser!"))]
       [event-id]))

(defn item-clicked-event
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (engine/item-clicked-event :my-extension :my-type)
  ;  =>
  ;  [:my-extension.my-type-browser/->item-clicked]
  ;
  ; @return (event-vector)
  [extension-id item-namespace]
  (let [event-id (keyword (str (name extension-id)   "."
                               (name item-namespace) "-browser")
                          "->item-clicked")]
       [event-id]))
