
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.update.subs
    (:require [mid-fruits.keyword                 :as keyword]
              [plugins.item-editor.core.subs      :as core.subs]
              [plugins.item-editor.transfer.subs  :as transfer.subs]
              [plugins.plugin-handler.update.subs :as update.subs]
              [x.app-core.api                     :refer [r]]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.update.subs
(def get-mutation-name update.subs/get-mutation-name)



;; -- Save item subscriptions -------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-saved-item-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) server-response
  ;
  ; @example
  ;  (r update.subs/get-saved-item-id :my-editor {my-handler/save-item! {:my-type/id "my-item"}})
  ;  =>
  ;  "my-item"
  ;
  ; @return (string)
  [db [_ editor-id server-response]]
  (let [new-item?      (r core.subs/new-item?             db editor-id)
        mutation-name  (r get-mutation-name               db editor-id (if new-item? :add-item! :save-item!))
        item-namespace (r transfer.subs/get-transfer-item db editor-id :item-namespace)
        id-key         (keyword/add-namespace item-namespace :id)]
       (get-in server-response [(symbol mutation-name) id-key])))
