
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.core.subs
    (:require [mid-fruits.keyword            :as keyword]
              [plugins.item-lister.core.subs :as plugins.item-lister.core.subs]
              [x.app-core.api                :as a :refer [r]]
              [x.app-router.api              :as router]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.item-lister.core.subs
(def get-description      plugins.item-lister.core.subs/get-description)
(def lister-disabled?     plugins.item-lister.core.subs/lister-disabled?)
(def get-downloaded-items plugins.item-lister.core.subs/get-downloaded-items)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-meta-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (keyword) item-key
  ;
  ; @return (*)
  [db [_ extension-id item-namespace item-key]]
  (get-in db [extension-id :item-browser/meta-items item-key]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-request-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (r get-request-id db :my-extension :my-type)
  ;  =>
  ;  :my-handler/synchronize-browser!
  ;
  ; @return (keyword)
  [db [_ extension-id item-namespace]]
  ; XXX#3055
  (if-let [handler-key (r get-meta-item db extension-id item-namespace :handler-key)]
          (keyword (name handler-key) "synchronize-browser!")))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-current-item-id
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  (r item-browser/get-current-item-id db :my-extension :my-type)
  ;
  ; @return (string)
  [db [_ extension-id item-namespace]]
  (r get-meta-item db extension-id item-namespace :item-id))

(defn get-derived-item-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (string)
  [db [_ extension-id item-namespace]]
  (r router/get-current-route-path-param db :item-id))

(defn get-root-item-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (string)
  [db [_ extension-id item-namespace]]
  (r get-meta-item db extension-id item-namespace :root-item-id))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-current-item-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (metamorphic-content)
  [db [_ extension-id item-namespace]]
  (let [current-item-id (r get-current-item-id db extension-id item-namespace)
        label-key       (r get-meta-item       db extension-id item-namespace :label-key)]
       (get-in db [extension-id :item-browser/data-items current-item-id label-key])))

(defn get-current-item-path
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (maps in vector)
  [db [_ extension-id item-namespace]]
  (let [current-item-id (r get-current-item-id db extension-id item-namespace)
        path-key        (r get-meta-item       db extension-id item-namespace :path-key)
        item-path       (get-in db [extension-id :item-browser/data-items current-item-id path-key])]
       (vec item-path)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn browser-disabled?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (r lister-disabled? db extension-id item-namespace))

(defn at-home?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (let [current-item-path (r get-current-item-path db extension-id item-namespace)]
       (empty? current-item-path)))

(defn get-parent-item-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (string)
  [db [_ extension-id item-namespace]]
  (let [current-item-path (r get-current-item-path db extension-id item-namespace)]
       (if-let [parent-link (last current-item-path)]
               (get parent-link (keyword/add-namespace item-namespace :id)))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) extension-id
; @param (keyword) item-namespace
; @param (keyword) item-key
;
; @usage
;  [:item-browser/get-meta-item :my-extension :my-type :my-item]
(a/reg-sub :item-browser/get-meta-item get-meta-item)

; @param (keyword) extension-id
; @param (keyword) item-namespace
;
; @usage
;  [:item-browser/get-current-item-label :my-extension :my-type]
(a/reg-sub :item-browser/get-current-item-label get-current-item-label)

; @param (keyword) extension-id
; @param (keyword) item-namespace
;
; @usage
;  [:item-browser/browser-disabled? :my-extension :my-type]
(a/reg-sub :item-browser/browser-disabled? browser-disabled?)

; @param (keyword) extension-id
; @param (keyword) item-namespace
;
; @usage
;  [:item-browser/at-home? :my-extension :my-type]
(a/reg-sub :item-browser/at-home? at-home?)

; @param (keyword) extension-id
; @param (keyword) item-namespace
;
; @usage
;  [:item-browser/get-description :my-extension :my-type]
(a/reg-sub :item-browser/get-description get-description)