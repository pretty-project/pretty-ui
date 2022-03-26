
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.view-selector.routes.events
    (:require [plugins.view-selector.transfer.subs :as transfer.subs]
              [x.app-core.api                      :refer [r]]
              [x.app-router.api                    :as router]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-parent-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) selector-id
  ;
  ; @return (map)
  [db [_ selector-id]]
  ; A view-selector plugin beállítja az aktuális útvonalhoz tartozó szülő-útvonalat ...
  ; ... a "/my-app/my-route/:view-id" útvonal szűlő-útvonala a "/my-app/my-route" útvonal helyett
  ;     a "/my-app" útvonal lesz.
  (let [parent-route (r transfer.subs/get-transfer-item db selector-id :parent-route)]
       (r router/set-temporary-parent! db parent-route)))
