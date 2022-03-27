
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.update.subs
    (:require [mid-fruits.keyword                 :as keyword]
              [plugins.item-editor.transfer.subs  :as transfer.subs]
              [plugins.plugin-handler.update.subs :as update.subs]
              [x.app-core.api                     :refer [r]]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.update.subs
(def get-mutation-name update.subs/get-mutation-name)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-copy-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) server-response
  ;
  ; @example
  ;  (r update.subs/get-copy-id :my-editor {my-handler/duplicate-item! {:my-type/id "my-item"}})
  ;  =>
  ;  "my-item"
  ;
  ; @return (string)
  [db [_ editor-id server-response]]
  (let [mutation-name  (r get-mutation-name               db editor-id :duplicate-item!)
        item-namespace (r transfer.subs/get-transfer-item db editor-id :item-namespace)
        item-id-key    (keyword/add-namespace item-namespace :id)]
       (get-in server-response [(symbol mutation-name) item-id-key])))
