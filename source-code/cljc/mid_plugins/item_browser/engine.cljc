
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

(defn mutation-name
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (keyword) action-key
  ;
  ; @example
  ;  (engine/mutation-name :my-extension :my-type :delete)
  ;  =>
  ;  "my-extension.my-type-browser/delete-item!"
  ;
  ; @return (string)
  [extension-id item-namespace action-key]
  (str (name extension-id)   "."
       (name item-namespace) "-browser/"
       (name action-key)     "-item!"))

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
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (engine/collection-name :my-extension :my-type)
  ;  =>
  ;  "my-extension"
  ;
  ; @return (string)
  [extension-id _]
  (name extension-id))

(defn component-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (keyword) component-key
  ;
  ; @example
  ;  (engine/component-id :my-extension :my-type :view)
  ;  =>
  ;  :my-extension.my-type-browser/view
  ;
  ; @return (keyword)
  [extension-id item-namespace component-key]
  ; XXX#5467
  (keyword (str (name extension-id)   "."
                (name item-namespace) "-browser")
           (name component-key)))

(defn dialog-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (keyword) action-key
  ;
  ; @example
  ;  (engine/dialog-id :my-extension :my-type :item-deleted)
  ;  =>
  ;  :my-extension.my-type-browser/item-deleted-dialog
  ;
  ; @return (namespaced keyword)
  [extension-id item-namespace action-key]
  (keyword (str (name extension-id)  "."
                (name item-namespace) "-browser")
           (str (name action-key)     "-dialog")))
