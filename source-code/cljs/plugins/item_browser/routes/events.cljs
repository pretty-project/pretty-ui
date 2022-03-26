
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.routes.events
    (:require [plugins.item-browser.transfer.subs :as transfer.subs]
              [x.app-core.api                     :refer [r]]
              [x.app-router.api                   :as router]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-parent-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  ; Az item-browser plugin beállítja az aktuális útvonalhoz tartozó szülő-útvonalat ...
  ; ... a "/my-app/my-route/:item-id" útvonal szűlő-útvonala a "/my-app/my-route" útvonal helyett
  ;     a "/my-app" útvonal lesz.
  (let [parent-route (r transfer.subs/get-transfer-item db extension-id item-namespace :parent-route)]
       (r router/set-temporary-parent! db parent-route)))
