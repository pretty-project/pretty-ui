
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-handler.core.subs
    (:require [engines.engine-handler.core.subs   :as core.subs]
              [engines.item-handler.body.subs     :as body.subs]
              [engines.item-handler.download.subs :as download.subs]
              [re-frame.api                       :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.engine-handler.core.subs
(def get-meta-item            core.subs/get-meta-item)
(def engine-synchronizing?    core.subs/engine-synchronizing?)
(def item-downloaded?         core.subs/item-downloaded?)
(def get-current-item-id      core.subs/get-current-item-id)
(def get-current-item-path    core.subs/get-current-item-path)
(def get-current-item         core.subs/get-current-item)
(def get-current-item-value   core.subs/get-current-item-value)
(def get-current-item-label   core.subs/get-current-item-label)
(def get-auto-title           core.subs/get-auto-title)
(def no-item-id-passed?       core.subs/no-item-id-passed?)
(def current-item-downloaded? core.subs/current-item-downloaded?)
(def export-current-item      core.subs/export-current-item)
(def use-query-prop           core.subs/use-query-prop)
(def use-query-params         core.subs/use-query-params)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-request-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ;
  ; @return (boolean)
  [db [_ handler-id]]
  (r core.subs/get-request-id db handler-id :handler))

(defn handler-synchronizing?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ;
  ; @return (boolean)
  [db [_ handler-id]]
  (r engine-synchronizing? db handler-id :handler))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn handling-item?
  ; @param (keyword) handler-id
  ; @param (string) item-id
  ;
  ; @usage
  ; (r handling-item? db :my-handler "my-item")
  ;
  ; @return (boolean)
  [db [_ handler-id item-id]]
  ; XXX#0079 (source-code/cljs/engines/engine_handler/core/subs.cljs)
  (as-> db % (r core.subs/current-item?   % handler-id item-id)
             (r body.subs/body-did-mount? % handler-id)))

(defn new-item?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ;
  ; @return (boolean)
  [db [_ handler-id]])
  ;(let [current-item-id (r get-current-item-id db handler-id)]
  ;     (= "create" current-item-id)]])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) handler-id
; @param (keyword) item-key
;
; @usage
; [:item-handler/get-meta-item :my-handler :my-item]
(r/reg-sub :item-handler/get-meta-item get-meta-item)

; @param (keyword) handler-id
;
; @usage
; [:item-handler/get-current-item-id :my-handler]
(r/reg-sub :item-handler/get-current-item-id get-current-item-id)

; @param (keyword) handler-id
;
; @usage
; [:item-handler/get-current-item :my-handler]
(r/reg-sub :item-handler/get-current-item get-current-item)

; @param (keyword) handler-id
;
; @usage
; [:item-handler/export-current-item :my-handler]
(r/reg-sub :item-handler/export-current-item export-current-item)

; @param (keyword) handler-id
;
; @usage
; [:item-handler/get-current-item-label :my-handler]
(r/reg-sub :item-handler/get-current-item-label get-current-item-label)

; @param (keyword) handler-id
;
; @usage
; [:item-handler/new-item? :my-handler]
(r/reg-sub :item-handler/new-item? new-item?)

; @param (keyword) handler-id
;
; @usage
; [:item-handler/current-item-downloaded? :my-handler]
(r/reg-sub :item-handler/current-item-downloaded? current-item-downloaded?)
