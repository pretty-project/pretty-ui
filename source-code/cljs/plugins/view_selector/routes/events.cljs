
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.view-selector.routes.events
    (:require [mid-fruits.candy                    :refer [return]]
              [plugins.view-selector.transfer.subs :as transfer.subs]
              [x.app-core.api                      :refer [r]]
              [x.app-router.api                    :as router]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn use-parent-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (map)
  [db [_ extension-id]]
  ; - A view-selector plugin beállítja az aktuális útvonalhoz tartozó szülő-útvonalat
  ; - A "/my-app/my-route/:view-id" útvonal szűlő-útvonala a "/my-app/my-route" útvonal helyett
  ;   a "/my-app" útvonal lesz
  (if-let [parent-route (r transfer.subs/get-transfer-item db extension-id :parent-route)]
          (r router/set-temporary-parent! db parent-route)
          (return db)))
