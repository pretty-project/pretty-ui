
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



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn extension-namespace
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (item-editor/extension-namespace :products :product)
  ;  =>
  ;  :product-editor
  ;
  ; @return (keyword)
  [_ item-namespace]
  (keyword/join item-namespace "-editor"))

(defn item-id->new-item?
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (keyword) item-id
  ;
  ; @example
  ;  (item-editor/item-id->new-item? :products :product :new-product)
  ;  =>
  ; true
  ;
  ; @example
  ;  (item-editor/item-id->new-item? :products :product :my-product)
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
  ;  (item-editor/item-id->form-label :products :product :new-product)
  ;  =>
  ; :add-product
  ;
  ; @example
  ;  (item-editor/item-id->form-label :products :product :my-product)
  ;  =>
  ; :edit-product
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
  ;  (item-editor/item-id->item-uri :products :product :my-product)
  ;  =>
  ;  "/products/my-product"
  ;
  ; @return (string)
  [extension-id _ item-id]
  (str "/" (name extension-id)
       "/" (name item-id)))

(defn request-id
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (item-editor/request-id :products :product)
  ;  =>
  ;  :product-editor/synchronize!
  ;
  ; @return (keyword)
  [extension-id item-namespace]
  (let [extension-namespace (extension-namespace extension-id item-namespace)]
       (keyword/add-namespace extension-namespace :synchronize!)))

(defn mutation-name
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (keyword) action-id
  ;
  ; @example
  ;  (item-editor/mutation-name :products :product :add)
  ;  =>
  ;  "products/add-product-item!"
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
  ;  (item-editor/form-id :products :product)
  ;  =>
  ;  :product-editor/form
  ;
  ; @return (keyword)
  [extension-id item-namespace]
  (let [extension-namespace (extension-namespace extension-id item-namespace)]
       (keyword/add-namespace extension-namespace :form)))

(defn route-id
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (item-editor/route-id :products :product)
  ;  =>
  ;  :products/editor-route
  ;
  ; @return (keyword)
  [extension-id _]
  (keyword/add-namespace extension-id :editor-route))

(defn route-template
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (item-editor/route-template :products :product)
  ;  =>
  ;  "/products/:product-id"
  ;
  ; @return (string)
  [extension-id item-namespace]
  (str "/"  (name extension-id)
       "/:" (name item-namespace) "-id"))

(defn parent-uri
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (item-editor/parent-uri :products :product)
  ;  =>
  ;  "/products"
  ;
  ; @return (string)
  [extension-id _]
  (str "/" (name extension-id)))

(defn render-event-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (item-editor/render-event-id :products :product)
  ;  =>
  ;  :products/render-product-editor!
  ;
  ; @return (keyword)
  [extension-id item-namespace]
  (keyword (name extension-id)
           (str "render-" (name item-namespace) "-editor!")))
