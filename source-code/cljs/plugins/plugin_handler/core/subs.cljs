
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.plugin-handler.core.subs
    (:require [mid-fruits.candy                     :refer [return]]
              [mid-fruits.map                       :as map]
              [mid-fruits.vector                    :as vector]
              [plugins.plugin-handler.body.subs     :as body.subs]
              [plugins.plugin-handler.routes.subs   :as routes.subs]
              [plugins.plugin-handler.transfer.subs :as transfer.subs]
              [re-frame.api                         :refer [r]]
              [x.app-activities.api                 :as activities]
              [x.app-sync.api                       :as sync]))



;; -- Meta-items subscriptions ------------------------------------------------
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



;; -- Request subscriptions ---------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-request-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (keyword) request-key
  ;
  ; @example
  ;  (r get-request-id db :my-plugin :browser)
  ;  =>
  ;  :my-handler/synchronize-browser!
  ;
  ; @return (keyword)
  [db [_ plugin-id request-key]]
  ; XXX#5476
  ; A pluginok a különböző lekéréseik elküldéséhez ugyanazt az azonosítót használják,
  ; mert egy közös azonosítóval egyszerűbb megállapítani, hogy valamelyik lekérésük folyamatban
  ; van-e (a plugin-synchronizing? függvénynek elegendő egy request-id azonosítót figyelnie).
  ;
  ; Ha szükséges, akkor a különböző lekéréseket el lehet látni egyedi azonosítókkal.
  ;
  ; A request-key kifejezés használata megkülönbözteti az egyes pluginok lekéréseit egymástól.
  ; Pl.: Az item-browser plugin request-item! lekérése és az item-browser plugin által indított
  ;      de az item-lister plugin request-items! lekérése előfordul, hogy egyszerre történik.
  ;      Ha a két plugin lekésérei nem lennének megkülönböztetve és a request-item! lekérés már
  ;      folyamatban lenne, akkor a request-items! lekérés nem indulna el, mert az item-lister
  ;      plugin tévésen úgy érzékelné, hogy az elemek letöltése már folyamatban van.
  (let [handler-key (r transfer.subs/get-transfer-item db plugin-id :handler-key)]
       (keyword (str                (name handler-key))
                (str "synchronize-" (name request-key) "!"))))

(defn plugin-synchronizing?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (keyword) request-key
  ;
  ; @usage
  ;  (r plugin-synchronizing? db :my-plugin :browser)
  ;
  ; @return (boolean)
  [db [_ plugin-id request-key]]
  ; XXX#5476
  (let [request-id (r get-request-id db plugin-id request-key)]
       (r sync/listening-to-request? db request-id)))



;; -- Current-item subscriptions ----------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-current-item-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ;
  ; @return (string)
  [db [_ plugin-id]]
  (r get-meta-item db plugin-id :item-id))

(defn no-item-id-passed?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ;
  ; @return (boolean)
  [db [_ plugin-id]]
  (let [current-item-id (r get-meta-item db plugin-id :item-id)]
       (nil? current-item-id)))

(defn current-item?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (string) item-id
  ;
  ; @return (boolean)
  [db [_ plugin-id item-id]]
  (= item-id (r get-meta-item db plugin-id :item-id)))

(defn get-current-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ;
  ; @return (map)
  [db [_ plugin-id]]
  ; XXX#6487
  (if-let [item-path (r body.subs/get-body-prop db plugin-id :item-path)]
          (get-in db item-path)))

(defn export-current-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ;
  ; @return (namespaced map)
  [db [_ plugin-id]]
  (let [item-namespace (r transfer.subs/get-transfer-item db plugin-id :item-namespace)
        current-item   (r get-current-item                db plugin-id)]
       (map/add-namespace current-item item-namespace)))

(defn get-current-item-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ;
  ; @return (metamorphic-content)
  [db [_ plugin-id]]
  (if-let [current-item (r get-current-item db plugin-id)]
          (let [label-key (r body.subs/get-body-prop db plugin-id :label-key)]
               (label-key current-item))))

(defn get-current-item-modified-at
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ;
  ; @return (string)
  [db [_ plugin-id]]
  (let [current-item (r get-current-item db plugin-id)]
       (if-let [modified-at (:modified-at current-item)]
               (r activities/get-actual-timestamp db modified-at))))

(defn get-auto-title
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  [db [_ plugin-id]]
  ; ...
  (if-let [auto-title? (r body.subs/get-body-prop db plugin-id :auto-title?)]
          (r get-current-item-label db plugin-id)))



;; -- Current view subscriptions ----------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-current-view-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ;
  ; @return (keyword)
  [db [_ plugin-id]]
  (r get-meta-item db plugin-id :view-id))

(defn no-view-id-passed?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ;
  ; @return (boolean)
  [db [_ plugin-id]]
  (let [current-view-id (r get-meta-item db plugin-id :view-id)]
       (nil? current-view-id)))



;; -- Downloaded items subscriptions ------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-downloaded-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ;
  ; @return (maps in vector)
  [db [_ plugin-id]]
  (let [items-path (r body.subs/get-body-prop db plugin-id :items-path)]
       (get-in db items-path)))

(defn export-downloaded-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ;
  ; @return (namespaced maps in vector)
  [db [_ plugin-id]]
  (let [item-namespace   (r transfer.subs/get-transfer-item db plugin-id :item-namespace)
        downloaded-items (r get-downloaded-items            db plugin-id)]
       (vector/->items downloaded-items #(map/add-namespace % item-namespace))))

(defn get-downloaded-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (integer) item-dex
  ;
  ; @return (map)
  [db [_ plugin-id item-dex]]
  (let [items-path (r body.subs/get-body-prop db plugin-id :items-path)
        item-path  (conj items-path item-dex)]
       (get-in db item-path)))

(defn get-downloaded-item-count
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ;
  ; @return (integer)
  [db [_ plugin-id]]
  (let [downloaded-items (r get-downloaded-items db plugin-id)]
       (count downloaded-items)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn use-query-prop
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (vector) query
  ;
  ; @usage
  ;  [body :my-plugin {:query [:my-query]}]
  ;  (r use-query-prop db :my-plugin [...])
  ;
  ; @return (vector)
  ;  [:my-query ...]
  [db [_ plugin-id query]]
  ; Az egyes pluginok body komponensének {:query [...]} tulajdonságaként esetlegesen
  ; átadott ...
  ; ... Pathom lekérés vektort összefűzi a use-query-prop függvény számára
  ;     query paraméterként átadott Pathom lekérés vektorral.
  ; ... Pathom lekérés vektort az elem(ek) letöltésekor elküldött Pathom lekéréssel
  ;     összefűzve elküldi a szervernek, így megoldható, hogy az egyes pluginok
  ;     használatához szükséges kiegészítő adatokat az elem(ek) letöltésekor
  ;     töltse le.
  (if-let [query-prop (r body.subs/get-body-prop db plugin-id :query)]
          (vector/concat-items query-prop query)
          (return                         query)))

(defn use-query-params
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (map) query-props
  ;
  ; @usage
  ;  (r download.subs/use-query-params db :my-plugin {...})
  ;
  ; @return (map)
  [db [_ plugin-id query-props]]
  ; XXX#7061
  ; Az egyes pluginok {:query-params {...}} meta-adataként beállított térképbe a plugin
  ; által beleírt adatok, minden a plugin által küldött Pathom resolver és mutation
  ; lekérés resolver-props és mutation-props térképének alapját képezi.
  ;
  ; Ha egy plugin beleír a query-params térképébe, akkor az összes általa küldött
  ; Pathom lekéréskor a szerver-oldali resolver és mutation függvények megkapják
  ; a query-params térkép adatait.
  (let [query-params (get-in db [:plugins :plugin-handler/meta-items plugin-id :query-params])]
       (merge query-params query-props)))
