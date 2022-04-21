
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.body.events
    (:require [plugins.item-browser.body.subs   :as body.subs]
              [plugins.item-browser.core.events :as core.events]
              [plugins.item-browser.core.subs   :as core.subs]
              [x.app-core.api                   :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn update-item-id!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ;
  ; @return (map)
  [db [_ browser-id]]
  ; Az update-item-id! függvény a body komponens React-fába csatolásakor felülvizsgálja
  ; az aktuálisan böngészett elem azonosítóját.
  ;
  ; Ha az [:item-browser/body-did-mount ...] esemény megtörténtekor az aktuálisan böngészett
  ; elem azonosítója ...
  ;
  ; A) ... már eltárolásra került, akkor NEM használja a body komponens {:root-item-id "..."}
  ;        paraméterének értékét.
  ;
  ; B) ... még NEM került eltárolásra és a body komponens paraméterként megkapta a {:root-item-id "..."}
  ;        tulajdonságot, akkor azt eltárolja az aktuálisan böngészett elem azonosítójaként.
  (r core.events/set-current-item-id! db browser-id (or (r core.subs/get-current-item-id db browser-id)                  ; A)
                                                        (r body.subs/get-body-prop       db browser-id :root-item-id)))) ; B)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (map) body-props
  ;  {:items-key (keyword)
  ;   :path-key (keyword)}
  ;
  ; @return (map)
  [db [_ browser-id {:keys [items-key path-key]}]]
  ; Az item-browser plugin minden Pathom lekérés küldésekor elküldi a szerver számára a body komponens
  ; {:items-key ...} és {:path-key ...} tulajdonságát.
  (cond-> db :update-item-id! (as-> % (r update-item-id! % browser-id))
             :send-items-key! (assoc-in [:plugins :plugin-handler/meta-items browser-id :default-query-params :items-key] items-key)
             :send-path-key!  (assoc-in [:plugins :plugin-handler/meta-items browser-id :default-query-params :path-key]  path-key)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body-will-unmount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ;
  ; @return (map)
  [db [_ browser-id]]
  (r core.events/reset-downloaded-item! db browser-id))
