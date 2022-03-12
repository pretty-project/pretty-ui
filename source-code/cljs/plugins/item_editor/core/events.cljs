
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.core.events
    (:require [mid-fruits.map                :refer [dissoc-in]]
              [plugins.item-editor.core.subs :as core.subs]
              [x.app-core.api                :as a :refer [r]]
              [x.app-db.api                  :as db]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-error-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id _]]
  ; Az item-editor plugin betöltésekor gondoskodni kell, arról hogy az előző betöltéskor
  ; esetlegesen beállított {:error-mode? true} beállítás törlődjön!
  (assoc-in db [extension-id :item-editor/meta-items :error-mode?] true))

(defn set-recovery-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id _]]

  ; - A {:recovery-mode? true} beállítással elindítitott item-editor plugin visszaállítja az elem
  ;   eltárolt változtatásait
  ; - A {:recovery-mode? true} állapot beállításakor szükséges az item-editor utolsó használatakor
  ;   esetlegesen beállított {:item-recovered? true} beállítást törölni, hogy a reset-editor! függvény
  ;   ne léptesse ki a plugint a {:recovery-mode? true} állapotból, ha az utolsó betöltés is
  ;   {:recovery-mode? true} állapotban történt ...

  (-> db (assoc-in  [extension-id :item-editor/meta-items :recovery-mode?] true)))

         ;(dissoc-in [extension-id :item-editor/meta-items :item-recovered?])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reset-editor!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  (let [inherited-props (r core.subs/get-inherited-props db extension-id item-namespace)]
       (-> db (dissoc-in [extension-id :item-editor/data-items])
              (dissoc-in [extension-id :item-editor/meta-items])
              (assoc-in  [extension-id :item-editor/meta-items] inherited-props))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-body-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) body-props
  ;
  ; @return (map)
  [db [_ extension-id item-namespace body-props]]
  (r db/apply-item! db [extension-id :item-editor/meta-items] merge body-props))

(defn init-header!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) header-props
  ;
  ; @return (map)
  [db [_ extension-id item-namespace header-props]]
  (r db/apply-item! db [extension-id :item-editor/meta-items] merge header-props))

(defn init-body!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) body-props
  ;
  ; @return (map)
  [db [_ extension-id item-namespace body-props]]
  (r store-body-props! db extension-id item-namespace body-props))

(defn destruct-body!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  ; Az item-editor plugin elhagyásakor visszaállítja a plugin állapotát, így a következő betöltéskor
  ; az init-body! függvény lefutása előtt nem villan fel a legutóbbi állapot!
  (r reset-editor! db extension-id item-namespace))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :item-editor/set-error-mode! set-error-mode!)
