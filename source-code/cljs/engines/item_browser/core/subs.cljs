
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
(def get-current-item-id    core.subs/get-current-item-id)
(def get-current-item       core.subs/get-current-item)
(def get-current-item-label core.subs/get-current-item-label)
(def get-auto-title         core.subs/get-auto-title)
(def use-query-params       core.subs/use-query-params)

; engines.item-lister.core.subs
(def get-all-item-count      engines.item-lister.core.subs/get-all-item-count)
(def lister-disabled?        engines.item-lister.core.subs/lister-disabled?)
(def get-downloaded-items    engines.item-lister.core.subs/get-downloaded-items)
(def export-downloaded-items engines.item-lister.core.subs/export-downloaded-items)
(def get-current-order-by    engines.item-lister.core.subs/get-current-order-by)



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
  ; A browsing-item? függvény visszatérési értéke akkor TRUE, ...
  ; ... ha az item-browser engine body komponense a React-fába van csatolva.
  ; ... ha az item-id paraméterként átadott azonosító az aktuálisan böngészett elem azonosítója.
  (r core.subs/current-item? db browser-id item-id))

(defn get-current-item-path
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ;
  ; @usage
  ; (r get-current-item-path db :my-browser)
  ; =>
  ; [{:my-type/id "my-item"} {...}]
  ;
  ; @return (maps in vector)
  [db [_ browser-id]]
  ; XXX#6487 (source-code/cljs/engines/engine_handler/core/subs.cljs)
  (if-let [current-item (r get-current-item db browser-id)]
          (let [path-key (r body.subs/get-body-prop db browser-id :path-key)]
               (-> current-item path-key vec))))



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
  ; XXX#6487 (source-code/cljs/engines/engine_handler/core/subs.cljs)
  (if-let [current-item-path (r get-current-item-path db browser-id)]
          (empty? current-item-path)))

(defn get-default-item-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ;
  ; @usage
  ; (r get-default-item-id db :my-browser)
  ;
  ; @return (string)
  [db [_ browser-id]]
  (r body.subs/get-body-prop db browser-id :default-item-id))

(defn get-parent-item-id
  ; @param (keyword) browser-id
  ;
  ; @usage
  ; (r get-parent-item-id db :my-browser)
  ;
  ; @return (string)
  [db [_ browser-id]]
  ; XXX#6487 (source-code/cljs/engines/engine_handler/core/subs.cljs)
  (if-let [current-item-path (r get-current-item-path db browser-id)]
          (let [item-namespace (r transfer.subs/get-transfer-item db browser-id :item-namespace)]
               (if-let [parent-link (last current-item-path)]
                       (get parent-link (keyword/add-namespace item-namespace :id))))))



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
; [:item-browser/get-downloaded-items :my-browser]
(r/reg-sub :item-browser/get-downloaded-items get-downloaded-items)

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
; [:item-browser/browser-disabled? :my-browser]
(r/reg-sub :item-browser/browser-disabled? browser-disabled?)

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
