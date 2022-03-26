
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.view-selector.routes.subs
    (:require [plugins.view-selector.transfer.subs :as transfer.subs]
              [x.app-core.api                      :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-view-route
  ; @param (keyword) selector-id
  ; @param (keyword) view-id
  ;
  ; @example
  ;  (r view-selector/get-view-route :my-selector :my-view)
  ;  =>
  ;  "/@app-home/my-selector/my-view"
  ;
  ; @return (string)
  [db [_ selector-id view-id]]
  (if-let [base-route (r transfer.subs/get-transfer-item db selector-id :base-route)]
          (str base-route "/" (name view-id))))
