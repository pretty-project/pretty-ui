
(ns plugins.config-editor.events
  (:require [plugins.config-editor.helpers :as configer.helpers]
            [mid-fruits.map                         :refer [dissoc-in]]
            [x.app-core.api                         :as a :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn clean-config!
  ; @param (keyword) configer-id
  ; @param (map) configer-props
  ;  {:backup-path (vector)
  ;   :config-path (vector)}
  ;
  ; @return (map)
  [db [_ configer-id {:keys [backup-path config-path]}]]
  (-> db (dissoc-in config-path)
         (dissoc-in backup-path)
         (dissoc-in [:entities :config-handler/meta-items configer-id])))

(defn update-backup!
  ; @param (keyword) configer-id
  ; @param (map) configer-props
  ;  {:config-path (vector)}
  ;
  ; @return (map)
  [db [_ configer-id {:keys [config-path]}]]
  ; A config-item mentésekor a róla készült másolatot frissíteni kell,
  ; hogy az adatok megváltozását vizsgáló függvények visszaálljanak alaphelyzetbe
  (let [config-item (get-in db config-path)]
       (assoc-in db [:entities :config-handler/backup-items configer-id] config-item)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-config!
  ; @param (keyword) configer-id
  ; @param (map) configer-props
  ;  {:config-path (vector)}
  ; @param (namespaced map) server-response
  ;
  ; @return (map)
  [db [_ configer-id {:keys [config-path]} server-response]]
  (let [config-item (configer.helpers/server-response->config-item configer-id server-response)]
       (assoc-in db config-path config-item)))

(defn config-received
  ; @param (keyword) configer-id
  ; @param (map) configer-props
  ; @param (namespaced map) server-response
  ;
  ; @return (map)
  [db [_ configer-id _ _]]
  (assoc-in db [:entities :config-handler/meta-items configer-id :config-received?] true))
