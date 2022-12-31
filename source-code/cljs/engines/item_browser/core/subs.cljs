
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-browser.core.subs
    (:require [engines.engine-handler.core.subs   :as core.subs]
              [engines.item-browser.body.subs     :as body.subs]
              [engines.item-browser.transfer.subs :as transfer.subs]
              [engines.item-lister.core.subs      :as engines.item-lister.core.subs]
              [keyword.api                        :as keyword]
              [re-frame.api                       :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.engine-handler.core.subs
(def get-meta-item          core.subs/get-meta-item)
(def engine-synchronizing?  core.subs/engine-synchronizing?)
(def get-default-item-id    core.subs/get-default-item-id)
(def get-current-item-id    core.subs/get-current-item-id)
(def get-current-item-path  core.subs/get-current-item-path)
(def get-current-item       core.subs/get-current-item)
(def get-current-item-label core.subs/get-current-item-label)
(def get-auto-title         core.subs/get-auto-title)
(def get-item-path          core.subs/get-item-path)
(def export-downloaded-item core.subs/export-downloaded-item)
(def get-item-order         core.subs/get-item-order)
(def item-listed?           core.subs/item-listed?)
(def use-query-params       core.subs/use-query-params)

; engines.item-lister.core.subs
(def get-all-item-count   engines.item-lister.core.subs/get-all-item-count)
(def get-listed-items     engines.item-lister.core.subs/get-listed-items)
(def export-listed-items  engines.item-lister.core.subs/export-listed-items)
(def get-current-order-by engines.item-lister.core.subs/get-current-order-by)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-request-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ;
  ; @return (boolean)
  [db [_ browser-id]]
  (r core.subs/get-request-id db browser-id :browser))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn browsing-item?
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ;
  ; @usage
  ; (r item-browser/browsing-item? db :my-browser "my-item")
  ;
  ; @return (boolean)
  [db [_ browser-id item-id]]
  ; XXX#0079 (source-code/cljs/engines/engine_handler/core/subs.cljs)
  (and (r core.subs/current-item?   db browser-id item-id)
       (r body.subs/body-did-mount? db browser-id item-id)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn at-home?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ;
  ; @return (boolean)
  [db [_ browser-id]]
  ; XXX#6487 (source-code/cljs/engines/engine_handler/core/subs.cljs)
  (if-let [current-item (r get-current-item db browser-id)]
          (let [path-key (r body.subs/get-body-prop db browser-id :path-key)]
               (-> current-item path-key empty?))))

(defn get-parent-item-id
  ; @param (keyword) browser-id
  ;
  ; @usage
  ; (r get-parent-item-id db :my-browser)
  ;
  ; @return (string)
  [db [_ browser-id]]
  ; XXX#6487 (source-code/cljs/engines/engine_handler/core/subs.cljs)
  (if-let [current-item (r get-current-item db browser-id)]
          (let [path-key       (r body.subs/get-body-prop         db browser-id :path-key)
                item-namespace (r transfer.subs/get-transfer-item db browser-id :item-namespace)]
               (-> current-item path-key last (keyword/add-namespace item-namespace :id)))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) browser-id
; @param (keyword) item-key
;
; @usage
; [:item-browser/get-meta-item :my-browser :my-item]
(r/reg-sub :item-browser/get-meta-item get-meta-item)

; @param (keyword) browser-id
;
; @usage
; [:item-browser/get-listed-items :my-browser]
(r/reg-sub :item-browser/get-listed-items get-listed-items)

; @param (keyword) browser-id
;
; @usage
; [:item-browser/get-all-item-count :my-browser]
(r/reg-sub :item-browser/get-all-item-count get-all-item-count)

; @param (keyword) browser-id
;
; @usage
; [:item-browser/get-current-item-id :my-browser]
(r/reg-sub :item-browser/get-current-item-id get-current-item-id)

; @param (keyword) browser-id
;
; @usage
; [:item-browser/get-current-item :my-browser]
(r/reg-sub :item-browser/get-current-item get-current-item)

; @param (keyword) browser-id
;
; @usage
; [:item-browser/get-current-item-label :my-browser]
(r/reg-sub :item-browser/get-current-item-label get-current-item-label)

; @param (keyword) browser-id
;
; @usage
; [:item-browser/at-home? :my-browser]
(r/reg-sub :item-browser/at-home? at-home?)

; @param (keyword) browser-id
;
; @usage
; [:item-browser/get-current-order-by :my-browser]
(r/reg-sub :item-browser/get-current-order-by get-current-order-by)

; @param (keyword) browser-id
;
; @usage
; [:item-browser/get-parent-item-id :my-browser]
(r/reg-sub :item-browser/get-parent-item-id get-parent-item-id)
