
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.config-editor.core.subs
    (:require [plugins.config-editor.body.subs     :as body.subs]
              [plugins.config-editor.download.subs :as download.subs]
              [plugins.plugin-handler.core.subs    :as core.subs]
              [x.app-core.api                      :as a :refer [r]]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.core.subs
(def get-meta-item         core.subs/get-meta-item)
(def plugin-synchronizing? core.subs/plugin-synchronizing?)
(def use-query-prop        core.subs/use-query-prop)
(def use-query-params      core.subs/use-query-params)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-request-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (boolean)
  [db [_ editor-id]]
  (r core.subs/get-request-id db editor-id :editor))

(defn editor-synchronizing?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (boolean)
  [db [_ editor-id]]
  (r plugin-synchronizing? db editor-id :editor))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn editor-disabled?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (boolean)
  [db [_ editor-id]]
  ; XXX#3219
  (let [data-received?        (r download.subs/data-received? db editor-id)
        editor-synchronizing? (r editor-synchronizing?        db editor-id)]
       (or editor-synchronizing? (not data-received?))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-current-config
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  (let [config-path (r body.subs/get-body-prop db editor-id :config-path)]
       (get-in db config-path)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) editor-id
; @param (keyword) item-key
;
; @usage
;  [:config-editor/get-meta-item :my-editor :my-item]
(a/reg-sub :config-editor/get-meta-item get-meta-item)

; @param (keyword) editor-id
;
; @usage
;  [:config-editor/editor-disabled? :my-editor]
(a/reg-sub :config-editor/editor-disabled? editor-disabled?)
