
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.view-selector.routes.subs
    (:require [plugins.view-selector.transfer.subs :as transfer.subs]
              [x.app-core.api                      :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-view-route
  ; @param (keyword) extension-id
  ; @param (keyword) view-id
  ;
  ; @example
  ;  (r view-selector/get-view-route :my-extension :my-view)
  ;  =>
  ;  "/@app-home/my-extension/my-view"
  ;
  ; @return (string)
  [db [_ extension-id view-id]]
  (if-let [base-route (r transfer.subs/get-transfer-item db extension-id :base-route)]
          (str base-route "/" (name view-id))))
