
(ns plugins.config-editor.subs
  (:require [mid-fruits.map :as map]
            [x.app-core.api :as a :refer [r]]
            [x.app-sync.api :as sync]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn config-synchronizing?
  ; @param (keyword) configer-id
  ;
  ; @usage
  ;  (r entities/synchronizing? db :my-configer)
  ;
  ; @return (boolean)
  [db _]
  (r sync/listening-to-request? db :entities.configer/synchronizing))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn config-received?
  ; @param (keyword) configer-id
  ;
  ; @usage
  ;  (r entities/config-received? db :my-configer)
  ;
  ; @return (boolean)
  [db [_ configer-id]]
  (get-in db [:entities :config-handler/meta-items configer-id :config-received?]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn config-form-changed?
  ; @param (keyword) configer-id
  ; @param (map) configer-props
  ;  {:backup-path (vector)
  ;   :config-path (vector)}
  ; @param (keywords in vector) change-keys
  ;
  ; @usage
  ;  (r entities/config-form-changed? db :my-configer {...} [:my-item])
  ;
  ; @return (boolean)
  [db [_ configer-id {:keys [backup-path config-path]} change-keys]]
  ; Megvizsgálja, hogy a config-item megadott kulcsú elemei megváltoztak-e
  ; az eredet másolat azonos kulcsú elemeihez képest
  (let [config-item (get-in db config-path)
        backup-item (get-in db backup-path)]
       (map/items-different? config-item backup-item change-keys)))

(defn config-item-changed?
  ; @param (keyword) configer-id
  ; @param (map) configer-props
  ;  {:backup-path (vector)
  ;   :config-path (vector)}
  ;
  ; @usage
  ;  (r entities/config-item-changed? db :my-configer {...})
  ;
  ; @return (boolean)
  [db [_ configer-id {:keys [backup-path config-path]}]]
  ; Megvizsgálja, hogy a config-item megváltozott-e az eredeti másolathoz képest
  (let [config-item (get-in db config-path)
        backup-item (get-in db backup-path)]
       (map/items-different? config-item backup-item)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:entities.configer/config-synchronizing? :my-configer]
(a/reg-sub :entities.configer/config-synchronizing? config-synchronizing?)

; @usage
;  [:entities.configer/config-received? :my-configer]
(a/reg-sub :entities.configer/config-received? config-received?)

; @usage
;  [:entities.configer/config-form-changed? :my-configer {...}]
(a/reg-sub :entities.configer/config-form-changed? config-form-changed?)

; @usage
;  [:entities.configer/config-item-changed? :my-configer {...}]
(a/reg-sub :entities.configer/config-item-changed? config-item-changed?)
