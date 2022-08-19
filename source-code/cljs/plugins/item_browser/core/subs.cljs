

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.core.subs
    (:require [mid-fruits.keyword                 :as keyword]
              [plugins.item-browser.body.subs     :as body.subs]
              [plugins.item-browser.transfer.subs :as transfer.subs]
              [plugins.item-lister.core.subs      :as plugins.item-lister.core.subs]
              [plugins.plugin-handler.core.subs   :as core.subs]
              [x.app-core.api                     :as a :refer [r]]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.item-lister.core.subs
(def get-all-item-count   plugins.item-lister.core.subs/get-all-item-count)
(def get-items-info       plugins.item-lister.core.subs/get-items-info)
(def lister-disabled?     plugins.item-lister.core.subs/lister-disabled?)
(def get-downloaded-items plugins.item-lister.core.subs/get-downloaded-items)
(def get-current-order-by plugins.item-lister.core.subs/get-current-order-by)

; plugins.plugin-handler.core.subs
(def get-meta-item          core.subs/get-meta-item)
(def get-query-params       core.subs/get-query-params)
(def plugin-synchronizing?  core.subs/plugin-synchronizing?)
(def get-current-item-id    core.subs/get-current-item-id)
(def get-current-item       core.subs/get-current-item)
(def get-current-item-label core.subs/get-current-item-label)
(def get-auto-title         core.subs/get-auto-title)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-request-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (boolean)
  [db [_ lister-id]]
  (r core.subs/get-request-id db lister-id :browser))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn browsing-item?
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ;
  ; @usage
  ;  (r item-browser/browsing-item? db :my-browser "my-item")
  ;
  ; @return (boolean)
  [db [_ browser-id item-id]]
  ; A browsing-item? függvény visszatérési értéke akkor TRUE, ...
  ; ... ha az item-browser plugin body komponense a React-fába van csatolva.
  ; ... ha az item-id paraméterként átadott azonosító az aktuálisan böngészett elem azonosítója.
  (r core.subs/current-item? db browser-id item-id))

(defn get-current-item-path
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ;
  ; @return (maps in vector)
  [db [_ browser-id]]
  (let [current-item (r get-current-item        db browser-id)
        path-key     (r body.subs/get-body-prop db browser-id :path-key)]
       (-> current-item path-key vec)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn browser-disabled?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ;
  ; @return (boolean)
  [db [_ browser-id]]
  (r lister-disabled? db browser-id))

(defn at-home?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ;
  ; @return (boolean)
  [db [_ browser-id]]
  (let [current-item-path (r get-current-item-path db browser-id)]
       (empty? current-item-path)))

(defn get-parent-item-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ;
  ; @return (string)
  [db [_ browser-id]]
  (let [item-namespace    (r transfer.subs/get-transfer-item db browser-id :item-namespace)
        current-item-path (r get-current-item-path           db browser-id)]
       (if-let [parent-link (last current-item-path)]
               (get parent-link (keyword/add-namespace item-namespace :id)))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) browser-id
; @param (keyword) item-key
;
; @usage
;  [:item-browser/get-meta-item :my-browser :my-item]
(a/reg-sub :item-browser/get-meta-item get-meta-item)

; @param (keyword) browser-id
;
; @usage
;  [:item-browser/get-all-item-count :my-browser]
(a/reg-sub :item-browser/get-all-item-count get-all-item-count)

; @param (keyword) browser-id
;
; @usage
;  [:item-browser/get-current-item-id :my-browser]
(a/reg-sub :item-browser/get-current-item-id get-current-item-id)

; @param (keyword) browser-id
;
; @usage
;  [:item-browser/get-current-item :my-browser]
(a/reg-sub :item-browser/get-current-item get-current-item)

; @param (keyword) browser-id
;
; @usage
;  [:item-browser/get-current-item-label :my-browser]
(a/reg-sub :item-browser/get-current-item-label get-current-item-label)

; @param (keyword) browser-id
;
; @usage
;  [:item-browser/browser-disabled? :my-browser]
(a/reg-sub :item-browser/browser-disabled? browser-disabled?)

; @param (keyword) browser-id
;
; @usage
;  [:item-browser/at-home? :my-browser]
(a/reg-sub :item-browser/at-home? at-home?)

; @param (keyword) browser-id
;
; @usage
;  [:item-browser/get-items-info :my-browser]
(a/reg-sub :item-browser/get-items-info get-items-info)

; @param (keyword) browser-id
;
; @usage
;  [:item-browser/get-current-order-by :my-browser]
(a/reg-sub :item-browser/get-current-order-by get-current-order-by)
