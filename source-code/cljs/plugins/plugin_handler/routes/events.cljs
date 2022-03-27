
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.plugin-handler.routes.events
    (:require [plugins.plugin-handler.transfer.subs :as transfer.subs]
              [x.app-core.api                       :refer [r]]
              [x.app-router.api                     :as router]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-parent-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ;
  ; @return (map)
  [db [_ plugin-id]]
  (let [parent-route (r transfer.subs/get-transfer-item db plugin-id :parent-route)]
       (r router/set-temporary-parent! db parent-route)))
