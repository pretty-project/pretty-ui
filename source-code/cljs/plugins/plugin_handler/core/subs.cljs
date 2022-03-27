
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.plugin-handler.core.subs
    (:require [plugins.plugin-handler.transfer.subs :as transfer.subs]
              [x.app-core.api                       :refer [r]]
              [x.app-sync.api                       :as sync]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-meta-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (keyword) item-key
  ;
  ; @return (*)
  [db [_ plugin-id item-key]]
  (get-in db [:plugins :plugin-handler/meta-items plugin-id item-key]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-request-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (keyword) plugin-key
  ;
  ; @example
  ;  (r core.subs/get-request-id db :my-plugin :browser)
  ;  =>
  ;  :my-handler/synchronize-browser!
  ;
  ; @return (keyword)
  [db [_ plugin-id plugin-key]]
  ; - A pluginok a különböző lekéréseik elküldéséhez ugyanazt az azonosítót használják,
  ;   mert egy közös azonosítóval egyszerűbb megállapítani, hogy valamelyik lekérésük folyamatban
  ;   van-e (a plugin-synchronizing? függvénynek elegendő egy request-id azonosítót figyelnie).
  ; - Ha szükséges, akkor a különböző lekéréseket el lehet látni egyedi azonosítókkal.
  ; - A plugin-key kifejezés használata megkülönbözteti az egyes pluginok lekéréseit egymástól.
  ;   Pl.: Az item-browser plugin request-item! lekérése és az item-browser plugin által indított
  ;        de az item-lister plugin request-items! lekérése előfordul, hogy egyszerre történik.
  ;        Ha a két plugin lekésérei nem lennének megkülönböztetve és a request-item! lekérés már
  ;        folyamatban lenne, akkor a request-items! lekérés nem indulna el, mert az item-lister
  ;        plugin tévésen úgy érzékelné, hogy az elemek letöltése már folyamatban van.
  (let [handler-key (r transfer.subs/get-transfer-item db plugin-id :handler-key)]
       (keyword (str                (name handler-key))
                (str "synchronize-" (name plugin-key) "!"))))

(defn plugin-synchronizing?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (keyword) plugin-key
  ;
  ; @usage
  ;  (r core.subs/plugin-synchronizing? db :my-plugin :browser)
  ;
  ; @return (boolean)
  [db [_ plugin-id plugin-key]]
  (let [request-id (r get-request-id db plugin-id plugin-key)]
       (r sync/listening-to-request? db request-id)))
