
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.routes.subs
    (:require [plugins.item-editor.transfer.subs :as transfer.subs]
              [x.app-core.api                    :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-item-route
  ; @param (keyword) editor-id
  ; @param (string) item-id
  ;
  ; @usage
  ;  (r item-editor/get-item-route db :my-editor "my-item")
  ;
  ; @return (string)
  [db [_ editor-id item-id]]
  (if-let [base-route (r transfer.subs/get-transfer-item db editor-id :base-route)]
          (str base-route "/" item-id)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:item-editor/get-item-route :my-editor "my-item"]
(a/reg-sub :item-editor/get-item-route get-item-route)
