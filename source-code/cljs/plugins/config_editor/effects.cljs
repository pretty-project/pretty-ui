
(ns plugins.config-editor.effects
  (:require [plugins.config-editor.events     :as configer.events]
            [plugins.config-editor.queries    :as configer.queries]
            [plugins.config-editor.prototypes :as configer.prototypes]
            [x.app-core.api                   :as a :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :entities.configer/load!
  ; @param (keyword) configer-id
  ; @param (map) configer-props
  ;  {:backup-path (vector)(opt)
  ;   :config-path (vector)(opt)}
  ;
  ; @usage
  ;  [:entities.configer/load! :my-configer {...}]
  (fn [{:keys [db]} [_ configer-id configer-props]]))
      ;(let [configer-props (configer.prototypes/configer-props-prototype configer-id configer-props)]
      ;     {:db       (r configer.events/clean-config! db)
      ;      :dispatch [:entities.configer/request-config! configer-id configer-props]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :entities.configer/save-changes!
  ; @param (keyword) configer-id
  ; @param (map) configer-props
  (fn [{:keys [db]} [_ configer-id configer-props]]
      {:db       (r configer.events/update-backup! db configer-id configer-props)
       :dispatch [:pathom/send-query! :entities.configer/synchronizing
                                      {:display-progress? true
                                       :query (r configer.queries/get-save-config-query db configer-id configer-props)}]}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :entities.configer/request-config!
  ; @param (keyword) configer-id
  ; @param (map) configer-props
  (fn [{:keys [db]} [_ configer-id configer-props]]
      [:pathom/send-query! :entities.configer/synchronizing
                           {:display-progress? true
                            :on-success [:entities.configer/receive-config! configer-id configer-props]
                            :on-stalled [:entities.configer/config-received configer-id configer-props]
                            :query      (r configer.queries/get-request-config-query db configer-id configer-props)}]))

(a/reg-event-fx
  :entities.configer/receive-config!
  ; @param (keyword) configer-id
  ; @param (map) configer-props
  ; @param (namespaced map) server-response
  (fn [{:keys [db]} [_ configer-id configer-props server-response]]
      {:db (r configer.events/store-config! db configer-id configer-props server-response)}))

(a/reg-event-fx
  :entities.configer/config-received
  ; @param (keyword) configer-id
  ; @param (map) configer-props
  ; @param (namespaced map) server-response
  (fn [{:keys [db]} [_ configer-id configer-props server-response]]
      {:db (r configer.events/config-received db configer-id configer-props server-response)}))
