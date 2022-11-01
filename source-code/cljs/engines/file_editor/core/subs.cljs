
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.file-editor.core.subs
    (:require [engines.engine-handler.core.subs  :as core.subs]
              [engines.file-editor.body.subs     :as body.subs]
              [engines.file-editor.download.subs :as download.subs]
              [re-frame.api                      :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.engine-handler.core.subs
(def get-meta-item         core.subs/get-meta-item)
(def engine-synchronizing? core.subs/engine-synchronizing?)
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
  (r engine-synchronizing? db editor-id :editor))



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

(defn get-current-content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  (let [content-path (r body.subs/get-body-prop db editor-id :content-path)]
       (get-in db content-path)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) editor-id
; @param (keyword) item-key
;
; @usage
;  [:file-editor/get-meta-item :my-editor :my-item]
(r/reg-sub :file-editor/get-meta-item get-meta-item)

; @param (keyword) editor-id
;
; @usage
;  [:file-editor/editor-disabled? :my-editor]
(r/reg-sub :file-editor/editor-disabled? editor-disabled?)
