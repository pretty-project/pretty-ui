
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.23
; Description:
; Version: v0.2.8
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-plugins.item-editor.engine
    (:require [mid-fruits.keyword :as keyword]))



;; -- Description -------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @description
;  Az elnevézesekben az item-namespace értéke helyettesíti az "item" szót.
;  Pl.: :my-extension/synchronize-item-editor! => :my-extension/synchronize-my-type-editor!
;  Így biztosítható, hogy egy névtér több különböző item-editor szerkesztőt tudjon megvalósítani.



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-id->new-item?
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (keyword) item-id
  ;
  ; @example
  ;  (item-editor/item-id->new-item? :my-extension :my-type :new-my-type)
  ;  =>
  ; true
  ;
  ; @example
  ;  (item-editor/item-id->new-item? :my-extension :my-type :my-item)
  ;  =>
  ; false
  ;
  ; @return (boolean)
  [_ item-namespace item-id]
  (= item-id (keyword/join "new-" item-namespace)))

(defn item-id->form-label
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (keyword) item-id
  ;
  ; @example
  ;  (item-editor/item-id->form-label :my-extension :my-type :new-my-type)
  ;  =>
  ; :add-my-type
  ;
  ; @example
  ;  (item-editor/item-id->form-label :my-extension :my-type :my-item)
  ;  =>
  ; :edit-my-type
  ;
  ; @return (metamorphic-content)
  [extension-id item-namespace item-id]
  (if (item-id->new-item? extension-id item-namespace item-id)
      (keyword/join "add-"  item-namespace)
      (keyword/join "edit-" item-namespace)))

(defn item-id->item-uri
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (keyword) item-id
  ;
  ; @example
  ;  (item-editor/item-id->item-uri :my-extension :my-type :my-item)
  ;  =>
  ;  "/my-extension/my-item"
  ;
  ; @return (string)
  [extension-id _ item-id]
  (str "/" (name extension-id)
       "/" (name item-id)))

(defn item-id-key
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (item-editor/item-id-key :my-extension :my-type)
  ;  =>
  ;  :my-type-id
  ;
  ; @return (keyword)
  [_ item-namespace]
  (keyword (str (name item-namespace) "-id")))

(defn request-id
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (item-editor/request-id :my-extension :my-type)
  ;  =>
  ;  :my-extension/synchronize-my-type-editor!
  ;
  ; @return (keyword)
  [extension-id item-namespace]
  (keyword (name extension-id)
           (str "synchronize-" (name item-namespace) "-editor!")))

(defn mutation-name
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (keyword) action-id
  ;
  ; @example
  ;  (item-editor/mutation-name :my-extension :my-type :add)
  ;  =>
  ;  "my-extension/add-my-type-item!"
  ;
  ; @return (string)
  [extension-id item-namespace action-id]
  (str (name extension-id)   "/"
       (name action-id)      "-"
       (name item-namespace) "-item!"))

(defn form-id
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (item-editor/form-id :my-extension :my-type)
  ;  =>
  ;  :my-extension/my-type-editor-form
  ;
  ; @return (keyword)
  [extension-id item-namespace]
  (keyword (name extension-id)
           (str (name item-namespace) "-editor-form")))

(defn route-id
  ; WARNING! NON-PUBLIC! DO NOT USE!

  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (item-editor/route-id :my-extension :my-type)
  ;  =>
  ;  :my-extension/my-type-editor-route
  ;
  ; @return (keyword)
  [extension-id item-namespace]
  (keyword (name extension-id)
           (str (name item-namespace) "-editor-route")))

(defn extended-route-id
  ; WARNING! NON-PUBLIC! DO NOT USE!

  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (item-editor/extended-route-id :my-extension :my-type)
  ;  =>
  ;  :my-extension/my-type-editor-extended-route
  ;
  ; @return (keyword)
  [extension-id item-namespace]
  (keyword (name extension-id)
           (str (name item-namespace) "-editor-extended-route")))

(defn route-template
  ; WARNING! NON-PUBLIC! DO NOT USE!

  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (item-editor/route-template :my-extension :my-type)
  ;  =>
  ;  "/my-extension/:my-type-id"
  ;
  ; @return (string)
  [extension-id item-namespace]
  (str "/"  (name extension-id)
       "/:" (name item-namespace) "-id"))

(defn extended-route-template
  ; WARNING! NON-PUBLIC! DO NOT USE!

  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (item-editor/extended-route-template :my-extension :my-type)
  ;  =>
  ;  "/my-extension/:my-type-id/:view-id"
  ;
  ; @return (string)
  [extension-id item-namespace]
  (str "/"  (name extension-id)
       "/:" (name item-namespace) "-id"
       "/:view-id"))

(defn parent-uri
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (item-editor/parent-uri :my-extension :my-type)
  ;  =>
  ;  "/products"
  ;
  ; @return (string)
  [extension-id _]
  (str "/" (name extension-id)))

(defn render-event
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (item-editor/render-event :my-extension :my-type)
  ;  =>
  ;  :my-extension/render-my-type-editor!
  ;
  ; @return (keyword)
  [extension-id item-namespace]
  (keyword (name extension-id)
           (str "render-" (name item-namespace) "-editor!")))
