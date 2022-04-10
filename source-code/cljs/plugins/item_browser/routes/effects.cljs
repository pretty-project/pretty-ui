
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.routes.effects
    (:require [plugins.item-browser.mount.subs    :as mount.subs]
              [plugins.item-browser.routes.events :as routes.events]
              [plugins.item-browser.transfer.subs :as transfer.subs]
              [x.app-core.api                     :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-browser/handle-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  (fn [{:keys [db]} [_ browser-id]]
      ; Ha az [:item-browser/handle-route! ...] esemény megtörténésekor a body komponens ...
      ; A) ... a React-fába van csatolva, akkor szükséges az infinite-loader komponenst újratölteni,
      ;        és az aktuálisan böngészett elem adatait letölteni.
      ;
      ; B) ... NINCS a React-fába csatolva, akkor
      (let [on-route    (r transfer.subs/get-transfer-item db browser-id :on-route)
            route-title (r transfer.subs/get-transfer-item db browser-id :route-title)]
           (if (r mount.subs/body-did-mount? db browser-id)
               ; A)
               {:db (r routes.events/handle-route! db browser-id)
                :dispatch-n [on-route [:tools/reload-infinite-loader! browser-id]
                                      [:item-browser/request-item!    browser-id]]}
               ; B)
               {:db (r routes.events/handle-route! db browser-id)
                :dispatch-n [on-route (if route-title [:ui/set-title! route-title])]}))))
